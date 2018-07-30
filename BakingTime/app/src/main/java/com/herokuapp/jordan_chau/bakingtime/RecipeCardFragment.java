package com.herokuapp.jordan_chau.bakingtime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeCardFragment extends Fragment {

    public RecipeCardFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_card, container, false);

        TextView recipeName = rootView.findViewById(R.id.tv_recipe_name);
        TextView servings = rootView.findViewById(R.id.tv_servings);

        //set textview values

        return rootView;
    }
}
