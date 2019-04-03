package com.onilearnapp.onilearnapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.onilearnapp.onilearnapp.Adapter.CategoryAdapter;
import com.onilearnapp.onilearnapp.Adapter.MainPagerAdapter;
import com.onilearnapp.onilearnapp.Model.Category;
import com.onilearnapp.onilearnapp.Model.Token;
import com.onilearnapp.onilearnapp.R;
import com.onilearnapp.onilearnapp.Repository.TokenRepository;
import com.onilearnapp.onilearnapp.Utils.SyncData;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_TASK = 001;
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPage);
        viewPager.setAdapter(mainPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Token.tmpToken != null)
            menu.findItem(R.id.action_login).setTitle("Logout");
        else
            menu.findItem(R.id.action_login).setTitle("Login");
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        switch (id) {
            case R.id.action_clock:
                Intent intent = new Intent(this, SettingTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TASK);
                return true;
            case R.id.action_sync:
                if (isConnected)
                    SyncData.syncSubject(this, getApplication());
                else
                    Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_login:
                if (isConnected) {
                    if (Token.tmpToken != null) {
                        logout();
                    } else {
                        Intent loginIntent = new Intent(this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                } else
                Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPage + ":" + 1);
        page.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void logout(){
        Thread myThread = new Thread() {
            @Override
            public void run() {
                super.run();
                TokenRepository tokenRepository = TokenRepository.getInstance(getApplication());
                tokenRepository.delete(new Token(Token.DEFAULT_ID, Token.tmpToken));
                Token.tmpToken = null;
            }
        };

        myThread.start();
        Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show();
    }

}
