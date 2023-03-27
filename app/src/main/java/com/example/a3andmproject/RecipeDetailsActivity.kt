package com.example.a3andmproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.a3andmproject.models.Recipe

class RecipeDetailsActivity : AppCompatActivity() {
    private val titleTextView: TextView by lazy { findViewById<TextView>(R.id.titleTextView) }
    private val ingredientsListTextView: TextView by lazy { findViewById<TextView>(R.id.ingredientsListTextView) }
    private val recipeImageView: ImageView by lazy { findViewById<ImageView>(R.id.recipeImageView) }
    private val updatedDateTextView: TextView by lazy { findViewById<TextView>(R.id.updatedDateTextView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as Recipe

        titleTextView.text = recipe.title
        updatedDateTextView.text = getString(R.string.updated) + recipe.date_updated
        ingredientsListTextView.text = recipe.ingredients.joinToString(separator = "\n")
        Glide.with(this)
            .load(recipe.featured_image)
            .into(recipeImageView)
    }

    companion object {
        const val EXTRA_RECIPE = "com.example.a3andmproject.EXTRA_RECIPE"
    }

}