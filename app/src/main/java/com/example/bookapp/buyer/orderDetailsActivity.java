package com.example.bookapp.buyer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.bookapp.R;
import com.example.bookapp.URLs;
import com.example.bookapp.adapters.cartAdapter;
import com.example.bookapp.models.Book;
import com.example.bookapp.models.BookCopy;
import com.example.bookapp.models.Cart;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class orderDetailsActivity extends BaseActivity {

    ArrayList<Cart> cartArrayList = new ArrayList<>();
    public ProgressDialog progressDialog = null;
    RecyclerView listview;
    private AlertManager alert ;
    private cartAdapter adapter = null;
    private TextView interface_title;
    private JSONObject jsonObject;
    private Button confirmOrderBtn;
    private String orderTotal = "0";
    private String order_id = "0";
    private TextView totalTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.order_details_list_activity, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        alert = new AlertManager(orderDetailsActivity.this);

        confirmOrderBtn = (Button) findViewById(R.id.confirmOrder);

        interface_title = (TextView) findViewById(R.id.interface_title);
        totalTxtView = (TextView) findViewById(R.id.totalTxtView);

        interface_title.setText("Order Details");
        listview = findViewById(R.id.list);

        listview.setHasFixedSize(true);
        listview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1  ));


        adapter = new cartAdapter( cartArrayList , orderDetailsActivity.this);
        listview.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey("order_id")) {
                order_id = extras.getString("order_id");
            }
        }
        loadOrderDetails();

    }

    private void loadOrderDetails() {

        String user_id = session.getUserID();
        String request = URLs.GET_ORDER_DETAILS;

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
                params.put("order_id" , order_id);
                params.put("user_id" , user_id);
                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void onLoadError() {
        alert.showAlertDialog(orderDetailsActivity.this,"Error","Cart is empty",false);
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
                    orderTotal  = jo.getString("total");
                    BookCopy copy = new BookCopy("0", book_id,  isbn, price, numberOfCopies, numberOfPages, copyAddDate );

                    ArrayList<BookCopy> copiesList = new ArrayList<>();
                    copiesList.add(copy);
                    book.setCopyArrayList(copiesList);
/*
                    String nickName = jo.getString("nickName");
                    String email = jo.getString("email");
                    String address = jo.getString("address");
                    User seller = new User("0",  nickName,  email, address);
*/
                    Cart o = new Cart(id,
                            book,
                            Integer.parseInt(qty));

                    cartArrayList.add(o);

                    totalTxtView.setText(orderTotal);

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



    public void removeItem(Cart cart) {
        alert.showMessageOKCancel("Are you sure to remove this item from cart?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(DialogInterface.BUTTON_POSITIVE == i){
                    doDelettion(cart);
                }
            }
        });
    }


    private void doDelettion(Cart cart){
        String user_id = session.getUserID();
        String request = URLs.CART_REMOVE_ITEM;

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

                                alert.showAlertDialog( orderDetailsActivity.this,
                                        "",
                                        getString(R.string.item_removed_success),
                                        true);
                                cartArrayList.remove(cart);
                                onLoadSucess();
                            }else{
                                // email / password doesn't match
                                alert.showAlertDialog( orderDetailsActivity.this,
                                        getString(R.string.error),
                                        getString(R.string.couldnt_remove_item),
                                        false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog( orderDetailsActivity.this,
                                    getString(R.string.error),
                                    getString(R.string.couldnt_remove_item),
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
                params.put("cart_id" , cart.getId());

                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void confirmOrder(Dialog dialog, String dOption,  String addressO) {

        String user_id = session.getUserID();
        String request = URLs.CONFIRM_ORDER;
        dialog.dismiss();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", getString(R.string.waiting), false, false);
        Toast.makeText(this, request, Toast.LENGTH_LONG).show();

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

                                alert.showAlertDialog(orderDetailsActivity.this,
                                        getString(R.string.success),
                                        getString(R.string.order_submitted_success),
                                        true,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(orderDetailsActivity.this, ordersList.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                onLoadSucess();
                            }else{
                                // email / password doesn't match
                                alert.showAlertDialog( orderDetailsActivity.this,
                                        getString(R.string.error),
                                        getString(R.string.couldnt_submit_order),
                                        false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog( orderDetailsActivity.this,
                                    getString(R.string.error),
                                    getString(R.string.couldnt_submit_order),
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
                params.put("deliveryOption" , dOption);
                params.put("deliveryAddress" , addressO);
                return params;
            }

        };

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
