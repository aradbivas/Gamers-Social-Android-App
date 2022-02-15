package com.example.finallproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivityUserProfile extends AppCompatActivity {


    public static final int CAMERA_ACTION_CODE = 1;
    ImageView imageView;
    Button addGames;
    ActivityResultLauncher<String> activityResultLauncher;
    ActivityResultLauncher<Intent> activityResultPermission;
    ArrayList<String> gameList = new ArrayList<String>();
    Uri path;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.avatar);
        TextView name = findViewById(R.id.textViewUserName);
        TextView email = findViewById(R.id.EmailTextView);
        TextView games = findViewById(R.id.GamesTextView);
        Button prvt = findViewById(R.id.prvt);
        prvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MessageMainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile(v);
            }
        });
        Button friendList = findViewById(R.id.buttonmyfriends);
        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivityFriendList.class);
                startActivity(intent);
            }
        });
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference= db.getReference("users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};

                email.setText("Email: " +snapshot.child("email").getValue(String.class));
                name.setText("UserName: " +snapshot.child("name").getValue(String.class));
                gameList = snapshot.child("gameList").getValue(t);
                games.setText("Games: " + gameList.toString().replace("]","").replace("[",""));
                try {
                    File local =  File.createTempFile("tmp",".bmp");
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images");
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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                    path = result;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(bitmap);
                uploadImage(result);
            }
        });


    }



    private void selectFile(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            takePermission(v);
        }
        activityResultLauncher.launch("image/*");
    }

    private void takePermissions(View v) {

        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
        {
            try
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                activityResultPermission.launch(intent);


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }

    }
    private boolean isPermission()
    {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();
        }
        else
        {
            int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExternal == PackageManager.PERMISSION_GRANTED;
        }
    }
    public void takePermission(View view)
    {
        if(isPermission())
        {
            //
        }
        else
        {
            takePermissions(view);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 )
        {
            if(requestCode == 101)
            {
                boolean read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(read)
                {

                }
            }
        }
    }
    public void uploadImage(Uri result)
    {
        StorageReference storageRef =   FirebaseStorage.getInstance().getReference();
        StorageReference file = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images");
        DatabaseReference databaseReference = db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("url");
        file.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivityUserProfile.this, "worked", Toast.LENGTH_LONG).show();
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivityUserProfile.this, "didnt worked", Toast.LENGTH_LONG).show();

            }
        });

    }
}