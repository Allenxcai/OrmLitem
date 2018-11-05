package com.allenxcai.biz;

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

    private Article article;
    private List<Article> articleList;
    private int num=0;
    OkHttpAgent okHttpAgent;


    public ArticleInstance() {
        articleList = new ArrayList<>();
        okHttpAgent = new OkHttpAgent();
        okHttpAgent.SetGetNetWorkDataListener(new OkHttpAgent.OnGetNetWorkDataListener.SimpleGetNetWorkDataListener() {
            @Override
            public void onSuccess(int code, String NetRawData) {

                Article articleTemp = new Article();
                try {
                    JSONObject jsonObject = new JSONObject(NetRawData);
                    articleTemp.setStatus(jsonObject.getInt("status"));
                    JSONObject detail = jsonObject.getJSONObject("data");
                    articleTemp.setContent(detail.getString("content"));
                    articleTemp.setAuthor(detail.getString("author"));
                    articleTemp.setTitle(detail.getString("title"));
                    articleTemp.setMsg(detail.getString("msg"));
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

                articleList.add(articleTemp);
            }

            @Override
            public void onFail(int code, String message) {

            }
        });

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

        Article articleTemp;

        if (articleList.size() > id) {
            articleTemp = articleList.get(id - 1);
        } else {

            String content = okHttpAgent.get("http://www.imooc.com/api/teacher?type=3&cid=" + id);
            articleTemp = new Article();

            if (content != null) {

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    articleTemp.setStatus(jsonObject.getInt("status"));
                    JSONObject detail = jsonObject.getJSONObject("data");
                    articleTemp.setContent(detail.getString("content"));
                    articleTemp.setAuthor(detail.getString("author"));
                    articleTemp.setTitle(detail.getString("title"));
                    articleTemp.setMsg(detail.getString("msg"));
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }


            }


        }


        return articleTemp;

    }

    public List<Article> getList(int start, int end) {

        for (int id = start; id <= end; id++) {

            okHttpAgent.response("http://www.imooc.com/api/teacher?type=3&cid=" + id);

        }
        return articleList;

    }
}

