package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import static com.example.myapplication.IncomingCallBroadcastReceiver.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private JSONArray jsonArray;
    ListView listView;
    ListView test;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor



    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ListView)view.findViewById(R.id.listview);
        EditText SafeNum=(EditText)view.findViewById(R.id.SafeNum);
        SafeNum.setFocusable(false);
        SafeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalSafetyNumActivity.class);
                startActivity(intent);
            }
        });

        loadDB();

    return view;
    }

    public void onStart(){
        super.onStart();
        EditText SafeNum=(EditText)getView().findViewById(R.id.SafeNum);
        SafeNum.setFocusable(false);
        EditText SafeNum_Text=(EditText) getView().findViewById(R.id.SafeNum_Text);
        SafeNum_Text.setFocusable(false);

        String SharedSafeNum=SharedPreferencesManager.get_personal_number(getContext(), "personal_number");
        Log.d("lll","MAIN->SharedSafe=>"+SharedSafeNum);
        if (SharedSafeNum!="") {
            SafeNum_Text.setText("안심\n번호");
            SafeNum.setText(SharedSafeNum);
            SafeNum.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            SafeNum.setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
            SafeNum.setTextColor(Color.parseColor("#000000"));
        } else {
            SafeNum_Text.setText("");
            SafeNum.setText("안심번호 설정하기");
            SafeNum.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            SafeNum.setTextColor(Color.parseColor("#fc5230"));
        }
    }


    void loadDB(){
        //volley library로 사용 가능
        //이 예제에서는 전통적 기법으로 함.

        new Thread(){
            @Override
            public void run() {
                SMSAdapter adapter=new SMSAdapter();

                String serverUri="http://3.130.43.138:8000/smishing_nlp/load_message?id=1";
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


                    Log.d(TAG,"test : "+buffer.toString());
                    String json = buffer.toString();


                        jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String sender = jsonObject.getString("sender");
                            String send_date = jsonObject.getString("send_date");
                           // String send_date="2021-10-14 11:25:28";
                            String contents = jsonObject.getString("contents");
                            //SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                          //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                          //  ParsePosition pos = new ParsePosition(0);
                           //Date date2 = dateFormat.parse(send_date);
                            Log.d(TAG,"test : "+sender+send_date+contents);
                            adapter.addItemToList(sender,send_date,contents);


                        }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });





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