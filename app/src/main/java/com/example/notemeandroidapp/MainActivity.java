package com.example.notemeandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.notemeandroidapp.fragment.DoneFragment;
import com.example.notemeandroidapp.fragment.OpenFragment;
import com.example.notemeandroidapp.fragment.ProgressFragment;
import com.example.notemeandroidapp.fragment.TestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.floatingActionButton4);

        //set open fragment as default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new OpenFragment());
        transaction.commit();

        //setup bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //replace transaction
                switch (item.getItemId()) {
                    case R.id.menu_open:
                        transaction.replace(R.id.container, new OpenFragment());
                        break;
                    case R.id.menu_in_progress:
                        transaction.replace(R.id.container, new ProgressFragment());
                        break;
                    case R.id.menu_test:
                        transaction.replace(R.id.container, new TestFragment());
                        break;
                    case R.id.menu_done:
                        transaction.replace(R.id.container, new DoneFragment());
                        break;
                }
                transaction.commit();
                return true;
            }
        });

        //floating action handle
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(MainActivity.this, AddTask.class), 100);
                startActivity(new Intent(MainActivity.this,AddTask.class));
                finish();
            }
        });
    }

}