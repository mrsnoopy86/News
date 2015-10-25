package ua.tremtyachiy.newsukraine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 25.10.2015.
 */
public class InternetConnection {

    /*Check internet connection*/
    public static boolean hasConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected())
        {
            return true;
        }
        info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null && info.isConnected())
        {
            return true;
        }
        info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
