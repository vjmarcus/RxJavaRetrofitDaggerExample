package com.example.rxjavaretrofitdaggerexample.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.rxjavaretrofitdaggerexample.utils.StoryResponseConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoryResponse implements Serializable {
    private int id;
    @Expose
    private List<Story> articles;

    @Ignore
    public StoryResponse(List<Story> articles) {
        this.articles = articles;
    }

    public StoryResponse(int id, List<Story> articles) {
        this.id = id;
        this.articles = articles;
    }

    public List<Story> getArticles() {
        return articles;
    }

    public int getId() {
        return id;
    }

}
