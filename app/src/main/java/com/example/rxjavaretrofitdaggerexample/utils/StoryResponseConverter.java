package com.example.rxjavaretrofitdaggerexample.utils;

import androidx.room.TypeConverter;
import com.example.rxjavaretrofitdaggerexample.model.Story;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class StoryResponseConverter {

    @TypeConverter
    public String fromStoryResponse(List<Story> storyList) {
        Gson gson = new Gson();
        return gson.toJson(storyList);
    }

    @TypeConverter
    public List<Story> toStoryResponse(String jsonString) {
        Type type = new TypeToken<String>(){}.getType();
        return new Gson().fromJson(jsonString, type);
    }
}
