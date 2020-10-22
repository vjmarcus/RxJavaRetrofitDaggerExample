package com.example.rxjavaretrofitdaggerexample;


import android.app.Application;
import android.util.Log;

import com.example.rxjavaretrofitdaggerexample.di.components.AppComponent;
import com.example.rxjavaretrofitdaggerexample.di.components.DaggerAppComponent;
import com.example.rxjavaretrofitdaggerexample.utils.Constants;

public class App extends Application {

    static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate: App");
        appComponent = DaggerAppComponent.builder()
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
