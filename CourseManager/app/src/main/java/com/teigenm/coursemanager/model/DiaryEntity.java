package com.teigenm.coursemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "diary_info")
public class DiaryEntity {

    @ColumnInfo(name="id")
    @PrimaryKey
    private long id;

    @ColumnInfo(name="dtitle")
    private String dtitle;

    @ColumnInfo(name="dcontent")
    private String dcontent;

    public DiaryEntity() {
    }

    @Ignore
    public DiaryEntity(long id, String dtitle, String dcontent) {
        this.id = id;
        this.dtitle = dtitle;
        this.dcontent = dcontent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getDcontent() {
        return dcontent;
    }

    public void setDcontent(String dcontent) {
        this.dcontent = dcontent;
    }
}
