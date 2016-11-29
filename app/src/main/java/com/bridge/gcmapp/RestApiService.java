package com.bridge.gcmapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApiService {

    @FormUrlEncoded
    @POST("api/users/login")
    Call<ResponseBody> login(@Field("user_id") String user_id, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/users/like/posts")
    Call<ResponseBody> like(@Field("user_id") String user_id, @Field("post_id") String post_id);

    @GET("api/users/like/posts")
    Call<ResponseBody> getLikes(@Query("user_id") String user_id);

    @FormUrlEncoded
    @DELETE("api/users/like/posts")
    Call<ResponseBody> unlike(@Field("user_id") String user_id, @Field("post_id") String post_id);
}
