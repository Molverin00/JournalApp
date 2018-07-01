package com.example.marouen.journalapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Journal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    @ColumnInfo(name = "created_date")
    private Date createdDate;
//    @ColumnInfo(name = "updated_date")
//    private Date updatedDate;

    public Journal() {

    }

    @Ignore
    public Journal(String title, String content, Date createdDate/*, Date updatedDate*/) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        //this.updatedDate = updatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
//    public Date getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedDate(Date updatedDate) {
//        this.updatedDate = updatedDate;
//    }
}
