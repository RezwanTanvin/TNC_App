package com.ellerlabs.tncapp.AdminScreen.CommuteData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellerlabs.tncapp.R;

import java.util.ArrayList;

public class ListDataAdapterForSACUser extends
        RecyclerView.Adapter<ListDataAdapterForSACUser.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DisplayNameTV;
        public TextView EmailTV;
        public ImageView arrowBtn;
        public Context context;
        public LinearLayout userInfoBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DisplayNameTV = itemView.findViewById(R.id.DisplayNameTV);
            EmailTV = itemView.findViewById(R.id.EmailTV);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            context = itemView.getContext();
            userInfoBox = itemView.findViewById(R.id.userInfoRow);

        }
    }

    private ArrayList<UserDataObjForSACUser> userDataObjForSACUserArrayList;

    public ListDataAdapterForSACUser(ArrayList<UserDataObjForSACUser> userDataObjForSACUserArrayList) {
        this.userDataObjForSACUserArrayList = userDataObjForSACUserArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.admin_mode_choose_user_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListDataAdapterForSACUser.ViewHolder holder, int position) {

        UserDataObjForSACUser data = userDataObjForSACUserArrayList.get(position);

        TextView DisplayNameTV_ = holder.DisplayNameTV;
        DisplayNameTV_.setText(data.DisplayName);

        TextView EmailTV_ = holder.EmailTV;
        EmailTV_.setText(data.Email);

        ImageView arrowBtn = holder.arrowBtn;

        LinearLayout userInfoBox_ = holder.userInfoBox;

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Key",data.Key);


            }
        });
    }

    @Override
    public int getItemCount() {
        if (userDataObjForSACUserArrayList == null){
            return 0;
        }
        else{
            return userDataObjForSACUserArrayList.size();
        }
    }


}
