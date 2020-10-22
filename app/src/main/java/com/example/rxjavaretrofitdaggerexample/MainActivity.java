package com.example.rxjavaretrofitdaggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Inject
    NewsApi newsApi;
    TextView textView;
    StoryDao storydao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().injectMainActivity(this);

        StoryDatabase db = StoryDatabase.getInstance(getApplicationContext());
        storydao = db.storyDao();

        textView = findViewById(R.id.textView);
        getDataFromWeb("software")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<StoryResponse>() {

                    @Override
                    public void onNext(@NonNull StoryResponse storyResponse) {
                        Log.d(Constants.TAG, "onNext: " + storyResponse.getArticles().get(0).getAuthor());
                        textView.setText(storyResponse.getArticles().get(0).getAuthor());
                        insertWithAsyncTask(storyResponse);
                        insertDb(storyResponse);
                        StoryResponseConverter storyResponseConverter = new StoryResponseConverter();
                        String articles = storyResponseConverter.fromStoryResponse(storyResponse.getArticles());
                        Log.d(Constants.TAG, "onNext: ==="
                                + articles);
                        List<Story> stories = storyResponseConverter.fromString(articles);
                        Log.d(Constants.TAG, "onNext: " + stories.size());


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

        getLastStoryResponseFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<StoryResponse>>() {
                    @Override
                    public void onSuccess(@NonNull List<StoryResponse> storyResponses) {
                        Log.d(Constants.TAG, "onSuccess:>>> " + storyResponses.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });


    }

    private void insertWithAsyncTask(StoryResponse storyResponse) {
        new InsertToDbAsyncTask().execute(storyResponse);

    }

    private Observable<StoryResponse> getDataFromWeb(String key) {
       return  newsApi.getPostsByDate(key, Constants.getCurrentDate(),
               Constants.getCurrentDate(), 20, "en", Constants.API_KEY);
    }

    private Completable insertDb(StoryResponse storyResponse) {
        Log.d(Constants.TAG, "insertDb: ");
        return storydao.insert(storyResponse)
                .subscribeOn(Schedulers.io());
    }

    private Single<List<StoryResponse>> getLastStoryResponseFromDb() {
        return storydao.getListOfResponse();
    }

    public class InsertToDbAsyncTask extends AsyncTask<StoryResponse, Void, Void> {
        @Override
        protected Void doInBackground(StoryResponse... storyResponses) {
            storydao.insert(storyResponses[0]);
            Log.d(Constants.TAG, "doInBackground: worked");
            return null;
        }
    }


}