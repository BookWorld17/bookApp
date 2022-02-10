package com.example.bookapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bookapp.R;
import com.example.bookapp.models.BookCopy;

import java.util.List;

public class spinnerCopiesAdapter extends ArrayAdapter<BookCopy> {
    LayoutInflater flater;

    public spinnerCopiesAdapter(Activity context, int resouceId, int textviewId, List<BookCopy> list){
        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BookCopy rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.spinner_dropdown_item,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(android.R.id.text1);
        txtTitle.setText(rowItem.getIsbn());

        return rowview;
    }
}
