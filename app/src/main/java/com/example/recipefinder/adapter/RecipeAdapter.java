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

/**
 * Adapter for recipes list in MainActivity
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private static List<RecipePreview> recipes;
    private static OnItemClickListener onClickListener;

    public void updateRecipes(List<RecipePreview> recipes) {
        RecipeAdapter.recipes = recipes;
    }

    /**
     * Interface for click listener
     */
    public interface OnItemClickListener {
        /**
         * Called when an item is clicked
         * @param item Item that was clicked
         */
        void onClick(RecipePreview item);
    }

    /**
     * Constructor for RecipeAdapter
     * @param listItems List of recipes
     */
    public RecipeAdapter(List<RecipePreview> listItems) {
        recipes = listItems;
    }

    /**
     * Sets the click listener
     * @param onClickListener Click listener
     */
    public void setOnClickListener(OnItemClickListener onClickListener) {
        RecipeAdapter.onClickListener = onClickListener;
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
                .inflate(R.layout.recipe_preview, parent, false);
        return new ViewHolder(v);
    }

    /**
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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