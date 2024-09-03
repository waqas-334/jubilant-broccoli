package com.waqas.animatedtictactoecompose

import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


private const val TAG = "TicTacToe"

sealed class Player {
    data object Cross : Player()
    data object Circle : Player()
    data object None : Player()

}

data class PlayerState(
    val index: Int,
    val rect: Rect,
    val player: Player = Player.None,
    val animationValue: Float = 0F
)

@Composable
fun dpToPx(dp: Dp): Float {
    // Get the current density from the composition
    val density = LocalDensity.current
    return with(density) { dp.toPx() }
}

@Composable
fun TicTacToe(modifier: Modifier = Modifier) {

    val size = Size(width = 300f, height = 300f)
    val distanceBetweenLinesDp = (size.height / 3).dp

    val linesPathPortion = remember { Animatable(0f) }
    val status = remember { mutableStateListOf<PlayerState>() }


    var current: Player = Player.Circle


    fun updatePlayerState() {
        current = if (current == Player.Circle) Player.Cross else Player.Circle
    }


    var clickedIndex by remember { mutableStateOf(-1) }
    val playerPathPortion: MutableList<Animatable<Float, AnimationVector1D>> = mutableListOf()
    repeat(9) {
        playerPathPortion.add( remember { Animatable(0f) })
    }
    LaunchedEffect(key1 = clickedIndex) {
        if(clickedIndex==-1) return@LaunchedEffect

        playerPathPortion[clickedIndex].animateTo(targetValue = 1F, animationSpec = tween(durationMillis = 1000))

        val portion = playerPathPortion[clickedIndex].value
        Log.e(TAG, "TicTacToe: LaunchedEffectExistingValue: $portion")
    }


    LaunchedEffect(key1 = Unit) {
        linesPathPortion.animateTo(targetValue = 1F, animationSpec = tween(durationMillis = 1000))
    }


    val oneBoxSize = dpToPx(dp = distanceBetweenLinesDp)

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

    val rectangles = listOf(r1c1, r1c2, r1c3, r2c1, r2c2, r2c3, r3c1, r3c2, r3c3)
    rectangles.forEachIndexed { index, rect ->
        status.add(PlayerState(index, rect))
    }


    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Canvas(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .align(Alignment.CenterHorizontally)
                .pointerInput(true) {
                    detectTapGestures { bounds ->
                        rectangles.forEachIndexed { index, rect ->
                            if (rect.contains(bounds)) {
                                val currentState = status[index]
                                if (currentState.player != Player.None) return@forEachIndexed

                                status[index] = currentState.copy(
                                    player = current,
                                )
                                clickedIndex = index
                                updatePlayerState()
                                return@forEachIndexed
                            }
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
                getSegment(0f, linesPathPortion.value * length, firstHorizontalOutPath, true)
                getPosTan(linesPathPortion.value * length, null, null)
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
                getSegment(0f, linesPathPortion.value * length, secondHorizontalOutPath, true)
                getPosTan(linesPathPortion.value * length, null, null)
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
                getSegment(0f, linesPathPortion.value * length, firstVerticalOutPath, true)
                getPosTan(linesPathPortion.value * length, null, null)
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

            Log.i(TAG, "TicTacToe: pathPortionValue: ${linesPathPortion.value}")
            val secondVerticalOutPath = android.graphics.Path()
            PathMeasure().apply {
                setPath(secondVerticalLine.asAndroidPath(), false)
                getSegment(0f, linesPathPortion.value * length, secondVerticalOutPath, true)
                getPosTan(linesPathPortion.value * length, null, null)
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


            status.forEachIndexed { index, it ->
//                drawRectangle(it.first, color = if (index%2==0) Color.Red else Color.Green)
                if (it.player is Player.None) return@forEachIndexed
                Log.i(TAG, "TicTacToe: pathPortionValue: ${it.animationValue}")
                if (it.player is Player.Circle) {
                    drawPlayerCircle(it.rect, playerPathPortion[index].value)
                } else drawCrossPlayer(it.rect, playerPathPortion[index].value)
            }


//            val oneBoxSize = distanceBetweenLinesDp.toPx()

//            val r1c1 = Rect(0f, 0f, oneBoxSize, oneBoxSize)
//            drawPlayerCircle(r1c1, pathPortion.value)

//            val r2c2 = Rect(oneBoxSize, oneBoxSize, oneBoxSize * 2, oneBoxSize * 2)
//            drawCrossPlayer(r2c2, pathPortion.value)


        }
    }

}
