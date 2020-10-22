package com.example.rxjavaretrofitdaggerexample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;


@Database(entities = {StoryResponse.class}, version = 2)
public abstract class StoryDatabase extends RoomDatabase {

    private static StoryDatabase instance;
    public abstract StoryDao storyDao();
    public static synchronized StoryDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StoryDatabase.class,
                    "story_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
