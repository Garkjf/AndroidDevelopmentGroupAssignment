package com.example.recipefinder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.model.RecipePreview;
import com.example.recipefinder.utils.BookmarkManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private static List<RecipePreview> recipes;
    private static OnItemClickListener onClickListener;

    public void updateRecipes(List<RecipePreview> recipes) {
        RecipeAdapter.recipes = recipes;
    }


    public interface OnItemClickListener {
        // Called when an item is clicked
        void onClick(RecipePreview item);
    }

    // Constructor
    public RecipeAdapter(List<RecipePreview> listItems) {
        recipes = listItems;
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        RecipeAdapter.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipePreview listItem = recipes.get(position);
        holder.recipeTitle.setText(listItem.getName());

        Picasso.get().load(listItem.getImgURL()+"/preview").resize(300, 300)
                .centerCrop().placeholder(R.drawable.ic_recipe).into(holder.recipeImg);

        ImageButton favButton = holder.favButton;
        favButton.setBackgroundResource(R.drawable.baseline_bookmark_border_24);

        BookmarkManager bookmarkManager = new BookmarkManager(holder.itemView.getContext());
        bookmarkManager.initializeFavoriteButton(favButton, listItem);
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    // ViewHolder for RecipeAdapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipeImg;
        public TextView recipeTitle;
        public ImageButton favButton;

        public ViewHolder(View itemView) {
            super(itemView);

            recipeImg = itemView.findViewById(R.id.ingr_img);
            recipeTitle = itemView.findViewById(R.id.ingr_name);
            favButton = itemView.findViewById(R.id.favourite_button);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(recipes.get(getAdapterPosition()));
                }
            });
        }
    }
}