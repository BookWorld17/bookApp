package com.example.bookapp.buyer;

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


public class chat_buyer extends BaseActivity {

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

        String buyer_id = session.getUserID();

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String seller_id = "0";
        String url = "";
        if(b!=null ) {
            if( b.containsKey("seller_id")) {
                seller_id = b.getString("seller_id");
                url = URLs.CHAT +"?userType=st&buyer_id="+buyer_id+"&seller_id="+seller_id+with_admin;
            }
            if(b.containsKey("admin")){
                seller_id = "1";
                with_admin = "&admin=true";
                url = URLs.ADMINCHAT +"?userType=buyer&buyer_id="+buyer_id;
            }
        }
        webView.loadUrl(url);

    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), BuyerMainActivity.class);
        startActivity(i);
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
