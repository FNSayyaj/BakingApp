package com.aboelfer.knightrider.bakingapp.Models;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class Recipes {

    private String id;
    private String name;
    private String ingredients;
    private String steps;
    private String servings;
    private String image;

    public Recipes(String id, String name, String ingredients, String steps, String servings, String image) {

        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getName() {
        return name;
    }
    public void setName(String name){this.name = name;}

    public String getIngredients() {
        return ingredients;
    }
    public void setingredients(String ingredients){this.ingredients = ingredients;}

    public String getSteps() {
        return steps;
    }
    public void setSteps(String steps){this.steps = steps;}

    public String getServings() {
        return servings;
    }
    public void setServings(String servings){this.servings = servings;}

    public String getImage() {
        return image;
    }
    public void setImage(String image){this.image = image;}

}