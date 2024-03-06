package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.models.CommentData
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.CommentsViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    postId: String,
    count: Int,
    onComment: (count:Int) -> Unit,
    onDismiss: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    var comment by remember {
        mutableStateOf("")
    }

    val commentsViewModel: CommentsViewModel = hiltViewModel()
    commentsViewModel.readComments(postId)
    var commentFlow =
        commentsViewModel.commentFlow.collectAsState(initial = emptyList())

    var postCommentFlow = commentsViewModel.postCommentFlow.collectAsState()

    postCommentFlow.value?.let {
        when (it) {
            is Resource.Success -> {
                keyboardController?.hide()
                comment = ""
                onComment(it.result)
                isLoading = false
            }

            Resource.Loading -> {
                isLoading = true
            }

            is Resource.Failure -> {
                isLoading = false
                Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                keyboardController?.hide()
                comment = ""
            }

        }
    }

    var comments by remember {
        mutableStateOf(emptyList<CommentData>())
    }

    commentFlow.value.let {
        comments = it!!
    }
    ModalBottomSheet(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 5.dp),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.Black
    ) {

        Text(
            text = "Comments",
            color = Color.LightGray,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Divider(thickness = 0.2.dp, color = Color.DarkGray)
        if (isLoading)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Box {
            if (comments.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(top = 60.dp)) {
                    items(count = comments.size) {
                        CommentItem(
                            commentData = CommentData(
                                comments[it].commentId,
                                comments[it].date,
                                comments[it].username,
                                comments[it].fullname,
                                comments[it].profilePic,
                                comments[it].text,
                                comments[it].uid
                            )
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Comments !", color = Color.Gray)
                }
            }

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Comment as ${DataManager.userData.username}",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    AsyncImage(
                        modifier = Modifier
                            .padding(horizontal = 17.dp)
                            .size(40.dp)
                            .clip(shape = CircleShape),
                        model = DataManager.userData.photoUrl,
                        contentDescription = "",
                        placeholder = painterResource(id = R.drawable.instagram_profile_place_holder)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        commentsViewModel.postComment(postId, comment, count)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.instagram_share_icon),
                            contentDescription = "Post Comment",
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(25.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        commentsViewModel.postComment(postId, comment, count)
                    }),
            )
        }
    }
}

@Composable
fun CommentItem(commentData: CommentData) {

    val simpleDateFormat = SimpleDateFormat("dd LLLL yyyy")

    Row(
        modifier = Modifier.padding(horizontal = 17.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = commentData.profilePic,
            contentDescription = "Commenter Image",
            modifier = Modifier
                .size(40.dp)
                .clip(shape = CircleShape)
        )

        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            val dateTime = simpleDateFormat.format(commentData.date).toString()
            Row {
                Text(text = commentData.username, color = Color.White, fontSize = 12.sp)
                Text(
                    text = dateTime,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
            Text(
                text = commentData.text,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Text(
                text = "Reply",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.instagram_favourite_outlined_icon),
                contentDescription = "",
                tint = Color.Gray,
                modifier = Modifier.size(19.dp)
            )
        }
    }

}