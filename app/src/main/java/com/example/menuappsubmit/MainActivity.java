package com.example.menuappsubmit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("username") && intent.hasExtra("password")){
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.REMEMBER_USER, MODE_PRIVATE);
        if (sharedPreferences.contains(Constants.USERNAME_TAG) && sharedPreferences.contains(Constants.PASSWORD_TAG)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            username = sharedPreferences.getString(Constants.USERNAME_TAG, "");
            password = sharedPreferences.getString(Constants.PASSWORD_TAG, "");
            editor.apply();
        }
        Toast.makeText(this, "Username= " + username + " Password= " + password, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            Intent intent = new Intent(this, LogIn.class);
            intent.putExtra(Constants.SHARED_PREF_NAME, "mypref");
            startActivity(intent);
            finish();
        }
        else if (item.getItemId() == R.id.create_recipe) {
            /*Intent intent = new Intent(this, Settings.class);
            startActivity(intent);*/
        }
        else if (item.getItemId() == R.id.favorite) {
   /*         Intent intent = new Intent(this, Settings.class);
            startActivity(intent);*/
        }

        return super.onOptionsItemSelected(item);
    }
}