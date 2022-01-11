package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class How_to extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
        ImageView how_to_image=findViewById(R.id.imageView2);
        ImageButton btnback=findViewById(R.id.btnback);

        String putExtra_String= getIntent().getStringExtra("PAGE");
        if(putExtra_String.equals("international_call")){
            how_to_image.setImageResource(R.drawable.international_call);
            how_to_image.setContentDescription("국제 전화 차단하는 방법 , 첫번째, 해당 어플 설정의 국제전화 차단에 들어갑니다. 두번째, 국제 전화 차단 설정에 들어갑니다. 세번째, 국제 전화 수신 제한을 활성화 시켜주세요.");
        }
        else
            how_to_image.setImageResource(R.drawable.unknown_install);
            how_to_image.setContentDescription("출처를 알 수 없는 어플 설치 차단하는 방법, 첫번째 해당 어플 설정에서 출처를 알 수 없는 어플 설치 차단에 들어가주세요. 두번째, 파일 목록에서 허용됨으로 설정된 어플을 찾아 터치해주세요. 세번째, 이 출처 허용을 비활성화 해주세요.");

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}