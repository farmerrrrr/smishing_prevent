package com.example.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YoutubeFragement #newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideSettingActivity extends AppCompatActivity {

    EditText edtName[] = new EditText[4];
    EditText edtNumber[] = new EditText[4];
    Button btnDelete[] = new Button[4];
    Integer[] Rid_Name = {
            R.id.edtNameResult_1, R.id.edtNameResult_2, R.id.edtNameResult_3, R.id.edtNameResult_4
    };
    Integer[] Rid_Num = {
            R.id.edtNumberResult_1, R.id.edtNumberResult_2, R.id.edtNumberResult_3, R.id.edtNumberResult_4
    };
    Integer[] Rid_Delete = {
            R.id.btnDelete_1, R.id.btnDelete_2, R.id.btnDelete_3, R.id.btnDelete_4
    };
    ImageButton btnback;
    Button btnInsert, btnSelect;
    MainActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_setting);

        btnback=(ImageButton)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        for (int i = 0; i < edtName.length; i++) {
            edtName[i] = (EditText) findViewById(Rid_Name[i]);
            edtNumber[i] = (EditText) findViewById(Rid_Num[i]);
            btnDelete[i] = (Button) findViewById(Rid_Delete[i]);

            btnDelete[i].setVisibility(View.INVISIBLE);
        }


        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnSelect = (Button) findViewById(R.id.btnSelect);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        myHelper = new MainActivity.myDBHelper(this);

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM guide_TBL LIMIT 4;", null);
        int i = 0;
        while (cursor.moveToNext()) {
            Log.d("lll", "onCreate>cursor_name=" + cursor.getString(1));
            Log.d("lll", "onCreate>cursor_num=" + cursor.getString(0));
            edtName[i].setText(cursor.getString(1));
            edtNumber[i].setText(cursor.getString(0));
            btnDelete[i].setVisibility(View.VISIBLE);
            i++;
        }

        cursor.close();
        sqlDB.close();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT COUNT(*) FROM guide_TBL;", null);
                cursor.moveToFirst();
                Log.d("lll", "개수 : " + cursor.getInt(0));
                if (cursor.getInt(0) >= 4) {
                    Toast.makeText(getApplicationContext(), "보호자는 최대 4명까지 설정 가능합니다.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    //호출 후, 연락처앱에서 전달되는 결과물을 받기 위해 startActivityForResult로 실행한다.
                    startActivityForResult(intent, 0);
                }
            }
        });
        for (int j = 0; j < Rid_Delete.length; j++) {
            final int INDEX;
            INDEX = j;
            btnDelete[INDEX].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setTitle("삭제하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("DELETE FROM guide_TBL WHERE guide_Name ='" + edtName[INDEX].getText().toString() + "';");
                            Log.d("lll", "DELETE FROM guide_TBL WHERE guide_Name ='" + edtName[INDEX].getText().toString() + "';");
                            sqlDB.close();
                            btnSelect.callOnClick();
                            Toast.makeText(getApplicationContext(), " 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            });
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lll", "조회");
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM guide_TBL LIMIT 4;", null);
                for (int i = 0; i < edtName.length; i++) {
                    edtName[i].setText("");
                    edtNumber[i].setText("");
                    btnDelete[i].setVisibility(View.INVISIBLE);
                }
                int i = 0;
                while (cursor.moveToNext()) {
                    Log.d("lll", "select>cursor_name=" + cursor.getString(1));
                    Log.d("lll", "select>cursor_num=" + cursor.getString(0));
                    if (cursor != null) {
                        edtName[i].setText(cursor.getString(1));
                        edtName[i].setContentDescription(cursor.getString(1));

                        edtNumber[i].setText(cursor.getString(0));
                        edtNumber[i].setContentDescription(cursor.getString(0));

                        btnDelete[i].setVisibility(View.VISIBLE);
                        btnDelete[i].setContentDescription("보호자 삭제하기");

                        i++;
                    } else
                        break;
                }

                cursor.close();
                sqlDB.close();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String name = "";
            String number = "";
            Cursor cursor = getContentResolver().query(intent.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            number = cursor.getString(1);   //1은 번호를 받아옵니다.
            Log.d("lll", "name = " + name + " / " + "number = " + number);
            cursor.close();
            Log.d("lll", "intent = " + intent);
            //삽입(가이드)
            sqlDB = myHelper.getReadableDatabase();
            Cursor sql_cursor;
            sql_cursor = sqlDB.rawQuery("SELECT * FROM guide_TBL WHERE guide_Num='" +number + "';", null);
            if(sql_cursor.moveToNext()){

            }
            else {
                //삽입(가이드)
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO guide_TBL VALUES ('" + number + "','" + name + "');");
                sqlDB.close();
                btnSelect.callOnClick();
            }
            sql_cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

}