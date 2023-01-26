package com.example.habit.presentation.statistics.components.categoryAmountChart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Integer.min
import kotlin.math.roundToInt

@Composable
fun CategoryAmountChart(
    state: CategoryAmountChartState
) {

    val upperValue = (state.dataSet.maxOfOrNull { it.amount } ?: 0).coerceAtLeast(3)
    val lowerValue = 0

    val textColor = MaterialTheme.colorScheme.onSurface
    val rectColor = MaterialTheme.colorScheme.primary
    val spacing = 100f
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    val rectPaint = remember {
        Paint().apply {
            color = rectColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    if (state.dataSet.isEmpty()) return

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(16.dp)){

        //VARIABLES
        val spacePerItem = (size.width - spacing) / state.dataSet.size

        //DATA
        for (i in 0 until state.dataSet.size){
            drawContext.canvas.nativeCanvas.apply {
                val left = spacing + spacePerItem * i
                val top =
                    if(state.dataSet[i].amount == 0) size.height - spacing - 0.1f * size.height / (upperValue + 1)
                    else size.height - spacing - state.dataSet[i].amount * size.height / (upperValue + 1)
                val right = spacing + spacePerItem * (i+1) - 2
                val bottom = size.height - spacing
                drawRect(left, top, right, bottom, rectPaint)
            }
        }

        //LEFT CAPTION
        val nofLeftCaptions = min(5,upperValue)
        val amountStep = (upperValue - lowerValue) / nofLeftCaptions.toFloat()
        (0..nofLeftCaptions).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (lowerValue + amountStep * i).roundToInt().toString(),
                    0f,
                    size.height - spacing - i * size.height / (nofLeftCaptions + 1),
                    textPaint
                )
            }
        }

        //BOTTOM CAPTION
        val nofBottomCaptions = min(2,state.dataSet.size)
        drawContext.canvas.nativeCanvas.apply {
            for (i in 0 until nofBottomCaptions) {
                val fraction = (i.toFloat()/nofBottomCaptions)
                val data = state.dataSet[(state.dataSet.size * fraction).toInt()]
                drawText(
                    data.name,
                    spacing + (state.dataSet.size * fraction).toInt() * spacePerItem,
                    size.height - 5,
                    textPaint
                )
            }
            val data = state.dataSet[(state.dataSet.size-1)]
            drawText(
                data.name,
                spacing + (state.dataSet.size-1) * spacePerItem,
                size.height - 5,
                textPaint
            )
        }
    }


}