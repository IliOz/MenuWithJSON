package com.example.menuappsubmit;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import kotlin.jvm.Throws;

public class Sign_up extends AppCompatActivity {

    private ArrayList<UserInfo> users;
    private EditText username, password;
    private Button signup, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsername();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initializeViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.register);
        goBack = findViewById(R.id.go_back);
    }

    // Show alert dialog, if the user sure he want to add this username and password
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to add this username and password?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Sign_up.this, MainActivity.class);
                intent.putExtra(Constants.USERNAME_TAG, username.getText().toString());
                intent.putExtra(Constants.PASSWORD_TAG, username.getText().toString());

                //


            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(Sign_up.this, "Didn't registered", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username.setText("");
                password.setText("");
                dialog.dismiss();
                Toast.makeText(Sign_up.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Add username to JSON
    public void addUsername() {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(Sign_up.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            users = getAllUsers();
            if (users == null)
                return;

            // Check if the username already exists
            for (UserInfo user : users) {
                if (user.getUserName().equals(username.getText().toString())) {
                    Toast.makeText(Sign_up.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // If the username doesn't exist, add it to the JSON file
            UserInfo user = new UserInfo(username.getText().toString(), password.getText().toString(), null);
            addUser(user);
        }
    }

    // Add new user to JSON
    public void addUser(UserInfo user) {
        FileInputStream inputStream = null;
        try {
            // Try to open the file that contains user data
            try {
                inputStream = openFileInput(Constants.USERS_PATH);
            } catch (FileNotFoundException e) {
                // If the file doesn't exist, create a new file
                JSONObject jsonObject = new JSONObject();
                JSONArray usersArray = new JSONArray();
                jsonObject.put(Constants.USER_TAG, usersArray);

                // Create the file and write the initial empty JSON data
                FileOutputStream outputStream = openFileOutput(Constants.USERS_PATH, MODE_PRIVATE);
                outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.close();
                return; // Exit early since we just created the file
            }

            // If the file exists, read its contents
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);

            // Convert the file content into a string
            String text = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(text);
            JSONArray usersArray = jsonObject.getJSONArray(Constants.USER_TAG);

            // Close the input stream after reading
            inputStream.close();

            // Add the new user to the users array
            usersArray.put(user.toJSON());

            // Save the updated JSON object back to the file
            FileOutputStream outputStream = openFileOutput(Constants.USERS_PATH, MODE_PRIVATE);
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();

        } catch (JSONException | IOException e) {
            // Handle errors appropriately
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ArrayList<UserInfo> getAllUsers() {
        FileInputStream inputStream = null;
        try {
            // Try opening the file stream
            try {
                inputStream = openFileInput(Constants.USERS_PATH);
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, so create it with default data
                JSONObject jsonObject = new JSONObject();
                JSONArray usersArray = new JSONArray();
                jsonObject.put(Constants.USER_TAG, usersArray);

                FileOutputStream outputStream = openFileOutput(Constants.USERS_PATH, MODE_PRIVATE);
                outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.close(); // Close the output stream after writing
                return new ArrayList<>(); // Return an empty list since the file was just created
            }

            // If the file exists, read the contents
            ArrayList<UserInfo> users = new ArrayList<>();

            // Read the file contents
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);

            // Convert the file content into a string (JSON format)
            String text = new String(buffer, StandardCharsets.UTF_8);

            // Create a JSONObject from the file content (string)
            JSONObject jsonObject = new JSONObject(text);

            JSONArray usersArray = jsonObject.getJSONArray(Constants.USER_TAG);

            // Iterate over the array of users
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);

                // Create an ArrayList to hold the recipes of the current user
                ArrayList<Recipe> recipes = new ArrayList<>();

                // Check if the recipes key exists in the user JSON object
                if (userJson.has(Constants.RECIPE_TAG)) {
                    JSONArray jsonArray = userJson.getJSONArray(Constants.RECIPE_TAG);

                    // Loop through the recipes of the current user
                    for (int index = 0; index < jsonArray.length(); index++) {
                        // Get the current recipe in JSONObject format
                        JSONObject recipeJson = jsonArray.getJSONObject(index);

                        // Create a Recipe object for each recipe in the user's recipe list
                        Recipe recipe = new Recipe(
                                recipeJson.getString(Constants.RECIPE_NAME_TAG),
                                recipeJson.getString(Constants.RECIPE_INSTRUCTIONS_TAG),
                                recipeJson.getBoolean(Constants.RECIPE_IS_FAVORITE_TAG),
                                recipeJson.getBoolean(Constants.RECIPE_IS_VEGAN_TAG),
                                recipeJson.getString(Constants.RECIPE_DISH_TYPE_TAG)
                        );

                        // Add the Recipe to the recipes list
                        recipes.add(recipe);
                    }
                }

                // Create a UserInfo object with the username, password, and recipes list
                UserInfo user = new UserInfo(
                        userJson.getString(Constants.USERNAME_TAG),
                        userJson.getString(Constants.PASSWORD_TAG),
                        recipes
                );

                // Add the UserInfo object to the users list
                users.add(user);
            }

            // Close the input stream after finishing reading
            inputStream.close();

            // Return the list of all users
            return users;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // Ensure the input stream is always closed in case of an exception
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}