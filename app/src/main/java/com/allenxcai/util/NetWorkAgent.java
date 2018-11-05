package com.allenxcai.util;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Author:Allenxcai
 * Date:2018/10/15/015
 * Description:
 */
public class NetWorkAgent {

    private static final String TAG = "NetworkAgent";
    private static String mResult;


    public NetWorkAgent() {

    }

    public static void GetNetWorkData(String url, OnGetNetWorkDataListener listener){
        GetNetWorkDataAsyncTask task = new GetNetWorkDataAsyncTask(url, listener);
        task.execute();
    }


    public static class GetNetWorkDataAsyncTask extends AsyncTask<String, Integer, Boolean> {

        String mUrl;
        int Errcode=0;
        Exception exception;
        OnGetNetWorkDataListener mListener;

        public GetNetWorkDataAsyncTask(String url, OnGetNetWorkDataListener listener) {

            mUrl =url;
            mListener = listener;
        }

        /**
         * 在异步任务之前，在主线程中
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 可操作UI  类似淘米,之前的准备工作
            if(mListener != null){
                mListener.onStart();
            }


        }

        /**
         * 在另外一个线程中处理事件
         *
         * @param params 入参  煮米
         * @return 结果
         */
        @Override
        protected Boolean doInBackground(String... params) {


            requestDataByGet(mUrl);



            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // 也是在主线程中 ，执行结果 处理
            mListener.onSuccess(0,mResult);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // 收到进度，然后处理： 也是在UI线程中。
            if (values != null && values.length > 0) {
                if(mListener != null){
                    mListener.onProgress(values[0]);
                }
            }

        }

    }


    public interface OnGetNetWorkDataListener{

        void onStart();

        void onSuccess(int code, String NetRawData);

        void onFail(int code, String message);

        void onProgress(int progress);

        public abstract class SimpleGetNetWorkDataListener implements OnGetNetWorkDataListener{
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }
        }



    }


    private static void requestDataByGet(String strURL) {
        try {
            //URL url = new URL("http://www.imooc.com/api/teacher?type=2&page=1");
            URL url = new URL(strURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30 * 1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.connect(); // 发起连接

            int responseCode = connection.getResponseCode();
            String reponseMessage = connection.getResponseMessage();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = connection.getInputStream();
                mResult = streamToString(inputStream);
                mResult = decode(mResult);

            } else {
                // TODO: error fail

                Log.e(TAG, "run: error code:" + responseCode + ", message:" + reponseMessage);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestDataByPost(String strURL) {
        try {

            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setUseCaches(false);
            connection.connect(); // 发起连接

            String data = "username=" + getEncodeValue("imooc") + "&number=" + getEncodeValue("150088886666");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            String reponseMessage = connection.getResponseMessage();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                mResult = streamToString(inputStream);

                mResult = decode(mResult);


            } else {
                // TODO: error fail

                Log.e(TAG, "run: error code:" + responseCode + ", message:" + reponseMessage);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private String getEncodeValue(String imooc) {

        String encode = null;
        try {
            encode = URLEncoder.encode(imooc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }


    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return 字符串
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * 将Unicode字符转换为UTF-8类型字符串
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


}
