package com.example.rxjavaretrofitdaggerexample.di.components;

import com.example.rxjavaretrofitdaggerexample.MainActivity;
import com.example.rxjavaretrofitdaggerexample.di.modules.NewsApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NewsApiModule.class})
@Singleton
public interface AppComponent {
    void injectMainActivity(MainActivity mainActivity);
}
