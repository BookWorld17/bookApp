package com.example.bookapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class singupActivity extends Activity {

    private Button save;
    private EditText uname;
    private EditText mail;
    private EditText pass;
    private EditText address;
    private EditText country;
    ProgressDialog loading;

    AlertManager alert ;
    ImageButton backBtn;
    private Spinner userType;
    private Spinner sellerType;


    String email, password, nickName;
    private JSONArray users = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_activity);
        alert = new AlertManager(singupActivity.this);
        uname = (EditText) findViewById(R.id.uname);
        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.pass);
        address = (EditText) findViewById(R.id.address);
        country = (EditText) findViewById(R.id.country);
        backBtn = (ImageButton) findViewById(R.id.backBtn);

        userType = (Spinner) findViewById(R.id.userType);
        sellerType = (Spinner) findViewById(R.id.sellerType);
        save = (Button) findViewById(R.id.registerBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(singupActivity.this, loginActivity.class));
            }
        });


        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                if(selected.equals("Seller")){
                    sellerType.setVisibility(View.VISIBLE);
                    country.setVisibility(View.VISIBLE);
                }else{
                    sellerType.setVisibility(View.GONE);
                    country.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void singUp() {

        if (validation()) {
            loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.signup,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            doAction(response);
                            Toast.makeText(singupActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            alert.showAlertDialog(singupActivity.this,
                                    "",
                                    error.toString(),false);
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    String userName = uname.getText().toString().trim();
                    String email = mail.getText().toString().trim();
                    String pass_txt = pass.getText().toString().trim();
                    String country_txt = country.getText().toString().trim();
                    String address_txt = address.getText().toString().trim();

                    params.put("nickName" , userName);
                    params.put("email" ,email);
                    params.put("pass" , pass_txt );
                    params.put("country" , country_txt );
                    params.put("address" , address_txt );
                    String userType_ = userType.getSelectedItem().toString();
                    params.put("userType", userType_ );
                    String sellerType_ = sellerType.getSelectedItem().toString();
                    params.put("sellerType", sellerType_ );
                    return params;
                }

            };

            //Adding the request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            alert.showAlertDialog(this, "Register failed..", "Check your inputs", false);
        }
    }

    public void doAction(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            users = jsonObject.getJSONArray("result");

            JSONObject jo = users.getJSONObject(0);
            String success = jo.getString("success");

            if(success.equals("1")){
                alert.showAlertDialog(singupActivity.this,
                        "",
                        "Your account is created successfully",
                        true,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                    finish();
                            }
                });
            }else{
                alert.showAlertDialog(singupActivity.this, "", "Couldn't create your account", false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean validation(){
        boolean validData = true;
        nickName = uname.getText().toString().trim();
        email = mail.getText().toString().trim();
        password = pass.getText().toString().trim();
        password = pass.getText().toString().trim();

        //validations
        if (TextUtils.isEmpty(nickName)) {
            uname.setError("Nick name is required");
            uname.requestFocus();
            validData = false;
        }



        if (TextUtils.isEmpty(email)) {
            mail.setError("Email is required");
            mail.requestFocus();
            validData = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mail.setError("write a vlid email");
            mail.requestFocus();
            validData = false;
        }

        Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        ".{6,}" +                // at least 6 characters
                        "$");
        if (TextUtils.isEmpty(password)) {
            pass.setError("Password is required");
            pass.requestFocus();
            validData = false;
        }
        else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            pass.setError("Password is too weak");
            validData = false;
        } else {
            pass.setError(null);
        }

        return validData;
    }

}
