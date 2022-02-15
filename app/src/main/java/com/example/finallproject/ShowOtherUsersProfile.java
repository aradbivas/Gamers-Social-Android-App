package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShowOtherUsersProfile extends AppCompatActivity {

    String uid;
    String myurl;
    Boolean isfriend = false;
    Button addFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        uid = getIntent().getExtras().get("Member_UID").toString();
        TextView name = findViewById(R.id.ShowProfileName);
        addFriend = findViewById(R.id.buttonAddFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(v);
                addFriend.setVisibility(View.INVISIBLE);
            }
        });
        getmyUri();
        TextView email = findViewById(R.id.ShowProfileEmail);
        TextView games = findViewById(R.id.ShowProfileGames);
        Button Dm = findViewById(R.id.dm);
        String UserName = getUserName();
        Dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivityPrivateMessages.class);
                String[] cleanName = name.getText().toString().split(" ");
                String[] f = cleanName;

                i.putExtra("Selected_Topic",cleanName[1]);
                i.putExtra("participants", UserName +","+ cleanName[1]);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        ImageView imageView = findViewById(R.id.ShowProfileImage);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= db.getReference("users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};

                email.setText("Email: "+snapshot.child("email").getValue(String.class));
                name.setText("UserName: " +snapshot.child("name").getValue(String.class));
                ArrayList<String> gameList = snapshot.child("gameList").getValue(t);
                games.setText("Games: " + gameList.toString().replace("]","").replace("[",""));
                try {
                    File local =  File.createTempFile("tmp",".bmp");
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(uid).child("Images");
                    FileDownloadTask file = (FileDownloadTask) ref.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bit = BitmapFactory.decodeFile(local.getAbsolutePath());
                            imageView.setImageBitmap(bit);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            imageView.setImageResource(R.drawable.profilepictmp);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendFriendRequest(View view)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= db.getReference("users").child(uid).child("friendRequest");
        databaseReference.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference= db.getReference("users").child(uid).child("friendRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name");
        databaseReference.setValue(getUserName());
        databaseReference= db.getReference("users").child(uid).child("friendRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("url");
        databaseReference.setValue(myurl);
        Toast.makeText(ShowOtherUsersProfile.this, "Friend Request Sent!", Toast.LENGTH_LONG).show();

    }
    public String getUserName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        return name;
    }
    public void getmyUri()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                };
                myurl = snapshot.child("url").getValue(String.class);

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if ("friendList".equals(ds.getKey())) {
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            String friendlistuids = ds2.getKey();
                            if (uid.equals(friendlistuids)) {
                                addFriend.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}