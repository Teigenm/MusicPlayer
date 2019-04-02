package com.teigenm.coursemanager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 学生
 * 属性：学号、密码、姓名、性别
 * 方法：getter方法：获取属性、setter方法：设置属性、toString方法：打印学生信息
 */
@Entity(tableName = "student_info")
public class StudentEntity {
    @NonNull
    @ColumnInfo(name="sid")
    @PrimaryKey
    private String sid;

    @ColumnInfo(name="spassword")
    private String spassword;

    @ColumnInfo(name="sname")
    private String sname;

    @ColumnInfo(name="ssex")
    private String ssex;

    public StudentEntity() {
    }
    @Ignore
    public StudentEntity(String sid, String spassword) {
        this.sid = sid;
        this.spassword = spassword;
    }

    @Ignore
    public StudentEntity(String sid, String spassword, String sname) {
        this.sid = sid;
        this.spassword = spassword;
        this.sname = sname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSpassword() {
        return spassword;
    }

    public void setSpassword(String spassword) {
        this.spassword = spassword;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }
}