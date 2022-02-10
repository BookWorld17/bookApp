package com.example.bookapp.buyer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.orderAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.example.bookapp.models.Order;
import com.example.bookapp.models.User;
import com.example.bookapp.recycleview.ClickListener;
import com.example.bookapp.recycleview.RecyclerTouchListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ordersList extends BaseActivity {

    ArrayList<Order> orderArrayList = new ArrayList<>();
    public ProgressDialog progressDialog = null;
    RecyclerView listview;
    private AlertManager alert ;
    private orderAdapter adapter = null;
    private TextView interface_title;
    private JSONObject jsonObject;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.orders_list_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        alert = new AlertManager(ordersList.this);


        interface_title = (TextView) findViewById(R.id.interface_title);
        interface_title.setText("List of My Orders");
        listview = findViewById(R.id.listview);

        listview.setHasFixedSize(true);
        listview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1  ));


        adapter = new orderAdapter( orderArrayList , ordersList.this);
        listview.setAdapter(adapter);



        /*Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras != null)
        {

            if(extras.containsKey("type")){
                interface_title.setText("List of recieved orders");
                loadProperties("recieved");
                type = "recieved";
            }

        }else {
            loadProperties("my_orders");
            type = "my_orders";
        }*/
        loadOrders("");
    }

    private void loadOrders(String type) {

        String user_id = session.getUserID();
        String request = URLs.GET_ORDERS;

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
                        onLoadError();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id" , user_id);
                params.put("type" , "buyer");
                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onLoadError() {
        alert.showAlertDialog(ordersList.this,"Error","No data Available",false);
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


                    String id = jo.getString("oid");
                    String buyer_id = jo.getString("buyer_id");
                    String orderDate = jo.getString("orderDate");
                    String orderStatus = jo.getString("orderStatus");
                    String deliveryDate = jo.getString("deliveryDate");
                    String deliveryOption = jo.getString("deliveryOption");
                    String deliveryAddress = jo.getString("deliveryAddress");

/*
                    String book_copy_id = jo.getString("book_copy_id");
                    String qty = jo.getString("qty");
                    String imgRUL= jo.getString("image");
                    char slash[] = new char[1];
                    slash[0] = (char)92;
                    imgRUL = URLs.SERVER + imgRUL.replace( new String(slash), "" );
                    String bookName = jo.getString("bookName");
                    String authorName = jo.getString("authorName");
                    String publisherName = jo.getString("publisherName");
                    String summary = jo.getString("summary");
                    String seller_id = jo.getString("seller_id");
                    String addDate = jo.getString("addDate");
                    String bookType = jo.getString("bookType");
                    String category_id = jo.getString("category_id");

                    Book book = new Book("0", bookName,  authorName, publisherName, summary, seller_id, addDate, bookType,imgRUL, category_id);


                    String book_id = jo.getString("book_id");
                    String isbn = jo.getString("isbn");
                    String price = jo.getString("price");
                    String numberOfCopies = jo.getString("numberOfCopies");
                    String numberOfPages = jo.getString("numberOfPages");
                    String copyAddDate = jo.getString("addDate");
                    BookCopy copy = new BookCopy("0", book_id,  isbn, price, numberOfCopies, numberOfPages, copyAddDate );

                    ArrayList<BookCopy> copiesList = new ArrayList<>();
                    copiesList.add(copy);
                    book.setCopyArrayList(copiesList);
*/
                    String nickName = jo.getString("nickName");
                    String email = jo.getString("email");
                    String address = jo.getString("address");
                    String seller_id = jo.getString("seller_id");
                    User seller = new User(seller_id,  nickName,  email, address);

                    Order o = new Order(id,
                            buyer_id,
                            orderDate,
                            orderStatus,
                            orderDate,
                            deliveryDate,
                            deliveryOption,
                            deliveryAddress,
                            seller);

                    orderArrayList.add(o);

                    String dispute = jo.getString("dispute");
                    o.setDisputeOpend(dispute);

                    onLoadSucess();
                }
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



}
