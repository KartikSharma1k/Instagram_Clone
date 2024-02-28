package com.instagram_clone.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    postId: String,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = { onDismiss() }, sheetState = sheetState) {

        Box(modifier = Modifier.fillMaxSize(1f)) {

            Text(text = postId, color = Color.White)

        }
    }
}