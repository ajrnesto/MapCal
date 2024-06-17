package com.mapcaladministrators.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapcaladministrators.Objects.User;
import com.mapcaladministrators.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.usersViewHolder> {

    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    Context context;
    ArrayList<User> arrUsers;
    private OnUsersListener mOnUsersListener;

    public UsersAdapter(Context context, ArrayList<User> arrUsers, OnUsersListener onUsersListener) {
        this.context = context;
        this.arrUsers = arrUsers;
        this.mOnUsersListener = onUsersListener;
    }

    @NonNull
    @Override
    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_user, parent, false);
        return new usersViewHolder(view, mOnUsersListener);
    }

    @Override
    public void onBindViewHolder(@NonNull usersViewHolder holder, int position) {
        User user = arrUsers.get(position);

        String uid = user.getUid();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String mobile = user.getMobile();
        String password = user.getPassword();
        String status = user.getStatus();
        String userType = user.getUserType();

        holder.tvFullName.setText(firstName + " " + lastName);
        holder.tvEmail.setText(email);
        holder.tvMobile.setText(mobile);
        loadStatus(holder, status);
    }

    private void loadStatus(usersViewHolder holder, String status) {
        if (Objects.equals(status, "ACTIVE")) {
            holder.btnAction.setText("Suspend");
            holder.tvStatus.setText("Active");
            holder.tvStatus.setTextColor(context.getColor(R.color.green_padua));
            holder.btnAction.setBackgroundColor(context.getColor(R.color.terracotta));
        }
        else if (Objects.equals(status, "SUSPENDED")) {
            holder.btnAction.setText("Activate");
            holder.tvStatus.setText("Suspended");
            holder.tvStatus.setTextColor(context.getColor(R.color.terracotta));
            holder.btnAction.setBackgroundColor(context.getColor(R.color.green_padua));
        }
    }

    @Override
    public int getItemCount() {
        return arrUsers.size();
    }

    public class usersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnUsersListener onUsersListener;
        TextView tvFullName, tvEmail, tvMobile, tvStatus;
        MaterialButton btnAction;

        public usersViewHolder(@NonNull View itemView, OnUsersListener onUsersListener) {
            super(itemView);

            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAction = itemView.findViewById(R.id.btnAction);

            this.onUsersListener = onUsersListener;
            btnAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onUsersListener.onUsersClick(getAdapterPosition());
        }
    }

    public interface OnUsersListener{
        void onUsersClick(int position);
    }
}
