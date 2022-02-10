package com.example.finallproject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CardMainActivity extends RecyclerView.Adapter<CardMainActivity.MyViewHolder>  {

    private ArrayList<DataModel> dataSet;
    Context context;

    public CardMainActivity(ArrayList<DataModel> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        public MyViewHolder (View itemView )
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textViewName = ( TextView) itemView.findViewById(R.id.editTextCard);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.item_image);

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
    public CardMainActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_card_main , parent ,false);

        CardMainActivity.MyViewHolder myViewHolder = new CardMainActivity.MyViewHolder(view);
        String UserName = getUserName();
        myViewHolder.imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TopicActivity.class);
                i.putExtra("Selected_Topic", myViewHolder.textViewName.getText());
                i.putExtra("User_Name", UserName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardMainActivity.MyViewHolder viewHolder, int listPosition) {


        viewHolder.textViewName.setText(dataSet.get(listPosition).getName());
        viewHolder.imageViewIcon.setImageResource(dataSet.get(listPosition).getImage());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}