package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.view.ViewCompat.NestedScrollType
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.instagram_clone.R
import com.instagram_clone.models.ProfileTabItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import androidx.compose.material.TabRow
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.hilt.navigation.compose.hiltViewModel
import com.instagram_clone.DataManager
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.ProfileViewModel

@Composable
fun ProfileScreen() {

    var isLoading by remember {
        mutableStateOf(false)
    }

    var postData by remember {
        mutableStateOf(emptyList<PostData>())
    }

    val _context = LocalContext.current

    val profileViewModel: ProfileViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        profileViewModel.getPost()
    }
    val postFlow = profileViewModel.postFlow.collectAsState()

    postFlow.let {
        when (it.value) {
            is Resource.Success -> {
                postData = (it.value as Resource.Success<List<PostData>>).result
            }

            Resource.Loading -> isLoading = true

            is Resource.Failure -> {
                Toast.makeText(
                    _context,
                    (it.value as Resource.Failure).exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }


    Scaffold(
        topBar = { CustomTopAppBar(userData = DataManager.userData) },
        containerColor = Color.Black
    ) {
        BoxWithConstraints(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            )
        ) {

            val screenHeight = maxHeight
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState)
            ) {
                UserProfileView(
                    modifier = Modifier.padding(horizontal = 17.dp),
                    userData = DataManager.userData,
                    postLength = postData.size
                )
                UserPostsView(
                    scrollState,
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .height(screenHeight),
                    posts = postData
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    userData: UserData
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Account Status",
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.White
                    )
                }

                Text(
                    text = userData.username,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Change Account",
                        modifier = Modifier
                            .size(23.dp),
                        tint = Color.White

                    )
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_add_icon),
                    contentDescription = "Add post",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(23.dp),
                    tint = Color.White

                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Account Options",
                    modifier = Modifier
                        .size(28.dp),
                    tint = Color.White

                )
            }
        }
    )
}

@Composable
fun UserProfileView(modifier: Modifier = Modifier, userData: UserData, postLength: Int) {

    Column(modifier = modifier.padding(top = 20.dp)) {

        ProfileImageStates(
            modifier = Modifier.fillMaxWidth(1f),
            userData = userData,
            postLength = postLength
        )

        ProfileNameBio(modifier = Modifier.fillMaxWidth(1f), userData = userData)

        ProfileButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        FeedsSection(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(1f)
        )

    }
}

@Composable
fun ProfileImageStates(modifier: Modifier = Modifier, userData: UserData, postLength: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NetworkImageBorder(
            imageSize = 85,
            networkImage = userData.photoUrl,
            description = "user pfp"
        )

        ProfileStates(postLength.toString(), "Posts")
        ProfileStates(userData.followers.size.toString(), "Followers")
        ProfileStates(userData.following.size.toString(), "Following")
    }
}

@Composable
fun ProfileNameBio(
    modifier: Modifier = Modifier, userData: UserData
) {
    Column(modifier = modifier) {
        Text(
            text = userData.fullName,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 15.sp,
            modifier = modifier
                .padding(top = 7.dp)
                .fillMaxWidth(1f)
        )
        Text(
            text = userData.bio,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable
fun ProfileButtons(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = {},
            colors = ButtonDefaults.textButtonColors(containerColor = Color(0x80444444)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 5.dp)
                .height(33.dp)
        ) {
            Text(text = "Edit Profile", color = Color.White)
        }

        TextButton(
            onClick = {},
            colors = ButtonDefaults.textButtonColors(containerColor = Color(0x80444444)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 5.dp)
                .height(33.dp)
        ) {
            Text(text = "Share Profile", color = Color.White)
        }

        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0x80444444)),
            modifier = Modifier
                .height(33.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.instagram_add_user_icon),
                contentDescription = "Add More Person",
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
fun FeedsSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        FeedsCell(
            networkImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/posts%2FcHty1PmTELWVBf26U6t7O2HSGc53%2F0da69700-f50f-1dcb-905d-2bf6f9f07f96?alt=media&token=df32f9ac-23e3-4828-b450-23ec0f3bd256",
            caption = "Feed Caption",
            modifier = Modifier.padding(end = 15.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        border = BorderStroke(1.dp, color = Color.White),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Create Feed",
                    tint = Color.White
                )
            }

            Text(
                text = "New",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 7.dp)
            )
        }
    }
}

