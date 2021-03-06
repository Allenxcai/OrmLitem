package com.allenxcai.biz;

import android.util.Log;

import com.allenxcai.bean.Article;
import com.allenxcai.util.OkHttpAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Project:OrmLitem
 * Author:Allenxcai
 * Date:2018/11/5/005
 * Description:
 */
public class ArticleInstance {

    private static final String        TAG = "ArticleInstance-axc";
    private              Article       article;
    private              List<Article> articleList;

    OkHttpAgent okHttpAgent;
    private static ArticleInstance.OnGetInstance mlistener;

    public static void setMlistener(OnGetInstance mlistener) {
        ArticleInstance.mlistener = mlistener;
    }

    public ArticleInstance() {
        articleList = new ArrayList<>();
        okHttpAgent = new OkHttpAgent();
        okHttpAgent.SetGetNetWorkDataListener(new OkHttpAgent.OnGetNetWorkDataListener.SimpleGetNetWorkDataListener() {
            @Override
            public void onSuccess(int code, String NetRawData,int sequence) {


                Article articleTemp = new Article();
                try {
                    JSONObject jsonObject = new JSONObject(NetRawData);
                    articleTemp.setStatus(jsonObject.getInt("status"));
                    JSONObject detail = jsonObject.getJSONObject("data");
                    articleTemp.setContent(detail.getString("content"));
                    articleTemp.setAuthor(detail.getString("author"));
                    articleTemp.setTitle(sequence+"."+detail.getString("title"));
                    //articleTemp.setMsg(detail.getString("msg"));
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }


                updateArticle(code, articleTemp,sequence);

                mlistener.onSuccess(code,sequence); //notice UI


            }

            @Override
            public void onFail(int code, String message) {
                Log.d(TAG, "onFail: ");
            }
        });

    }

    private synchronized void updateArticle(int code, Article articleTemp,int sequence) {
        if (code == 200) {
            article = articleTemp;
            Log.d(TAG, "updateArticle id: "+sequence);
            //articleList.add(articleTemp);
            articleList.set(sequence-1,articleTemp);
        } else
            article = articleTemp;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }


    public Article get(int id) {

        Article articleTemp = null;

        if (articleList.size() > id && articleList.get(id - 1).getStatus() != 0) {

            articleTemp = articleList.get(id - 1);
            article = articleTemp;
            Log.d(TAG, "get: xxxxxxx");


            mlistener.onSuccess(100,id); //notice UI

        } else {
            Log.d(TAG, "get: xxxxxyyyyyy" + id);

            okHttpAgent.get("http://www.imooc.com/api/teacher?type=3&cid=" + id,id);

        }


        return articleTemp;

    }

    public List<Article> getList(int start, int end) {

           while (articleList.size()<end)
            articleList.add(new Article());



        for (int id = start; id <= end; id++) {

            okHttpAgent.response("http://www.imooc.com/api/teacher?type=3&cid=" + id,id);
            Log.d(TAG, "getList: " + id);

        }
        return articleList;

    }


    public interface OnGetInstance {


        void onSuccess(int code,int sequence);

        void onFail(int code, String message);


        public abstract class SimpleOnGetInstance implements OnGetInstance {

            @Override
            public void onFail(int code, String message) {

            }
        }


    }


    @Override
    public String toString() {

        String ret = "";

        for (int i = 1; i <= articleList.size(); i++) {

            ret = i + articleList.get(i).getTitle() + "\n";

        }

        return ret;

    }
}

