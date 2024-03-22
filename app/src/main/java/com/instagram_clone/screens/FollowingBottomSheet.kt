package com.instagram_clone.screens

import android.widget.Space
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instagram_clone.models.UserData
import com.instagram_clone.ui.theme.BottomSheetColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowingBottomSheet(
    modifier: Modifier = Modifier,
    userData: UserData,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    onDismiss: () -> Unit,
    onUnfollow: () -> Unit
) {

    ModalBottomSheet(
        modifier = modifier
            .padding(bottom = 5.dp),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = BottomSheetColor()
    ) {
        Text(
            text = userData.username,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Divider(thickness = 0.2.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Add to Close Friends list", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .border(width = 1.dp, color = Color.White, shape = CircleShape)
                    .size(25.dp)
                    .padding(5.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Add to favourites", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Rounded.StarOutline,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Mute", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "",
                tint = Color.Gray,
                modifier = Modifier
                    .size(25.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Restrict", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "",
                tint = Color.Gray,
                modifier = Modifier
                    .size(25.dp)
            )
        }

        Text(
            text = "Unfollow",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .fillMaxWidth()
                .clickable { onUnfollow() }
        )

        Spacer(modifier = Modifier.padding(bottom = 20.dp))

    }

}