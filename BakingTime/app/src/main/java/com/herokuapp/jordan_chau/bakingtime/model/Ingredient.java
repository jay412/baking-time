package com.herokuapp.jordan_chau.bakingtime.model;

public class Ingredient {
    private int quantity;
    private String measure, ingredient;

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
