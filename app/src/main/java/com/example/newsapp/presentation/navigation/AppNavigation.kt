package com.example.newsapp.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsapp.presentation.ui.news_detail.NewsDetailScreenUI
import com.example.newsapp.presentation.ui.news_list.NewsListScreenUI
import com.example.newsapp.presentation.ui.saved_articles.SavedArticlesScreenUI
import com.example.newsapp.presentation.ui.search_screen.SearchScreenUI


@Composable
fun AppNavigation() {

    val bottomNavItems = listOf(
        BottomNavigationItem(
            label = "Headlines",
            route = Routes.NewsScreen,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            badgeCount = 45                 // e.g number of newly added unread articles
        ),
        BottomNavigationItem(
            label = "Saved",
            route = Routes.SavedArticlesScreen,
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            hasNews = true
        ),
        BottomNavigationItem(
            label = "Search",
            route = Routes.SearchScreen,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            hasNews = false
        )
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("AppNavigation","Current destination route is: $currentRoute")


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { bottomNavItem ->
                    val selected = bottomNavItem.route::class.qualifiedName == currentRoute
                    Log.d("AppNavigation", "Item: ${bottomNavItem.label}, Route Class Name: ${bottomNavItem.route::class.qualifiedName}, Current Route: $currentRoute, Selected: $selected")

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            Log.d("AppNavigation", "Clicked: ${bottomNavItem.label}, Navigating to: ${bottomNavItem.route::class.qualifiedName}")

                            if (!selected) {                                                        // if already selected then it does not call again that selected one
                                navController.navigate(bottomNavItem.route) {
                                    Log.d(
                                        "AppNavigation",
                                        "Clicked: ${bottomNavItem.label}, Navigating to: ${bottomNavItem.route}"
                                    )
                                    popUpTo(navController.graph.startDestinationId) {       // Optional: Pop up to the start destination to avoid building up a large stack
                                        saveState = true                                                    // Save state of popped destinations
                                    }
                                    launchSingleTop = true              // Avoid multiple copies of the same destination when reselecting the same item
                                    restoreState = true                 // Restore state when reselecting a previously selected item
                                }
                            }

                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (bottomNavItem.badgeCount != null) {
                                        Badge {
                                            Text(text = bottomNavItem.badgeCount.toString())
                                        }
                                    }else if (bottomNavItem.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selected) bottomNavItem.selectedIcon else bottomNavItem.unselectedIcon,
                                    contentDescription = bottomNavItem.label,
//                                    tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        label = { Text(text = bottomNavItem.label) },
//                        alwaysShowLabel = false                     // hides the label except selectedItem
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.NewsScreen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Routes.NewsScreen> {
                NewsListScreenUI(navController = navController)
            }
            composable<Routes.SavedArticlesScreen> {
                SavedArticlesScreenUI(navController = navController)
            }
            composable<Routes.NewsDetailScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<Routes.NewsDetailScreen>()
                val articleUrl = args.articleUrl
                NewsDetailScreenUI(
                    navController = navController,
//                    articleUrl = articleUrl
                )
            }

             composable<Routes.SearchScreen> {
                SearchScreenUI(navController = navController)
             }
        }
    }


}


data class BottomNavigationItem(
    val label: String,
    val route: Routes,              // Using the sealed class Route as the 'route' property type for type safety
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)