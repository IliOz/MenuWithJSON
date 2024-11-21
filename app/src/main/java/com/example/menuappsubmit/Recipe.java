package com.example.menuappsubmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Recipe implements Serializable {
    private String name;
    private String instructions;
    private String dishType;
    private boolean isFavorite;
    private boolean isVegan;

    public Recipe(String name, String instructions, boolean isFavorite, boolean isVegan, String dishType) {
        this.name = name;
        this.instructions = instructions;
        this.isFavorite = isFavorite;
        this.isVegan = isVegan;
        this.dishType = dishType;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
