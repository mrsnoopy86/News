package ua.tremtyachiy.newsukraine.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ua.tremtyachiy.newsukraine.database.DataBaseJSON;


public class AddDataToList {

    /*Create a list from database*/
    public static List<Item> getListOfNews(Context context){
        List<Item> list = new ArrayList<>();
        DataBaseJSON dataBaseJSON = new DataBaseJSON(context);
        SQLiteDatabase database = dataBaseJSON.getWritableDatabase();
        Cursor cursor = database.query("tablejsonparse", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            int indexTitle = cursor.getColumnIndex("title");
            int indexText = cursor.getColumnIndex("textnews");
            int indexUrlImage = cursor.getColumnIndex("imageurl");
            int indexUrlImageFull = cursor.getColumnIndex("imageurlfull");
            int indexUrlNews = cursor.getColumnIndex("urlnews");
            int indexTitleCompany = cursor.getColumnIndex("company");
            int indexTitleTime = cursor.getColumnIndex("timeago");
            do{
                list.add(new Item(cursor.getString(indexTitle), cursor.getString(indexText), cursor.getString(indexUrlImage), cursor.getString(indexUrlImageFull),
                        cursor.getString(indexUrlNews),cursor.getString(indexTitleCompany)," - " + cursor.getString(indexTitleTime)));
            } while (cursor.moveToNext());
        }
        dataBaseJSON.close();
        return list;
    }
}
