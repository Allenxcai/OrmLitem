package com.allenxcai.util;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Project:OrmLitem
 * Author:Allenxcai
 * Date:2018/11/4/004
 * Description:
 */

public class OkHttpAgent {

    private static final String TAG = "axc-OkHttpAgent";

    private static OnGetNetWorkDataListener mlistener;

    private OkHttpClient mClient;

    private         String retStr;
    public volatile int    status;
    public static   int    ErrFailed  = 1;
    public static   int    ErrPassed  = 0;
    public static   int    ErrUnknown = -1;


    public OkHttpAgent() {
        this.mClient = new OkHttpClient();
    }


    public String response(final String urlstr,final int sequence) {


        status = ErrUnknown;
        Request.Builder builder = new Request.Builder();
        builder.url(urlstr);

        Request request = builder.build();

        Log.d(TAG, "response: " + request);

        Call call = mClient.newCall(request);
        retStr = null;

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
                status = ErrFailed;
                mlistener.onFail(-1, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                status = ErrPassed;
                int code = response.code();
                //                Headers headers = response.headers();
                //                String content = response.body().string();
                //                final StringBuilder buf = new StringBuilder();
                //                buf.append("code: " + code);
                //                buf.append("\nHeaders: \n" + headers);
                //                buf.append("\nbody: \n" + content);
                retStr = DataUtil.decode(response.body().string());


                mlistener.onSuccess(200, retStr,sequence);


            }
        });

        return retStr;
    }

    public String get(final String urlstr,final int sequence) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        retStr = null;
        status = ErrUnknown;
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                builder.url(urlstr);

                Request request = builder.build();

                Log.d(TAG, "run: " + request);

                Call call = mClient.newCall(request);
                try {
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        retStr = DataUtil.decode(response.body().string());
                        mlistener.onSuccess(100, retStr,sequence);
                        status = ErrPassed;
                    } else {
                        status = ErrFailed;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.shutdown();
        return retStr;
    }


    public void post(String url) {
        //        Request.Builder builder = new Request.Builder();
        //        builder.url(url);
        //        builder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, "Hello world github/linguist#1 **cool**, and #1!"));
        //        Request request = builder.build();
        //        Call call = mClient.newCall(request);
        //        call.enqueue(new Callback() {
        //            @Override
        //            public void onFailure(Call call, IOException e) {
        //
        //            }
        //
        //            @Override
        //            public void onResponse(Call call, Response response) throws IOException {
        //                if (response.isSuccessful()) {
        //                    final String content = response.body().string();
        //                    runOnUiThread(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            mContentTextView.setText(content);
        //                        }
        //                    });
        //                }
        //            }
        //        });


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                //builder.url("https://raw.githubusercontent.com/square/okhttp/master/README.md");
                builder.url("http://www.imooc.com/api/teacher?type=3&cid=1");

                Request request = builder.build();
                Log.d(TAG, "run: " + request);
                Call call = mClient.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        final String string = response.body().string();
                        //                        runOnUiThread(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        //                                mContentTextView.setText(string);
                        //                            }
                        //                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
    }


    public String getRetStr() {
        return retStr;
    }

    public void setRetStr(String retStr) {
        this.retStr = retStr;
    }

    public static void SetGetNetWorkDataListener(OnGetNetWorkDataListener listener) {
        mlistener = listener;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public interface OnGetNetWorkDataListener {

        void onStart();

        void onSuccess(int code, String NetRawData,int sequence);

        void onFail(int code, String message);

        void onProgress(int progress);

        public abstract class SimpleGetNetWorkDataListener implements OnGetNetWorkDataListener {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }
        }


    }

}


