package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.TypeVariable;
import static android.speech.tts.TextToSpeech.ERROR;
import java.util.Locale;

public class SettingFragment extends Fragment {
    public TextToSpeech tts;
    Context ct;
    public static boolean protect_monitor_permission;
    TextView explain[] = new TextView[4];
    Integer[] Rid_explain = {
            R.id.explain_1, R.id.explain_2, R.id.explain_3, R.id.explain_4
    };



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(R.layout.setting_fragment, container, false);
        //이 코드를 앞에 작성해야한다 .
        //이유 : 아직 inflation 이전의 View 컴포넌트를 부르는 함수를 호출하기 때문이다.
        TextView explain_text1=(TextView)v.findViewById(R.id.explain_1);
        TextView explain_text2=(TextView)v.findViewById(R.id.explain_2);
        float original_size = explain_text1.getTextSize();
        TextView go_Settring=(TextView)v.findViewById(R.id.go_setting);
        TextView go_call_settings=(TextView)v.findViewById(R.id.go_call_setting);



        ImageButton btn_magnifier=(ImageButton) v.findViewById(R.id.btn_magnifier);
        ImageButton btn_cancel_magnifier=(ImageButton) v.findViewById(R.id.btn_cancel_magnifier);
        btn_cancel_magnifier.setVisibility(View.GONE);
        ImageButton volumn_1=(ImageButton)v.findViewById(R.id.volumn_1);
        ImageButton volumn_2=(ImageButton)v.findViewById(R.id.volumn_2);
        ImageButton volumn_3=(ImageButton)v.findViewById(R.id.volumn_3);
        ImageButton volumn_4=(ImageButton)v.findViewById(R.id.volumn_4);
        ImageButton volumn_5=(ImageButton)v.findViewById(R.id.volumn_5);


        for (int i = 0; i < explain.length; i++) {
            explain[i] = (TextView) v.findViewById(Rid_explain[i]);
        }

        tts = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    // 언어를 선택한다.
                    int result_tts = tts.setLanguage(Locale.KOREAN);

                    if(result_tts==TextToSpeech.LANG_MISSING_DATA || result_tts == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(getContext().getApplicationContext(), "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ct = container.getContext();
        String android_id = Settings.Secure.getString(ct.getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.d("lll", "Android_ID >>> "+android_id);
        //fragment에서 context구하기

        LinearLayout btn_guide_setting = (LinearLayout) v.findViewById(R.id.btn_guide_setting);
        btn_guide_setting.setFocusable(false);
        //Fragment에서는 'findViewById'만을 단독으로 사용할 수 없다.
        // 'getView().findViewById()'를 사용한다.
        TextView btn_personal_safety_number = (TextView) v.findViewById(R.id.btn_personal_safety_number_setting);
        TextView btn_personal_safety_number2 = (TextView) v.findViewById(R.id.btn_personal_safety_number_setting2);


        Switch protect_monitor = (Switch) v.findViewById(R.id.protect_monitor);
        Switch url_alarm = (Switch) v.findViewById(R.id.url_alarm);
        float original_size2=protect_monitor.getTextSize();


        Boolean protect_monitor_permission = SharedPreferencesManager.get_protect_monitor_permission(ct, "protect");
        protect_monitor.setChecked(protect_monitor_permission);

        btn_guide_setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GuideSettingActivity.class);
                startActivity(intent);
            }
        });


        btn_personal_safety_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalSafetyNumActivity.class);
                startActivity(intent);

            }
        });
        btn_personal_safety_number2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalSafetyNumActivity.class);
                startActivity(intent);

            }
        });
        // Inflate the layout for this fragment

        protect_monitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesManager.set_protect_monitor_permission(ct, "protect", true);
                } else
                    SharedPreferencesManager.set_protect_monitor_permission(ct, "protect", false);
            }
        });

        btn_magnifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_magnifier.setVisibility(View.GONE);
                btn_magnifier.setContentDescription("");
                btn_cancel_magnifier.setVisibility(View.VISIBLE);
                btn_cancel_magnifier.setContentDescription("돋보기 취소");
                for (int i = 0; i < explain.length; i++) {
                    explain[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size+12.0f );
                }
                explain[0].setText("사용자의 휴대폰에 저장되어 있지 않은 번호와 일정 시간 이상 전화 연결이 지속될 경우, 지정된 보호자에게 알려주는 기능입니다. ");
                protect_monitor.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2+5.0f );
                btn_personal_safety_number.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2+5.0f );
                go_Settring.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2+5.0f);
                go_call_settings.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2+5.0f);
                url_alarm.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2+5.0f);

            }
        });
        btn_cancel_magnifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < explain.length; i++) {
                    explain[i].setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size);
                }
                btn_magnifier.setVisibility(View.VISIBLE);
                btn_magnifier.setContentDescription("돋보기로 확대하기");
                btn_cancel_magnifier.setVisibility(View.GONE);
                btn_cancel_magnifier.setContentDescription("");
                explain[0].setText("사용자의 휴대폰에 저장되어 있지 않은 번호와 \n일정 시간 이상 전화 연결이 지속될 경우,\n지정된 보호자에게 알려주는 기능입니다. ");
                protect_monitor.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2);
                btn_personal_safety_number.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2 );
                go_Settring.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2);
                go_call_settings.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2);
                url_alarm.setTextSize(TypedValue.COMPLEX_UNIT_PX,original_size2);

            }
        });


        go_Settring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES");
                startActivity(intent);
            }
        });

        go_call_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.telecom.action.SHOW_CALL_SETTINGS");
                startActivity(intent);
            }
        });
        LinearLayout how_to_unknown_app=(LinearLayout)v.findViewById(R.id.how_to_unknown_app);
        LinearLayout how_to_international_number=(LinearLayout)v.findViewById(R.id.how_to_international_number);
        how_to_international_number.setFocusable(false);
        how_to_unknown_app.setFocusable(false);
        how_to_unknown_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),How_to.class);
                intent.putExtra("PAGE","unknown_install");
                startActivity(intent);
            }
        });

        how_to_international_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),How_to.class);
                intent.putExtra("PAGE","international_call");
                startActivity(intent);
            }
        });

        volumn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(protect_monitor.getText().toString()+" , "+explain[0].getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        volumn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(url_alarm.getText().toString()+" , "+explain[1].getText().toString(),TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        volumn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(btn_personal_safety_number.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        volumn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(go_Settring.getText().toString()+" , "+explain[2].getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        volumn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(go_call_settings.getText().toString()+" , "+explain[3].getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        return v;

    }

    public void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

}