@Composable
fun FeedsCell(networkImage: String, caption: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {

        NetworkImageBorder(imageSize = 70, networkImage = networkImage, description = "Feeds")

        Text(
            text = caption,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun NetworkImageBorder(imageSize: Int, networkImage: String, description: String) {
    Box(contentAlignment = Alignment.Center) {

        AsyncImage(
            model = networkImage,
            contentDescription = description,
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(imageSize.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.instagram_profile_place_holder)
        )
        Box(
            modifier = Modifier
                .size((imageSize + 10).dp)
                .border(
                    border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                    shape = CircleShape
                )
        )

    }
}

@Composable
fun ProfileStates(value: String, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold)
        Text(text = description, color = Color.LightGray)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun UserPostsView(scrollState: ScrollState, modifier: Modifier = Modifier, posts: List<PostData>) {

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val coroutineScope = rememberCoroutineScope()

    val tabItems = listOf(
        ProfileTabItem.UserFeed,
        ProfileTabItem.UserReels,
        ProfileTabItem.UserTags
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(initialPage = 0)

    LaunchedEffect(selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedIndex = pagerState.currentPage
    }

    Column(modifier = modifier) {

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedIndex,
            indicator = {
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, it)
                )
                /*Box(
                    modifier = Modifier
                        .tabIndicatorOffset(it[selectedIndex])
                        .height(1.dp)
                        .clip(RoundedCornerShape(8.dp)) // clip modifier not working
                        .background(color = Color.White)
                )*/
            },
            contentColor = Color.White,
            backgroundColor = Color.Black
        ) {
            tabItems.forEachIndexed { index, profileTabItem ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    icon = {
                        Icon(
                            painterResource(id = profileTabItem.icon),
                            contentDescription = "${profileTabItem.title} icon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
            }
        }


        com.google.accompanist.pager.HorizontalPager(
            state = pagerState,
            count = tabItems.size,
            modifier = Modifier
                .fillMaxHeight()
                .nestedScroll(remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            return if (available.y > 0) Offset.Zero else Offset(
                                x = 0f,
                                y = -scrollState.dispatchRawDelta(-available.y)
                            )
                        }
                    }
                })
        ) {
            when (it) {
                0 -> UserFeeds(label = tabItems[it].title, posts = posts)
                1 -> UserReels(label = tabItems[it].title)
                else -> UserTags(label = tabItems[it].title)
            }
        }

    }

}

@Composable
fun UserFeeds(label: String, posts: List<PostData>) {

    /*val gridHeight = remember(itemCount) {
        if (itemCount % 3 == 0)
            (itemCount / 3) * 200.dp // Adjust item height as needed
        else
            ((itemCount / 3) * 200.dp) + 200.dp
    }*/

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(posts.size) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(all = 0.5.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    model = posts[it].photoUrl[0],
                    contentDescription = "Feeds",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp)
                )
                if (posts[it].photoUrl.size > 1)
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_carousel_icon),
                        contentDescription = "Multiple Images",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp),
                        tint = Color.White
                    )
            }
        }
    }
}


@Composable
fun UserReels(label: String) {
    val itemCount = 20
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(itemCount) {
            AsyncImage(
                model = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2FsaTQ3i2H7wg8vmMY6Sbzz5inC012?alt=media&token=a43b4fad-1448-418e-b4c3-31610789ec71",
                contentDescription = "Reels",
                modifier = Modifier
                    .size(280.dp)
                    .padding(all = 0.5.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun UserTags(label: String) {
    val itemCount = 20
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(itemCount) {
            AsyncImage(
                model = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2FcHty1PmTELWVBf26U6t7O2HSGc53?alt=media&token=f459846e-a0b2-4480-9a57-855a8d68f018",
                contentDescription = "Feeds",
                modifier = Modifier
                    .size(200.dp)
                    .padding(all = 0.5.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}