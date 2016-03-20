package com.lfk.justweengine.Utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufengkai on 16/3/18.
 */
public class DataBase {
    // 数据库相关信息类
    private DataBaseMessage mDBMessage;
    // 数据库新建
    private DataBaseHelper mDBHelper;
    // 实体数据库
    private SQLiteDatabase mDB;

    public DataBase() {
    }

    public DataBase(DataBaseMessage mDBMessage) {
        this.mDBMessage = mDBMessage;
    }

    // 入口方法
    public static DataBase initAndOpen(String name, Class<?> clazz) {
        DataBase dataBase = new DataBase();
        dataBase.mDBMessage = dataBase.getCreateSQL(clazz);
        dataBase.mDBMessage.SQL_NAME = name;
        return dataBase;
    }

    public boolean open(Context context) {
        try {
            mDBHelper = new DataBaseHelper(context, mDBMessage.SQL_NAME, 1);
            mDB = mDBHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        mDB.close();
        mDBHelper.close();
    }

    /**
     * 尺寸
     *
     * @return 数据库存储条目
     */
    public int size() {
        int size = 0;
        Cursor mCursor = mDB.query(mDBMessage.TABLE_NAME, new String[]{mDBMessage.PRIMARY_KEY}, null, null, null, null,
                null, null);
        if (mCursor != null) {
            size = mCursor.getCount();
            mCursor.close();
        }
        return size;
    }

    /**
     * 插入条目
     *
     * @param node
     * @return
     */
    public boolean insert(Node node) {
        Log.e("node", node.toString());
        ContentValues values = new ContentValues();
        for (int i = 0; i < mDBMessage.LABEL_NAME.size(); i++) {
            if (node.arrayList.get(i) instanceof Integer)
                values.put(mDBMessage.LABEL_NAME.get(i), (Integer) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof String)
                values.put(mDBMessage.LABEL_NAME.get(i), (String) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Float)
                values.put(mDBMessage.LABEL_NAME.get(i), (Float) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Long)
                values.put(mDBMessage.LABEL_NAME.get(i), (Long) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Boolean)
                values.put(mDBMessage.LABEL_NAME.get(i), (Boolean) node.arrayList.get(i));
        }
        node.key = mDB.insert(mDBMessage.TABLE_NAME, null, values);
        if (node.key == -1) {
            Log.e("DATABASE", "db insert fail!");
            return false;
        }
        return true;
    }

    /**
     * 更新
     *
     * @param node
     * @return
     */
    public boolean update(Node node) {
        if (node.key == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        for (int i = 0; i < mDBMessage.LABEL_NAME.size(); i++) {
            if (node.arrayList.get(i) instanceof Integer)
                values.put(mDBMessage.LABEL_NAME.get(i), (Integer) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof String)
                values.put(mDBMessage.LABEL_NAME.get(i), (String) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Float)
                values.put(mDBMessage.LABEL_NAME.get(i), (Float) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Long)
                values.put(mDBMessage.LABEL_NAME.get(i), (Long) node.arrayList.get(i));
            else if (node.arrayList.get(i) instanceof Boolean)
                values.put(mDBMessage.LABEL_NAME.get(i), (Boolean) node.arrayList.get(i));
        }
        String condition = mDBMessage.PRIMARY_KEY + "=" + "\'" + node.key + "\'";
        return update(values, condition, null);
    }

    protected boolean update(ContentValues values, String whereClause, String[] whereArgs) {
        int rows = mDB.update(mDBMessage.TABLE_NAME, values, whereClause, whereArgs);
        if (rows <= 0) {
            Log.d("DATABASE", "db update fail!");
            return false;
        }
        return true;
    }

    /**
     * 删除指定条目
     *
     * @param position
     * @return
     */
    public boolean delete(int position) {
        long key = getKey(position, null);
        if (key == -1) {
            return false;
        }
        String condition = mDBMessage.PRIMARY_KEY + "=" + "\'" + key + "\'";
        return delete(condition, null);
    }

    protected boolean delete(String whereClause, String[] whereArgs) {
        int rows = mDB.delete(mDBMessage.TABLE_NAME, whereClause, whereArgs);
        if (rows <= 0) {
            Log.e("DATABASE", "db delete fail!");
            return false;
        }
        return true;
    }

    public boolean clear() {
        return delete(null, null);
    }

    /**
     * 一坨get方法
     *
     * @param position
     * @return
     */
    public List<Node> get(int position) {
        return get(position, null);
    }

    public List<Node> get(long id) {
        String condition = mDBMessage.PRIMARY_KEY + "=" + "\'" + id + "\'";
        List<Node> notes = query(condition);
        if (notes.isEmpty()) {
            return null;
        }
        return notes;
    }

    public List<Node> get(int position, String condition) {
        Cursor cursor = mDB.query(mDBMessage.TABLE_NAME, null, condition, null, null, null,
                mDBMessage.PRIMARY_KEY + " DESC", null);
        List<Node> notes = extract(position, cursor);
        if (notes.isEmpty()) {
            return null;
        }
        return notes;
    }


    public List<Node> query() {
        Cursor cursor = mDB.query(mDBMessage.TABLE_NAME, null, null, null, null, null,
                mDBMessage.PRIMARY_KEY + " DESC", null);
        return extract(0, cursor);
    }

    public List<Node> query(String condition) {
        Cursor cursor = mDB.query(mDBMessage.TABLE_NAME, null, condition, null, null, null,
                mDBMessage.PRIMARY_KEY + " DESC", null);
        return extract(0, cursor);
    }

    public List<Node> query(int offset, int limit) {
        return query(null, offset, limit);
    }

    public List<Node> query(String condition, int offset, int limit) {
        Cursor cursor = mDB.query(mDBMessage.TABLE_NAME, null, condition, null, null, null,
                mDBMessage.PRIMARY_KEY + " DESC", offset + "," + limit);
        return extract(0, cursor);
    }

    /**
     * 从某个位置进行查询
     *
     * @param offset
     * @param cursor
     * @return
     */
    protected List<Node> extract(int offset, Cursor cursor) {

        List<Node> notes = new ArrayList<>();
        if (cursor == null || cursor.getCount() <= offset) {
            return notes;
        }

        cursor.moveToFirst();
        cursor.moveToPosition(offset);

        do {
            Node note = new Node();
            note.key = cursor.getLong(cursor.getColumnIndex(mDBMessage.PRIMARY_KEY));
            for (int i = 0; i < mDBMessage.LABEL_NAME.size(); i++) {
                note.arrayList.add(cursor.getColumnIndex(mDBMessage.LABEL_NAME.get(i)));
            }
            notes.add(note);
        } while (cursor.moveToNext());

        cursor.close();

        return notes;
    }

    /**
     * 获取存储键值
     *
     * @param position  位置
     * @param condition 信息
     * @return
     */
    protected long getKey(int position, String condition) {
        long key = -1;
        Cursor cursor = mDB.query(true, mDBMessage.TABLE_NAME, new String[]{mDBMessage.PRIMARY_KEY}, condition, null, null, null,
                mDBMessage.PRIMARY_KEY + " DESC", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToPosition(position);
            key = cursor.getLong(cursor.getColumnIndex(mDBMessage.PRIMARY_KEY));
            cursor.close();
        }
        return key;
    }

    private class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(mDBMessage.CREATE_SQL);
            Log.d("create", mDBMessage.CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + mDBMessage.TABLE_NAME);
            onCreate(db);
        }
    }


