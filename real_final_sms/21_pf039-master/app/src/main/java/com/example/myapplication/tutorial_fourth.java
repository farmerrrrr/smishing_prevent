package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import static com.example.myapplication.IncomingCallBroadcastReceiver.TAG;

public class tutorial_fourth extends Fragment {
    private String title;
    private int page;
    EditText USER_NAME, USER_NUMBER;
    Button NEXT_BTN;
    FragmentPagerAdapter adapterViewPager;


    public static tutorial_fourth newInstance(int page, String title) {
        tutorial_fourth fragment = new tutorial_fourth();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_fourth, container, false);
        USER_NAME = (EditText) view.findViewById(R.id.edtUserName);
        NEXT_BTN = (Button) view.findViewById(R.id.next_btn);

        boolean sharedPreferences = SharedPreferencesManager.get_first_app_execute(getContext(),"first");
        Log.d("lll", "sharedPreferences=>"+sharedPreferences);
        if (sharedPreferences==false) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        NEXT_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.set_first_app_execute(v.getContext(), "first", false);
                String name= USER_NAME.getText().toString();
                startActivity(new Intent(v.getContext(), MainActivity.class));
                loadDB_id();
            }
        });

        return view;
    }

    void loadDB_id(){
        //volley library로 사용 가능
        //이 예제에서는 전통적 기법으로 함.

        new Thread(){
            @Override
            public void run() {
                //SMSAdapter adapter=new SMSAdapter();

                String serverUri="http://3.130.43.138:8000/smishing/register?name="+USER_NAME.getText().toString();
                try {
                    URL url = new URL(serverUri);

                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    //connection.setDoOutput(true);// 이 예제는 필요 없다.
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
                        int member_id = jsonObject.getInt("id");
                        //String send_date = jsonObject.getString("send_date");
                        // String send_date="2021-10-14 11:25:28";
                        //String contents = jsonObject.getString("contents");
                        //SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                        //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //  ParsePosition pos = new ParsePosition(0);
                        //Date date2 = dateFormat.parse(send_date);
                        Log.d(TAG,"이게 바로 새로운 아이디 : "+member_id);
                        //adapter.addItemToList(sender,send_date,contents);

                    }
                    //getActivity().runOnUiThread(new Runnable() {
                      //  @Override
                       // public void run() {
                            //listView.setAdapter(adapter);
                       // }
                    //});





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
    }

}