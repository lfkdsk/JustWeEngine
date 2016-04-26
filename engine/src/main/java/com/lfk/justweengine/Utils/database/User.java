package com.lfk.justweengine.Utils.database;

/**
 * Demo for DataBase
 *
 * @author liufengkai
 *         Created by liufengkai on 16/3/18.
 */
@TableName(tableName = "lfkdsk", ifNotExist = true)
public class User extends Node {

    @LabelName(autoincrement = true, type = LabelName.Type.INTEGER, columnName = "name", generatedId = true)
    private int name;

    @LabelName(type = LabelName.Type.TEXT, columnName = "user_name")
    private String user_name;

    public User(int name, String user_name) {
        super(name, user_name);
        this.name = name;
        this.user_name = user_name;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
