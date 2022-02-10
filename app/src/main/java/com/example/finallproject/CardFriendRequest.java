package com.example.finallproject;



import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardFriendRequest extends RecyclerView.Adapter<CardFriendRequest.requestViewHolder>  {

    private ArrayList<ProfileData> dataSet;
    Context context;

    public CardFriendRequest(ArrayList<ProfileData> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    public static class requestViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        String name;
        ImageView imageViewIcon;
        String uid;
        String Url;
        Button accept;
        Button Decline;

        public String getUrl() {
            return Url;
        }

        public requestViewHolder (View itemView )
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textViewName = ( TextView) itemView.findViewById(R.id.editTextcard_req);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.friendreqimg);
            accept =(Button) itemView.findViewById(R.id.buttonAcceptFriend);
            Decline = (Button) itemView.findViewById(R.id.buttonAcceptFriend);
        }

        public String getUid() {
            return uid;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getImageViewIcon() {
            return imageViewIcon;
        }
    }
    public String getUserName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        return name;
    }
    @NonNull
    @Override
    public CardFriendRequest.requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_card_friend_request , parent ,false);
        Button accept;
        Button declaine;
        CardFriendRequest.requestViewHolder ViewHolder = new CardFriendRequest.requestViewHolder(view);
        ViewHolder.imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ViewHolder.uid != "123") {
                    Intent i = new Intent(context, ShowProfile.class);
                    i.putExtra("Member_UID", ViewHolder.getUid());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
        accept =  view.findViewById(R.id.buttonAcceptFriend);
        declaine = view.findViewById(R.id.buttonDecline);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference= db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendList");
                databaseReference.setValue(ViewHolder.getUid());
                databaseReference= db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendList").child(ViewHolder.getUid());
                DatabaseReference databaseReference2 = db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendList").child(ViewHolder.getUid()).child("name");
                databaseReference2.setValue(ViewHolder.name);
                DatabaseReference databaseReference3= db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendList").child(ViewHolder.getUid()).child("url");
                databaseReference3.setValue(ViewHolder.getUrl());
                databaseReference = db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendRequest").child(ViewHolder.getUid());
                databaseReference.removeValue();
                Toast.makeText(context, "Friend added!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MainActivityFriendList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        declaine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = db.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("friendList").child("friendRequest").child(ViewHolder.getUid());
                databaseReference.removeValue();
                Toast.makeText(context, "Friend decliend!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MainActivityFriendList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardFriendRequest.requestViewHolder viewHolder, int listPosition) {


        viewHolder.textViewName.setText(dataSet.get(listPosition).getName());
        viewHolder.uid = dataSet.get(listPosition).getUid();
        viewHolder.name = dataSet.get(listPosition).getName();
        if(dataSet.get(listPosition).uri != null) {
            Picasso.get().load(dataSet.get(listPosition).uri).into(viewHolder.imageViewIcon);
            viewHolder.Url =dataSet.get(listPosition).uri;
        }
        else if(viewHolder.uid != "123")
        {
            viewHolder.imageViewIcon.setImageResource(R.drawable.profilepictmp);
            viewHolder.accept.setVisibility(View.INVISIBLE);
            viewHolder.Decline.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
