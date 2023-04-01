package com.example.a3andmproject.database

import com.example.a3andmproject.models.Recipe

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun insertRecipe(recipes: List<Recipe>) {
        recipeDao.insertRecipes(recipes)
    }

    fun getAllRecipes(): List<Recipe> {
        return recipeDao.getRecipes()
    }
}