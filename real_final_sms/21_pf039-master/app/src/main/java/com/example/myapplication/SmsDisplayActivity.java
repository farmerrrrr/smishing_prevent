package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SmsDisplayActivity extends AppCompatActivity {
    //객체 선언
    Button btnTitle, btnClose;
    TextView tvMsg;
    MainActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_display);

        //객체 초기화
        tvMsg = findViewById(R.id.tvMsg);
        btnTitle = findViewById(R.id.btnTitle);
        btnClose = findViewById(R.id.btnClose);



        //btnClose 기능 추가 : 창 닫기
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //onCreate에 있으면 새로 받은 문자가 갱신이 안된다.
        //인텐트 받기
        Intent displayIntent = getIntent();
        processIntent(displayIntent);
    }

    //새 문자를 받을때(이미 창이 만들어져 있어서 onCreate가 작동을 안할 때, 새 Intent를 받을 때) 작동
    //매개 변수에는 자동으로 갱신되는 인텐트가 들어간다.
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    //인텐트를 받아서 내용을 TextView에 출력하는 메서드
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void processIntent(Intent displayIntent) {
        String sender = displayIntent.getStringExtra("sender");
        String receivedDate = displayIntent.getStringExtra("receivedDate");
        String contents = displayIntent.getStringExtra("contents");
        //CatchWord(contents, "송금");
        //보낸 사람이 있으면
        if(sender != null) {
            btnTitle.setText(sender + "에서 문자 수신");
            tvMsg.setText("[" + receivedDate + "]\n" + contents);

        }
    }
}