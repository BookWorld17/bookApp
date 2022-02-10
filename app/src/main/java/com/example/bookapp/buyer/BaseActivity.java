package com.example.bookapp.buyer;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bookapp.AlertManager;
import com.example.bookapp.R;
import com.example.bookapp.SessionManager;
import com.example.bookapp.seller.SellerMainActivity;
import com.example.bookapp.seller.addBookActivity;
import com.example.bookapp.seller.booksList;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public Toolbar toolbar;
    public SessionManager session;
    private JSONObject jsonObject;
    private AlertManager alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        setContentView(R.layout.buyer_activity_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        alert = new AlertManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();

                switch (item.getItemId()) {

                    case R.id.buyer_home:
                        Intent i = new Intent(getApplicationContext(), BuyerMainActivity.class);
                        startActivity( i);
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.my_cart:
                        startActivity( new Intent(getApplicationContext(), cart.class));
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.my_orders:
                        startActivity( new Intent(getApplicationContext(), ordersList.class));
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.my_disputes:
                        startActivity( new Intent(getApplicationContext(), disputeList.class));
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.chat_with_admin:
                        Intent intent_ = new Intent(getApplicationContext(), chat_buyer.class);
                        intent_.putExtra("admin" , "admin");
                        startActivity( intent_);
                        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                        drawerLayout.closeDrawers();
                        break;




                    case R.id.logout:
                        session.logoutUser();
                        drawerLayout.closeDrawers();
                        finish();
                        break;

                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}