package com.socialcodia.stockmanagement.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.socialcodia.stockmanagement.R;
import com.socialcodia.stockmanagement.activities.AboutUsActivity;
import com.socialcodia.stockmanagement.activities.ChangePasswordActivity;
import com.socialcodia.stockmanagement.activities.ContactUsActivity;
import com.socialcodia.stockmanagement.activities.EditProfileActivity;
import com.socialcodia.stockmanagement.activities.NotificationActivity;

public class SettingsFragment extends Fragment {

    private String[] settingList = {"Account","Notification","Change Password","Logout","Contact Us","About Us"};
    private String[] settingDesc = {"Update Profile, Update Information","Notification Setting, Device Verification","Change your account Password","Logout your account","Contact us for any query","About the developer"};
    private int[] settingIcons = {R.drawable.user,R.drawable.notification,R.drawable.password,R.drawable.logout,R.drawable.contact_us,R.drawable.info};
    private ListView listView;
    private ImageView ivSetting;
    private TextView tvSetting,tvDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        listView = view.findViewById(R.id.settingListView);

        SettingAdapter settingAdapter = new SettingAdapter();
        listView.setAdapter(settingAdapter);


        return view;
    }
    
    class SettingAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return settingList.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.row_setting,null);
            tvSetting = view.findViewById(R.id.tvSetting);
            tvDesc = view.findViewById(R.id.tvDesc);
            ivSetting  = view.findViewById(R.id.ivSetting);

            tvSetting.setText(settingList[i]);
            tvDesc.setText(settingDesc[i]);
            ivSetting.setImageResource(settingIcons[i]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (settingList[i])
                    {
                        case "Account":
                            sendToEditProfileActivity();
                            break;
                        case "Notification":
                            sendToNotificationActivity();
                            break;
                        case "Change Password":
                            sendToChangePasswordActivity();
                            break;
                        case "Contact Us":
                            sendToContactUsActivity();
                            break;
                        case "About Us":
                            sendToAboutUsActivity();
                            break;
                        default:
                            TastyToast.makeText(getContext(),"Feature will come soon",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
                    }
                }
            });


            return view;
        }
    }

    private void sendToNotificationActivity() {
        startActivity(new Intent(getContext(), NotificationActivity.class));
    }

    private void sendToAboutUsActivity() {
        startActivity(new Intent(getContext(), AboutUsActivity.class));
    }

    private void sendToContactUsActivity() {
        startActivity(new Intent(getContext(), ContactUsActivity.class));
    }

    private void sendToChangePasswordActivity()
    {
        startActivity(new Intent(getContext(), ChangePasswordActivity.class));
    }

    private void sendToEditProfileActivity(){
            startActivity(new Intent(getContext(), EditProfileActivity.class));
    }


}