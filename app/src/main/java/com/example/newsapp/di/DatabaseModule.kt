package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.local.database.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides                                           // Provides the Room Database instance
    @Singleton                                          // Singleton instance of the database
    fun provideDatabase(
        @ApplicationContext context: Context            // Hilt provides application context
    ): ArticleDatabase{
        return Room.databaseBuilder(
            context = context,
            ArticleDatabase::class.java,
            "articles_db"
        )
                                                        // Wiping database on upgrade is NOT for production
//             .fallbackToDestructiveMigration()
            .build()
    }

    @Provides                                           // Provides the ArticleDao instance
    @Singleton                                          // Singleton instance of the DAO (from the singleton database)
    fun provideArticleDao(db: ArticleDatabase): ArticleDao{
                                                        // Get the DAO from the provided database instance
        return db.articleDao()
    }
}