package com.example.rxjavaretrofitdaggerexample.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task8.data.model.StoryResponse;

import io.reactivex.Completable;

@Dao
public interface StoryDao {

    @Insert
    Completable insert(StoryResponse storyResponse);


    @Query("DELETE FROM story_response_table")
    Completable deleteAll();

}
