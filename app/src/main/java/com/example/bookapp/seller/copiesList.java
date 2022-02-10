package com.example.bookapp.seller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.bookAdapter;
import com.example.bookapp.adapters.copyBookAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class copiesList extends BaseActivity {

    ArrayList<BookCopy> copyArrayList = new ArrayList<>();
    public ProgressDialog progressDialog = null;
    RecyclerView listview;
    private SessionManager session;
    private AlertManager alert ;
    private copyBookAdapter adapter = null;
    private TextView interface_title;
    private Book book;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.seller_copies_list_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        session = new SessionManager(copiesList.this);
        alert = new AlertManager(copiesList.this);


        interface_title = (TextView) findViewById(R.id.interface_title);
        interface_title.setText("Copies of Book");
        listview = findViewById(R.id.list);

        listview.setHasFixedSize(true);
        listview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1  ));


        adapter = new copyBookAdapter( copyArrayList , copiesList.this);
        listview.setAdapter(adapter);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("book")){
                book = (Book)extras.getSerializable("book");
                interface_title.setText("Copies of Book " + book.getBookName());
                loadProperties();
            }
        }

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.bookAddBtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(copiesList.this, addCopyActivity.class);
                intent.putExtra("book",  book);
                startActivity(intent);
            }
        });
    }

    private void loadProperties() {

        String seller_id = session.getUserID();
        String request = URLs.GET_COPIES_BOOK;

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", getString(R.string.waiting), false, false);

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
                        onLoadError();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id" , seller_id);
                params.put("book_id" , book.getID());
                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onLoadError() {
        alert.showAlertDialog(copiesList.this,"Error","No data Available",false);
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
                for(int i=1;i<users.length();i++){
                    jo = users.getJSONObject(i);


                    String id = jo.getString("id");
                    String book_id = jo.getString("book_id");
                    String isbn = jo.getString("isbn");
                    String price = jo.getString("price");
                    String numberOfCopies = jo.getString("numberOfCopies");
                    String numberOfPages = jo.getString("numberOfPages");
                    String addDate = jo.getString("addDate");
                    BookCopy copy = new BookCopy(id, book_id,  isbn, price, numberOfCopies, numberOfPages, addDate );
                    copyArrayList.add(copy);


                    onLoadSucess();
                }
                book.setCopyArrayList(copyArrayList);
            }else{
                onLoadError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onLoadSucess() {

        adapter.notifyDataSetChanged();

    }


    @Override
    protected void onResume() {
        super.onResume();
        copyArrayList.clear();
        loadProperties();
    }

    public void delete(BookCopy bc) {

        alert.showMessageOKCancel("Are you sure to delete book with ISBN ["+bc.getIsbn()+"]?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DialogInterface.BUTTON_POSITIVE == i){
                    doDelettion(bc);
                }
            }
        });
    }


    private void doDelettion(BookCopy cp){
        String user_id = session.getUserID();
        String request = URLs.DELETE_BOOKCOPY;

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", getString(R.string.waiting), false, false);
        //Toast.makeText(this, request, Toast.LENGTH_LONG).show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            users = jsonObject.getJSONArray("result");

                            JSONObject jo = users.getJSONObject(0);
                            String success = jo.getString("success");

                            if(success.equals("1")){

                                alert.showAlertDialog( copiesList.this,
                                        "",
                                        getString(R.string.delete_success),
                                        true);
                                copyArrayList.remove(cp);
                                onLoadSucess();
                            }else{
                                // email / password doesn't match
                                alert.showAlertDialog( copiesList.this,
                                        getString(R.string.error),
                                        getString(R.string.couldnt_delete),
                                        false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog( copiesList.this,
                                    getString(R.string.error),
                                    getString(R.string.couldnt_delete),
                                    false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(register.this,error.toString(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        onLoadError();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("user_id" , user_id);
                params.put("book_copy_id" , cp.getID());

                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
