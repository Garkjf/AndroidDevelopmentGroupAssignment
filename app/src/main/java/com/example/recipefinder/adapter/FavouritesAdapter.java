package com.example.recipefinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.db.RecipeDAO;
import com.example.recipefinder.model.RecipePreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private static List<RecipePreview> recipes;
    private static FavouritesAdapter.OnItemClickListener onClickListener;
    private static RecipeDAO recipeDAO;

    public void updateRecipes(List<RecipePreview> recipes) {
        FavouritesAdapter.recipes = recipes;
    }

    public interface OnItemClickListener {
        // Called when an item is clicked
        void onClick(RecipePreview item);
    }

    // Constructor
    public FavouritesAdapter(List<RecipePreview> listItems, RecipeDAO recipeDAO) {
        recipes = listItems;
        FavouritesAdapter.recipeDAO = recipeDAO;
    }

    public void setOnClickListener(FavouritesAdapter.OnItemClickListener onClickListener) {
        FavouritesAdapter.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_preview, parent, false);
        return new FavouritesAdapter.ViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.ViewHolder holder, int position) {
        RecipePreview recipe = recipes.get(position);

        holder.bindRecipe(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // ViewHolder for FavouritesAdapter
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView recipeImg;
        private final TextView recipeTitle;
        public final ImageButton favButton;
        private final Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            this.context = context;
            recipeImg = itemView.findViewById(R.id.ingr_img);
            recipeTitle = itemView.findViewById(R.id.ingr_name);
            favButton = itemView.findViewById(R.id.favourite_button);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(recipes.get(getAdapterPosition()));
                }
            });
        }

        public void bindRecipe(RecipePreview recipe) {
            recipeTitle.setText(recipe.getName());

            Picasso.get().load(recipe.getImgURL() + "/preview")
                    .resize(300, 300).centerCrop()
                    .placeholder(R.drawable.ic_recipe).into(recipeImg);

            favButton.setBackgroundResource(R.drawable.baseline_bookmark_24);
            favButton.setOnClickListener(v -> {
                recipeDAO.deleteRecipe(recipe.getApiId());
                Toast.makeText(context, "Recipe removed from favourites", Toast.LENGTH_SHORT).show();
                recipes.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}