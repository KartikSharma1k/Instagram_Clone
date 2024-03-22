package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.instagram_clone.DataManager
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.Resource
import com.instagram_clone.ui.theme.InstagramBlue
import com.instagram_clone.ui.theme.InstagramDarkButton
import com.instagram_clone.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(uid: String = DataManager.userData.uid, onBack: () -> Unit) {

    var isLoading by remember {
        mutableStateOf(false)
    }

    var postData by remember {
        mutableStateOf(emptyList<PostData>())
    }

    var userData by remember {
        mutableStateOf(UserData())
    }

    var isFollowLoading by remember {
        mutableStateOf(false)
    }

    val isUser = DataManager.userData.uid == uid

    val _context = LocalContext.current

    val profileViewModel: ProfileViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        profileViewModel.getPost(uid)
        profileViewModel.getUserData(uid)
    }
    val postFlow = profileViewModel.postFlow.collectAsState()

    val userDataFlow = profileViewModel.userDataFlow.collectAsState()

    val userFollowFlow = profileViewModel.userFollowFlow.collectAsState()

    val userUnfollowFlow = profileViewModel.userUnfollowFlow.collectAsState()

    userDataFlow.value?.let {
        when (it) {
            is Resource.Failure -> {
                Toast.makeText(_context, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }

            Resource.Loading -> isLoading = true
            is Resource.Success -> {
                userData = it.result
            }

        }
    }

    postFlow.value?.let {
        when (it) {
            is Resource.Success -> {
                postData = it.result
            }

            Resource.Loading -> isLoading = true

            is Resource.Failure -> {
                Toast.makeText(
                    _context,
                    it.exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    userFollowFlow.value?.let {
        when (it) {
            is Resource.Failure -> {
                isFollowLoading = false
                Toast.makeText(_context, it.exception.message, Toast.LENGTH_SHORT).show()
            }

            Resource.Loading -> {
                isFollowLoading = true
            }

            is Resource.Success -> {
                isFollowLoading = false
                Toast.makeText(_context, "Followed", Toast.LENGTH_SHORT).show()
                DataManager.userData.following.add(userData.uid)
                userData.followers.add(DataManager.userData.uid)
            }
        }
    }


    userUnfollowFlow.value?.let {
        when (it) {
            is Resource.Failure -> {
//                isFollowLoading = false
                Toast.makeText(_context, it.exception.message, Toast.LENGTH_SHORT).show()
            }

            Resource.Loading -> {
//                isFollowLoading = true
            }

            is Resource.Success -> {
//                isFollowLoading = false
                Toast.makeText(_context, "Unfollowed", Toast.LENGTH_SHORT).show()
                DataManager.userData.following.remove(userData.uid)
                userData.followers.remove(DataManager.userData.uid)
            }
        }
    }


    Scaffold(
        topBar = { CustomTopAppBar(userData = userData, isUser = isUser) { onBack() } },
        containerColor = Color.Black,
        modifier = Modifier.animateContentSize()
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
                    userData = userData,
                    postLength = postData.size,
                    isUser = isUser,
                    isFollowLoading = isFollowLoading,
                    onFollow = { profileViewModel.follow(userData.uid) },
                    onUnfollow = { profileViewModel.unfollow(userData.uid) }
                )
                if (isUser || userData.type == "public" || DataManager.userData.following.contains(
                        userData.uid
                    )
                ) {
                    UserPostsView(
                        scrollState,
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .height(screenHeight),
                        posts = postData
                    )
                } else {

                    Divider(modifier = Modifier.padding(vertical = 30.dp), thickness = 0.2.dp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.White, shape = CircleShape)
                                .padding(10.dp)
                        )

                        Column(
                            modifier = Modifier.padding(start = 20.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "This account is private",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Follow this account to see their photos and videos.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 7.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    userData: UserData,
    isUser: Boolean,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = {
                    if (!isUser) {
                        onBack()
                    }
                }) {
                    Icon(
                        imageVector = if (!isUser) Icons.Outlined.ArrowBack else Icons.Outlined.Lock,
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

                if (isUser)
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

            if (isUser || userData.type == "public" || DataManager.userData.following.contains(
                    userData.uid
                )
            )
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = if (isUser) R.drawable.instagram_add_icon else R.drawable.instagram_notification_icon),
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
                    imageVector = if (isUser) Icons.Outlined.Menu else Icons.Default.MoreVert,
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
fun UserProfileView(
    modifier: Modifier = Modifier,
    userData: UserData,
    postLength: Int,
    isUser: Boolean,
    isFollowLoading: Boolean,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit
) {

    Column(modifier = modifier.padding(top = 20.dp)) {

        ProfileImageStates(
            modifier = Modifier.fillMaxWidth(1f),
            userData = userData,
            postLength = postLength
        )

        ProfileNameBio(modifier = Modifier.fillMaxWidth(1f), userData = userData)

        if (!isUser)
            ProfileFollowers(modifier = Modifier.fillMaxWidth(1f), userData = userData)

        ProfileButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            userData = userData,
            isUser = isUser,
            isFollowLoading = isFollowLoading,
            onFollow = { onFollow() },
            onUnfollow = { onUnfollow() }
        )


        if (isUser || userData.type == "public" || DataManager.userData.following.contains(
                userData.uid
            )
        )
            FeedsSection(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(1f),
                isUser = isUser
            )

    }
}

@Composable
fun ProfileFollowers(modifier: Modifier, userData: UserData) {

    Row(
        modifier = modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.wrapContentWidth()) {
            AsyncImage(
                model = userData.photoUrl,
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(28.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                error = painterResource(id = R.drawable.instagram_profile_place_holder)
            )
            AsyncImage(
                model = DataManager.userData.photoUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(28.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                error = painterResource(id = R.drawable.instagram_profile_place_holder)
            )
        }

        Text(
            text = "Followed by ",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
        Text(
            text = "${DataManager.userData.username},${userData.username}",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " and ",
            color = Color.White,
            fontSize = 12.sp,
        )
        Text(
            text = "5 others",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileButtons(
    modifier: Modifier = Modifier,
    userData: UserData,
    isUser: Boolean,
    isFollowLoading: Boolean,
    onFollow: () -> Unit,
    onUnfollow: () -> Unit
) {

    var button1 by remember {
        mutableStateOf("")
    }

    var button1color by remember {
        mutableStateOf(InstagramDarkButton())
    }

    var button2 by remember {
        mutableStateOf("")
    }

    var isButton2 by remember {
        mutableStateOf(false)
    }

    var isFollowingBottomSheet by remember {
        mutableStateOf(false)
    }


    button1 = if (DataManager.userData.following.contains(userData.uid) && !isUser) {
        button1color = InstagramDarkButton()
        "Following"
    } else if (userData.following.contains(DataManager.userData.uid) && !isUser) {
        button1color = InstagramBlue()
        "Follow Back"
    } else if (!isUser) {
        button1color = InstagramBlue()
        "Follow"
    } else {
        button1color = InstagramDarkButton()
        "Edit Profile"
    }


    button2 =
        if ((DataManager.userData.following.contains(userData.uid) || userData.type == "public") && !isUser) {
            isButton2 = true
            "Message"
        } else if (userData.following.contains(DataManager.userData.uid) && !isUser) {
            isButton2 = false
            "Follow Back"
        } else if (!isUser) {
            isButton2 = false
            "Follow"
        } else {
            isButton2 = true
            "Share Profile"
        }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = {
                if (DataManager.userData.following.contains(userData.uid) && !isUser) {
//                    button1color = InstagramDarkButton()
//                    "Following"
                    isFollowingBottomSheet = true
                } else if (userData.following.contains(DataManager.userData.uid) && !isUser) {
//                    button1color = InstagramBlue()
//                    "Follow Back"
                    onFollow()
                } else if (!isUser) {
//                    button1color = InstagramBlue()
//                    "Follow"
                    onFollow()
                } else {
//                    button1color = InstagramDarkButton()
//                    "Edit Profile"
                }
            },
            colors = ButtonDefaults.textButtonColors(containerColor = button1color),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 5.dp)
                .height(33.dp)
                .wrapContentHeight()
        ) {
            if (isFollowLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(17.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = button1, color = Color.White)
                if (DataManager.userData.following.contains(userData.uid) && !isUser)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                        tint = Color.White
                    )
            }
        }

        if (isButton2) {
            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(containerColor = InstagramDarkButton()),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
                    .height(33.dp)
            ) {
                Text(text = button2, color = Color.White)
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

    if (isFollowingBottomSheet) {
        FollowingBottomSheet(userData = userData, onDismiss = { isFollowingBottomSheet = false }) {
            onUnfollow()
            isFollowingBottomSheet = false
        }
    }
}

@Composable
fun FeedsSection(modifier: Modifier = Modifier, isUser: Boolean) {
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

        if (isUser)
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
            placeholder = painterResource(id = R.drawable.instagram_profile_place_holder),
            error = painterResource(id = R.drawable.instagram_profile_place_holder)
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
fun UserPostsView(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    posts: List<PostData>
) {

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