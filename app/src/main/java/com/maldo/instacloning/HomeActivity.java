package com.maldo.instacloning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ListView lvUser;
    List<String> listUser;
    ArrayAdapter adapter;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvUser = (ListView) findViewById(R.id.lvUser);
        listUser = new ArrayList<>();
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listUser);

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(getApplicationContext(), UserFeedActivity.class);
                intent.putExtra("username", listUser.get(i));
                startActivity(intent);
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e==null){
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            listUser.add(user.getUsername());
                        }
                        lvUser.setAdapter(adapter);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                Toast.makeText(HomeActivity.this, "Ad Closed", Toast.LENGTH_SHORT).show();

                //AdRequest adRequest = new AdRequest().Builder().build();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                Toast.makeText(HomeActivity.this, "Failed - " + Integer.toString(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();

                Toast.makeText(HomeActivity.this, "Ad Left Application", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.menu_upload){
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.menu_logout){
            ParseUser.logOut();

            super.onBackPressed();
            Toast.makeText(HomeActivity.this, "Bye Bye Bro " , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
