package me.pengj.arcompose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun ActionButton(textDetected: Boolean,
                 onClick: () -> Unit) {
    AnimatedVisibility(visible = textDetected) {
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp)
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = "Share Text",
                    tint = Color.Blue
                )
            }
        }

    }

}