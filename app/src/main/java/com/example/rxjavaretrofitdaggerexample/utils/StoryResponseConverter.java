package com.example.rxjavaretrofitdaggerexample.utils;

import androidx.room.TypeConverter;

import com.example.rxjavaretrofitdaggerexample.model.Story;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StoryResponseConverter {

//    @TypeConverter
//    public static ArrayList<String> fromString(String value) {
//        Type listType = new TypeToken<List<Story>>() {}.getType();
//        return new Gson().fromJson(value, listType);
//    }
//
//    @TypeConverter
//    public static String fromArrayList(List<Story>storyList) {
//        Gson gson = new Gson();
//        String json = gson.toJson(storyList);
//        return json;
//    }

    @TypeConverter
    public String fromStoryResponse(List<Story> storyList) {
        Gson gson = new Gson();
        return gson.toJson(storyList);
    }

    @TypeConverter
    public  List<Story> fromString(String value) {
        Type listType = new TypeToken<List<Story>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
}
