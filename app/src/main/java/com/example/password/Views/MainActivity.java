package com.example.password.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.password.Models.InterProcess;
import com.example.password.Models.LogModel;
import com.example.password.Models.PassModel;
import com.example.password.R;
import com.example.password.SQLConnection;
import com.example.password.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private ActivityMainBinding binding;

    private InterProcess.MyBinder appService = null;
    private LogModel model;

    private PassModel passModel;

    private SQLConnection sqlConnection;
    private Connection con;

    Intent intent;

    ResultSet rs;
    String name,str;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        model = new ViewModelProvider(this).get(LogModel.class);
        passModel = new ViewModelProvider(this).get(PassModel.class);

        model.initDatabase(this);

        intent = new Intent(MainActivity.this, InterProcess.class);
        startService(intent);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_log);




        //bottomNavigationView = findViewById(R.id.bottom_navigation);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Link NavController with BottomNavigationView
        //NavigationUI.setupWithNavController(bottomNavigationView, navController);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_log);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void connect() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (con == null) {
                    str = "Error";
                } else {
                    str = "Connected with SQL server";

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            runOnUiThread(() -> {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            });

        });
    }

/*
    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
        stopService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("comp3018", "MainActivity onServiceConnected");
            appService = (InterProcess.MyBinder) service;






        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("comp3018", "MainActivity onServiceDisconnected");

            appService = null;

        }
    };

 */
}