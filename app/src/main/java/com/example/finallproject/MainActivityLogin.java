package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    User person;
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
                                Toast.makeText(MainActivityLogin.this, "Login Worked", Toast.LENGTH_LONG).show();
                                function_go_to_Main(view);

                            } else {
                                Toast.makeText(MainActivityLogin.this, "Login Didn't Work", Toast.LENGTH_LONG).show();
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
            Toast.makeText(MainActivityLogin.this, email, Toast.LENGTH_LONG).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FlagClass.setFlag(true);
                                Toast.makeText(MainActivityLogin.this, "Register Worked", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_addGamesFragment);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

                                user.updateProfile(profileUpdates);
                            } else {
                                Toast.makeText(MainActivityLogin.this, "Register Didn't Work", Toast.LENGTH_LONG).show();
                                FlagClass.setFlag(false);

                            }

                        }
                    });
        }
        return FlagClass.isFlag();
    }
    public User CreatePerson()
    {
        String email = ((EditText) findViewById(R.id.EmailReg)).getText().toString();
        String userName = ((EditText) findViewById(R.id.UserNameReg)).getText().toString();
        User person = new User(userName, email);

        return person;

    }
    public User addGamestoPerson(User person, String gamename)
    {

        person.gameList.add(gamename);
        return person;

    }
    public User RemoveGamestoPerson(User person, String gamename)
    {
        person.gameList.remove(gamename);
        return person;

    }
    public void addData(User person) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        Map<String, User> users = new HashMap<>();
        users.put(person.getEmail(), person);
        myRef.setValue(person);
    }
    public void function_go_to_Main(View view) {
        Intent intent = new Intent(this, MainActivityView.class);
        startActivity(intent);


    }

}