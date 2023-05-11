package com.example.smartmeterv3.HelperClass;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.PhoneAuthOptions;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersession;
    SharedPreferences.Editor editor;
    Context context;


    private static final String IS_LOGIN = "Isloggedin";

    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_PHONENUMBER = "phonenumber";
    public static final String KEY_PIN = "lpin";
    public static final String KEY_READING = "electricity_reading";
    public static final String KEY_LIMIT = "electricity_limit";
    public static final String KEY_PRICE = "electricity_price";

    public SessionManager(Context _context){
        context = _context;
        usersession = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = usersession.edit();
    }

    public void createLoginSession(String phone,String name,String lPin, String reading, String limit, String price){

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_PHONENUMBER, phone);
        editor.putString(KEY_FULLNAME,name);
        editor.putString(KEY_PIN,lPin);
        editor.putString(KEY_READING,reading);
        editor.putString(KEY_LIMIT,limit);
        editor.putString(KEY_PRICE,price);

        editor.commit();
    }

    public HashMap<String,String> getUserDetailsFromSessinon(){
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_PHONENUMBER,usersession.getString(KEY_PHONENUMBER,null));
        userData.put(KEY_FULLNAME,usersession.getString(KEY_FULLNAME,null));
        userData.put(KEY_PIN,usersession.getString(KEY_PIN,null));
        userData.put(KEY_READING,usersession.getString(KEY_READING,null));
        userData.put(KEY_LIMIT,usersession.getString(KEY_LIMIT,null));
        userData.put(KEY_PRICE,usersession.getString(KEY_PRICE,null));

        return userData;
    }

    public boolean checkLogin(){
        if (usersession.getBoolean(IS_LOGIN,false)){
            return true;
        }
        else
            return false;
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }
}
