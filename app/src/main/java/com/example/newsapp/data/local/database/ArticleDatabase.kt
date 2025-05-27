package com.example.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class], // List our entities here
    version = 2, // Define the database version
    exportSchema = false // Set to true to export schema for version control/inspection
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao // Define abstract methods to get your DAOs
}





// Note: version = 1 is for the initial database version.
// If we change entities later, we'll need to increase the version and provide migrations.