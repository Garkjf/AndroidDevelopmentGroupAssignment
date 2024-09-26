package com.example.recipefinder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.model.RecipePreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final List<RecipePreview> recipes;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(RecipePreview item);
    }

    public RecipeAdapter(List<RecipePreview> listItems, OnItemClickListener listener) {
        this.recipes = listItems;
        this.listener = listener;
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
        holder.recipeDesc.setText(listItem.getDesc());

        Picasso.get().load(listItem.getImgURL()).resize(300,300)
                .centerCrop().into(holder.recipeImg);

        holder.btnViewRecipe.setOnClickListener(v -> listener.onViewClick(listItem));
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipeImg;
        public TextView recipeTitle;
        public TextView recipeDesc;
        public Button btnViewRecipe;

        public ViewHolder(View itemView) {
            super(itemView);

            recipeImg = itemView.findViewById(R.id.ingr_img);
            recipeTitle = itemView.findViewById(R.id.ingr_name);
            recipeDesc = itemView.findViewById(R.id.recipeDesc);
            btnViewRecipe = itemView.findViewById(R.id.btn_viewRecipe);
        }
    }
}