package com.example.bookapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.models.BookCopy;
import com.example.bookapp.seller.addCopyActivity;
import com.example.bookapp.seller.copiesList;

import java.util.List;

public class copyBookAdapter extends RecyclerView.Adapter<copyBookAdapter.MyViewHolder> {

    List<BookCopy> list;
    Activity context;
    SessionManager session;
    public copyBookAdapter(List<BookCopy> list, Activity context) {
        this.list = list;
        this.context = context;
        session = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_single_view_copybook, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.isbn.setText(list.get(position).getIsbn());
        holder.numberOfCopies.setText(list.get(position).getNumberOfCopies());
        holder.numberOfPages.setText(list.get(position).getNumberOfPages());

         holder.copyDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 BookCopy i = list.get(position);
                 copiesList b = (copiesList) context;
                 b.delete(i);
             }
         });

         holder.copyEdit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 BookCopy i = list.get(position);
                 Intent intent = new Intent(context, addCopyActivity.class);
                 intent.putExtra("book_copy",  i);
                 context.startActivity(intent);
             }
         });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  isbn, numberOfPages, numberOfCopies;
        ImageButton copyEdit ;
        ImageButton copyDelete ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            isbn = itemView.findViewById(R.id.isbn);
            numberOfCopies = itemView.findViewById(R.id.numberOfCopies);
            numberOfPages = itemView.findViewById(R.id.totalPrice);

            copyEdit = itemView.findViewById(R.id.copyEdit);
            copyDelete = itemView.findViewById(R.id.copyDelete);

        }
    }
}
