package com.example.recipefinder.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.model.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for ingredients list in RecipeActivity
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private final List<Ingredient> ingredients;

    // Constructor
    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingrName.setText(ingredient.toString());

        String imgURL = String.format("https://www.themealdb.com/images/ingredients/%s-Small.png",
                                        ingredient.getName().replace(" ", "%20"));
        Picasso.get().load(imgURL).resize(80, 80)
                .centerCrop().placeholder(R.drawable.ic_ingredient).into(holder.ingrImg);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // Sets the ingredients
    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }

    // ViewHolder for IngredientAdapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ingrImg;
        public TextView ingrName;

        public ViewHolder(View itemView) {
            super(itemView);

            ingrImg = itemView.findViewById(R.id.ingr_img);
            ingrName = itemView.findViewById(R.id.ingr_name);
        }
    }
}
