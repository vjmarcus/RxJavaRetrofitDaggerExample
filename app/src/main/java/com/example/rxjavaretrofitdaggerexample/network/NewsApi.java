package com.example.rxjavaretrofitdaggerexample.network;

import com.example.rxjavaretrofitdaggerexample.model.StoryResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("everything")
    Observable<StoryResponse> getPostsByDate(@Query("q") String key,
                                             @Query("from") String fromDate,
                                             @Query("to") String toDate,
                                             @Query("pageSize") int pageSize,
                                             @Query("language") String language,
                                             @Query("apiKey") String apiKey);

    @GET("everything")
    Call<StoryResponse> getCallStoryResponse(@Query("q") String key,
                                             @Query("from") String fromDate,
                                             @Query("to") String toDate,
                                             @Query("pageSize") int pageSize,
                                             @Query("language") String language,
                                             @Query("apiKey") String apiKey);
}
