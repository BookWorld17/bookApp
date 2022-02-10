package com.example.bookapp.seller;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.AlertManager;
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addCopyActivity extends BaseActivity {

    private static final String TAG = "addBookActivity";
    private boolean allowMediaAccess = true;
    private ImageView bookImage;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    private Spinner bookTypeSpinner;

    private EditText bookName, authorName, isbn,price,numberOfCopies,numberOfPages;


    AlertManager alert ;
    private Bitmap uploadedImage;
    private Book book;
    private BookCopy book_copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.add_copy_book_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        bookName = (EditText) findViewById(R.id.txtBookName);
        authorName = (EditText) findViewById(R.id.authorName);
        isbn = (EditText) findViewById(R.id.isbn);
        price = (EditText) findViewById(R.id.price);
        numberOfCopies = (EditText) findViewById(R.id.numberOfCopies);
        numberOfPages = (EditText) findViewById(R.id.totalPrice);

        alert = new AlertManager(addCopyActivity.this);

        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(checkData() ){
                    addNewBook();
                }else{
                    String errMsg = "Please insert book data";
                    showMessageOKCancel(errMsg,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                }
            }
        });

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("book")){
                book = (Book)extras.getSerializable("book");
                bookName.setText(book.getBookName());
                authorName.setText(book.getAuthorName());
            }
            if(extras.containsKey("book_copy")){
                book_copy = (BookCopy)extras.getSerializable("book_copy");
                isbn.setText(book_copy.getIsbn());
                numberOfCopies.setText(book_copy.getNumberOfCopies());
                numberOfPages.setText(book_copy.getNumberOfPages());
                price.setText(book_copy.getPrice());

                bookName.setVisibility(View.GONE);
                authorName.setVisibility(View.GONE);
            }
        }

    }

    private boolean checkData() {
        boolean valid = true;
        String isbnT = isbn.getText().toString();

        if (isbnT.isEmpty()) {
            isbn.setError("ISBN Must be set");
            valid = false;
        } else {
            isbn.setError(null);
        }


        String regex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(isbnT);
        if(!matcher.matches()){
            isbn.setError("ISBN is not valid");
            valid = false;
        } else {
            isbn.setError(null);
        }

        String numberOfCopiesText = numberOfCopies.getText().toString();

        if (numberOfCopiesText.isEmpty()  ) {
            numberOfCopies.setError("Write number of copies");
            valid = false;
        } else {
            numberOfCopies.setError(null);
        }

        String numberOfPagesText = numberOfPages.getText().toString();

        if (numberOfPagesText.isEmpty()  ) {
            numberOfPages.setError("Write number of pages");
            valid = false;
        } else {
            numberOfPages.setError(null);
        }

        String priceText = price.getText().toString();

        if (priceText.isEmpty()  ) {
            price.setError("set the pice");
            valid = false;
        } else {
            price.setError(null);
        }

        return valid;
    }


    private void addNewBook() {

        String request = URLs.ADD_COPY_BOOK ;

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", getString(R.string.waiting), false, false);
        Toast.makeText(this, request, Toast.LENGTH_LONG).show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        doAction(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(register.this,error.toString(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        onUpdateFailed();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();


                params.put("numberOfCopies", numberOfCopies.getText().toString().trim());
                params.put("numberOfPages", numberOfPages.getText().toString().trim());
                params.put("price", price.getText().toString().trim());
                params.put("isbn", isbn.getText().toString().trim());



                SessionManager session = new SessionManager(addCopyActivity.this);
                String userID = session.getUserID();
                params.put("user_id",userID);

                if(book != null){
                    params.put("book_id",book.getID());
                }
                if(book_copy != null){
                    params.put("book_id",book_copy.getBook_id());
                    params.put("book_copy_id" , book_copy.getID());
                    params.put("action" , "edit");

                }
                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private JSONArray users = null;

    public void doAction(String response){
        JSONObject jsonObject = null;
        try {
            System.out.println(response);
            jsonObject = new JSONObject(response);
            users = jsonObject.getJSONArray("result");

            JSONObject jo = users.getJSONObject(0);
            String success = jo.getString("success");

            if(success.equals("1")){
                onUpdateSuccess();
            }else{
                onUpdateFailed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    private void onUpdateSuccess() {
        alert.showAlertDialog(addCopyActivity.this, "Saved","Your data have been saved successfully",true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    private void onUpdateFailed() {
        showMessageOKCancel("Error, Data not saved",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(addCopyActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
