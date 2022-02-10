package com.example.bookapp.seller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.AlertManager;
import com.example.bookapp.LoadImgFromURL;
import com.example.bookapp.R;
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.spinnerCopiesAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sellerBookDetailsActivity extends BaseActivity {

    private static final String TAG = "viewBookActivity";
    private ImageView bookImage;

    private TextView bookName,authorName,publisherName,summary ,bookTypeTxt;
    private TextView price,numberOfPages,numberOfCopies ;


    AlertManager alert ;
    private Book book;
    private TextView interface_title;
    private ArrayList<BookCopy> copiesList = new ArrayList<>();
    private Spinner copiesSpinner;
    private BookCopy selectedCopy;
    private spinnerCopiesAdapter adapter;
    private Button btn_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.book_details_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);


        bookName = (TextView) findViewById(R.id.txtBookName);
        authorName = (TextView) findViewById(R.id.authorName);
        publisherName = (TextView) findViewById(R.id.publisherName);
        summary = (TextView) findViewById(R.id.summary);
        bookTypeTxt = (TextView) findViewById(R.id.bookTypeTxt);
        interface_title = (TextView) findViewById(R.id.interface_title);

        price = (TextView) findViewById(R.id.price);
        numberOfPages = (TextView) findViewById(R.id.totalPrice);
        numberOfCopies = (TextView) findViewById(R.id.numberOfCopies);



        //link the adapter to the spinner
        copiesSpinner = (Spinner) findViewById(R.id.copiesSpinner);


        adapter = new spinnerCopiesAdapter(sellerBookDetailsActivity.this,
                R.layout.spinner_dropdown_item, android.R.id.text1, copiesList);

        copiesSpinner.setAdapter(adapter);

        alert = new AlertManager(sellerBookDetailsActivity.this);

        Button saveBtn = (Button) findViewById(R.id.btn_save);

        bookImage = (ImageView) findViewById(R.id.imageView2);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("Book")){
                book = (Book)extras.getSerializable("Book");
                interface_title.setText(  book.getBookName() + " Details");
                bookName.setText(book.getBookName());
                authorName.setText(book.getAuthorName());
                publisherName.setText(book.getPublisherName());
                summary.setText(book.getSummary());
                bookTypeTxt.setText(book.getBookType());
                new LoadImgFromURL(bookImage,0,0).execute(book.getImage());

                copiesList = book.getCopyArrayList();


                adapter = new spinnerCopiesAdapter(sellerBookDetailsActivity.this,
                        R.layout.spinner_dropdown_item, android.R.id.text1, book.getCopyArrayList());

                copiesSpinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(copiesList.size()>0) {
                    selectedCopy = copiesList.get(0);
                    price.setText(selectedCopy.getPrice()+ getResources().getString(R.string.currency));
                    numberOfCopies.setText(selectedCopy.getNumberOfCopies());
                    numberOfPages.setText(selectedCopy.getNumberOfPages());
                }
                if(extras.containsKey("orderBtn")){
                    if(extras.getString("orderBtn").equals("hide")){
                        saveBtn.setVisibility(View.GONE);
                    }
                }
            }
        }
        saveBtn.setVisibility(View.GONE);

        copiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCopy = copiesList.get(i);
                price.setText(selectedCopy.getPrice()+ getResources().getString(R.string.currency));
                numberOfCopies.setText(selectedCopy.getNumberOfCopies());
                numberOfPages.setText(selectedCopy.getNumberOfPages());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_rate = (Button)findViewById(R.id.btn_rate);

        if(session.getUserType().equals("Seller")){
            btn_rate.setVisibility(View.GONE);
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(sellerBookDetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



}
