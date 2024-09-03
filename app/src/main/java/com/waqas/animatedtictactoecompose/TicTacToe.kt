package com.waqas.animatedtictactoecompose

import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waqas.animatedtictactoecompose.WinnerChecker.Companion.checkWinner
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


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
//    val animationValue: Float = 0F
)

sealed class GameState {
    data object PlayerCross : GameState()
    data object PlayerCircle : GameState()
    data class Ended(val winner: Player) : GameState()
}

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

    var clickedIndex by remember { mutableIntStateOf(-1) }
    val playerPathPortion: MutableList<Animatable<Float, AnimationVector1D>> = mutableListOf()
    repeat(9) {
        playerPathPortion.add(remember { Animatable(0f) })
    }


    //we need a separate coroutine that updates the value of animation
    //if we just use Launched Effect it would cancel the existing animation
    //as user presses another button before the existing animation ends
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = clickedIndex) {
        if (clickedIndex == -1) return@LaunchedEffect

        scope.launch {
            playerPathPortion[clickedIndex].animateTo(
                targetValue = 1F, animationSpec = tween(durationMillis = 1000)
            )
        }

        if (!scope.isActive) {
            scope.cancel()
        }

    }


    LaunchedEffect(key1 = Unit) {
        linesPathPortion.animateTo(targetValue = 1F, animationSpec = tween(durationMillis = 1000))
    }

    val inifiniteTransition = rememberInfiniteTransition()
    val phase by inifiniteTransition.animateFloat(
        initialValue = 10000f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = tween(60000, easing = LinearEasing)),
        label = ""
    )
    val oneBoxSize = dpToPx(dp = distanceBetweenLinesDp)

    val r1c1 = Rect(0f, 0f, oneBoxSize, oneBoxSize)
    val r1c2 = Rect(oneBoxSize, 0f, oneBoxSize * 2, oneBoxSize)
    val r1c3 = Rect(oneBoxSize * 2, 0f, oneBoxSize * 3, oneBoxSize)

    val r2c1 = Rect(0f, oneBoxSize, oneBoxSize, oneBoxSize * 2)
    val r2c2 = Rect(oneBoxSize, oneBoxSize, oneBoxSize * 2, oneBoxSize * 2)
    val r2c3 = Rect(oneBoxSize * 2, oneBoxSize, oneBoxSize * 3, oneBoxSize * 2)

    val r3c1 = Rect(0f, oneBoxSize * 2, oneBoxSize, oneBoxSize * 3)
    val r3c2 = Rect(oneBoxSize, oneBoxSize * 2, oneBoxSize * 2, oneBoxSize * 3)
    val r3c3 = Rect(oneBoxSize * 2, oneBoxSize * 2, oneBoxSize * 3, oneBoxSize * 3)

    val rectangles = listOf(r1c1, r1c2, r1c3, r2c1, r2c2, r2c3, r3c1, r3c2, r3c3)
    rectangles.forEachIndexed { index, rect ->
        status.add(PlayerState(index, rect))
    }

    var winnerCombination by remember {
        mutableStateOf(Winner())
    }
    var clickCounter by remember {
        mutableIntStateOf(0)
    }

    var statusText by remember {
        mutableStateOf("Tap to start")
    }

    fun updateStatus() {
        statusText = if (current == Player.Cross) "Cross Next" else "Circle Next"
    }



    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = statusText, fontSize = 30.sp, style = TextStyle(
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Canvas(modifier = Modifier
            .width(300.dp)
            .height(300.dp)
            .align(Alignment.CenterHorizontally)
            .pointerInput(true) {
                detectTapGestures { bounds ->
                    if (winnerCombination.combination.size >= 3) return@detectTapGestures  //someone won
                    rectangles.forEachIndexed { index, rect ->
                        if (rect.contains(bounds)) {
                            val currentState = status[index]
                            if (currentState.player != Player.None) return@forEachIndexed

                            status[index] = currentState.copy(
                                player = current,
                            )

                            clickedIndex = index
                            updatePlayerState()
                            checkWinner(status)?.let {
                                winnerCombination = it
                            }
                            clickCounter++
                            updateStatus()
                            return@forEachIndexed
                        }
                    }

                    if (clickCounter >= 9) {
                        statusText = "Draw"
                        return@detectTapGestures
                    }
                }
            }) {


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
                    width = 5.dp.toPx(), cap = StrokeCap.Round
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
                    width = 5.dp.toPx(), cap = StrokeCap.Round
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
                    width = 5.dp.toPx(), cap = StrokeCap.Round
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
                    width = 5.dp.toPx(), cap = StrokeCap.Round
                ),
            )


            status.forEachIndexed { index, it ->
//                drawRectangle(it.first, color = if (index%2==0) Color.Red else Color.Green)
                if (it.player is Player.None) return@forEachIndexed
                if (it.player is Player.Circle) {
                    drawPlayerCircle(it.rect, playerPathPortion[index].value)
                } else drawCrossPlayer(it.rect, playerPathPortion[index].value)
            }

            if (winnerCombination.combination.size < 3) return@Canvas

            val b1 = status[winnerCombination.combination[0]].rect
            val b3 = status[winnerCombination.combination[2]].rect
            val winnerLineType = winnerCombination.lineType

            val buffer = (distanceBetweenLinesDp.toPx() / 3)

            val startOffSet = when (winnerLineType) {
                WinnerLineType.Column -> Offset(
                    x = b1.topCenter.x, y = b1.topCenter.y + buffer
                )

                WinnerLineType.Row -> Offset(
                    x = b1.centerLeft.x + buffer, y = b1.centerLeft.y
                )

                WinnerLineType.Diagonal -> b1.center
            }
            val endOffSet = when (winnerLineType) {
                WinnerLineType.Column -> Offset(
                    x = b3.bottomCenter.x, y = b3.bottomCenter.y - buffer
                )

                WinnerLineType.Diagonal -> b3.center
                WinnerLineType.Row -> Offset(
                    x = b3.centerRight.x - buffer, y = b3.centerRight.y
                )
            }

            val oval = Path().apply {
                addOval(Rect(topLeft = Offset(-20f, -20f), bottomRight = Offset(20f, 20f)))
            }
            drawLine(
                color = Color.Gray,
                start = startOffSet,
                end = endOffSet,
                strokeWidth = 10.dp.toPx(),
                cap = StrokeCap.Round,
//                pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(20f, 80f),
                pathEffect = PathEffect.stampedPathEffect(
                    shape = oval,
                    advance = 100f,
                    phase = phase,
                    style = StampedPathEffectStyle.Rotate
                )

            )

            statusText = if (current == Player.Cross) "Cross WON" else "Circle WON"
        }
    }

}


