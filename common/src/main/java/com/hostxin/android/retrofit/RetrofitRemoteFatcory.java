package com.hostxin.android.retrofit;

import com.google.gson.Gson;
import com.hostxin.android.common.NetworkReceiver;
import com.hostxin.android.util.Dbg;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRemoteFatcory implements NetworkReceiver.NetworkWatcher{

    private static final String TAG = "RetrofitRemoteFatcory";

    private static final RetrofitRemoteFatcory mInstence = new RetrofitRemoteFatcory();

    private static final GsonConverterFactory mGsonConverterFactory = GsonConverterFactory.create();

    private Gson gson = new Gson();

    private Cache mCache;

    private String cachePath;

    private AtomicBoolean mNetworkOn = new AtomicBoolean(false);

    private Retrofit createRetrofit(String baseUrl,Cache cache) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mLogInterceptor)
                .addNetworkInterceptor(mCacheInterceptor)
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(mGsonConverterFactory)
                .build();
        return retrofit;
    }

    private Retrofit createRetrofit(String baseUrl) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mLogInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(mGsonConverterFactory)
                .build();
        return retrofit;
    }

    private synchronized Cache getCache(){
        if(mCache == null){
            File file = new File(cachePath);
            Dbg.d(TAG,"cacheFile:"+file.getAbsolutePath());
            mCache = new Cache(file,1024*1024);
        }
        return mCache;
    }

    public void init(String cachePath,boolean isNetworkOn) {
        this.cachePath = cachePath;
        NetworkReceiver.addWatcher(this);
    }

    private Interceptor mLogInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long t1 = System.nanoTime();
            Dbg.d(TAG, "________"+t1+"________");
            Dbg.d(TAG, "Request:" + chain.request());
            Dbg.d(TAG, "Headers:" + chain.request().headers());
            Dbg.d(TAG, "RequestBody:" + chain.request().body());
            if (chain.request().body() instanceof FormBody) {
                Dbg.d(TAG, "Form:");
                FormBody body = (FormBody) chain.request().body();
                for (int i = 0; i < body.size(); i++) {
                    Dbg.d(TAG, "name:" + body.name(i) + ",value:" + body.value(i));
                }
            }

            Response response = chain.proceed(chain.request());

            long t2 = System.nanoTime();
            Dbg.d(TAG, "Response:" + response);
            Dbg.d(TAG, "ResponseBody:" + response.body());
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Dbg.d(TAG, "ResponseBodyString:" + responseBody.string());
            Dbg.d(TAG, "ReceiveTime:" + (t2 - t1) / 1e6d + "ms");
            Dbg.d(TAG, "________"+t1+"________");
            responseBody.close();
            responseBody = null;
            return response;
        }
    };

    private Interceptor mCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(mNetworkOn.get()){
                Dbg.d(TAG, "mCacheInterceptor mNetworkOn true");
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").build();
            }else{
                Dbg.d(TAG, "mCacheInterceptor mNetworkOn false");
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    @Override
    public void onNetChange(boolean isOn) {
        mNetworkOn.set(isOn);
    }

    public void setNetworkOn(boolean on) {
        this.mNetworkOn.set(on);
    }


}
