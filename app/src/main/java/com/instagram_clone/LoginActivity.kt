package com.instagram_clone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.instagram_clone.screens.LoginScreen
import com.instagram_clone.screens.SignUpScreen
import com.instagram_clone.ui.theme.Instagram_CloneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class LoginActivity @Inject constructor() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Instagram_CloneTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val navController = rememberNavController();
    val navHost = NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LoginScreen(onSignUp = {
                navController.popBackStack()
                navController.navigate("signUp")
            }) {
                //On Login Successful
                navController.navigate("main")
            }
        }
        composable(route = "signUp") {
            SignUpScreen(onLogin = {
                navController.popBackStack()
                navController.navigate("login")
            }) {
                //On SignUp Successful
                navController.navigate("main")
            }
        }
        composable(route = "main") {
            LaunchedEffect(key1 = Unit) {
                context.startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    )
                )
                val act = context as Activity
                act.finish()
            }
        }
    }

}