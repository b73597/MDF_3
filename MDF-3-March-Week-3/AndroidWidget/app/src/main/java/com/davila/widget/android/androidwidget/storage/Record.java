// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget.storage;



import java.io.Serializable;

public class Record implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String dater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDater() {
        return dater;
    }

    public void setDater(String dater) {
        this.dater = dater;
    }
}
