package com.example.rxjavaretrofitdaggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rxjavaretrofitdaggerexample.db.StoryDao;
import com.example.rxjavaretrofitdaggerexample.db.StoryDatabase;
import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;
import com.example.rxjavaretrofitdaggerexample.network.NewsApi;
import com.example.rxjavaretrofitdaggerexample.utils.Constants;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Inject
    NewsApi newsApi;
    TextView textView;
    StoryDao storyDao;
    Button button;
    boolean isFromWeb = true;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);

        StoryDatabase db = StoryDatabase.getInstance(getApplicationContext());
        storyDao = db.storyDao();
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        Observable<Integer> observable = Observable.just(1, 2, 3);
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }


    private Observable<StoryResponse> getDataFromWeb(String key) {
        return newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
    }

    private void insertDb(StoryResponse storyResponse) {
        Log.d(Constants.TAG, "insertDb: size = " + storyResponse.getArticles().size());
        storyDao.insert(storyResponse).subscribeOn(Schedulers.io()).subscribe();
    }

    private void deleteAll() {
        Log.d(Constants.TAG, "deleteAll: ");
        storyDao.deleteAll().subscribeOn(Schedulers.io()).subscribe();
    }

    private Single<StoryResponse> getLastStoryResponse() {
        Log.d(Constants.TAG, "getLastStoryResponse: ");
        return storyDao.getLastAddedResponse();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.dispose();
    }
}
