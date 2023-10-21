package com.socialcodia.stockmanagement.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.models.ModelUser;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;


public class ContactUsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private ImageView btnWhatsapp, btnFacebook, btnInstagram,btnTwitter;
    private SharedPrefHandler sharedPrefHandler;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();


        actionBar = getSupportActionBar();
        actionBar.setTitle("Contact Us");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        btnWhatsapp.setOnClickListener(v -> {
            whatsapp();
        });

        btnFacebook.setOnClickListener(v -> {
            facebook();
        });

        btnInstagram.setOnClickListener(v -> {
            instagram();
        });

        btnTwitter.setOnClickListener(v -> {
            twitter();
        });

    }

    private void twitter()
    {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/"+getString(R.string.twitter_username))));
    }

    private void instagram()
    {
        String url = "http://instagram.com/u/"+getString(R.string.instagram_username);
        try {
            PackageManager packageManager = getPackageManager();
            packageManager.getPackageInfo("com.instagram.android",PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://instagram.com/u/"+getString(R.string.instagram_username))));
            e.printStackTrace();
        }
    }

    private void facebook()
    {
        String url = "fb://page/"+getString(R.string.facebook_username);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+getString(R.string.facebook_username))));
        }
    }

    private void whatsapp()
    {
        String contact = getString(R.string.owner_whatsapp_number);
        String url = "https://api.whatsapp.com/send?phone="+contact;
        try {
            PackageManager packageManager = getPackageManager();
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            TastyToast.makeText(getApplicationContext(),"Whatsapp not installed in your phone",TastyToast.LENGTH_LONG,TastyToast.ERROR);
        }
    }


    private void init()
    {
        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnInstagram = findViewById(R.id.btnInstagram);
        btnTwitter = findViewById(R.id.btnTwitter);

        sharedPrefHandler = SharedPrefHandler.getInstance(getApplicationContext());
        ModelUser mUser = sharedPrefHandler.getUser();
        token = mUser.getToken();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}