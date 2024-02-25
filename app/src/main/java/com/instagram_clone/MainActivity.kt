package com.instagram_clone

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.instagram_clone.ui.theme.Instagram_CloneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                    navController.navigate(screen.route)
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
fun CircularProfileView(border: Int) {
    AsyncImage(
        model = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2FcHty1PmTELWVBf26U6t7O2HSGc53?alt=media&token=f459846e-a0b2-4480-9a57-855a8d68f018",
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(30.dp)
            .border(
                border = BorderStroke(width = border.dp, color = Color.White),
                shape = CircleShape
            )
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

