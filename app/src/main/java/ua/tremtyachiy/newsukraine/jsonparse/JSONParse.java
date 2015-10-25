package ua.tremtyachiy.newsukraine.jsonparse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.tremtyachiy.newsukraine.database.DataBaseJSON;


public class JSONParse {
    DataBaseJSON dataBaseJSON;
    private String urlLoad = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=ukraine&ned=ru_ru&rsz=8&scoring=d";

    /*Create a String line from JSON*/
    private String getJSONLine(String URL){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = "";
        try {
            URL url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*input result from String line in database*/
    public void getInformation(Context context){
        Date date = new Date();
        SimpleDateFormat formatOut = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        SimpleDateFormat formatIn = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
        String imageUrl;
        String imageUrlFull;
        ContentValues content;
        dataBaseJSON = new DataBaseJSON(context);
        SQLiteDatabase database = dataBaseJSON.getWritableDatabase();
        try {
            JSONObject dataJsonObj = new JSONObject(getJSONLine(urlLoad));
            JSONObject dataJson = dataJsonObj.getJSONObject("responseData");
            JSONArray result = dataJson.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                imageUrl = "";
                imageUrlFull = "";
                content = new ContentValues();
                JSONObject news = result.getJSONObject(i);
                try {
                    JSONObject image = news.getJSONObject("image");
                    imageUrl = image.getString("tbUrl");
                    imageUrlFull = image.getString("url");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                String text = Html.fromHtml(news.getString("content")).toString();
                String title = Html.fromHtml(news.getString("titleNoFormatting")).toString();
                String company = Html.fromHtml(news.getString("publisher")).toString();
                String time = Html.fromHtml(news.getString("publishedDate")).toString();
                String urlNews = news.getString("unescapedUrl");
                try {
                    date = formatIn.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!searchSameItem(database, urlNews)) {
                    content.put("title", title);
                    content.put("textnews", text);
                    content.put("imageurl", imageUrl);
                    content.put("imageurlfull", imageUrlFull);
                    content.put("urlnews", urlNews);
                    content.put("company", company);
                    content.put("timeago", formatOut.format(date));
                    database.insert("tablejsonparse", null, content);
                }
            }
            dataBaseJSON.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*Check if database has the same news*/
    private boolean searchSameItem(SQLiteDatabase sqLiteDatabase, String urlNews){
        List<String> listUrl = new ArrayList<>();
        boolean result = false;
        Cursor cursor = null;
        if(sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query("tablejsonparse", null, null, null, null, null, null);
        }
        if(cursor != null & cursor.moveToFirst()){
            int indexUrlNews = cursor.getColumnIndex("urlnews");
            do{
                listUrl.add(cursor.getString(indexUrlNews));
            } while (cursor.moveToNext());
        }
        if(listUrl.contains(urlNews)){
            result = true;
        }
        return result;
    }
}
