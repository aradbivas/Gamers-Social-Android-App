package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MessageMainActivity extends AppCompatActivity {

    ArrayList<String> listofTopics = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ArrayList<PrivateChatData> dataSet = new ArrayList<PrivateChatData>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dbr = database.getReference("PrivateMessages");
    DatabaseReference dbr2 = database.getReference("users");
    DatabaseReference dbr3 = database.getReference("users");
    private FirebaseAuth mAuth;
    String UserName;
    String ChatName;
    String uidS;
    Map<String,String> nameanduids = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);

        RecyclerView recycleView = (RecyclerView) findViewById(R.id.listviewMSG);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ScrollView scrollView =(ScrollView) findViewById(R.id.sv);
        String myusername = getUserName();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for(DataSnapshot child : snapshot.getChildren())
                {
                    String names = child.getKey();
                    String[] uid = names.split(",");
                    String chatName = uid[1];
                    for(String name : uid)
                    {
                        if (name.contentEquals(myusername))
                        {
                            PrivateChatData privateChatData = new PrivateChatData();
                            if (chatName.contentEquals(myusername))
                            {
                                privateChatData.setChatUserName(uid[0]);
                                privateChatData.setMyUserName(uid[1]);
                                privateChatData.OpenedChat = true;
                            }
                            else
                            {
                                privateChatData.setChatUserName(uid[1]);
                                privateChatData.setMyUserName(uid[0]);
                                privateChatData.OpenedChat = false;
                            }

                            dataSet.add(privateChatData);
                        }
                    }
               }
                if(dataSet.size() > 0) {
                    CardPrivateMessages addapter = new CardPrivateMessages(dataSet, getApplicationContext());
                    recycleView.setAdapter(addapter);
                }
                else
                {
                    PrivateChatData privateChatData2 = new PrivateChatData();
                    privateChatData2.setChatUserName("123");
                    dataSet.add(privateChatData2);
                    CardPrivateMessages addapter = new CardPrivateMessages(dataSet, getApplicationContext());
                    recycleView.setAdapter(addapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public String getUserName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        return name;
    }
}