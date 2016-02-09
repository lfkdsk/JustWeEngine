package com.lfk.justweengine.Utils.script;

import com.lfk.justweengine.Utils.tools.ValidatorsUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ScriptManager
 *
 * @author liufengkai
 *         Created by liufengkai on 16/2/8.
 */
public class ScriptManager implements KeyCode {

    private HashMap<String, Function> functionIndex;

    private Stack<Long> indexStack;

    private Stack<Boolean> ifStack;

    private RandomAccessFile file;

    private long fileLength;

    private long fileLines = 0;

    private final Object lock = new Object();

    private Exp exp = new Exp();

    private static HashMap<String, Object> flagDefaultMap = new HashMap<>();


    public ScriptManager() {
        functionIndex = new HashMap<>();
        indexStack = new Stack<>();
        ifStack = new Stack<>();
    }


    public void open(String name) {
        try {
            file = new RandomAccessFile(name, "rw");
            fileLength = file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CodeThread thread = new CodeThread();
        thread.start();

        thread.setPreTreatment(new OnAfterPreTreatment() {
            @Override
            public void AfterPreTreatment() {

                for (int i = 0; i < 50; i++) {
                    doExecute();
                }

            }
        });
    }

    /**
     * 预处理线程:
     * 接到脚本后进行预处理,把方法的开始和末尾的位置保存
     * 并且定位主函数入栈
     *
     * @example :
     * <p>
     * 位置入栈:
     * BEGIN FUNCTION:
     * ...
     * END FUNCTION
     * <p>
     * 主函数标记:
     * <p>
     * MAIN:
     */
    private class CodeThread extends Thread {
        private long startIndex;
        private long endIndex;
        private String functionName;
        private OnAfterPreTreatment preTreatment;

        public void setPreTreatment(OnAfterPreTreatment preTreatment) {
            this.preTreatment = preTreatment;
        }

        public CodeThread() {
            clearData();
            fileLines = 0;
        }

        private void clearData() {
            startIndex = 0;
            endIndex = 0;
            functionName = null;
        }

        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                try {
                    // 处理整个脚本
                    while (file.getFilePointer() < fileLength) {
                        // 读入每一行
                        String line = new String((file.readLine()).
                                getBytes("iso-8859-1"), "UTF-8").trim();
                        fileLines++;
                        // 检测开始标记
                        if (line.startsWith(KeyCode.FLAG_BEGIN_TAG)) {
                            startIndex = file.getFilePointer();
                            functionName = line.substring(6, line.length() - 1);
                            // 检测结束标记
                        } else if (line.startsWith(KeyCode.FLAG_END_TAG) &&
                                !line.startsWith(KeyCode.FLAG_END_IF_TAG)) {
                            // 方法名字相同 然后保存
                            if (functionName.equals(line.substring(4, line.length()))) {
                                endIndex = file.getFilePointer();
                                functionIndex.put(functionName,
                                        new Function(startIndex, endIndex));
                            } else {
                                // 清理数据
                                clearData();
                            }
                            // 寻找主函数
                        } else if (line.startsWith("MAIN")) {
                            indexStack.add(file.getFilePointer());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 指针放到主函数位置
                setIndex(indexStack.peek());

                if (preTreatment != null)
                    preTreatment.AfterPreTreatment();
            }
        }
    }

    public interface OnAfterPreTreatment {
        void AfterPreTreatment();
    }

    public synchronized void doExecute() {
        try {

            if (file.getFilePointer() == fileLength) {
                return;
            }

            String line = new String((file.readLine()).
                    getBytes("iso-8859-1"), "UTF-8").trim();

            updateIndex();

            // 判断空行
            if (line.equals("")) {
                line = file.readLine();
                updateIndex();
            }

            if (!ifStack.empty()) {
                // false
                if (!ifStack.peek()) {
                    while (true) {
                        line = file.readLine();
                        if (hasRax(KeyCode.FLAG_ELSE_TAG, line)) {
                            ifStack.pop();
                            break;
                        } else if (hasRax(KeyCode.FLAG_ELSE_IF_TAG, line)) {
                            break;
                        } else if (hasRax(KeyCode.FLAG_END_IF_TAG, line)) {
                            ifStack.pop();
                            return;
                        }
                    }
                }
            }

            if (line.startsWith(KeyCode.FLAG_RUN_TAG)) {
                runParse(line);
            } else if (line.startsWith(KeyCode.FLAG_END_TAG) &&
                    !line.startsWith(KeyCode.FLAG_END_IF_TAG)) {
                endParse();
            } else if (hasRax("=", line)) {
                evalParse(line);
            } else if (hasRax(KeyCode.FLAG_IF_TAG, line)) {
                ifStack.push(ifParse(line));
            } else if (hasRax(KeyCode.FLAG_ELSE_IF_TAG, line)) {
                if (!ifStack.peek()) {
                    ifStack.pop();
                    ifStack.push(elseIfParse(line));
                } else {
                    ifEndParse();
                }
            } else if (hasRax(KeyCode.FLAG_ELSE_TAG, line)) {
                doExecute();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateIndex() {
        try {
            indexStack.pop();
            indexStack.push(file.getFilePointer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setIndex(long index) {
        if (index <= fileLength) {
            try {
                file.seek(index);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void ifEndParse() {
        while (true) {
            String line = null;
            try {
                line = file.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (hasRax(KeyCode.FLAG_END_IF_TAG, line)) {
                ifStack.pop();
                break;
            }
        }
    }

    private void runParse(String command) {
        String name = command.substring(4, command.length());
        if (functionIndex.containsKey(name)) {
            long index = functionIndex.get(name).getStartIndex();
            setIndex(index);
            indexStack.push(index);
        }
    }

    private void endParse() {
        indexStack.pop();
        setIndex(indexStack.peek());
    }

    private boolean elseIfParse(String command) {
        return coverParse(command.substring(7, command.length() - 1));
    }

    private boolean ifParse(String command) {
        return coverParse(command.substring(3, command.length() - 1));
    }

    private boolean coverParse(String command) {
        List list = commandToCondition(command);
        Object valueA = list.get(1);
        Object valueB = list.get(2);
        valueA = !flagDefaultMap.containsKey(valueA.toString()) ?
                valueA : flagDefaultMap.get(valueA.toString());
        valueB = !flagDefaultMap.containsValue(valueB.toString()) ?
                valueB : flagDefaultMap.get(valueB.toString());
        String condition = list.get(0).toString();
        if ("==".equals(condition)) {
            return valueA.toString().equals(valueB.toString());
            // 非等
        } else if ("!=".equals(condition)) {
            return !valueA.toString().equals(valueB.toString());
            // 大于
        } else if (">".equals(condition)) {
            float numberA = Float.parseFloat(valueA.toString());
            float numberB = Float.parseFloat(valueB.toString());
            return numberA > numberB;
            // 小于
        } else if ("<".equals(condition)) {
            float numberA = Float.parseFloat(valueA.toString());
            float numberB = Float.parseFloat(valueB.toString());
            return numberA < numberB;

            // 大于等于
        } else if (">=".equals(condition)) {
            float numberA = Float.parseFloat(valueA.toString());
            float numberB = Float.parseFloat(valueB.toString());
            return numberA >= numberB;
            // 小于等于
        } else if ("<=".equals(condition)) {
            float numberA = Float.parseFloat(valueA.toString());
            float numberB = Float.parseFloat(valueB.toString());
            return numberA <= numberB;
        }
        return false;
    }

    private void evalParse(String command) {
        String com = command.replace(" ", "");
        int index = com.indexOf("=");
        String left = com.substring(0, index);
        String right = com.substring(index + 1);
        float rightNum;
        // 不是字符串
        // 是表达式
        if (exp.exp(right)) {
            // 替换
            for (Object o : flagDefaultMap.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                right = rax(entry.getKey().toString(), right);
            }
            // 计算
            rightNum = exp.parse(right);
            flagDefaultMap.put(left, rightNum);
        } else if (ValidatorsUtils.isNumeric(right)) {
            rightNum = exp.parse(right);
            flagDefaultMap.put(left, rightNum);
        }
    }

    private String rax(String rax, String command) {
        String X = "([^a-zA-Z0-9]|^)" + rax + "([^a-zA-Z0-9]|$)";
        Pattern pattern = Pattern.compile(X);
        Matcher matcher = pattern.matcher(command);
//        printIt(command + "\n");
        while (matcher.find()) {
            System.out.println("start:" + matcher.start());
            System.out.println("end:" + matcher.end());
            command = command.substring(0, matcher.start() + 1) +
                    flagDefaultMap.get(rax).toString()
                    + command.substring(matcher.end() - 1);
        }
        return command;
    }

    public List commandToCondition(String command) {
        List<String> list = new ArrayList<>();
        int index;
        if (command.contains(KeyCode.FLAG_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_EQUAL_TAG);
        } else if (command.contains(KeyCode.FLAG_UN_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_UN_EQUAL_TAG);
        } else if (command.contains(KeyCode.FLAG_SMALLER_TAG)
                && !command.contains(KeyCode.FLAG_SMALLER_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_SMALLER_TAG);
        } else if (command.contains(KeyCode.FLAG_BIGGER_TAG)
                && !command.contains(KeyCode.FLAG_BIGGER_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_BIGGER_TAG);
        } else if (command.contains(KeyCode.FLAG_SMALLER_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_SMALLER_EQUAL_TAG);
        } else if (command.contains(KeyCode.FLAG_BIGGER_EQUAL_TAG)) {
            list.add(KeyCode.FLAG_BIGGER_EQUAL_TAG);
        }
        index = command.indexOf(list.get(0));
        list.add(command.substring(0, index).trim());
        list.add(command.substring(index + list.get(0).length()));
        return list;
    }

    private boolean hasRax(String rax, String command) {
        String X = "([^a-zA-Z0-9]|^)" + rax + "([^a-zA-Z0-9]|$)";
        Pattern pattern = Pattern.compile(X);
        Matcher matcher = pattern.matcher(command);
        return matcher.find();
    }

    public void printIt() {
//        printIt(functionIndex.size() + "");
        for (Object o : functionIndex.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            Function val = (Function) entry.getValue();
            System.out.print("key:" + key
                    + "val:" + val.getStartIndex() + "\n"
            );
        }

//        printIt(flagDefaultMap.size() + "");
        for (Object o : flagDefaultMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            System.out.print("key:" + key
                    + "val:" + entry.getValue().toString() + "\n"
            );
        }
    }

    private static void printIt(String name) {
        System.out.print(name + "\n");
    }


}
