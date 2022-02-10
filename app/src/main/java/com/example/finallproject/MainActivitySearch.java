package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivitySearch extends AppCompatActivity {

    private String searchname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_group_members);
        RecyclerView memberList = (RecyclerView) findViewById(R.id.MembersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        memberList.setLayoutManager(layoutManager);
        searchname = getIntent().getExtras().get("Search_name").toString();
        memberList.setItemAnimator(new DefaultItemAnimator());
        ArrayList<ProfileData> profileData = new ArrayList<ProfileData>();
        String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                };
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String uid = snapshot1.getKey();
                    if (!myuid.contentEquals(uid)) {

                        String name = snapshot1.child("name").getValue(String.class);
                        if(name.contains(searchname)) {
                            ProfileData profileData1 = new ProfileData();
                            String imgurl = snapshot1.child("url").getValue(String.class);
                            profileData1.uri = imgurl;
                            profileData1.setName(name);
                            profileData1.setUid(uid);
                            profileData.add(profileData1);
                        }

                    }
                }
                if(profileData.size() >0 ) {
                    CardMembersList adapter = new CardMembersList(profileData, getApplicationContext());
                    memberList.setAdapter(adapter);
                }
                else
                {
                    ProfileData profileData2 = new ProfileData();
                    profileData2.name = "No players with name " +searchname;
                    profileData2.uid = "123";
                    profileData.add(profileData2);
                    CardMembersList adapter = new CardMembersList(profileData, getApplicationContext());
                    memberList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}