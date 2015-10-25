package ua.tremtyachiy.newsukraine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseJSON extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "tablejsonparse";
    private static final String DATABASE = "database";

    public DataBaseJSON(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + " ("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "textnews text,"
                + "imageurl text,"
                + "imageurlfull text,"
                + "urlnews text,"
                + "company text,"
                + "timeago text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }
}
