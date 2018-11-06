package com.allenxcai.ormlitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenxcai.bean.Article;
import com.allenxcai.biz.ArticleInstance;
import com.allenxcai.util.DataUtil;
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

    Button btn_last, btn_favorite, btn_next;
    TextView tv_title, tv_author, tv_articleDetail, tv_myFavorite;
    ImageView iv_ads;
    private static final String TAG = "MainActivity";

    private ArticleInstance articleInstance;
    private Article article;
    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvent();


        articleInstance = new ArticleInstance();
        getArticle(curPage);


        articleInstance.getList(1, 30);
        
        getAds();


    }

    private void initEvent() {
        articleInstance.setMlistener(new ArticleInstance.OnGetInstance.SimpleOnGetInstance() {

            @Override
            public void onSuccess(int code) {

                if (code == 100) {

                    article = articleInstance.getArticle();
                    onGetResult();
                }
                
                if(code ==200)
                {

                    Log.d(TAG, "onSuccess: zzzzzz articleInstance size:"+articleInstance.getArticleList().size()+":"+articleInstance.toString());
                }


            }
        });


    }

    private void initViews() {
        btn_last = findViewById(R.id.btn_last);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_next = findViewById(R.id.btn_next);
        tv_title = findViewById(R.id.tv_title);
        tv_articleDetail = findViewById(R.id.tv_articleDetail);
        tv_author = findViewById(R.id.tv_author);
        tv_myFavorite = findViewById(R.id.tv_myFavorite);

    }


    public void operate(View view) {
        Intent it;

        switch (view.getId()) {

            case R.id.btn_last:
                curPage = (curPage <= 1) ? 30 : curPage - 1;
                getArticle(curPage);


                break;
            case R.id.btn_next:

                curPage = (curPage == 30) ? 1 : curPage + 1;
                getArticle(curPage);


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


        articleInstance.get(page);


    }

    private void onGetResult() {

        tv_author.setText(article.getAuthor());
        tv_articleDetail.setText(article.getContent());
        tv_title.setText(article.getTitle());
    }

    private void getAds() {

    }

    private void addToMyFavorite() {
    }


}
