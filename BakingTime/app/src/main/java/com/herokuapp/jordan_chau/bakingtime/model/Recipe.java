package com.herokuapp.jordan_chau.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable{
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

    //Parcelable implemented methods & constructors
    protected Recipe(Parcel in) {
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(servings);
        dest.writeString(name);
        dest.writeString(image);
    }
    //End of Parcelable implemented methods & constructors

    //getters
    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }


    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> s){
        steps = s;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> i){
        ingredients = i;
    }

    public String getImage(){
        return image;
    }
}
