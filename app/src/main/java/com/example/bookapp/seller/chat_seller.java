package com.example.bookapp.seller;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.bookapp.AlertManager;
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.URLs;
import com.google.android.material.navigation.NavigationView;


public class chat_seller extends BaseActivity {

    private SessionManager session;
    private AlertManager alert;
    private String with_admin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.web_interface, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        alert = new AlertManager(this);
        session = new SessionManager(getApplicationContext());

        WebView webView = (WebView) findViewById(R.id.webbiew);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        String seller_id = session.getUserID();

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String buyer_id = "0";
        if(b!=null && b.containsKey("buyer_id")) {
            buyer_id = b.getString("buyer_id");

        }
        if(b!=null && b.containsKey("")) {
            buyer_id = b.getString("buyer_id");

        }
        String url = "";
        if(b!=null ) {
            if( b.containsKey("buyer_id")) {
                buyer_id = b.getString("buyer_id");
                url = URLs.CHAT +"?userType=te&buyer_id="+buyer_id+"&seller_id="+seller_id;
            }
            if(b.containsKey("admin")){
                buyer_id = session.getUserID();
                url = URLs.ADMINCHAT +"?userType=seller&seller_id="+seller_id ;
            }


        }
        webView.loadUrl(url);

    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), SellerMainActivity.class);
        startActivity(i);
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
