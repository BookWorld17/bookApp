package com.example.bookapp.buyer;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.spinnerCategoryAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.Category;
import com.example.bookapp.models.Order;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class openDisputeActivity extends BaseActivity {

    private static final String TAG = "openDisputeActivity";
    private boolean allowMediaAccess = true;
    private ImageView bookImage;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private EditText summary;


    AlertManager alert ;
    private Bitmap uploadedImage;
    private TextView interface_title ,order_date , order_status , book_name ,seller_name , seller_address , seller_email;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.open_dispute_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        order_date = (TextView) findViewById(R.id.order_date);
        order_status = (TextView) findViewById(R.id.order_status);
        book_name = (TextView) findViewById(R.id.book_name);

        summary = (EditText) findViewById(R.id.summary);
        interface_title = (TextView) findViewById(R.id.interface_title);


        alert = new AlertManager(openDisputeActivity.this);

        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(checkData() ){
                    openDispute();
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

        bookImage = (ImageView) findViewById(R.id.imageView2);
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });


        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("Order")){
                order = (Order)extras.getSerializable("Order");

                order_date.setText(getString(R.string.order_date) + " " + order.getOrder_date());
                order_status.setText(getString(R.string.order_status) + " " + order.getStatusText(this));
                book_name.setText("Order ID ("+order.getId()+")");
            }
        }
    }

    private boolean checkData() {
        boolean valid = true;

        String summaryText = summary.getText().toString();

        if (summaryText.isEmpty()  ) {
            summary.setError("You have to write some description");
            valid = false;
        } else {
            summary.setError(null);
        }

        if(uploadedImage == null){
            Toast.makeText(openDisputeActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }


    private void openDispute() {

        String request = URLs.OPEN_DISPUTE ;

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

                String img = getStringImage(uploadedImage);
                params.put("image",img);
                params.put("summary",summary.getText().toString());



                SessionManager session = new SessionManager(openDisputeActivity.this);
                String userID = session.getUserID();
                params.put("user_id",userID);
                params.put("order_id" , order.getId());
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




    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }




    private void onUpdateSuccess() {
        alert.showAlertDialog(openDisputeActivity.this, "Saved","Your dispute have been saved successfully",true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), ordersList.class);
                        startActivity(i);
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



    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        if(allowMediaAccess) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView2);
                // Set the Image in ImageView after decoding the String
                uploadedImage = BitmapFactory.decodeFile(imgDecodableString);
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(openDisputeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
