package com.example.bookapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.LoadImgFromURL;
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.buyer.BookDetailsActivity;
import com.example.bookapp.buyer.cart;
import com.example.bookapp.buyer.orderDetailsActivity;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.example.bookapp.models.Cart;
import com.example.bookapp.seller.addCopyActivity;
import com.example.bookapp.seller.copiesList;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder> {

    List<Cart> list;
    Activity context;
    SessionManager session;
    public cartAdapter(List<Cart> list, Activity context) {
        this.list = list;
        this.context = context;
        session = new SessionManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_single_cart_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(list.get(position).getBook().getBookName());
        holder.totalPrice.setText(Double.toString(list.get(position).getTotalPrice())+ context.getResources().getString(R.string.currency));
        new LoadImgFromURL(holder.bookImg, 0,0).execute(list.get(position).getBook().getImage());
        holder.qty.setText(Integer.toString(list.get(position).getQty()));

        holder.bookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book b = list.get(position).getBook();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("Book",  b);
                //intent.putExtra("orderBtn",  "hide");
                context.startActivity(intent);
            }
        });
        if(context instanceof orderDetailsActivity){
            holder.cartItemDelete.setVisibility(View.GONE);
        }
        holder.cartItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart temp = (cart)context;
                temp.removeItem(list.get(position));
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  title, totalPrice, qty;
        ImageView bookImg ;
        ImageButton cartItemDelete ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            bookImg = itemView.findViewById(R.id.bookImg);
            totalPrice = itemView.findViewById(R.id.totalPrice);

            qty = itemView.findViewById(R.id.qty);
            cartItemDelete = itemView.findViewById(R.id.cartItemDelete);

        }
    }
}
