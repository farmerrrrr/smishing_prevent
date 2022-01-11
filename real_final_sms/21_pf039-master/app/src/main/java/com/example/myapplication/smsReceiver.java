package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.IncomingCallBroadcastReceiver.TAG;
import static java.security.AccessController.getContext;

public class smsReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private int count = 0;
    String sender, contents;
    Date receivedDate;

  //  private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    //연-월-일 시:분:초 형태로 출력하게끔 정하는 메서드
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //문자가 오면 반드시 작동하는 메서드
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 호출됨");


        //intent의 내용을 bundle에 넣는다.
        Bundle bundle = intent.getExtras();

        //sms 메세지가 한 개가 아니므로 배열로 만든다.
        SmsMessage[] messages = parseSmsMessage(bundle);

        //메세지 내용이 있을 경우 작동
        if (messages != null && messages.length > 0) {
            Log.d(TAG, "onReceive: SMS를 수신하였습니다");

            //보낸 사람
            sender = messages[0].getOriginatingAddress();
            Log.d(TAG, "onReceive: sender:" + sender);

            //받은 날짜
            receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "onReceive: receivedDate: " + receivedDate);

            //내용
            contents = messages[0].getMessageBody();
            Log.d(TAG, "onReceive: contents: " + contents);

            new Thread(){
                @Override
                public void run() {
                    //SMSAdapter adapter=new SMSAdapter();

                    String serverUri="http://3.130.43.138:8000/smishing/register?id="+SharedPreferencesManager.get_personal_number(context, "id")+"sender="+sender+"receivedDate"+receivedDate+"contents"+contents;
                    try {
                        URL url = new URL(serverUri);
                        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setDoInput(true);
                        connection.setUseCaches(false);

                        InputStream is=connection.getInputStream();
                        InputStreamReader isr= new InputStreamReader(is);
                        BufferedReader reader= new BufferedReader(isr);

                        final StringBuffer buffer= new StringBuffer();
                        String line= reader.readLine();
                        while (line!=null){
                            buffer.append(line+"\n");
                            line= reader.readLine();
                        }

                        Log.d("lll","test : "+buffer.toString());
                        String json = buffer.toString();

                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String result = jsonObject.getString("result");
                            Log.d(TAG,"결과 : "+result);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            //실제 요청 작업을 수행해주는 요청큐 객체 생성
        /*Retrofit retrofit=new Retrofit.Builder().baseUrl("http://3.130.43.138:8000/smishing_nlp/insert_message/?id=1")
                .addConverterFactory(GsonConverterFactory.create()).build();
        SMS sms =retrofit.create(SMS.class);
        //ID는 회원번호로 바꿔주기
        /*sms.getData("1").enqueue(new Callback<List<Post>>(){
            @Override
                    public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response){
                if(response.isSuccessful()){
                    List<Post> data=response.body();
                    Log.d("Test", "성공");
                    Log.d("Test", data.get(0).getSender());
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call , Throwable t){
                t.printStackTrace();
            }
        });*/
/*
        HashMap<String, Object> input=new HashMap<>();
        //input.put("id", 1);
        input.put("Sender", sender);
        input.put("Date_send",receivedDate);
        input.put("Content", contents);
        sms.postData(input).enqueue(new Callback<Post>(){
            @Override
            public void onResponse(Call<Post> call, Response<Post> response){
                if(response.isSuccessful()){
                    Post data=response.body();
                    Log.d("Test", "Post 성공");
                    Log.d("Test",data.getContent());
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t){

            }
        });*/
           // post_message();



            //doInBackground("http://3.130.43.138:8000/smishing");


            //SmsDisplayActivity 화면에 띄우기
            //Flag : 속성을 부여하는 키워드
            //예시 : M화면 → A화면 → SMS메시지화면 → B화면
            //NEW_TASK : 새 화면을 띄우겠다 (SMS메시지화면)
            //CLEAR_TOP : SMS메시지 위의 화면들을 없앰 (B화면 이하 화면들)
            //SINGLE_TOP : 기존의 SMS메시지 화면이 있으면 그걸 사용하라는 뜻
            if (contents.contains("송금")||contents.contains("택배")) {
                Intent displayIntent = new Intent(context, SmsDisplayActivity.class);
                displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

                //값을 추가로 보냄
                displayIntent.putExtra("sender", sender);
                displayIntent.putExtra("receivedDate", dateFormat.format(receivedDate));
                Log.d(TAG, "onReceive: receivedDate:" + dateFormat.format(receivedDate).getClass().getName());
                displayIntent.putExtra("contents", contents);
                context.startActivity(displayIntent);


                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.putExtra("notificationId", count); //전달할 값
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle("스미싱 꼼짝마!")
                        .setContentText(sender + "으로부터 스미싱 위험이 있는 문자를 수신하였습니다")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);
               /*Uri soundUri= Uri.parse("android.resource://"+getPackageName()+"/"+R.음악파일폴더.음악파일이름);
                builder.setSound(soundUri); 음악파일넣는코드인데 에뮬이라 그런가 소리가 안들림;;*/


                //OREO API 26 이상에서는 채널 필요
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    CharSequence channelName = "노티페케이션 채널";
                    String description = "오레오 이상을 위한 것임";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                    channel.setDescription(description);

                    // 노티피케이션 채널을 시스템에 등록
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);

                } else
                    builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                assert notificationManager != null;
                notificationManager.notify(1, builder.build()); // 고유숫자로 노티피케이션 동작시킴


            }
            if (contents.contains("http") | contents.contains("https") | contents.contains("www.")) {

                Intent mintent = new Intent(context, URLsetting.class);
                mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                context.startActivity(mintent);
            }
        }


    }


    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        //pdus에 메세지가 담겨있다.
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for (int i = 0; i < objs.length; i++) {
            //M버젼 (API 23 마시멜로우)이상일 때와 아닐때의 메세지 저장 형식 지정
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }
        return messages;
    }

}