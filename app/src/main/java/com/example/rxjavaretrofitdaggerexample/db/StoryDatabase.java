package com.example.rxjavaretrofitdaggerexample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.rxjavaretrofitdaggerexample.model.Story;
import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;
import com.example.rxjavaretrofitdaggerexample.utils.StoryResponseConverter;


@Database(entities = {Story.class}, version = 1)
@TypeConverters({StoryResponseConverter.class})
public abstract class StoryDatabase extends RoomDatabase {

    private static StoryDatabase instance;
    public abstract StoryDao storyDao();
    public static synchronized StoryDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StoryDatabase.class,
                    "story_database").build();
        }
        return instance;
    }
}
