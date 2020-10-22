package com.example.rxjavaretrofitdaggerexample;

import io.reactivex.Observable;

public class Repository {
    public static Observable<String> getData() {
        String string = "HOHOHO";
        return Observable.just(string);
    }
}
