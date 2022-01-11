package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;


public class SMSAdapter extends BaseAdapter {
    ArrayList<SMSItem> list = new ArrayList<>();
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,viewGroup,false);
        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView sender = view.findViewById(R.id.sender);
        TextView date = view.findViewById(R.id.date);
        TextView content = view.findViewById(R.id.content);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        SMSItem talkItem = list.get(i);

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        sender.setText(talkItem.getsender());
        date.setText(talkItem.getDate_send());
        content.setText(talkItem.getContent());

        return view;
    }
    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함 다른방시으로 구현해도 됨
    public void addItemToList(String sender, String date, String contents){
        SMSItem listdata = new SMSItem();

        listdata.setsender(sender);
        listdata.setDate_send(date);
        listdata.setContent(contents);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);

    }
}