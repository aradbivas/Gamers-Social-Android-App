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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardMembersList extends RecyclerView.Adapter<CardMembersList.ViewHolder>  {

    private ArrayList<ProfileData> dataSet;
    Context context;

    public CardMembersList(ArrayList<ProfileData> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        String uid;
        TextView textViewName;
        CardView cardView;

        public TextView getTextViewName() {
            return textViewName;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getImg() {
            return img;
        }

        ImageView img;

        public String getUid() {
            return uid;
        }

        public ViewHolder (View itemView )
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_member);
            textViewName = ( TextView) itemView.findViewById(R.id.editTextcard_member);
            img = (ImageView) itemView.findViewById(R.id.friendreqimg);

        }

    }
    @NonNull
    @Override
    public CardMembersList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_card_members_list , parent ,false);

        CardMembersList.ViewHolder myViewHolder = new CardMembersList.ViewHolder(view);
        myViewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.getUid() != "123") {
                    Intent i = new Intent(context, ShowProfile.class);
                    i.putExtra("Member_UID", myViewHolder.getUid());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }

        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardMembersList.ViewHolder viewHolder, int listPosition) {

        viewHolder.textViewName.setText(dataSet.get(listPosition).getName());
        viewHolder.uid = dataSet.get(listPosition).getUid();

        if(dataSet.get(listPosition).uri != null) {
            Picasso.get().load(dataSet.get(listPosition).uri).into(viewHolder.img);
        }
        else if(viewHolder.uid != "123")
        {
            viewHolder.img.setImageResource(R.drawable.profilepictmp);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}