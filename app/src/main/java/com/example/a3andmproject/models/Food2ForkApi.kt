package com.example.a3andmproject.models

import java.io.Serializable

data class Food2ForkApi(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Recipe>
)

data class Recipe(
    val pk: Int,
    var title: String,
    val publisher: String,
    val featured_image: String,
    val rating: Int,
    val source_url: String,
    val description: String?,
    val cooking_instructions: String?,
    val ingredients: List<String>,
    val date_added: String,
    val date_updated: String,
    val long_date_added: Long,
    val long_date_updated: Long
) : Serializable
