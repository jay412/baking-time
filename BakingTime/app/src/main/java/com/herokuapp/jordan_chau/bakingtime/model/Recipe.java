package com.herokuapp.jordan_chau.bakingtime.model;

import java.util.ArrayList;

public class Recipe {
    private int id, servings;
    private String name, image;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }
}
