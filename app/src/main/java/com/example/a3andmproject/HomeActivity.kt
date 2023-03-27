package com.example.a3andmproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.a3andmproject.models.Food2ForkApi
import com.example.a3andmproject.models.Recipe
import com.example.a3andmproject.utils.Constant
import com.google.gson.Gson
import java.io.Serializable

class HomeActivity : AppCompatActivity() {
    private val recipeListRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recipeListRecyclerView) }
    private val searchEditText: EditText by lazy { findViewById<EditText>(R.id.searchEditText) }
    private val buttonSubmit: Button by lazy { findViewById<Button>(R.id.buttonSubmit) }
    private var searchQuery = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonSubmit.setOnClickListener {
            searchQuery = searchEditText.text.toString()
            makeRequest()
        }

        makeRequest()
    }

    private fun makeRequest() {

        val queue = Volley.newRequestQueue(this)
        val url = Constant.URL + searchQuery

        val getRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { json ->
                Log.d("json", json)
                val api = Gson().fromJson(json, Food2ForkApi::class.java)
                Log.d("api", api.results.toString())

                recipeListRecyclerView.layoutManager = LinearLayoutManager(this)
                val adapter = RecipeAdapter(api.results)
                recipeListRecyclerView.adapter = adapter
            },
            Response.ErrorListener { error ->
                Log.e("Error","error => " + error.toString())
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
                return params
            }
        }
        queue.add(getRequest)
    }
    inner class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
            return RecipeViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            val recipe = recipes[position]
            recipe.title = recipe.title.replace("&nbsp;", " ").replace("S&#8217;", " ")
            holder.bind(recipe)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, RecipeDetailsActivity::class.java).apply {
                    putExtra(RecipeDetailsActivity.EXTRA_RECIPE, recipe as Serializable)
                }
                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return recipes.size
        }

        inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
            private val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)

            fun bind(recipe: Recipe) {
                titleTextView.text = recipe.title

                Glide.with(itemView.context)
                    .load(recipe.featured_image)
                    .placeholder(R.drawable.placeholder)
                    .into(recipeImageView)
            }
        }
    }
}