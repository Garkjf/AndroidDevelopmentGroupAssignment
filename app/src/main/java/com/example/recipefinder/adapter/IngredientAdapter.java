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

    /**
     * Constructor for IngredientAdapter
     * @param ingredients List of ingredients
     */
    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingrName.setText(ingredient.toString());

        String imgURL = String.format("https://www.themealdb.com/images/ingredients/%s-Small.png",
                                        ingredient.getName().replace(" ", "%20"));
        Picasso.get().load(imgURL).resize(80, 80)
                .centerCrop().placeholder(R.drawable.ic_ingredient).into(holder.ingrImg);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for IngredientAdapter
     */
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
