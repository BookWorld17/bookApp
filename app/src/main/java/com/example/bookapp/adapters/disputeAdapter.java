package com.example.bookapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.LoadImgFromURL;
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.example.bookapp.buyer.BookDetailsActivity;
import com.example.bookapp.buyer.openDisputeActivity;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.Dispute;
import com.example.bookapp.models.Order;

import java.util.List;

public class disputeAdapter extends RecyclerView.Adapter<disputeAdapter.MyViewHolder> {

    List<Dispute> list;
    Activity context;
    SessionManager session;
    public disputeAdapter(List<Dispute> list, Activity context) {
        this.list = list;
        this.context = context;
        session = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_dispute, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Dispute item = list.get(position);

        holder.dispute_date.setText(context.getString(R.string.dispute_date) + " " + item.getDispute_date());
        holder.dispute_status.setText(context.getString(R.string.dispute_status) + " " + item.getStatusText(context));
        holder.dispute_description.setText(  item.getDescription());
        if(item.getStatus().equals("1")) {
            holder.admin_dec_txt.setText(  "Decision Date: \n"+item.getDecision_date()+"\n\n"+item.getAdmin_decision());
        }

        new LoadImgFromURL(holder.evidance, 0 , 0).execute(item.getImg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dispute_date, dispute_status , dispute_description, admin_dec_txt;
        ImageView evidance;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dispute_date = itemView.findViewById(R.id.dispute_date);
            dispute_status = itemView.findViewById(R.id.dispute_status);
            dispute_description = itemView.findViewById(R.id.dispute_description);
            evidance = itemView.findViewById(R.id.evidance);
            admin_dec_txt = itemView.findViewById(R.id.admin_dec_txt);

        }
    }
}
