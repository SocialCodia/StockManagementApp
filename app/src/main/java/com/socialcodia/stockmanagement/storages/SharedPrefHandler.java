package com.socialcodia.stockmanagement.storages;

import android.content.Context;
import android.content.SharedPreferences;

import com.socialcodia.stockmanagement.models.ModelUser;

public class SharedPrefHandler {
    private static final String SHARED_PREF_NAME = "AzmiUnaniStore";
    private static SharedPrefHandler mInstance;
    private static SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefHandler(Context context)
    {
        this.context = context;
    }

    public static synchronized SharedPrefHandler getInstance(Context context)
    {
        if (mInstance==null)
            mInstance = new SharedPrefHandler(context);
        return mInstance;
    }

    public void saveUser(ModelUser user)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.USER_ID,user.getId());
        editor.putString(Constants.USER_NAME,user.getName());
        editor.putString(Constants.USER_EMAIL,user.getEmail());
        editor.putString(Constants.USER_IMAGE,user.getImage());
        editor.putString(Constants.USER_TOKEN,user.getToken());
        editor.apply();
    }

    public void saveFirebaseToken(String firebaseToken)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firebaseToken",firebaseToken);
        editor.apply();
    }

    public String getFirebaseToken()
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("firebaseToken",null);
    }

    public ModelUser getUser()
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new ModelUser(
                sharedPreferences.getInt(Constants.USER_ID,-1),
                sharedPreferences.getString(Constants.USER_NAME,null),
                sharedPreferences.getString(Constants.USER_EMAIL,null),
                sharedPreferences.getString(Constants.USER_IMAGE,null),
                sharedPreferences.getString(Constants.USER_TOKEN,null)
    );
    }

    public boolean isUserLoggedIn()
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        int isLogin = sharedPreferences.getInt(Constants.USER_ID,-1);
        return isLogin != -1;
    }

    public boolean isFirebaseTokenSentToServer()
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(Constants.DEVICE_TOKEN,-1);
        return result!=-1;
    }

    public void setFirebaseTokenSentToServer()
    {
        sharedPreferences =  context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.DEVICE_TOKEN,1);
        editor.apply();
    }

    public void doLogout()
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
