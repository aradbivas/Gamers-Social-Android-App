package com.example.finallproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

public class CardPrivateMessages extends RecyclerView.Adapter<CardPrivateMessages.PrivateViewHolder>  {

    private ArrayList<PrivateChatData> dataSet;
    Context context;

    public CardPrivateMessages(ArrayList<PrivateChatData> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    public static class PrivateViewHolder extends RecyclerView.ViewHolder
    {
        String MyUserName;
        String ChatUserName;
        TextView textViewName;
        CardView cardView;
        Boolean openedChat;

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


        public PrivateViewHolder (View itemView )
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_private);
            textViewName = ( TextView) itemView.findViewById(R.id.editTextcard_Private);
            img = (ImageView) itemView.findViewById(R.id.privateImg);

        }

    }
    @NonNull
    @Override
    public CardPrivateMessages.PrivateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.activity_card_private_messages , parent ,false);

        CardPrivateMessages.PrivateViewHolder myViewHolder = new CardPrivateMessages.PrivateViewHolder(view);
        myViewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivityPrivateMessages.class);
                if (!myViewHolder.ChatUserName.equals("123")) {
                    if (myViewHolder.openedChat) {
                        i.putExtra("Selected_Topic", myViewHolder.MyUserName);
                        i.putExtra("participants", myViewHolder.ChatUserName + "," + myViewHolder.MyUserName);
                    } else {
                        i.putExtra("Selected_Topic", myViewHolder.ChatUserName);
                        i.putExtra("participants", myViewHolder.MyUserName + "," + myViewHolder.ChatUserName);

                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateViewHolder holder, int position) {
//        viewHolder.img.setImageDrawable(dataSet.get(listPosition).Img.getDrawable());
        holder.ChatUserName = dataSet.get(position).getChatUserName();
        if(!holder.ChatUserName.equals("123"))
        {
            holder.textViewName.setText(dataSet.get(position).getChatUserName());
            holder.MyUserName = dataSet.get(position).getMyUserName();
            holder.openedChat = dataSet.get(position).OpenedChat;


        }
        else
        {
            holder.textViewName.setText("There is not private messages");
            holder.img.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}