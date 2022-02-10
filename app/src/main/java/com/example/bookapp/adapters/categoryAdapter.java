package com.example.bookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;
import com.example.bookapp.models.Category;

import java.util.List;


public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.MyViewHolder>{



    private List<Category> itmesList;

    private Context _context = null;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{


        private Button categ_btn;


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


        public MyViewHolder(View view) {
            super(view);
            categ_btn = (Button) view.findViewById(R.id.cat_btn);
        }
    }

    public categoryAdapter(Context context , List<Category> itmesList) {
        this.itmesList = itmesList;
        _context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category itemRow = itmesList.get(position);

        holder.categ_btn.setText(itemRow.getName());

    }

    @Override
    public int getItemCount() {
        return itmesList.size();
    }
}
