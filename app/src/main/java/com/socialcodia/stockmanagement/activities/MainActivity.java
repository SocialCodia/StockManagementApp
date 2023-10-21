package com.socialcodia.stockmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.apis.ApiClient;
import com.socialcodia.stockmanagement.fragments.AddAdminFragment;
import com.socialcodia.stockmanagement.fragments.AddProductFragment;
import com.socialcodia.stockmanagement.fragments.AddProductInfoFragment;
import com.socialcodia.stockmanagement.fragments.AddSellerFragment;
import com.socialcodia.stockmanagement.fragments.AdminsFragment;
import com.socialcodia.stockmanagement.fragments.CreditorsFragment;
import com.socialcodia.stockmanagement.fragments.CreditsFragment;
import com.socialcodia.stockmanagement.fragments.DashboardFragment;
import com.socialcodia.stockmanagement.fragments.ExpiredProductsFragment;
import com.socialcodia.stockmanagement.fragments.ExpiringProductsFragment;
import com.socialcodia.stockmanagement.fragments.InvoicesFragment;
import com.socialcodia.stockmanagement.fragments.ProductsFragment;
import com.socialcodia.stockmanagement.fragments.ProductsNoticeFragment;
import com.socialcodia.stockmanagement.fragments.ProductsRecordFragment;
import com.socialcodia.stockmanagement.fragments.SalesAllFragment;
import com.socialcodia.stockmanagement.fragments.SalesTodayFragment;
import com.socialcodia.stockmanagement.fragments.SellProductFragment;
import com.socialcodia.stockmanagement.fragments.SellToSellerFragment;
import com.socialcodia.stockmanagement.fragments.SellersFragment;
import com.socialcodia.stockmanagement.fragments.SettingsFragment;
import com.socialcodia.stockmanagement.helper.Helper;
import com.socialcodia.stockmanagement.pojos.ResponseDefault;
import com.socialcodia.stockmanagement.storages.SharedPrefHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "101";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private SharedPrefHandler sp;
    private boolean doublePressToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment fragment = new DashboardFragment();
        setFragment(fragment);


        bottomNavigationView.setItemIconTintList(null);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        sp = SharedPrefHandler.getInstance(getApplicationContext());
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment1;
            switch (id)
            {
                case R.id.miDashboard:
                    fragment1 = new DashboardFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Dashboard");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miSellProduct:
                    fragment1 = new SellProductFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Sell Product");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miSalesToday:
                    fragment1 = new SalesTodayFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Sales Today");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miSalesAll:
                    fragment1 = new SalesAllFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("All Sales");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miProducts:
                    fragment1 = new ProductsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Products");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miProductsRecord:
                    fragment1 = new ProductsRecordFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Products Record");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miProductsNotice:
                    fragment1 = new ProductsNoticeFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Products Notice");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miExpiringProducts:
                    fragment1 = new ExpiringProductsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Expiring Products");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miExpiredProducts:
                    fragment1 = new ExpiredProductsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Expired Products");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miSellToSeller:
                    fragment1 = new SellToSellerFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Sell To Seller");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miCredits:
                    fragment1 = new CreditsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Credits");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miCreditors:
                    fragment1 = new CreditorsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Creditors");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miInvoices:
                    fragment1 = new InvoicesFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Invoices");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miSellers:
                    fragment1 = new SellersFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Sellers");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAddSeller:
                    fragment1 = new AddSellerFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Add Seller");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAdmins:
                    fragment1 = new AdminsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Admins");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAddAdmin:
                    fragment1 = new AddAdminFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Add Admin");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAddProduct:
                    fragment1 = new AddProductFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Add Product");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAddProductInfo:
                    fragment1 = new AddProductInfoFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Add Product Info");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miLogout:
                    doLogout();
                    break;
                case R.id.miSetting:
                    fragment1 = new SettingsFragment();
                    setFragment(fragment1);
                    actionBar.setTitle("Setting");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miAboutUs:
                    sendToAboutUsActivity();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.miContactUs:
                    sendToContactUsActivity();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Working On It", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment12;
            switch (id)
            {
                case R.id.miDashboard:
                    fragment12 = new DashboardFragment();
                    setFragment(fragment12);
                        actionBar.setTitle("Dashboard");
                    break;
                case R.id.miProducts:
                    fragment12 = new ProductsFragment();
                    setFragment(fragment12);
                        actionBar.setTitle("Products");
                    break;
                case R.id.miSalesToday:
                    fragment12 = new SalesTodayFragment();
                    setFragment(fragment12);
                        actionBar.setTitle("Today Sales");
                    break;
                case R.id.miSellProduct:
                    fragment12 = new SellProductFragment();
                    setFragment(fragment12);
                    actionBar.setTitle("Sell Product");
                    break;
                case R.id.miCredits:
                    fragment12 = new CreditsFragment();
                    setFragment(fragment12);
                    actionBar.setTitle("Credits");
                    break;
                default:
                    Toast.makeText(MainActivity.this,"Working",Toast.LENGTH_LONG).show();
            }
            return true;
        });

        createNotificationChannel();
        getToken();

        FirebaseMessaging.getInstance().subscribeToTopic("authenticated");

        if (!sp.isFirebaseTokenSentToServer())
        {
            updateFirebaseToken();
        }

    }

    private void updateFirebaseToken()
    {
        if (Helper.isNetworkAvailable())
        {
            Call<ResponseDefault> call = ApiClient.getInstance().getApi().updateFirebaseToken(Helper.getToken(),Helper.getFirebaseToken());
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    if (response.isSuccessful())
                    {
                        sp.setFirebaseTokenSentToServer();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    Helper.saveFirebaseToken(token);
                    Log.d("FirebaseToken", "onComplete: "+token);
                }
            }
        });
    }


    private void sendToAboutUsActivity() {
        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
    }

    private void sendToContactUsActivity() {
        startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SocialCodia";
            String description = "Receive Notification From The Official Website On Specific Events";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onBackPressed() {
        if (doublePressToExit)
            super.onBackPressed();
        Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show();
        this.doublePressToExit = true;

        Handler handler = new Handler();
        handler.postDelayed(() -> doublePressToExit = false,2000);
    }

    private void doLogout()
    {
        sp.doLogout();
        sendToLogin();
    }

    public void sendToLogin()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    public void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }
}