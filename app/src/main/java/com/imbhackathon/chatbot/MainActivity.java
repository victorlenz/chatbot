package com.imbhackathon.chatbot;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConversationService service;
    Thread thread;
    Handler handler;

    List<ChatClass> chatMessage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView)findViewById(R.id.listView);

        final EditText text = (EditText)findViewById(R.id.editText);
        Button button = (Button)findViewById(R.id.button);

        chatMessage = new ArrayList<>();
        final chatAdapter adapter = new chatAdapter(chatMessage,this);
        listView.setAdapter(adapter);

        ChatClass chatClass = new ChatClass();
        chatClass.setMessage("Hi !, I am Your Pizza Assistant,My i take your order");
        chatClass.setPos(1);
        chatMessage.add(chatClass);
        adapter.notifyDataSetChanged();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Log.d("Handled ",msg.getData().get("text").toString());

                ChatClass chatClass = new ChatClass();
                chatClass.setMessage(msg.getData().get("text").toString());
                chatClass.setPos(1);

                chatMessage.add(chatClass);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        };


        service = new ConversationService("2016-07-11");
        service.setUsernameAndPassword("362267ae-9b1b-420d-b7c6-8f767fbd1f80", "ejmFvnYnOWR6");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatClass chatClass = new ChatClass();
                chatClass.setMessage(text.getText().toString());
                chatClass.setPos(0);

                chatMessage.add(chatClass);
                adapter.notifyDataSetChanged();


                thread = new Thread(new MyThread(text.getText().toString()));

                text.setText("");
                listView.setSelection(adapter.getCount() - 1);
                thread.start();



            }
        });

    }
    class MyThread implements Runnable{

        String message;

        MyThread(String m)
        {
            message=m;
        }

        @Override
        public void run() {
            MessageRequest newMessage = new MessageRequest.Builder()
                    .inputText(message)
                    // Replace with the context obtained from the initial request
                    //.context(...)
                    .build();
            final String workS = "12a3f2a3-ae54-4203-a68e-dd36836a819a";

            MessageResponse response = service
                    .message(workS, newMessage)
                    .execute();


            try {
                JSONObject jsonObject = new JSONObject(response.toString());

                jsonObject =  new JSONObject(jsonObject.get("output").toString());


                Log.d("response",jsonObject.get("text").toString());

                Bundle bundle = new Bundle();
                String s = jsonObject.getJSONArray("text").get(0).toString();


                bundle.putString("text",s.substring(0,s.length()));

                Message message = Message.obtain();
                message.setData(bundle);
                handler.sendMessage(message);

            } catch (JSONException e) {
                Log.d("parse error",e.toString());
            }


        }
    }
}
