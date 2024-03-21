package com.instagram_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.instagram_clone.repos.AuthRepository
import com.instagram_clone.ui.theme.Instagram_CloneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Instagram_CloneTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    InstagramApp()
                }
            }
        }
    }
}

@Composable
fun InstagramApp() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        contentColor = Color.Black,
        containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier.padding(
                bottom = it.calculateBottomPadding(),
                top = it.calculateTopPadding()
            )
        ) {
            InstagramNavGraph(navController)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {

    var profileIcon by remember {
        mutableIntStateOf(-1)
    }

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val screensList = listOf(
        BottomNavScreens.Home,
        BottomNavScreens.Search,
        BottomNavScreens.AddPost,
        BottomNavScreens.Reels,
        BottomNavScreens.Profile
    )

    BottomNavigation {
        screensList.forEachIndexed { index, screen ->
            BottomNavigationItem(
                selected = index == selectedIndex,
                onClick = {
                    profileIcon = if (index == 4) 1 else -1
                    selectedIndex = index
                    navController.navigate(
                        if (index == 4) "${screen.route}/{uid}".replace(
                            "{uid}",
                            DataManager.userData.uid
                        ) else screen.route
                    )
                },
                icon = {

                    when (index) {
                        4 -> {
                            CircularProfileView(profileIcon)
                        }

                        else -> {
                            Icon(
                                painter = painterResource(id = if (index == selectedIndex) screen.iconSelected else screen.iconNotSelected),
                                contentDescription = "Icon",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.background(color = Color.Black),
            )
        }
    }
}

@Composable
fun CircularProfileView(border: Int, photoUrl: String = DataManager.userData.photoUrl) {
    AsyncImage(
        model = photoUrl,
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(30.dp)
            .border(
                border = BorderStroke(width = border.dp, color = Color.White),
                shape = CircleShape
            )
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.instagram_profile_place_holder),
        error = painterResource(id = R.drawable.instagram_profile_place_holder)
    )
}

