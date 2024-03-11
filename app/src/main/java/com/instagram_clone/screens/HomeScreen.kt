package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.models.PostData
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.FeedsViewModel
import java.text.SimpleDateFormat

@Composable
fun HomeScreen() {

    var feeds by remember {
        mutableStateOf(emptyList<PostData>())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val _context = LocalContext.current

    val feedsViewModel: FeedsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        feedsViewModel.getFeeds()
    }
    val feedsFlow = feedsViewModel.feedsFlow.collectAsState()

    feedsFlow.value?.let {
        when (it) {
            is Resource.Success -> {
                feeds = it.result
            }

            Resource.Loading -> isLoading = true;

            is Resource.Failure -> {
                Toast.makeText(_context, it.exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CustomTopAppBar()
        }) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = it)
        ) {
            item {
                LazyRow {
                    item {
                        UserStory(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                            networkImage = DataManager.userData.photoUrl,
                            description = "Your Story",
                            imageSize = 78
                        )
                    }
                    items(count = 10) {
                        StoryCell(
                            caption = "Kartik_Sharma",
                            networkImage = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2FcHty1PmTELWVBf26U6t7O2HSGc53?alt=media&token=f459846e-a0b2-4480-9a57-855a8d68f018",
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                Divider(thickness = 0.2.dp, modifier = Modifier.padding(vertical = 5.dp))
            }
            items(count = feeds.size) { post ->
                if (feeds.isEmpty()) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(trackColor = Color.White)
                    }
                } else {
                    FeedCell(
                        feeds[post],
                        liked = feeds[post].likes.contains(DataManager.userData.uid)
                    ) {
                        Toast.makeText(_context, "Liked", Toast.LENGTH_SHORT).show()
                        feedsViewModel.addLike(
                            postId = feeds[post].postId,
                            likes = feeds[post].likes
                        )
                        if (feeds[post].likes.contains(DataManager.userData.uid)) feeds[post].likes.remove(
                            DataManager.userData.uid
                        )
                        else feeds[post].likes.add(DataManager.userData.uid)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() {
    TopAppBar(
        title = {
            Icon(
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = "Instagram",
                modifier = Modifier.size(120.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        actions = {
            IconButton(onClick = {}, modifier = Modifier.padding(end = 5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_favourite_outlined_icon),
                    contentDescription = "favourite Icon",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(35.dp)
                )
            }
            IconButton(onClick = {}, modifier = Modifier.padding(end = 5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_chat_icon),
                    contentDescription = "Chat Icon",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(30.dp)
                )
            }
        }
    )
}

@Composable
fun UserStory(
    modifier: Modifier = Modifier,
    networkImage: String,
    description: String,
    imageSize: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier,
        ) {
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
                    .clip(shape = CircleShape)
                    .background(color = Color.White)
                    .border(width = 3.dp, color = Color.Black, shape = CircleShape)
                    .size(27.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Your Story",
                    tint = Color(0xFF0095F6),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = "Your Story",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedCell(
    post: PostData,
    liked: Boolean,
    onLike: () -> Unit
) {

    var postId by remember {
        mutableStateOf("")
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var commentCount by remember {
        mutableIntStateOf(post.comments.coerceAtLeast(post.tempComments))
    }

    val context = LocalContext.current

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PostHeader(
                modifier = Modifier.padding(horizontal = 15.dp),
                35,
                post.profileImage,
                "PostUserImage"
            )

            Text(text = post.username, color = Color.White, fontSize = 15.sp)

            Spacer(modifier = Modifier.weight(1.0f))

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Account Options",
                    modifier = Modifier
                        .size(25.dp),
                    tint = Color.White
                )
            }
        }

        PostImage(
            post = post,
            onLike = onLike,
            liked = liked,
            commentCount = post.comments.coerceAtLeast(post.tempComments),
            onComment = { id ->
                postId = id
                showBottomSheet = true
            })

        if (showBottomSheet) {
            CommentSheet(
                postId = postId,
                count = post.comments.coerceAtLeast(post.tempComments)
            ) {
                commentCount = it
                post.tempComments = it
                showBottomSheet = false
                /*Toast.makeText(
                    context,
                    "onDismiss post.comments - ${post.comments},\n post.tempComment - ${post.tempComments},\n it -  $it",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }
}

@Composable
fun StoryCell(networkImage: String, caption: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {

        PostHeader(imageSize = 75, networkImage = networkImage, description = "Feeds")

        Text(
            text = caption,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 10.dp)
        )

    }
}

@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    imageSize: Int,
    networkImage: String,
    description: String
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
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
                .size((imageSize + 5).dp)
                .border(
                    border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                    shape = CircleShape
                )
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostImage(
    modifier: Modifier = Modifier,
    post: PostData,
    onLike: () -> Unit,
    liked: Boolean,
    commentCount: Int,
    onComment: (postId: String) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = {
        post.photoUrl.size
    })

    var isLike by remember {
        mutableStateOf(false)
    }

    var isMiniLike by remember {
        mutableStateOf(liked)
    }

    var likeCount by remember {
        mutableIntStateOf(post.likes.size)
    }

    var size = 130.dp

    val animatedSize by animateDpAsState(
        targetValue = if (isLike) size else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 500f // because I like it this way
        ), label = "like Animation"
    )

    Column {
        Box(modifier = Modifier.padding(top = 5.dp), contentAlignment = Alignment.Center) {
            HorizontalPager(
                state = pagerState,
            ) { page ->
                // Our page content
                AsyncImage(
                    model = post.photoUrl[page],
                    contentDescription = null,
                    modifier = Modifier
//                        .aspectRatio(4f / 5f)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .heightIn(max = 500.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = {
                                if (!isMiniLike) {
                                    onLike()
                                    ++likeCount
                                }
                                isLike = true
                                isMiniLike = true

                            })
                        },
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                )
            }

            if (isLike) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_favourite_filled_icon),
                    contentDescription = "Like",
                    tint = Color.White,
                    modifier = Modifier.size(animatedSize)
                )
                if (animatedSize == size) {
                    isLike = false
                }
            }
        }

        PostStatsIcons(
            pagerState = pagerState,
            post.photoUrl.size, onLike = {
                if (!isMiniLike) {
                    isLike = !isLike
                }
                isMiniLike = !isMiniLike
                if (!isMiniLike) --likeCount
                else ++likeCount
                onLike()
            }, isLike = isMiniLike
        ) {
            onComment(post.postId)
        }

        PostStats(modifier = Modifier.padding(horizontal = 15.dp), likeCount = likeCount)

        PostDescription(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
            username = post.username,
            caption = post.description
        )

        PostComment(
            modifier = Modifier.padding(horizontal = 15.dp),
            count = commentCount,
            onComment = { onComment(post.postId) }
        )

        val simpleDateFormat = SimpleDateFormat("dd LLLL yyyy")
        val dateTime = simpleDateFormat.format(post.datePublished.time).toString()

        Text(
            text = dateTime,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, start = 15.dp, end = 15.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun PostDescription(modifier: Modifier = Modifier, username: String, caption: String) {
    Row(modifier = modifier) {
        Text(
            text = username,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(text = " $caption", color = Color.White, fontSize = 13.sp)
    }
}

@Composable
fun PostComment(
    modifier: Modifier = Modifier,
    count: Int = 12,
    onComment: (postId: String) -> Unit
) {

    Column(modifier = modifier.padding(bottom = 5.dp)) {

        val comment = when (count) {
            0 -> {
                "No Comments"
            }

            1 -> {
                "$count comment"
            }

            else -> {
                "View all $count comments"
            }
        }

        Text(
            text = comment,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.clickable { onComment("") })

        /*Row(Modifier.padding(top = 5.dp)) {
            Text(
                text = "_ritu032_ ",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Mine ❤🥰", fontSize = 13.sp, color = Color.White)
        }

        Row(Modifier.padding(top = 5.dp)) {
            Text(
                text = "_ritu032_ ",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(text = "I love you ❤", fontSize = 13.sp, color = Color.White)
        }*/
    }
}

@Composable
fun PostStats(
    modifier: Modifier = Modifier,
    networkImage: String = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2FcHty1PmTELWVBf26U6t7O2HSGc53?alt=media&token=f459846e-a0b2-4480-9a57-855a8d68f018",
    description: String = "",
    imageSize: Int = 25,
    commenterId: String = "_ritu032_",
    likeCount: Int = 67
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.wrapContentWidth()) {

/*            AsyncImage(
                model = networkImage,
                contentDescription = description,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(imageSize.dp)
                    .clip(shape = CircleShape)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = networkImage,
                contentDescription = description,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(imageSize.dp)
                    .clip(shape = CircleShape)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = networkImage,
                contentDescription = description,
                modifier = Modifier
                    .size(imageSize.dp)
                    .clip(shape = CircleShape)
                    .border(width = 2.dp, color = Color.Black, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        }*/

            /*        Text(
            text = "Liked by ",
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 7.dp)
        )*/
            /*Text(
            text = commenterId,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = " and ",
            color = Color.White,
            fontSize = 13.sp,
        )*/
            if (likeCount > 0) {

                val count = {
                    if (likeCount == 1) "$likeCount Like"
                    else "$likeCount Likes"
                }

                Text(
                    text = count(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostStatsIcons(
    pagerState: PagerState,
    size: Int,
    onLike: () -> Unit,
    isLike: Boolean,
    onComment: (postId: String) -> Unit
) {

    val animatedSize by animateDpAsState(
        targetValue = if (isLike) 30.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 500f // because I like it this way
        ), label = "like Animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 6.dp, end = 4.dp)
    ) {
        if (size > 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color(0xFF0095F6) else Color.DarkGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(if (pagerState.currentPage == iteration) 6.dp else 5.dp)
                    )
                }
            }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {


            IconButton(onClick = {
                onLike()
            }) {
                if (!isLike)
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_favourite_outlined_icon),
                        contentDescription = "Like Icon",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                else {
                    Icon(
                        painter = painterResource(id = R.drawable.instagram_favourite_filled_icon),
                        contentDescription = "Like Icon",
                        tint = Color.Red,
                        modifier = Modifier.size(animatedSize)
                    )
                }
            }

            IconButton(onClick = { onComment("") }) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_comment_icon),
                    contentDescription = "comment Icon",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_share_icon),
                    contentDescription = "share Icon",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_save_icon),
                    contentDescription = "share Icon",
                    tint = Color.White,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}