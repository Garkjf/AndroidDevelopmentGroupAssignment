package com.example.recipefinder;

public class RecipePreview {
    private String name, imgURL, id, desc;

    public RecipePreview(String id, String name, String imgURL, String desc) {
        this.name = name;
        this.imgURL = imgURL;
        this.id = id;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
