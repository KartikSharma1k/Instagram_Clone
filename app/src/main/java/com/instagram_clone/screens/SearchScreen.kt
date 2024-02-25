package com.instagram_clone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SearchScreen(){
    Scaffold(containerColor = Color.Black) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
                .fillMaxSize()
        ) {
            Text(text = "Search Screen", color = Color.White)
        }
    }
}