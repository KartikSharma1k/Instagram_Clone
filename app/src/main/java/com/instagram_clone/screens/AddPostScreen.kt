package com.instagram_clone.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.AddPostViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(onSuccess:() -> Unit) {

    val context = LocalContext.current

    var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    val pagerState = rememberPagerState(pageCount = {
        selectImages.size
    })
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)) { uriList ->
            // process with the received image uri
            selectImages = uriList
        }

    var description by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val addPostViewModel: AddPostViewModel = hiltViewModel()
    val addPostFlow = addPostViewModel.addPostFlow.collectAsState()

    addPostFlow.let {
        when (it.value) {
            is Resource.Success -> {
                onSuccess()
            }

            Resource.Loading -> isLoading = true

            is Resource.Failure -> {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text(text = "Create a Post") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Column(horizontalAlignment = Alignment.End) {
                LaunchedEffect(Unit) {
                    galleryLauncher.launch(PickVisualMediaRequest())
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))

                ) { page ->
                    // Our page content
                    Image(
                        painter = rememberImagePainter(selectImages[page]),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 5f)
                    )
                }
                if (selectImages.size > 1)
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(top = 7.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color(0xFF0095F6) else Color.DarkGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(7.dp)
                            )
                        }
                    }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(top = 10.dp),
                    placeholder = { Text(text = "Write a caption...") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
                )

                ElevatedButton(
                    onClick = {
                        if (!isLoading) {
                            addPostViewModel.addPost(selectImages, description)
                        }
                    },
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    if (isLoading) CircularProgressIndicator(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    ) else Text(
                        text = "Post",
                        modifier = Modifier.padding(5.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
