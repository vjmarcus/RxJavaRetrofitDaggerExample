package com.example.rxjavaretrofitdaggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rxjavaretrofitdaggerexample.db.StoryDao;
import com.example.rxjavaretrofitdaggerexample.db.StoryDatabase;
import com.example.rxjavaretrofitdaggerexample.model.Story;
import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;
import com.example.rxjavaretrofitdaggerexample.network.NewsApi;
import com.example.rxjavaretrofitdaggerexample.utils.Constants;
import com.example.rxjavaretrofitdaggerexample.utils.StoryResponseConverter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Inject
    NewsApi newsApi;
    TextView textView;
    StoryDao storydao;
    Button button;
    boolean isFromWeb = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);

        StoryDatabase db = StoryDatabase.getInstance(getApplicationContext());
        storydao = db.storyDao();
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFromWeb) {
                    getDataFromWeb("software")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<StoryResponse>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    Log.d(Constants.TAG, "onSubscribe: ");
                                }

                                @Override
                                public void onNext(@NonNull StoryResponse storyResponse) {
                                    Log.d(Constants.TAG, "onNext: from web size is " + storyResponse.getArticles().size());
                                    insertDb(storyResponse);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d(Constants.TAG, "onError: ");
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(Constants.TAG, "onComplete: ");
                                }
                            });
                    isFromWeb = false;
                } else {
                    getLastStoryResponse()
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<StoryResponse>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    Log.d(Constants.TAG, "onSubscribe: ");
                                }

                                @Override
                                public void onNext(@NonNull StoryResponse storyResponse) {
                                    Log.d(Constants.TAG, "onNext from db: size is" + storyResponse.getArticles().size());
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.d(Constants.TAG, "onError: ");
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(Constants.TAG, "onComplete: ");
                                }
                            });
                    isFromWeb = true;
                }
            }
        });
    }


    private Observable<StoryResponse> getDataFromWeb(String key) {
        return newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
    }

    private void insertDb(StoryResponse storyResponse) {
        Log.d(Constants.TAG, "insertDb: size = " + storyResponse.getArticles().size());
        storydao.insert(storyResponse).subscribeOn(Schedulers.io()).subscribe();
    }

    private void deleteAll() {
        Log.d(Constants.TAG, "deleteAll: ");
        storydao.deleteAll().subscribeOn(Schedulers.io()).subscribe();
    }

    private Flowable<StoryResponse> getLastStoryResponse() {
        Log.d(Constants.TAG, "getLastStoryResponse: ");
        return storydao.getLastAddedResponse();
    }

}
