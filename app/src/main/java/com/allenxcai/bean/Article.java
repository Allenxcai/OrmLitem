package com.allenxcai.bean;

/**
 * Project:OrmLitem
 * Author:Allenxcai
 * Date:2018/11/5/005
 * Description:
 */
public class Article {

    private int status;
    private String title;
    private String author;
    private String content;
    private String msg;

    public Article() {
    }

    public Article(int status, String title, String author, String content, String msg) {
        this.status = status;
        this.title = title;
        this.author = author;
        this.content = content;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}


