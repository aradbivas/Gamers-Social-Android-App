package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainActivityFriendList extends AppCompatActivity {

        ArrayList<String> Members = new ArrayList<String>();
        ArrayAdapter adapter;
        String SelectedTopic;
        ImageView loclimg;
        ArrayList<String> listUids = new ArrayList<String>();
        Map<String,ImageView> dictionary = new HashMap<String,ImageView>();
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_friend_list);
            RecyclerView memberList =(RecyclerView) findViewById(R.id.friendRecycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
            memberList.setLayoutManager(layoutManager);
            memberList.setItemAnimator(new DefaultItemAnimator());
            ArrayList<ProfileData> profileData = new ArrayList<ProfileData>();
            String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Button friendRequest = findViewById(R.id.buttonFriendRequest);
            friendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToFriendRequest(v);
                }
            });
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference= db.getReference("users").child(myuid).child("friendList");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {} ;
                    for (DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        String uid = snapshot1.getKey();
                        for(DataSnapshot ds2 : snapshot.getChildren()) {
                            String name = snapshot1.child("name").getValue(String.class);

                            ProfileData profileData1 = new ProfileData();
                            String imgurl = snapshot1.child("url").getValue(String.class);
                            profileData1.uri = imgurl;
                            profileData1.setName(name);
                            profileData1.setUid(uid);
                            profileData.add(profileData1);
                        }
                    }
                    if(profileData.size() >0 ) {
                        CardMembersList adapter = new CardMembersList(profileData, getApplicationContext());
                        memberList.setAdapter(adapter);
                    }
                    else
                    {
                        ProfileData profileData2 = new ProfileData();
                        profileData2.name = "No friends added. ";
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

    private void goToFriendRequest(View view)
    {
        Intent i = new Intent(getApplicationContext(), MainActivityFriendRequest.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }
}