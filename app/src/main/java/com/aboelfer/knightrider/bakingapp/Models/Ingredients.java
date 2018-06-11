package com.aboelfer.knightrider.bakingapp.Models;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class Ingredients {

    private String quantity;
    private String measure;
    private String ingredient;


    public Ingredients(String quantity, String measure, String ingredient) {

        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;

    }

    public String getQuantity(){return quantity;}
    public void setQuantity(String quantity){this.quantity = quantity;}

    public String getMeasure() {
        return measure;
    }
    public void setMeasure(String measure){this.measure = measure;}

    public String getIngredient() {
        return ingredient;
    }
    public void setingredient(String ingredient){this.ingredient = ingredient;}

}
