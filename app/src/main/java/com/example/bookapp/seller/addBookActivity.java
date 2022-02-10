package com.example.bookapp.seller;


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

import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.spinnerCategoryAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.Category;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addBookActivity extends BaseActivity {

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

    private EditText bookName,authorName,publisherName,summary;


    AlertManager alert ;
    private Bitmap uploadedImage;
    private Book book;
    private TextView interface_title;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.add_new_book_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        isAppHasAccessPremission();

        bookName = (EditText) findViewById(R.id.txtBookName);
        authorName = (EditText) findViewById(R.id.authorName);
        publisherName = (EditText) findViewById(R.id.publisherName);
        summary = (EditText) findViewById(R.id.summary);
        interface_title = (TextView) findViewById(R.id.interface_title);

        alert = new AlertManager(addBookActivity.this);

        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(checkData() ){
                    addBook();
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

        bookTypeSpinner = (Spinner) findViewById(R.id.bookTypeSpinner);

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        spinnerCategoryAdapter categAdapter = new spinnerCategoryAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                android.R.id.text1,
                URLs.categoriesList);
        categorySpinner.setAdapter(categAdapter);
        categAdapter.notifyDataSetChanged();

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("book")){
                book = (Book)extras.getSerializable("book");
                interface_title.setText("Edit Book " + book.getBookName());
                bookName.setText(book.getBookName());
                authorName.setText(book.getAuthorName());
                publisherName.setText(book.getPublisherName());
                summary.setText(book.getSummary());
                new LoadImgFromURL(bookImage,0,0).execute(book.getImage());
                if(book.getBookType().equals("New")) {
                    bookTypeSpinner.setSelection(0);
                }else{
                    bookTypeSpinner.setSelection(1);
                }
            }
        }
    }

    private boolean checkData() {
        boolean valid = true;
        String pName = bookName.getText().toString();

        if (pName.isEmpty() || pName.length() < 3) {
            bookName.setError("at least 3 characters");
            valid = false;
        } else {
            bookName.setError(null); }
        String summaryText = summary.getText().toString();

        if (summaryText.isEmpty()  ) {
            summary.setError("You have to write some description");
            valid = false;
        } else {
            summary.setError(null);
        }

        if(uploadedImage == null){
            Toast.makeText(addBookActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }


    private void addBook() {

        String request = URLs.ADD_BOOK ;

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

                String bookImg = getStringImage(uploadedImage);
                params.put("image",bookImg);

                String name = bookName.getText().toString().trim();
                String author = authorName.getText().toString().trim();
                String publisher = publisherName.getText().toString().trim();
                String summaryTxt = summary.getText().toString().trim();

                params.put("bookName",name);
                params.put("authorName",author);
                params.put("publisherName",publisher);
                params.put("summary",summaryTxt);


                String book_type = bookTypeSpinner.getSelectedItem().toString();
                params.put("book_type", book_type);

                String category_id = ((Category)categorySpinner.getSelectedItem()).getId();
                params.put("category_id", category_id);

                SessionManager session = new SessionManager(addBookActivity.this);
                String userID = session.getUserID();
                params.put("user_id",userID);
                if(book != null){
                    params.put("book_id" , book.getID());
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




    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }




    private void onUpdateSuccess() {
        alert.showAlertDialog(addBookActivity.this, "Saved","Your data have been saved successfully",true,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), booksList.class);
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



    private void isAppHasAccessPremission()
    {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                Log.d(TAG, "Permission requests");
                allowMediaAccess = false;
            }
        }
    }



    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    allowMediaAccess = true;
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(addBookActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
