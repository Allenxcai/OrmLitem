package com.allenxcai.ormlitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenxcai.util.OkHttpAgent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final int currentArticle = 1;
    Button btn_last,btn_favorite,btn_next;
    TextView tv_title,tv_author,tv_articleDetail,tv_myFavorite;
    ImageView iv_ads;

    private final OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getAds();
    }

    private void initViews() {
        btn_last =findViewById(R.id.btn_last);
        btn_favorite =findViewById(R.id.btn_favorite);
        btn_next =findViewById(R.id.btn_next);
        tv_title = findViewById(R.id.tv_title);
        tv_articleDetail = findViewById(R.id.tv_articleDetail);
        tv_author = findViewById(R.id.tv_author);
        tv_myFavorite=findViewById(R.id.tv_myFavorite);

    }


    public void operate(View view) {
        Intent it;

        switch (view.getId()) {

            case R.id.btn_last:
                getArticle(currentArticle - 1);
                break;
            case R.id.btn_next:
                getArticle(currentArticle + 1);

                break;
            case R.id.btn_favorite:

                addToMyFavorite();

                break;

            case R.id.tv_myFavorite:
                it = new Intent(this, favorite.class);
                startActivity(it);
                break;
        }
    }

    private void getArticle(int page) {


        for (int i = 0; i <10 ; i++) {

            ;

        }

        return null;

    }

    private void getAds() {
    }

    private void addToMyFavorite() {
    }


}
