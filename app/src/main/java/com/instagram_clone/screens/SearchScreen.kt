package com.instagram_clone.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.models.PostData
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.SearchViewModel

@Composable
fun SearchScreen(onItemClicked: (uid: String) -> Unit) {

    val searchViewModel: SearchViewModel = hiltViewModel()
    val gridFlow = searchViewModel.gridFlow.collectAsState()
    val searchQueryFlow = searchViewModel.searchQueryFlow.collectAsState()

    var gridItems by remember {
        mutableStateOf(emptyList<PostData>())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var searchList by remember {
        mutableStateOf(emptyList<UserData>())
    }

    val context = LocalContext.current

    gridFlow.value?.let {
        when (it) {
            is Resource.Success -> {
                gridItems = it.result
                isLoading = false
            }

            is Resource.Loading -> isLoading = true

            is Resource.Failure -> {
                isLoading = false
            }
        }
    }

    searchQueryFlow.value?.let {
        when (it) {
            is Resource.Failure -> {
                Log.d("searchData", "SearchScreen: $it")
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
            }

            Resource.Loading -> {

            }

            is Resource.Success -> {
                Log.d("searchData", "SearchScreen: $it")
                searchList = it.result
            }
        }
    }

    Scaffold(containerColor = Color.Black, topBar = {
        TopAppSearchBar(searchList, onSearch = {
            searchViewModel.getSearchQuery(it)
        }) {
            onItemClicked(it)
        }
    }) {
        Box(modifier = Modifier.padding(paddingValues = it)) {
            PostGrid(gridItems, isLoading)
        }
    }
}

@Composable
fun PostGrid(gridItems: List<PostData>, isLoading: Boolean) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3)) {
            items(gridItems.size) {
                AsyncImage(
                    model = gridItems[it].photoUrl[0],
                    contentDescription = "",
                    modifier = Modifier
                        .aspectRatio(gridItems[it].aspectRatio)
                        .padding(0.7.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppSearchBar(
    items: List<UserData>,
    onSearch: (query: String) -> Unit,
    onItemClicked: (uid: String) -> Unit
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var isSearchActive by remember {
        mutableStateOf(false)
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = searchQuery,
        onQueryChange = {
            searchQuery = it
            onSearch(searchQuery)
        },
        onSearch = {
            onSearch(searchQuery)
        },
        active = isSearchActive,
        onActiveChange = { isSearchActive = it },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.instagram_search_outlined_icon),
                contentDescription = ""
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty())
                IconButton(onClick = { searchQuery = "" }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "clear")
                }
        },
        placeholder = { Text(text = "Search") },
        colors = SearchBarDefaults.colors(
            containerColor = Color.Black,
            dividerColor = Color.DarkGray
        ),
    ) {

        LazyColumn(
            modifier = Modifier.padding(
                start = 15.dp
            )
        ) {
            item {
                Text(
                    text = "Recent",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }
            items(count = items.size) {
                SearchItem(items[it]) {
                    onItemClicked(it)
                }
            }
        }
    }
}

@Composable
fun SearchItem(userData: UserData, onItemClicked: (uid: String) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onItemClicked(userData.uid)
            }
    ) {
        AsyncImage(
            model = userData.photoUrl,
            contentDescription = "",
            modifier = Modifier
                .size(61.dp)
                .clip(shape = CircleShape),
            placeholder = painterResource(id = R.drawable.instagram_profile_place_holder),
            error = painterResource(id = R.drawable.instagram_profile_place_holder)
        )

        Column(
            modifier = Modifier.padding(start = 13.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userData.username,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = userData.fullName,
                color = Color.Gray,
                fontSize = 13.sp
            )
            Text(
                text = "Following",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = Color.Gray,
                modifier = Modifier.size(15.dp)
            )
        }
    }

}