package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    myDBHelper myHelper;
    SettingFragment SettingFragment;
    YoutubeFragement YoutubeFragement;
    MainFragment MainFragment;
    InformationFragment InformationFragment;
    SQLiteDatabase sqlDB;

    ListView listView;
    SMSAdapter SMSAdapter;
    ArrayList<SMSItem> SMSItems= new ArrayList<>();

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean sharedPreferences = SharedPreferencesManager.get_first_app_execute(this,"first");
        Log.d("lll", "sharedPreferences=>"+sharedPreferences);
        if (sharedPreferences==true) {
            //튜토리얼로 넘어가기
            startActivity(new Intent(this, TutorialAcitivity.class));
        }

        setContentView(R.layout.activity_main);

        checkDangerousPermissions();

        YoutubeFragement = new YoutubeFragement();
        SettingFragment = new SettingFragment();
        MainFragment= new MainFragment();
        InformationFragment=new InformationFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment).commit();
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        MenuItem first_btn = bottom_menu.getMenu().getItem(0);
        MenuItem second_btn = bottom_menu.getMenu().getItem(1);
        MenuItem third_btn = bottom_menu.getMenu().getItem(2);

        MenuItem forth_btn = bottom_menu.getMenu().getItem(3);


        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.first_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment).commit();
                        item.setIcon(R.drawable.home_hover);
                        second_btn.setIcon(R.drawable.youtube_btn);
                        third_btn.setIcon(R.drawable.setting_btn);
                        forth_btn.setIcon(R.drawable.information_btn);
                        return true;
                    case R.id.second_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, YoutubeFragement).commit();
                        item.setIcon(R.drawable.youtube);
                        first_btn.setIcon(R.drawable.home);
                        third_btn.setIcon(R.drawable.setting_btn);
                        forth_btn.setIcon(R.drawable.information_btn);

                        return true;
                    case R.id.third_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, SettingFragment).commit();
                        item.setIcon(R.drawable.setting);
                        second_btn.setIcon(R.drawable.youtube_btn);
                        first_btn.setIcon(R.drawable.home);
                        forth_btn.setIcon(R.drawable.information_btn);

                        return true;
                    case R.id.forth_tab:
                        startActivity(new Intent(MainActivity.this, TutorialAcitivity.class));
                        item.setIcon(R.drawable.information_hover);
                        first_btn.setIcon(R.drawable.home);
                        second_btn.setIcon(R.drawable.youtube_btn);
                        third_btn.setIcon(R.drawable.setting_btn);

                        return true;
                }
                return false;
            }
        });

    }


    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {

            super(context, "Smishing_DB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS guide_TBL (guide_Num CHAR(20) PRIMARY KEY, guide_Name CHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS guide_TBL");
            onCreate(db);
        }
    }


    //위험 권한 체크
    //manifest와 java에 둘 다 권한 허가받는 코드를 작성한다.
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



}