package com.teigenm.coursemanager.model;


import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 *  课程
 *  属性：学生学号、课程名、课程号、是否提醒、开始时间、结束时间
 *  方法：getter方法：获取属性、setter方法：设置属性、toString方法：打印课程信息
 */
@Entity(tableName = "course_info")
public class CourseEntity implements Serializable {

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name="sid")
    private String sid;

    @ColumnInfo(name="cno")
    private String cno;

    @ColumnInfo(name="cname")
    private String cname;

    @ColumnInfo(name="cteacher")
    private String cteacher;

    @ColumnInfo(name="cweek_start")
    private int cweekStart;

    @ColumnInfo(name="cweek_end")
    private int cweekEnd;

    @ColumnInfo(name="csection_start")
    private int csectionStart;

    @ColumnInfo(name="csection_end")
    private int csectionEnd;

    @ColumnInfo(name="cremind")
    private int cremind;

    @ColumnInfo(name="cweek_day")
    private int cweekDay;

    public CourseEntity() {
    }

    @Ignore
    public CourseEntity(String sid, String cno, String cname, String cteacher,
                        int cweekStart, int cweekEnd, int csectionStart, int csectionEnd, int cremind, int cweekDay) {
        this.sid = sid;
        this.cno = cno;
        this.cname = cname;
        this.cteacher = cteacher;
        this.cweekStart = cweekStart;
        this.cweekEnd = cweekEnd;
        this.csectionStart = csectionStart;
        this.csectionEnd = csectionEnd;
        this.cremind = cremind;
        this.cweekDay = cweekDay;
    }

    @Ignore
    public CourseEntity(long id, String sid, String cno, String cname, String cteacher, int cweekStart, int cweekEnd, int csectionStart, int csectionEnd, int cremind, int cweekDay) {
        this.id = id;
        this.sid = sid;
        this.cno = cno;
        this.cname = cname;
        this.cteacher = cteacher;
        this.cweekStart = cweekStart;
        this.cweekEnd = cweekEnd;
        this.csectionStart = csectionStart;
        this.csectionEnd = csectionEnd;
        this.cremind = cremind;
        this.cweekDay = cweekDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCteacher() {
        return cteacher;
    }

    public void setCteacher(String cteacher) {
        this.cteacher = cteacher;
    }

    public int getCweekStart() {
        return cweekStart;
    }

    public void setCweekStart(int cweekStart) {
        this.cweekStart = cweekStart;
    }

    public int getCweekEnd() {
        return cweekEnd;
    }

    public void setCweekEnd(int cweekEnd) {
        this.cweekEnd = cweekEnd;
    }

    public int getCsectionStart() {
        return csectionStart;
    }

    public void setCsectionStart(int csectionStart) {
        this.csectionStart = csectionStart;
    }

    public int getCsectionEnd() {
        return csectionEnd;
    }

    public void setCsectionEnd(int csectionEnd) {
        this.csectionEnd = csectionEnd;
    }

    public int getCremind() {
        return cremind;
    }

    public void setCremind(int cremind) {
        this.cremind = cremind;
    }

    public int getCweekDay() {
        return cweekDay;
    }

    public void setCweekDay(int cweekDay) {
        this.cweekDay = cweekDay;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", sid='" + sid + '\'' +
                ", cno='" + cno + '\'' +
                ", cname='" + cname + '\'' +
                ", cteacher='" + cteacher + '\'' +
                ", cweekStart=" + cweekStart +
                ", cweekEnd=" + cweekEnd +
                ", csectionStart=" + csectionStart +
                ", csectionEnd=" + csectionEnd +
                ", cremind=" + cremind +
                ", cweekDay=" + cweekDay +
                '}';
    }
}
