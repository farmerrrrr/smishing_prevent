package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "PHONE STATE";
    private static String mLastState;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("lll", "onReceive()");

        if (SharedPreferencesManager.get_protect_monitor_permission(context, "protect")) {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                //두 번 호출되는 문제 해결
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE); // 현재 폰 상태 가져옴
                if (state.equals(mLastState))
                    return;
                else {
                    mLastState = state;
                    String phoneNo = "";

                } if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("lll", "통화종료 혹은 통화벨 종료");
                    //통화기록 불러오기
                    alarmGuide(context);

                }
            }


        } else
            return;
    }

    public String phNumber;
    public int callType;
    public String date_str;
    public int callDuration;

    public void alarmGuide(Context context) {
        Log.d("lll","alarmGuide()");
        TelecomManager telephony = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        //마지막 통화기록 가져오기
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

        int number = c.getColumnIndex(CallLog.Calls.NUMBER);
        int type = c.getColumnIndex(CallLog.Calls.TYPE);
        int date = c.getColumnIndex(CallLog.Calls.DATE);
        int duration = c.getColumnIndex(CallLog.Calls.DURATION);

        c.moveToFirst();
        phNumber = c.getString(number);
        callType = c.getInt(type);
        long callDate = c.getLong(date);
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        date_str = datePattern.format(new Date(callDate));
        callDuration = c.getInt(duration);
        c.close();
        Log.d("lll", "마지막 통화 기록 : " + phNumber + " " + callType + " " + date_str + " " + callDuration);

        if ((callType == CallLog.Calls.INCOMING_TYPE) & callDuration > 60) { //수신 & 5분 이상 통화
            Log.d("lll", "if 수신&5분이상");
            //연락처 비교
            Compare_Call(context, phNumber);
        }
    }

    //연락처 비교
    public void Compare_Call(Context context, String phNumber){
        Log.d("lll","Compare_Call()");
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String storedName = "";

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                storedName=cursor.getString(0);
                cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                cursor.close();
                Log.d("lll","moveToFirst > storedName : "+0);
            }else{
                Log.d("lll","X->cursor.moveToFirst");
                //문자 보내기
                cursor.close();
                Sending_Guide_SMS(context);
            }
        }


    }
    MainActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    //문자 보내기
    public void Sending_Guide_SMS(Context context) {
        int hour = callDuration / (60 * 60);
        int minute = callDuration / 60 - (hour * 60);
        int second = callDuration % 60;
        StringBuffer Duration_str = new StringBuffer();
        if (hour == 0) {
            Duration_str.append(minute + "분");
        } else Duration_str.append(hour + "시" + minute + "분");

        String sms = "[스미싱 꼼짝마]" + date_str + " 보호대상자님 핸드폰으로" + "(" + phNumber + ")가" + Duration_str + "동안 전화를 수신하셨습니다.";
        Log.d("lll", sms);
        myHelper = new MainActivity.myDBHelper(context);

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor=sqlDB.rawQuery("SELECT * FROM guide_TBL;",null);

        try {
            while(cursor.moveToNext()) {
                SmsManager smsManager = SmsManager.getDefault();
                Log.d("lll","보호자 번호 =>"+cursor.getString(0));
                smsManager.sendTextMessage(cursor.getString(0), null, sms, null, null);
            }
            Log.d("lll", "전송 완료!");
            Toast.makeText(context.getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.d("lll", "실패 전송!");
            Toast.makeText(context.getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}



