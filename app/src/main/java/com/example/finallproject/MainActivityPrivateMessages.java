package com.example.finallproject;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainActivityPrivateMessages extends AppCompatActivity {

        Button sndMsg;
        EditText sendText;
        TextView Title;
        ListView messageList;
        ArrayList<String> ListConvo = new ArrayList<String>();
        ArrayAdapter adapter;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbr;
        DatabaseReference dbr2;
        ScrollView sv;
        String UserName, SelectedTopic, usr_msg_key;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_topic);
            sv = findViewById(R.id.sv);

            sndMsg = findViewById(R.id.buttonsendtext);
            sendText = findViewById(R.id.TextSendText);
            messageList = findViewById(R.id.ConvoList);
            messageList.setStackFromBottom(true);
            messageList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            Title = findViewById(R.id.TopicNameText);
            Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToMembers(v);
                }
            });
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListConvo);

            messageList.setAdapter(adapter);
            SelectedTopic = getIntent().getExtras().get("Selected_Topic").toString();
            String uids = getIntent().getExtras().get("participants").toString();
            UserName = getUserName();
            Title.setText(SelectedTopic);
            dbr = database.getReference().child("PrivateMessages").child(uids);
            sndMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> map = new HashMap<String,Object>();
                    usr_msg_key = dbr.push().getKey();
                    dbr.updateChildren(map);
                    dbr2 = dbr.child(usr_msg_key);
                    Map<String,Object> map2 = new HashMap<String,Object>();
                    map2.put("msg", sendText.getText().toString());
                    map2.put("user", UserName);
                    sendText.setText("");

                    dbr2.updateChildren(map2);

                }
            });

            dbr.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                {

                    updateConvo(snapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    updateConvo(snapshot);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void updateConvo(DataSnapshot snapshot)
        {
            String msg,usr;

            Iterator i = snapshot.getChildren().iterator();

            while (i.hasNext())
            {
                msg = (String) ((DataSnapshot)i.next()).getValue();
                usr = (String) ((DataSnapshot)i.next()).getValue();
                adapter.insert(usr + ":" + msg,  ListConvo.size());
                adapter.notifyDataSetChanged();
            }

        }
        public void goToMembers(View view)
        {
            Intent intent = new Intent(this, TopicGroupMembers.class);
            intent.putExtra("selected_Topic",SelectedTopic);
            startActivity(intent);
        }
    public String getUserName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        return name;
    }

}