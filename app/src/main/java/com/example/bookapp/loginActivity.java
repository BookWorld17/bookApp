package com.example.bookapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookapp.buyer.BuyerMainActivity;
import com.example.bookapp.seller.SellerMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends Activity {

    SessionManager session;
    EditText email, txtPassword;
    private Button btnLogin;
    AlertManager alert;
    ProgressDialog loading;
    private JSONArray users = null;
    private String userID = "0";
    private String userName = "";
    private Spinner userType;
    private String sellerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        alert = new AlertManager(this);
        session = new SessionManager(getApplicationContext());


        email = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        userType = (Spinner) findViewById(R.id.userType);


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String emailTxt = email.getText().toString();
                String password = txtPassword.getText().toString();
                if(emailTxt.trim().length() > 0 && password.trim().length() > 0){
                    login();
                }else{
                    alert.showAlertDialog(loginActivity.this,
                            "",
                            "Wrong Inputs",
                            false);
                }

            }
        });

        TextView textNewSeller_lbl = (TextView) findViewById(R.id.registerLbl);
        textNewSeller_lbl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), singupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void login() {
        loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            System.out.println(response);
                            jsonObject = new JSONObject(response);
                            users = jsonObject.getJSONArray("result");

                            JSONObject jo = users.getJSONObject(0);
                            String success = jo.getString("success");

                            boolean isActive = false;
                            if(success.equals("1")){
                                for(int i=1;i<users.length();i++){
                                    jo = users.getJSONObject(i);
                                    if(jo.getString("active").equals("1")) {
                                        userID = jo.getString("id");
                                        userName = jo.getString("name");
                                        sellerType = jo.getString("sellerType");
                                        isActive = true;
                                    }
                                }
                                if(isActive ) {
                                    String type = userType.getSelectedItem().toString();
                                    session.startSession(userName,
                                            email.getText().toString(),
                                            userID,
                                            type,
                                            sellerType);
                                    if (type.equals("Seller")) {

                                        Intent intent = new Intent(getApplicationContext(), com.example.bookapp.seller.booksList.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), BuyerMainActivity.class);
                                        startActivity(intent);
                                    }
                                }else{
                                    alert.showAlertDialog( loginActivity.this,
                                            "",getString(R.string.account_disabled),false);
                                }
                            }else{
                                alert.showAlertDialog( loginActivity.this,
                                        "","Invalid User",false);
                            }
                        } catch (JSONException e) {
                            alert.showAlertDialog( loginActivity.this,
                                    "",
                                    "Netowrk Error",
                                    false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(loginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                String em = email.getText().toString();
                String pass = txtPassword.getText().toString();
                params.put("email", em);
                params.put("pass", pass);
                String type = userType.getSelectedItem().toString();
                params.put("userType", type );

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
