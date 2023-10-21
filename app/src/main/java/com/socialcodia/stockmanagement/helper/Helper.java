package com.socialcodia.stockmanagement.helper;

import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.SocialCodia;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

public class Helper extends SocialCodia {

    public static void playSuccess() {
        try {
            MediaPlayer player = MediaPlayer.create(getInstance(), R.raw.success);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playError() {
        try {
            MediaPlayer player = MediaPlayer.create(getInstance(), R.raw.error);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playWarning() {
        try {
            MediaPlayer player = MediaPlayer.create(getInstance(), R.raw.warning);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playBeep()
    {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getInstance(),R.raw.beep);
            mediaPlayer.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveFirebaseToken(String firebaseToken)
    {
        SharedPrefHandler.getInstance(getInstance()).saveFirebaseToken(firebaseToken);
    }

    public static String getFirebaseToken()
    {
        return SharedPrefHandler.getInstance(getInstance()).getFirebaseToken();
    }

    public static void playVibrateLong() {
        try {
            Vibrator vibrator = (Vibrator) getInstance().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playVibrate() {
        try {
            Vibrator vibrator = (Vibrator) getInstance().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable() {
        if (isNetworkOk())
            return true;
        else
        {
            TastyToast.makeText(getInstance(), "No Internet Connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            playWarning();
            playVibrate();
            return false;
        }
    }

    public static boolean isNetworkOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getInstance().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static String getToken() {
        return SharedPrefHandler.getInstance(getInstance()).getUser().getToken();
    }
}
