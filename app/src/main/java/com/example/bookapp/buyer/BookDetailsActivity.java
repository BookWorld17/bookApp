package com.example.bookapp.buyer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.bookapp.adapters.reviewAdapter;
import com.example.bookapp.adapters.spinnerCopiesAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.example.bookapp.models.Review;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDetailsActivity extends BaseActivity {

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
    private ProgressDialog loading;

    private List<Review> reviewsList = new ArrayList<>();
    private reviewAdapter revAdapter;
    private RecyclerView recyclerView;
    private JSONArray result;

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


        adapter = new spinnerCopiesAdapter(BookDetailsActivity.this,
                R.layout.spinner_dropdown_item, android.R.id.text1, copiesList);

        copiesSpinner.setAdapter(adapter);

        alert = new AlertManager(BookDetailsActivity.this);

        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showConfirmDialog( );
            }
        });

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


                adapter = new spinnerCopiesAdapter(BookDetailsActivity.this,
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




        revAdapter = new reviewAdapter(this, reviewsList);

        recyclerView = (RecyclerView) findViewById(R.id.comments_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(revAdapter);
        btn_rate = findViewById(R.id.btn_rate);
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDoRateDialog();
            }
        });
        loadReviews();
    }




    private void showDoRateDialog( ){
        final Dialog dialog = new Dialog(BookDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.review_book);
        Button send = dialog.findViewById(R.id.send);
        EditText txtComment = dialog.findViewById(R.id.txtComment);
        RatingBar ratingbar = dialog.findViewById(R.id.rating);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtComment.getText().toString().equals(""))
                {
                    Toast.makeText( getApplicationContext(), "You must write your comment", Toast.LENGTH_LONG).show();
                }else {
                    setReview( txtComment.getText().toString(), Float.toString(ratingbar.getRating()), dialog);
                    Toast.makeText(getApplicationContext(), "Rating...", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    private void setReview(String comment, String rating, Dialog dialog) {
        loading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SET_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        dialog.dismiss();
                        reviewsList.clear();
                        revAdapter.notifyDataSetChanged();
                        loadReviews();
                        JSONObject jsonObject = null;
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");

                            JSONObject jo = result.getJSONObject(0);
                            String success = jo.getString("success");

                            if(success.equals("1")){
                                alert.showAlertDialog(BookDetailsActivity.this,"Success","Your review submitted successfully", true);
                                btn_rate.setVisibility(View.GONE);
                            }else{
                                alert.showAlertDialog(BookDetailsActivity.this,"Error","You are already submit a review", false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog(BookDetailsActivity.this,"Error","Couldn't post your review", false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(BookDetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("book_id" , book.getID());
                params.put("buyer_id" , session.getUserID());
                params.put("comment" , comment);
                params.put("rate" , rating);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadReviews() {
        loading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GET_REVIEWS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");

                            JSONObject jo = result.getJSONObject(0);
                            String success = jo.getString("success");

                            if(success.equals("1")){
                                for(int i=1;i<result.length();i++){
                                    jo = result.getJSONObject(i);

                                    String id = jo.getString("id");
                                    String buyer_id = jo.getString("buyer_id");
                                    String book_id = jo.getString("book_id");
                                    String comment = jo.getString("comment");
                                    String rate = jo.getString("rate");
                                    String rev_date = jo.getString("rev_date");
                                    Review r = new Review(id, buyer_id, book_id, comment, rate, rev_date);
                                    if(r.getBuyer_id().equals(session.getUserID())){
                                        btn_rate.setVisibility(View.GONE);
                                    }
                                    reviewsList.add(r);
                                }
                                revAdapter.notifyDataSetChanged();
                            }else{

                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog( BookDetailsActivity.this,
                                    "Error",
                                    "Could't fetch reviews",
                                    false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(BookDetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("book_id" , book.getID());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    private void loading(){
        loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(BookDetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    private void showConfirmDialog( ){
        final Dialog dialog = new Dialog(BookDetailsActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addtocart_dialog_form);
        Button confirmBtn = dialog.findViewById(R.id.confirmBtn);
        TextView orderMsg = dialog.findViewById(R.id.orderMsg);
        EditText qty = dialog.findViewById(R.id.qty);
        TextView total = dialog.findViewById(R.id.total);



        orderMsg.setText("You ordered ["+book.getBookName()+"], " +
                "Version/ISBN ["+selectedCopy.getIsbn()+"] " +
                "whith price ("+selectedCopy.getPrice()+ getResources().getString(R.string.currency)+") and available qty is "+selectedCopy.getNumberOfCopies());

        int qtyNum = Integer.parseInt(qty.getText().toString());
        int avaQty = Integer.parseInt(selectedCopy.getNumberOfCopies());
        total.setText(Double.toString(qtyNum * Double.parseDouble(selectedCopy.getPrice()))+ getResources().getString(R.string.currency));
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qty.getText().toString().equals(""))
                {
                    Toast.makeText( getApplicationContext(), "You must set the quantity", Toast.LENGTH_LONG).show();
                }else {
                    int qtyNum = Integer.parseInt(qty.getText().toString());
                    if(qtyNum<1 || qtyNum > avaQty){
                        Toast.makeText( getApplicationContext(), "Quantity must be  between 0 and "+avaQty, Toast.LENGTH_LONG).show();
                    }else {
                        addToCart( dialog, qty.getText().toString());
                        Toast.makeText(getApplicationContext(), "Submitting Order...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                calculate(total, s);
            }
        });
        dialog.show();
    }

    private void calculate(TextView total, CharSequence s){
        try {
            int qtyNum = Integer.parseInt(s.toString());
            total.setText(Double.toString(qtyNum * Double.parseDouble(selectedCopy.getPrice()))+ getResources().getString(R.string.currency));
        }catch (Exception e){}
    }

    private void addToCart(Dialog dialog, String qtyNum) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ADD_TO_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(dialog != null)
                            dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");

                            JSONObject jo = result.getJSONObject(0);
                            String success = jo.getString("success");

                            if(success.equals("1")){
                                String msg = getString(R.string.book_added_to_cart_success);
                                alert.showAlertDialog(BookDetailsActivity.this,getString(R.string.success),msg, true);
                            }else{
                                String msg = getString(R.string.book_not_to_cart_success);
                                alert.showAlertDialog(BookDetailsActivity.this,getString(R.string.error),msg, false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog(BookDetailsActivity.this,getString(R.string.error),getString(R.string.book_not_to_cart_success), false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BookDetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("book_copy_id" , selectedCopy.getID());
                params.put("buyer_id" , session.getUserID());
                params.put("qty" , qtyNum);
                params.put("seller_id" , book.getSeller_id());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
