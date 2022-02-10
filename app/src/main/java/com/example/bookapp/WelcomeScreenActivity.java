package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.buyer.BuyerMainActivity;
import com.example.bookapp.models.Category;
import com.example.bookapp.seller.SellerMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIMER = 2000;
    private SessionManager session;
    private JSONArray result;
    private ArrayList<Category> categoriesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcomepage);
        session= new SessionManager(WelcomeScreenActivity.this);
        loadCategories();
    }



    private void loadCategories(){

        //loading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GET_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //loading.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");

                            JSONObject jo = result.getJSONObject(0);
                            String success = jo.getString("success");

                            String isActive = jo.getString("isActive");
                            if(success.equals("1")){
                                JSONArray data = result.getJSONArray(1);
                                for(int i=0;i<data.length();i++){
                                    jo = data.getJSONObject(i);
                                    String id = jo.getString("id");
                                    String name = jo.getString("name");
                                    categoriesList.add(new Category(id, name));
                                }
                            }
                            URLs.categoriesList = categoriesList;
                            if(session.getUserID() != null) {
                                run(isActive.equals("1"));
                            }else{
                                run(true);
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loading.dismiss();
                        Toast.makeText(WelcomeScreenActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if(session.getUserID() != null) {
                    params.put("user_id", session.getUserID());
                    params.put("user_type", session.getUserType());
                }
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void run(boolean isActive) {
        if(isActive) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (session.isLoggedIn()) {
                        if (session.getUserType().equals("Seller")) {
                            Intent intent = new Intent(WelcomeScreenActivity.this, com.example.bookapp.seller.booksList.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(WelcomeScreenActivity.this, BuyerMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent i = new Intent(WelcomeScreenActivity.this, loginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIMER);
        }else{
            Toast.makeText(WelcomeScreenActivity.this,getString(R.string.account_disabled),Toast.LENGTH_LONG).show();
            session.logoutUser();
        }
    }
}