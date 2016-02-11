package me.anikraj.popularmoviespart1;

/**
 * Created by anik on 09/02/16.
 */
public class ReviewItem {
    String author, content;

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
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
}
