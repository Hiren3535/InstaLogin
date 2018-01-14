package com.hirenpatel.instalogin.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hirenpatel on 13/01/18.
 */

public class AppConstants {

    public static class InstaData{
        public static final String CLIENT_ID = "02ec149544bc434d9c06c220a6c4be91";
        public static final String CLIENT_SECRET = "45bcb760ca4a4705a8b5d592836ac02a";
        public static final String CALLBACK_URL = "http://localhost";
    }

    public static class InstagramSession {

        public static SharedPreferences sharedPref;
        public static SharedPreferences.Editor editor;

        public static final String SHARED = "Instagram_Preferences";
        public static final String FirstLogin = "FirstLogin";
        public static final String API_USERNAME = "username";
        public static final String API_ID = "id";
        public static final String API_NAME = "name";
        public static final String API_ACCESS_TOKEN = "access_token";

        public InstagramSession(Context context) {
            sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
            editor = sharedPref.edit();
        }
        public void storeAccessToken(String accessToken, String id, String username, String name) {
            editor.putString(API_ID, id);
            editor.putString(API_NAME, name);
            editor.putString(API_ACCESS_TOKEN, accessToken);
            editor.putString(API_USERNAME, username);
            editor.commit();
        }
        public void storeAccessToken(String accessToken) {
            editor.putString(API_ACCESS_TOKEN, accessToken);
            editor.commit();
        }
        public void resetAccessToken() {
            editor.putString(API_ID, null);
            editor.putString(API_NAME, null);
            editor.putString(API_ACCESS_TOKEN, null);
            editor.putString(API_USERNAME, null);
            editor.commit();
        }
        public String getUsername() {
            return sharedPref.getString(API_USERNAME, null);
        }
        public String getId() {
            return sharedPref.getString(API_ID, null);
        }
        public String getName() {
            return sharedPref.getString(API_NAME, null);
        }
        public String getAccessToken() {
            return sharedPref.getString(API_ACCESS_TOKEN, null);
        }
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }
}
