package com.example.menuappsubmit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo {
    private String userName;
    private String password;
    private ArrayList<Recipe> recipes;

    public UserInfo(String name, String pass, ArrayList<Recipe> recipes){
        this.userName = name;
        this.password = pass;
        if (recipes == null)
            this.recipes = new ArrayList<Recipe>();
        else
            this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public JSONObject toJSON(){
        try{
            JSONObject json = new JSONObject();
            json.put("userName", this.userName);
            json.put("password", this.password);
            json.put("recipes", this.recipes);
            return json;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
