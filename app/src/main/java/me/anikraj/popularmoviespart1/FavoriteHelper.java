package me.anikraj.popularmoviespart1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoriteHelper {
    public static void createTodo(ContentResolver resolver, FavoriteObject item) {
        Uri uri = resolver.insert(FavTable.CONTENT_URI, FavTable.getContentValues(item, false));
        if (uri != null) {
            Log.d("contentprovider", "added new task");
        }
    }
    public static int deleteTodo(ContentResolver resolver, FavoriteObject item) {
        int result = resolver.delete(FavTable.CONTENT_URI, FavTable.FIELD__ID + "=?", new String[]{String.valueOf(item.idd)});
        if (result > 0) {
            Log.d("contentprovider", "deleted task");
        }
        return result;
    }

    public static FavoriteObject getTodo(ContentResolver resolver, int id) {
        Cursor cursor = resolver.query(FavTable.CONTENT_URI, null, FavTable.FIELD__ID + "=?", new String[]{String.valueOf(id)}, null);
        if(cursor != null && cursor.moveToFirst()){
        return FavTable.getRow(cursor, true);}
        else {
        Log.d("contentprovider", "null not found "+id);return null;}
    }

    public static FavoriteObject getTodo(ContentResolver resolver, String idd) {
        Cursor cursor = resolver.query(FavTable.CONTENT_URI, null, FavTable.FIELD_TITLE + "=?", new String[]{String.valueOf(idd)}, null);
        if(cursor != null && cursor.moveToFirst()){

            FavoriteObject temp =FavTable.getRow(cursor, true);//cursor.close();
            return temp;}
        else { cursor.close();
            Log.d("contentprovider", "null not found "+idd);return null;}
    }

    public static void deleteTodo2(ContentResolver resolver, String idd) {
        int result = resolver.delete(FavTable.CONTENT_URI, FavTable.FIELD_TITLE + "=?", new String[]{String.valueOf(idd)});
        if (result > 0) {
            Log.d("contentprovider", "deleted task");
        }
    }
    public static void listall(ContentResolver resolver) {
        Cursor cursor = resolver.query(FavTable.CONTENT_URI, null, FavTable.FIELD__ID, null, null);
        if(cursor != null && cursor.moveToFirst()){
            for(int i=0;i<cursor.getCount();i++)
            Log.d("contentprovider", "found "+ FavTable.getRow(cursor, true).title );

        }

    }

    public static ArrayList<GridItem> getall(ContentResolver resolver) {
        Cursor cursor = resolver.query(FavTable.CONTENT_URI, null, FavTable.FIELD_FAVORITE , null, null);
        if(cursor != null && cursor.moveToFirst()){
            ArrayList<GridItem> items = new ArrayList<>();
            for(int i=0;i<cursor.getCount();i++) {
                FavoriteObject xx=FavTable.getRow(cursor, false);
                items.add(new GridItem(xx.idd + "", xx.image, xx.title, xx.title, xx.synopsis, xx.vote_avg, xx.vote_count, xx.backdrop, xx.date));
               // Log.d("anik", items.get(i).getId());

                Log.d("anik",items.get(i).getDate());
                Log.d("anik",items.get(i).getImage());
                Log.d("anik",items.get(i).getSynopsys());
                Log.d("anik",items.get(i).getTitle());
                Log.d("anik",items.get(i).getVideo());
                Log.d("anik",items.get(i).getVote_avg()+"");
                Log.d("anik",items.get(i).getVote_count()+"");
                Log.d("anik", items.get(i).getId());Log.d("anik", items.get(i).getBackdrop());

                cursor.moveToNext();
            }
            cursor.close();
            return items;
        }
        return null;

    }
    public static void listall2(ContentResolver resolver) {
        Cursor cursor  = resolver.query(FavTable.CONTENT_URI, null,FavTable.FIELD_FAVORITE , null,null);
        Log.d("contentprovider", "found "+cursor.getCount());

    }
}
