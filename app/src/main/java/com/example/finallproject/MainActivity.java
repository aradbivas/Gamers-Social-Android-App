package com.example.finallproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Person person;
    ArrayList<String> games = new ArrayList<String>();
    public static boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
    public void funcLogin(View view) {
        String email = ((EditText)findViewById(R.id.PasswordTextLogin)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();
        if(!email.equals("") || !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Worked", Toast.LENGTH_LONG).show();
                                function_go_to_Main(view);

                            } else {
                                Toast.makeText(MainActivity.this, "Login Didn't Work", Toast.LENGTH_LONG).show();
                                flag = false;
                            }
                        }
                    });
        }
    }
    public boolean funcRegister(View view) {
        String email = ((EditText) findViewById(R.id.EmailReg)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordReg)).getText().toString();
        if(!email.equals("") || !password.equals(""))
        {
            String username = ((EditText) findViewById(R.id.UserNameReg)).getText().toString();
            Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FlagClass.setFlag(true);
                                Toast.makeText(MainActivity.this, "Register Worked", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_addGamesFragment);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

                                user.updateProfile(profileUpdates);
                            } else {
                                Toast.makeText(MainActivity.this, "Register Didn't Work", Toast.LENGTH_LONG).show();
                                FlagClass.setFlag(false);

                            }

                        }
                    });
        }
        return FlagClass.isFlag();
    }
    public Person CreatePerson()
    {
        String email = ((EditText) findViewById(R.id.EmailReg)).getText().toString();
        String userName = ((EditText) findViewById(R.id.UserNameReg)).getText().toString();
        Person person = new Person(userName, email);

        return person;

    }
    public Person addGamestoPerson(Person person, String gamename)
    {

        person.gameList.add(gamename);
        return person;

    }
    public Person RemoveGamestoPerson(Person person, String gamename)
    {
        person.gameList.remove(gamename);
        return person;

    }
    public void addData(Person person) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        Map<String, Person> users = new HashMap<>();
        users.put(person.getEmail(), person);
        myRef.setValue(person);
    }
    public void function_go_to_Main(View view) {
        Intent intent = new Intent(this, MainActivityView.class);
        startActivity(intent);


    }

}