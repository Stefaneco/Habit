package com.example.habit.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BottomCreator(
    buttonText: String,
    buttonOnClick: () -> Unit,
    onCloseEvent: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonEnabled : Boolean = true,
    closeOffset: Int = 250,
    content: @Composable () -> Unit,
) {
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    println(offsetY)
                    if (offsetY + delta > 0) offsetY += delta
                },
                onDragStopped = {
                    if (offsetY > closeOffset) onCloseEvent()
                    else offsetY = 0f
                }
            )
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Card(
            modifier = modifier.fillMaxWidth(1f),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 12.dp
            )
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp).fillMaxWidth()
            ){
                Spacer(modifier = Modifier.padding(4.dp))
                Box(modifier = Modifier
                    .width(50.dp)
                    .height(4.dp)
                    .background(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ))
                Spacer(modifier = Modifier.padding(4.dp))

                //CONTENT
                content()

                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    enabled = isButtonEnabled,
                    onClick = buttonOnClick) {
                    Text(buttonText)
                }
            }
        }
    }
}