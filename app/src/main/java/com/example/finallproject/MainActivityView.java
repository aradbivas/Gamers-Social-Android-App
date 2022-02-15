package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivityView extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<String> games;
    String searchTxt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        RecyclerView recycleView = (RecyclerView) findViewById(R.id.myRecycoerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        recycleView.setLayoutManager(layoutManager);

        recycleView.setItemAnimator(new DefaultItemAnimator());


        Button searchButton = findViewById(R.id.buttonsearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTxt = ((EditText)findViewById(R.id.edittextsearch)).getText().toString();
                if(!searchTxt.equals("")) {
                    function_go_to_SearchActivty(v);
                }
            }
        });

        ArrayList<DataModel> dataSet = new ArrayList<DataModel>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= db.getReference("users").child(uid).child("gameList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                games = snapshot.getValue(t);
                ArrayList<Integer> drawableArray = new ArrayList<Integer>();
                for (String game : games)
                {
                    String gamesWithOutSpaces = game.replace(" ","");
                    String gamesWithOOutCapitals = gamesWithOutSpaces.toLowerCase(Locale.ROOT);
                    Integer id = GetImage(getApplicationContext(),gamesWithOOutCapitals);
                    drawableArray.add(id);
                }
                for(int i=0 ; i<games.size() ; i++)
                {
                    dataSet.add(new DataModel(games.get(i), GamesData.id_[i], drawableArray.get(i)));
                }
                CardMainActivity addapter = new CardMainActivity(dataSet,getApplicationContext());
                recycleView.setAdapter(addapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        Button Profile = findViewById(R.id.buttonProfileSetting);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function_go_to_MainProfile(v);
            }
        });

    }
    public static Integer GetImage(Context c, String ImageName) {
        Integer id = c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
        return id;
    }
    public void function_go_to_MainProfile(View view) {

        Intent intent = new Intent(this, MainActivityUserProfile.class);
        startActivity(intent);


    }
    public void function_go_to_SearchActivty(View view)
    {
        Intent intent = new Intent(this, MainActivitySearch.class);
        intent.putExtra("Search_name", searchTxt);
        startActivity(intent);
    }

}