    /**
     * SQL辅助信息
     */
    public class DataBaseMessage {
        String SQL_NAME;
        // 创建
        String CREATE_SQL;
        // 表名
        String TABLE_NAME;
        // 主键
        String PRIMARY_KEY;
        // 存放
        ArrayList<String> LABEL_NAME;

        public DataBaseMessage() {
            LABEL_NAME = new ArrayList<>();
        }
    }

    /**
     * 反射方法
     * 通过注释类产生SQL语句
     *
     * @param clazz
     * @return
     */
    public DataBaseMessage getCreateSQL(Class<?> clazz) {
        DataBaseMessage msg = new DataBaseMessage();

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");

        if (clazz.isAnnotationPresent(TableName.class)) {
            TableName t = clazz.getAnnotation(TableName.class);
            if (t.ifNotExist())
                builder.append(" ").append("IF NOT EXISTS ");
            builder.append(t.tableName());
            // table name
            msg.TABLE_NAME = t.tableName();
        }

        builder.append(" (");

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(LabelName.class)) {

                LabelName f = fields[i].getAnnotation(LabelName.class);

                builder.append(f.columnName());
                builder.append(" ").append(f.type());
                msg.LABEL_NAME.add(f.columnName());

                if (f.generatedId()) {
                    builder.append(" ").append("PRIMARY KEY");
                    msg.PRIMARY_KEY = f.columnName();

                    if (f.autoincrement()) {
                        builder.append(" ").append("AUTOINCREMENT");
                        msg.LABEL_NAME.remove(msg.LABEL_NAME.size() - 1);
                    }
                }

                builder.append(",");
            }
        }

        builder.delete(builder.length() - 1, builder.length());

        builder.append(") ");

        msg.CREATE_SQL = builder.toString();

        return msg;
    }
}
