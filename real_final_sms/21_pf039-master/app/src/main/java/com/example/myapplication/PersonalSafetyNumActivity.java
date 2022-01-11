package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalSafetyNumActivity extends AppCompatActivity {
    ImageButton btnback;
    Button btnInsert, btnInit, btnSelect;
    EditText edtNum, edtNumResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_safety_number);

        btnback = (ImageButton) findViewById(R.id.btnback);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnInit = (Button) findViewById(R.id.btnInit);

        edtNum = (EditText) findViewById(R.id.edtNumber);
        edtNumResult = (EditText) findViewById(R.id.edtNumberResult);
        edtNumResult.setFocusable(false);
        edtNumResult.setClickable(false);

        String SafeNum = SharedPreferencesManager.get_personal_number(this, "personal_number");
        edtNumResult.setText(SafeNum);


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNum.getText().toString().length() == 0)
                    Toast.makeText(getApplicationContext(), "번호를 입력해주세요", Toast.LENGTH_LONG).show();
                else {
                    String edtShared=edtNum.getText().toString();
                    Log.d("lll", "edtShared=>"+edtShared);
                    SharedPreferencesManager.set_personal_number(v.getContext(), "personal_number", edtShared);
                    btnSelect.callOnClick();
                }
            }
        });

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.set_personal_number(v.getContext(), "personal_number", "");
                btnSelect.callOnClick();
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lll", "btnSelect()"+SafeNum);
                String SafeNum = SharedPreferencesManager.get_personal_number(v.getContext(), "personal_number");
                Log.d("lll", "btnSelect->Shared()"+SafeNum);
                edtNumResult.setText(SafeNum);
                edtNumResult.setContentDescription(SafeNum);
            }
        });

    }


}
