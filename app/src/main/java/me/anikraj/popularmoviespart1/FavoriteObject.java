package me.anikraj.popularmoviespart1;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by anik on 12/02/16.
 */
@SimpleSQLTable(table = "fav",provider = "FavoriteProvider")
public class FavoriteObject {
    @SimpleSQLColumn(value = "_id",primary = true)
    public int id;


    @SimpleSQLColumn("idd")
    public String idd;

    @SimpleSQLColumn("title")
    public String title;

    @SimpleSQLColumn("image")
    public String image;

    @SimpleSQLColumn("synopsis")
    public String synopsis;

    @SimpleSQLColumn("backdrop")
    public String backdrop;

    @SimpleSQLColumn("vote_avg")
    public double vote_avg;

    public FavoriteObject() {
    }

    public FavoriteObject(String id, String title, String image, String synopsis, String backdrop, double vote_avg, int vote_count, String date, boolean f) {
        this.idd = id;
        this.title = title;
        this.image = image;
        this.synopsis = synopsis;
        this.backdrop = backdrop;
        this.vote_avg = vote_avg;
        this.vote_count = vote_count;
        this.date = date;
        this.favorite=f;
    }

    @SimpleSQLColumn("vote_count")

    public int vote_count;

    @SimpleSQLColumn("date")
    public String date;

    @SimpleSQLColumn("favorite")
    public boolean favorite=false;
}
