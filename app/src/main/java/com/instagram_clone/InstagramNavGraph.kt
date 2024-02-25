package com.instagram_clone

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.instagram_clone.screens.AddPostScreen
import com.instagram_clone.screens.ChatScreen
import com.instagram_clone.screens.HomeScreen
import com.instagram_clone.screens.NotificationScreen
import com.instagram_clone.screens.ProfileScreen
import com.instagram_clone.screens.ReelsScreen
import com.instagram_clone.screens.SearchScreen
import javax.inject.Inject

@Composable
fun InstagramNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomNavRoutes.HOME.route) {

        composable(route = BottomNavRoutes.HOME.route) {
            HomeScreen()
        }
        composable(route = BottomNavRoutes.SEARCH.route) {
            SearchScreen()
        }
        composable(route = BottomNavRoutes.ADDPOST.route) {
            AddPostScreen(onSuccess = { navController.navigate(BottomNavRoutes.PROFILE.route) })
        }
        composable(route = BottomNavRoutes.REELS.route) {
            ReelsScreen()
        }
        composable(route = BottomNavRoutes.PROFILE.route) {
            ProfileScreen()
        }
        composable(route = BottomNavRoutes.NOTIFICATION.route) {
            NotificationScreen()
        }
        composable(route = BottomNavRoutes.CHAT.route) {
            ChatScreen()
        }
    }

}