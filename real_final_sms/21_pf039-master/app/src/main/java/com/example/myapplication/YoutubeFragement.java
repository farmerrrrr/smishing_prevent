package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

@SuppressWarnings("deprecation")
public class YoutubeFragement extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.youtube_fragment, container, false);

        WebView webView = view.findViewById(R.id.webView);
        //WebViewClient로 웹 뷰에서 일어나는 요청, 상태, 에러 등 다양한 상황에서의 콜백을 조작할 수 있다.
        webView.setWebViewClient(new WebViewClient());
        //자바스크립트 허용
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.loadUrl("https://www.youtube.com/watch?v=pexgHFs4mbw&list=PLEiyyo_klG7gNDnmy6jLSafz7rykUx4lw&index=1");

        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}