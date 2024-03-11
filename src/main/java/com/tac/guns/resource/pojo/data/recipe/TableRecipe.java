package com.tac.guns.resource.pojo.data.recipe;

import com.google.gson.annotations.SerializedName;
import com.tac.guns.crafting.GunSmithTableIngredient;
import com.tac.guns.crafting.GunSmithTableResult;

import java.util.List;

public class TableRecipe {
    @SerializedName("materials")
    private List<GunSmithTableIngredient> materials;

    @SerializedName("result")
    private GunSmithTableResult result;

    public List<GunSmithTableIngredient> getMaterials() {
        return materials;
    }

    public GunSmithTableResult getResult() {
        return result;
    }
}
