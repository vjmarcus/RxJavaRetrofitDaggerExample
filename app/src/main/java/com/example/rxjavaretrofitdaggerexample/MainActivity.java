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
    StoryResponse storyResponse1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);

        StoryDatabase db = StoryDatabase.getInstance(getApplicationContext());
        storydao = db.storyDao();
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        getDataFromWeb("software").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull StoryResponse storyResponse) {
                        storyResponse1 = storyResponse;

                        insertDb(storyResponse1);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private Observable<StoryResponse> getDataFromWeb(String key) {
        return newsApi.getPostsByDate(key, Constants.getCurrentDate(),
                Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
    }

    private void insertDb(StoryResponse storyResponse) {
        Log.d(Constants.TAG, "insertDb: ");
        storydao.insert(storyResponse).subscribeOn(Schedulers.io()).subscribe();
    }

    private void deleteAll() {
        Log.d(Constants.TAG, "deleteAll: ");
        storydao.deleteAll().subscribeOn(Schedulers.io()).subscribe();
    }
}
