package com.instagram_clone

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.instagram_clone.screens.AddPostScreen
import com.instagram_clone.screens.ChatScreen
import com.instagram_clone.screens.CommentSheet
import com.instagram_clone.screens.HomeScreen
import com.instagram_clone.screens.NotificationScreen
import com.instagram_clone.screens.ProfileScreen
import com.instagram_clone.screens.ReelsScreen
import com.instagram_clone.screens.SearchScreen
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomNavRoutes.SEARCH.route) {

        composable(route = BottomNavRoutes.HOME.route) {
            HomeScreen()
        }
        composable(route = BottomNavRoutes.SEARCH.route) {
            SearchScreen {
                navController.navigate(
                    "${BottomNavRoutes.PROFILE.route}/{uid}".replace(
                        "{uid}",
                        it
                    )
                )
            }
        }
        composable(route = BottomNavRoutes.ADDPOST.route) {
            AddPostScreen(onSuccess = { navController.navigate(BottomNavRoutes.PROFILE.route) })
        }
        composable(route = BottomNavRoutes.REELS.route) {
            ReelsScreen()
        }
        composable(route = "${BottomNavRoutes.PROFILE.route}/{uid}") { navBackStackEntry ->
            val uid = navBackStackEntry.arguments?.getString("uid")
            uid?.let {
                ProfileScreen(it) {
                    navController.popBackStack()
                }
            }
        }
        composable(route = BottomNavRoutes.NOTIFICATION.route) {
            NotificationScreen()
        }
        composable(route = BottomNavRoutes.CHAT.route) {
            ChatScreen()
        }
        /* composable(route = BottomNavRoutes.COMMENTS.route) {
             CommentSheet(postId = "", onDismiss = {})
         }*/
    }

}