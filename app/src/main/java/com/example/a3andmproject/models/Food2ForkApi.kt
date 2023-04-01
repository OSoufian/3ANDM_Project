package com.example.a3andmproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.a3andmproject.database.AppDatabase
import java.io.Serializable

data class Food2ForkApi(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Recipe>
)

@Entity
@TypeConverters(AppDatabase::class)
data class Recipe(
    @PrimaryKey val pk: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "publisher") val publisher: String,
    @ColumnInfo(name = "featured_image") val featured_image: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "source_url") val source_url: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "cooking_instructions") val cooking_instructions: String?,
    @ColumnInfo(name = "ingredients") val ingredients: List<String>,
    @ColumnInfo(name = "date_added") val date_added: String,
    @ColumnInfo(name = "date_updated") val date_updated: String,
    @ColumnInfo(name = "long_date_added") val long_date_added: Long,
    @ColumnInfo(name = "long_date_updated") val long_date_updated: Long
) : Serializable
