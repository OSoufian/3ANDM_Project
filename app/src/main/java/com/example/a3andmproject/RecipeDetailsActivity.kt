package com.example.a3andmproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a3andmproject.models.Recipe

class RecipeDetailsActivity : AppCompatActivity() {
    private val titleTextView: TextView by lazy { findViewById<TextView>(R.id.titleTextView) }
    private val ingredientsTextView: TextView by lazy { findViewById<TextView>(R.id.ingredientsTextView) }
    private val ingredientsListTextView: TextView by lazy { findViewById<TextView>(R.id.ingredientsListTextView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as Recipe

        titleTextView.text = recipe.title
        ingredientsTextView.text = recipe.ingredients.joinToString(separator = "\n")
    }

    companion object {
        const val EXTRA_RECIPE = "com.example.a3andmproject.EXTRA_RECIPE"
    }

}