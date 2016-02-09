package com.lfk.justweengine.Utils.script;

import java.util.HashMap;
import java.util.Random;

public class Exp {

    private static final int MAX_LENGTH = 128;

    public static final int STACK_VARIABLE = 11;

    public static final int STACK_RAND = -1;

    public static final int STACK_NUM = 0;

    public static final int PLUS = 1;

    public static final int MINUS = 2;

    public static final int MULTIPLE = 3;

    public static final int DIVISION = 4;

    public static final int MODULO = 5;

    private HashMap<String, Compute> computes = new HashMap<>();

    private char[] expChr;

    /**
     * 是否包含运算符号
     *
     * @param exp
     * @return
     */
    public boolean exp(String exp) {
        return exp.contains("+") || exp.contains("-")
                || exp.contains("*") || exp.contains("/")
                || exp.contains("%");
    }

    public float parse(Object v) {
        return parse(v.toString());
    }

    public float parse(String v) {
        if (!exp(v)) {
            // 不含有直接翻译
            if (NumberUtils.isNan(v)) {
                return Float.parseFloat(v);
            } else {
                throw new RuntimeException(v + " not parse !");
            }
        }
        // 含有进行分析
        return eval(v);
    }

    private void evalFloatValue(Compute compute, int stIdx, int lgt,
                                float sign) {
        if (expChr[stIdx] == '$') {
            String label = new String(expChr, stIdx + 1, lgt - 1);
            if (label.equals("rand")) {
                compute.push(0, STACK_RAND);
            } else {
                int idx;
                try {
                    idx = Integer.parseInt(label) - 1;
                } catch (NumberFormatException e) {
                    compute.push(0, STACK_NUM);
                    return;
                }
                compute.push(0, STACK_VARIABLE + idx);
            }
        } else {
            try {
                compute.push(Float.parseFloat(new String(expChr, stIdx, lgt))
                        * sign, STACK_NUM);
            } catch (NumberFormatException e) {
                compute.push(0, STACK_NUM);
            }
        }
    }

    private void evalExp(Compute compute, int stIdx, int edIdx) {
        int op[] = new int[]{-1, -1};
        while (expChr[stIdx] == '(' && expChr[edIdx - 1] == ')') {
            stIdx++;
            edIdx--;
        }
        for (int i = edIdx - 1; i >= stIdx; i--) {
            char c = expChr[i];
            if (c == ')') {
                do {
                    i--;
                } while (expChr[i] != '(');
            } else if (op[0] < 0 && (c == '*' || c == '/' || c == '%')) {
                op[0] = i;
            } else if (c == '+' || c == '-') {
                op[1] = i;
                break;
            }
        }
        if (op[1] < 0) {
            if (op[0] < 0) {
                evalFloatValue(compute, stIdx, edIdx - stIdx, 1);
            } else {
                switch (expChr[op[0]]) {
                    case '*':
                        evalExp(compute, stIdx, op[0]);
                        evalExp(compute, op[0] + 1, edIdx);
                        compute.setOperator(MULTIPLE);
                        break;
                    case '/':
                        evalExp(compute, stIdx, op[0]);
                        evalExp(compute, op[0] + 1, edIdx);
                        compute.setOperator(DIVISION);
                        break;
                    case '%':
                        evalExp(compute, stIdx, op[0]);
                        evalExp(compute, op[0] + 1, edIdx);
                        compute.setOperator(MODULO);
                        break;
                }
            }
        } else {
            if (op[1] == stIdx) {
                switch (expChr[op[1]]) {
                    case '-':
                        evalFloatValue(compute, stIdx + 1, edIdx - stIdx - 1,
                                -1);
                        break;
                    case '+':
                        evalFloatValue(compute, stIdx + 1, edIdx - stIdx - 1, 1);
                        break;
                }
            } else {
                switch (expChr[op[1]]) {
                    case '+':
                        evalExp(compute, stIdx, op[1]);
                        evalExp(compute, op[1] + 1, edIdx);
                        compute.setOperator(PLUS);
                        break;
                    case '-':
                        evalExp(compute, stIdx, op[1]);
                        evalExp(compute, op[1] + 1, edIdx);
                        compute.setOperator(MINUS);
                        break;
                }
            }
        }
    }

    public float eval(String exp) {
        Compute compute = computes.get(exp);
        if (compute == null) {
            expChr = new char[exp.length()];
            int ecIdx = 0;
            boolean skip = false;
            StringBuilder buf = new StringBuilder(exp);
            int depth = 0;
            boolean balance = true;
            char ch;
            for (int i = 0; i < buf.length(); i++) {
                ch = buf.charAt(i);
                switch (ch) {
                    case ' ':
                    case '\n':
                        skip = true;
                        break;
                    case ')':
                        depth--;
                        if (depth < 0)
                            balance = false;
                        break;
                    case '(':
                        depth++;
                        break;
                }
                if (skip) {
                    skip = false;
                } else {
                    expChr[ecIdx] = ch;
                    ecIdx++;
                }
            }
            if (depth != 0 || !balance) {
                return 0;
            }
            compute = new Compute();
            evalExp(compute, 0, ecIdx);
            computes.put(exp, compute);
        }
        return compute.calc();
    }

    final private class Compute {

        private float[] num = new float[MAX_LENGTH];

        private int[] opr = new int[MAX_LENGTH];

        private int idx;

        private float[] stack = new float[MAX_LENGTH];

        public Compute() {
            idx = 0;
        }

        private float calcOp(int op, float n1, float n2) {
            switch (op) {
                case PLUS:
                    return n1 + n2;
                case MINUS:
                    return n1 - n2;
                case MULTIPLE:
                    return n1 * n2;
                case DIVISION:
                    return n1 / n2;
                case MODULO:
                    return n1 % n2;
            }
            return 0;
        }

        public void setOperator(int op) {
            if (idx >= MAX_LENGTH) {
                return;
            }
            if (opr[idx - 1] == STACK_NUM && opr[idx - 2] == STACK_NUM) {
                num[idx - 2] = calcOp(op, num[idx - 2], num[idx - 1]);
                idx--;
            } else {
                opr[idx] = op;
                idx++;
            }
        }

        public void push(float nm, int vr) {
            if (idx >= MAX_LENGTH) {
                return;
            }
            num[idx] = nm;
            opr[idx] = vr;
            idx++;
        }

        public final float calc() {
            int stkIdx = 0;
            for (int i = 0; i < idx; i++) {
                switch (opr[i]) {
                    case STACK_NUM:
                        stack[stkIdx] = num[i];
                        stkIdx++;
                        break;
                    case STACK_RAND:
                        stack[stkIdx] = new Random().nextFloat();
                        stkIdx++;
                        break;
                    default:
                        if (opr[i] >= STACK_VARIABLE) {
                            stkIdx++;
                        } else {
                            stack[stkIdx - 2] = calcOp(opr[i],
                                    stack[stkIdx - 2], stack[stkIdx - 1]);
                            stkIdx--;
                        }
                        break;
                }
            }
            return stack[0];
        }
    }

    public void dispose() {
        if (computes != null) {
            computes.clear();
        }
    }

    public static void main(String[] args) {
        Exp exp = new Exp();
        System.out.print(exp.parse("(1 + 1) /2"));
    }
}