package com.tagcor.tagcor.RetrofitHelpers;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static final String BASE_URL = "http://tagcor.com/Classified_api/";

    public static final String B2B_BASE_URL = "http://tagcor.com/B2buser_api/";

    public static final String Jobs_BASE_URL = "http://tagcor.com/Jobs_api/";

    public static final String Professional_BASE_URL = "http://tagcor.com/Professional_api/";

    public static final String Member = "Member";
    //private static final String Member = "Member";

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder().addHeader("authorization", "Basic YWRtaW46MTIzNA==").build();
                return chain.proceed(request);
            }
        });
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).client(httpClient.build()).build();

    }

    public static ApiInterface getBaseClassService() {
        return getClient().create(ApiInterface.class);
    }

    //b2b retrofit client code

    public static Retrofit getClientB2B() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder().addHeader("authorization", "Basic YWRtaW46MTIzNA==").build();
                return chain.proceed(request);
            }
        });
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(B2B_BASE_URL).client(httpClient.build()).build();

    }

    public static ApiInterface getBaseClassService1() {
        return getClientB2B().create(ApiInterface.class);
    }

    //jobs retrofit client

    public static Retrofit getClientJobs() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder().addHeader("authorization", "Basic YWRtaW46MTIzNA==").build();
                return chain.proceed(request);
            }
        });
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Jobs_BASE_URL).client(httpClient.build()).build();

    }

    public static ApiInterface getBaseClassService2() {
        return getClientJobs().create(ApiInterface.class);
    }

    //Professional retrofit client

    public static Retrofit getClientProfessional() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder().addHeader("authorization", "Basic YWRtaW46MTIzNA==").build();
                return chain.proceed(request);
            }
        });
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Professional_BASE_URL).client(httpClient.build()).build();

    }

    public static ApiInterface getBaseClassService3() {
        return getClientProfessional().create(ApiInterface.class);
    }

}
