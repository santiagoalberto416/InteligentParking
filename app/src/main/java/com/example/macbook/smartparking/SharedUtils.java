package com.example.macbook.smartparking;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by santiagoalbertokirk on 21/10/17.
 */

public class SharedUtils {

    private final static String USER_ID = "id_user";
    private final static String USER_TYPE = "user_type";
    private final static String USER_PREFERENCES = "user_preferences";
    private static SharedUtils myObj;
    /**
     * Create private constructor
     */
    private SharedUtils(){

    }
    /**
     * Create a static method to get instance.
     */
    public static SharedUtils getInstance(){
        if(myObj == null){
            myObj = new SharedUtils();
        }
        return myObj;
    }

    public void setUserId(Context context, int id){
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(USER_ID, id );
        editor.commit();
    }

    public int getUserId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(USER_ID, 0);
    }

    public void setUserType(Context context, int type){
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(USER_TYPE, type);
        editor.commit();
    }

    public int getUserType(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getInt(USER_TYPE, 0);
    }


}




