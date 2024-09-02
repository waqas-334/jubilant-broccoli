package com.waqas.animatedtictactoecompose

import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.concurrent.timer


private const val TAG = "TicTacToe"

@Composable
fun TicTacToe(modifier: Modifier = Modifier) {

    val size = Size(width = 300f, height = 300f)
    val distanceBetweenLinesDp = (size.height / 3).dp

    val pathPortion = remember { Animatable(0f) }

    LaunchedEffect(key1 = Unit) {
        pathPortion.animateTo(targetValue = 1F, animationSpec = tween(durationMillis = 1000))
    }

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Canvas(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .align(Alignment.CenterHorizontally)
                .pointerInput(true) {
                    detectTapGestures { bounds ->
                        val oneBoxSize = distanceBetweenLinesDp.toPx()
                        val r1c1 = Rect(0f, 0f, oneBoxSize, oneBoxSize)
                        val r1c2 = Rect(oneBoxSize, 0f, oneBoxSize * 2, oneBoxSize)
                        val r1c3 = Rect(oneBoxSize * 2, 0f, oneBoxSize * 3, oneBoxSize)
                        val r2c1 = Rect(0f, oneBoxSize, oneBoxSize, oneBoxSize * 2)
                        val r2c2 = Rect(oneBoxSize, oneBoxSize, oneBoxSize * 2, oneBoxSize * 2)
                        val r2c3 = Rect(oneBoxSize * 2, oneBoxSize, oneBoxSize * 3, oneBoxSize * 2)
                        val r3c1 = Rect(0f, oneBoxSize * 2, oneBoxSize, oneBoxSize * 3)
                        val r3c2 = Rect(oneBoxSize, oneBoxSize * 2, oneBoxSize * 2, oneBoxSize * 3)
                        val r3c3 =
                            Rect(oneBoxSize * 2, oneBoxSize * 2, oneBoxSize * 3, oneBoxSize * 3)

                        if (r1c1.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 1st box")
                        } else if (r1c2.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 2nd box")
                        } else if (r1c3.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 3rd box")
                        } else if (r2c1.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 4th box")
                        } else if (r2c2.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 5th box")
                        } else if (r2c3.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 6th box")
                        } else if (r3c1.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 7th box")
                        } else if (r3c2.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 8th box")
                        } else if (r3c3.contains(bounds)) {
                            Log.d(TAG, "TicTacToe: 9th box")
                        }

                    }
                }
        ) {


            val horizontalFirstLine = Path().apply {
                moveTo(0f, distanceBetweenLinesDp.toPx())
                lineTo(size.width.dp.toPx(), distanceBetweenLinesDp.toPx())
            }

            val firstHorizontalOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(horizontalFirstLine.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, firstHorizontalOutPath, true)
                getPosTan(pathPortion.value * length, null, null)
            }


            //1st horizontal line
            drawPath(
                path = firstHorizontalOutPath.asComposePath(),
                color = Color.Black,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                ),
            )


            val horizontalSecondLine = Path().apply {
                moveTo(0f, distanceBetweenLinesDp.toPx() * 2)
                lineTo(size.width.dp.toPx(), distanceBetweenLinesDp.toPx() * 2)
            }
            val secondHorizontalOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(horizontalSecondLine.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, secondHorizontalOutPath, true)
                getPosTan(pathPortion.value * length, null, null)
            }


            //second horizontal line
            drawPath(
                path = secondHorizontalOutPath.asComposePath(),
                color = Color.Black,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                ),
            )

            val firstVerticalLine = Path().apply {
                moveTo(distanceBetweenLinesDp.toPx(), 0f)
                lineTo(distanceBetweenLinesDp.toPx(), size.height.dp.toPx())
            }
            val firstVerticalOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(firstVerticalLine.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, firstVerticalOutPath, true)
                getPosTan(pathPortion.value * length, null, null)
            }


            //first vertical line
            drawPath(
                path = firstVerticalOutPath.asComposePath(),
                color = Color.Black,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                ),
            )

            val secondVerticalLine = Path().apply {
                moveTo(distanceBetweenLinesDp.toPx() * 2, 0f)
                lineTo(distanceBetweenLinesDp.toPx() * 2, size.height.dp.toPx())
            }

            val secondVerticalOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(secondVerticalLine.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, secondVerticalOutPath, true)
                getPosTan(pathPortion.value * length, null, null)
            }

            //second vertical line
            drawPath(
                path = secondVerticalOutPath.asComposePath(),
                color = Color.Black,
                style = Stroke(
                    width = 5.dp.toPx(),
                    cap = StrokeCap.Round
                ),
            )
//
//            val r1C1 = Path().apply {
//                moveTo(0f, 0f)
//                lineTo(distanceBetweenLinesDp.toPx(), 0f)
//                lineTo(distanceBetweenLinesDp.toPx(), distanceBetweenLinesDp.toPx())
//                lineTo(0f, distanceBetweenLinesDp.toPx())
//                close()
//            }
//            val r1C1Offset = Offset( x = distanceBetweenLinesDp.toPx(), y = distanceBetweenLinesDp.toPx())
//            val rect = Rect(
//                topLeft = r1C1Offset,
//                bottomRight = Offset(distanceBetweenLinesDp.toPx(),distanceBetweenLinesDp.toPx())
//            )
//
//

            val buffer = 0f
            val circlePath = Path().apply {
                moveTo(0f, 0f)
                addOval(
                    Rect(
                        Offset(
                            distanceBetweenLinesDp.toPx() / 2 + buffer,
                            distanceBetweenLinesDp.toPx() / 2 + buffer
                        ), 80f
                    )
                )
            }


            val circleOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(circlePath.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, circleOutPath, true)
                getPosTan(pathPortion.value * length, null, null)
            }


//            drawPath(path = r1C1, color = Color.Green, style = Stroke(width = 5.dp.toPx()))
//            drawPath(
//                path = circleOutPath.asComposePath(),
//                color = Color.Green,
//                style = Stroke(width = 10.dp.toPx())
//            )

            val oneBoxSize = distanceBetweenLinesDp.toPx()
            val r1c1 = Rect(0f, 0f, oneBoxSize, oneBoxSize)
            val r1c2 = Rect(oneBoxSize * 2, oneBoxSize * 2, oneBoxSize * 3, oneBoxSize * 3)
//            drawRect(color = Color.Red, topLeft = r1c2.topLeft, size = r1c2.size)

        }
    }
}