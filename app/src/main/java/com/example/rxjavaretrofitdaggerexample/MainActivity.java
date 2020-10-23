package com.example.rxjavaretrofitdaggerexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rxjavaretrofitdaggerexample.db.StoryDao;
import com.example.rxjavaretrofitdaggerexample.db.StoryDatabase;
import com.example.rxjavaretrofitdaggerexample.model.Source;
import com.example.rxjavaretrofitdaggerexample.model.Story;
import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;
import com.example.rxjavaretrofitdaggerexample.network.NewsApi;
import com.example.rxjavaretrofitdaggerexample.utils.Constants;
import com.example.rxjavaretrofitdaggerexample.utils.StoryResponseConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Inject
    NewsApi newsApi;
    TextView textView;
    StoryDao storydao;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);

        StoryDatabase db = StoryDatabase.getInstance(getApplicationContext());
        storydao = db.storyDao();
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        addAll();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                insertDb((new Story(new Source("source_name"), "author", "title",
//                        "desc", "url", "pub")));
                deleteAll();
            }
        });
    }

    private void insertDb(Story story) {
        storydao.insertUser(story).subscribeOn(Schedulers.io()).subscribe();
    }

    private void deleteAll() {
        storydao.deleteAll().subscribeOn(Schedulers.io()).subscribe();
    }



    public Observable<Story> getLiveDataFromWeb(String searchKey) {
        Observable<StoryResponse> observable = newsApi.getPostsByDate(searchKey, Constants.getCurrentDate(),
                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
        return observable
                .subscribeOn(Schedulers.io())
                .flatMapIterable((Function<StoryResponse, Iterable<Story>>) storyResponse -> storyResponse.getArticles());
    }


//    private Completable insertDb(Story story) {
//        Log.d(Constants.TAG, "insertDb: " + story.getAuthor());
//        return storydao.insert(story);
//    }

    private Flowable<List<Story>> addAll() {
        Flowable flowable  = storydao.getAll();
        Log.d(Constants.TAG, "addAll: " + flowable.blockingFirst().toString());
        return flowable;
    }


}