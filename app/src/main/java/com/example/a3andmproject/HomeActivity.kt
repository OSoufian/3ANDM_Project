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
import androidx.room.Room.databaseBuilder
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.a3andmproject.database.AppDatabase
import com.example.a3andmproject.models.Food2ForkApi
import com.example.a3andmproject.models.Recipe
import com.example.a3andmproject.utils.Constant
import com.example.a3andmproject.utils.Network
import com.google.gson.Gson
import java.io.Serializable


class HomeActivity : AppCompatActivity() {
    private val recipeListRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recipeListRecyclerView) }
    private val searchEditText: EditText by lazy { findViewById<EditText>(R.id.searchEditText) }
    private val submitButton: Button by lazy { findViewById<Button>(R.id.submitButton) }
    private val chickenButton: Button by lazy { findViewById<Button>(R.id.chickenButton) }
    private val beefButton: Button by lazy { findViewById<Button>(R.id.beefButton) }
    private val soupButton: Button by lazy { findViewById<Button>(R.id.soupButton) }
    private val frenchButton: Button by lazy { findViewById<Button>(R.id.frenchButton) }
    private var searchQuery = ""

    private var currentPage = 1
    private var isLoading = false
    private var hasMorePages = true

    val db = AppDatabase.getInstance(this).recipeDao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = RecipeAdapter(mutableListOf())
        recipeListRecyclerView.adapter = adapter

        recipeListRecyclerView.layoutManager = LinearLayoutManager(this)



        submitButton.setOnClickListener {
            searchQuery = searchEditText.text.toString()
            currentPage = 1
            makeRequest()
        }

        chickenButton.setOnClickListener {
            searchQuery = "Chicken"
            currentPage = 1
            makeRequest()
        }

        beefButton.setOnClickListener {
            searchQuery = "Beef"
            currentPage = 1
            makeRequest()
        }

        soupButton.setOnClickListener {
            searchQuery = "Soup"
            currentPage = 1
            makeRequest()
        }

        frenchButton.setOnClickListener {
            searchQuery = "French"
            currentPage = 1
            makeRequest()
        }

        recipeListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && hasMorePages && (lastVisibleItemPosition + 1) >= totalItemCount) {
                    loadNextPage()
                }
            }
        })

        makeRequest()
    }

    private fun loadNextPage() {
        currentPage++
        makeRequest()
    }

    private fun makeRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = String.format(Constant.URL, currentPage, searchQuery)
        val loadingImageView = findViewById<ImageView>(R.id.loadingImageView)

        if (Network.isNetworkAvailable(this)) {
            val getRequest = object : StringRequest(
                Method.GET, url,
                Response.Listener { json ->
                    Log.d("json", json)
                    val api = Gson().fromJson(json, Food2ForkApi::class.java)
                    Log.d("api", api.results.toString())

                    val adapter = recipeListRecyclerView.adapter as RecipeAdapter
                    if (currentPage == 1) {
                        adapter.setData(api.results)
                    } else {
                        adapter.addItems(api.results)
                    }

                    isLoading = false
                    loadingImageView.visibility = View.GONE
                    hasMorePages = api.results.isNotEmpty()

                    // Ajout des recipes dans la database
//                    val recipeDao = AppDatabase.getInstance(this).recipeDao()
//                    GlobalScope.launch {
//                        recipeDao.insertRecipes(api.results)
//                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Error","error => " + error.toString())
                    loadingImageView.visibility = View.GONE
                }

            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
                    return params
                }
            }

            loadingImageView.visibility = View.VISIBLE
            queue.add(getRequest)
        } else {
//            val recipes = getRecipesFromDatabase()

            val adapter = recipeListRecyclerView.adapter as RecipeAdapter
            if (currentPage == 1) {
//                adapter.setData(recipes)
            } else {
//                adapter.addItems(recipes)
            }

            isLoading = false
            loadingImageView.visibility = View.GONE

//            hasMorePages = recipes.isNotEmpty()
        }
    }

    private fun saveDataToDatabase(recipes: List<Recipe>) {
//        val db = AppDatabase.getInstance(applicationContext)

//        db.recipeDao().deleteRecipes()
//
//        db.recipeDao().insertRecipes(recipes)
    }

//    private fun getRecipesFromDatabase(): List<Recipe> {
//        val db = AppDatabase.getInstance(applicationContext)
//        return db.recipeDao().getRecipes()
//    }
    inner class RecipeAdapter(private val recipes: MutableList<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

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

        fun setData(newRecipes: List<Recipe>) {
            recipes.clear()
            recipes.addAll(newRecipes)
            notifyDataSetChanged()
        }

        fun addItems(newRecipes: List<Recipe>) {
            val oldSize = recipes.size
            recipes.addAll(newRecipes)
            notifyItemRangeInserted(oldSize, newRecipes.size)
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