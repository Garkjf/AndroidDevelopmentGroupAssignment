package com.example.recipefinder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private final List<String> ingredients, measures;

    public IngredientAdapter(List<String> ingredients, List<String> measures) {
        this.ingredients = ingredients;
        this.measures = measures;
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
        String ingredient = ingredients.get(position), measure = measures.get(position);
        holder.ingrName.setText(String.format("%s %s", measure, ingredient));

        String imgURL = String.format("https://www.themealdb.com/images/ingredients/%s-Small.png",
                                        ingredient.replace(" ", "%20"));
        Picasso.get().load(imgURL).resize(200, 200).centerCrop().into(holder.ingrImg);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

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
