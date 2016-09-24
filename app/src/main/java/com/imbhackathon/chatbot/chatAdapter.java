package com.imbhackathon.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 9/23/2016.
 */
public class chatAdapter extends BaseAdapter {

   List<ChatClass> message;
    Context context;

    chatAdapter(List<ChatClass> message,Context context)
    {
        this.message= message; this.context = context;
    }

    @Override
    public int getCount() {
        return
         message.size();
    }

    @Override
    public Object getItem(int i) {
        return message.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ChatClass chatClass = message.get(i);

        if(chatClass.getPos() == 1)
        view=   li.inflate(R.layout.row,null);
        else view =  li.inflate(R.layout.row_right,null);

        TextView textView = (TextView) view.findViewById(R.id.textView_row);

        textView.setText(chatClass.getMessage());
        return  view;
    }
}
