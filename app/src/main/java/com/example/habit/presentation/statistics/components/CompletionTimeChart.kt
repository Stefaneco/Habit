package com.example.habit.presentation.statistics.components

import android.graphics.Paint
import android.util.Log
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
import com.example.habit.domain.util.DateTimeUtil
import com.example.habit.domain.util.rangeTo
import kotlinx.datetime.*

@Composable
fun CompletionTimeChart(
    startDate: LocalDate,
    endDate: LocalDate,
    dataSet: List<LocalDateTime> = emptyList(),
    perfectCompletionTime: LocalTime,
    noDataMessage : String = ""
) {
    //VARIABLES
    var upperValue = dataSet.maxOfOrNull { it.time } ?: LocalTime(10,0)
    var lowerValue = dataSet.minOfOrNull { it.time } ?: LocalTime(10,0)
    if( lowerValue > perfectCompletionTime) lowerValue = perfectCompletionTime
    if( upperValue < perfectCompletionTime) upperValue = perfectCompletionTime
    //lowerValue = LocalTime(lowerValue.hour-1, lowerValue.minute)

    val textColor = MaterialTheme.colorScheme.onSurface
    val pointColor = MaterialTheme.colorScheme.primary
    val spacing = 100f
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    val pointPaint = remember {
        Paint().apply {
            color = pointColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(16.dp)){

        drawContext.canvas.nativeCanvas.apply {

            if (dataSet.isEmpty()) {
                drawText(
                    noDataMessage,
                    (size.width - spacing)/2f,
                    (size.height - spacing)/2f,
                    textPaint
                )
                return@Canvas
            }

            //VARIABLES 2
            val widthChunk = (size.width - spacing) / startDate.daysUntil(endDate)
            val dataGroupedByDate = dataSet.groupBy { it.date }

            //LEFT CAPTION AND PERFECT LINE
            val leftSpan = upperValue.hour + 1 - lowerValue.hour
            val heightChunk = size.height / (leftSpan + 1)
            for(i in 0..leftSpan){
                drawText(
                    (lowerValue.hour + i).toString(),
                    0f,
                    size.height - spacing - i * heightChunk,
                    textPaint
                )

                /*drawLine(
                    0f,
                    size.height - spacing - i * heightChunk,
                    size.width,
                    size.height - spacing - i * heightChunk,
                    textPaint
                )*/

                if(lowerValue.hour + i == perfectCompletionTime.hour)
                {
/*                    Log.e("CODO", "lowerValue: $lowerValue, upperValue: $upperValue, " +
                            "leftSpan: $leftSpan, size.height: ${size.height}, perfectTime: $perfectCompletionTime")*/
                    val y = (size.height - spacing - i * heightChunk) -
                            (heightChunk) * (perfectCompletionTime.minute/60f)
                    val startX = spacing
                    val stopX = size.width
                    drawLine(
                        startX,
                        y,
                        stopX,
                        y,
                        textPaint
                    )
                }

            }

            //DATA
            var iterator = 0
            for(date in startDate..endDate){
                if(!dataGroupedByDate.containsKey(date)){
                    iterator += 1
                    continue
                }
                for(pointDateTime in dataGroupedByDate[date]!!){
                    val x = iterator * widthChunk
                    val y = size.height - spacing - heightChunk *
                            (((pointDateTime.time.hour*60+pointDateTime.minute) - (lowerValue.hour*60))/60f)
                    Log.e("Time", "time: $pointDateTime.time, width: ${size.width}, height: ${size.height}" +
                            "x: $x, y: $y")
                    drawCircle(
                        x,
                        y,
                        15f,
                        pointPaint
                    )
                }
                iterator += 1
            }

            //BOTTOM CAPTION
            drawText(
                DateTimeUtil.formatDate(startDate, getYear = false),
                spacing,
                size.height - 5,
                textPaint
            )
            drawText(
                DateTimeUtil.formatDate(startDate.plus(DatePeriod(days = startDate.daysUntil(endDate)/2)), getYear = false),
                spacing + (startDate.daysUntil(endDate)/2) * widthChunk,
                size.height - 5,
                textPaint
            )
            drawText(
                DateTimeUtil.formatDate(endDate, getYear = false),
                size.width - spacing,
                size.height - 5,
                textPaint
            )
        }

    }
}