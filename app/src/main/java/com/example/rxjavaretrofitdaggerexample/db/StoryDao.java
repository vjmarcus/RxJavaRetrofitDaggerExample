package com.example.rxjavaretrofitdaggerexample.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface StoryDao {

    @Insert
    Completable insert(StoryResponse storyResponse);


    @Query("DELETE FROM story_response_table")
    Completable deleteAll();

    @Query("SELECT * FROM story_response_table WHERE id = (SELECT MAX(id) FROM story_response_table)")
    Single<StoryResponse> getLastAddedResponse();

    @Query("SELECT * FROM story_response_table")
    Single<List<StoryResponse>> getListOfResponse();

    @Query("SELECT * FROM story_response_table")
    Flowable<List<StoryResponse>> getAll();
}
