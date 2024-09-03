package com.waqas.animatedtictactoecompose

import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.unit.dp


//This will draw a cross sign animating using the provide pathPortion
private const val TAG = "CanvasExt"

fun DrawScope.drawCrossPlayer(pos: Rect, pathPortion: Float, padding: Float = 70f) {
    val position = pos.addPadding(padding)

    val crossPathTopToBottomRight = Path().apply {
        //LTRB = 275,275,550,550
        moveTo(x = position.left, y = position.top)
        lineTo(x = position.right, y = position.bottom)

    }

    val crossPathBottomToTopLeft = Path().apply {
        //LTRB = 275,275,550,550
        moveTo(x = position.left, y = position.bottom)
        lineTo(x = position.right, y = position.top)


    }

    val crossPathTopLeftToBottomRight = android.graphics.Path()
    PathMeasure().apply {
        setPath(crossPathTopToBottomRight.asAndroidPath(), false)
        getSegment(0f, pathPortion * length, crossPathTopLeftToBottomRight, true)
        getPosTan(pathPortion * length, null, null)
    }

    val crossPathBottomLeftToTopRight = android.graphics.Path()
    PathMeasure().apply {
        setPath(crossPathBottomToTopLeft.asAndroidPath(), false)
        getSegment(0f, pathPortion * length, crossPathBottomLeftToTopRight, true)
        getPosTan(pathPortion * length, null, null)
    }

    drawPath(
        crossPathTopLeftToBottomRight.asComposePath(),
        color = Color.Red,
        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
    )
    drawPath(
        crossPathBottomLeftToTopRight.asComposePath(),
        color = Color.Red,
        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
    )

}

fun DrawScope.drawPlayerCircle(position: Rect, pathPortion: Float, padding: Float = 70f) {
//fun DrawScope.drawPlayerCircle(x: Float, y: Float, pathPortion: Float) {

    val positionWithPadding = position.addPadding(padding)

    val circlePath = Path().apply {
        moveTo(positionWithPadding.left, positionWithPadding.top)
        addOval(positionWithPadding)
    }


    val circleOutPath = android.graphics.Path()
    PathMeasure().apply {
        setPath(circlePath.asAndroidPath(), false)
        getSegment(0f, pathPortion * length, circleOutPath, true)
        getPosTan(pathPortion * length, null, null)
    }


    drawPath(
//        circlePath,
        circleOutPath.asComposePath(),
        color = Color.Green,
        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
    )

}

fun DrawScope.drawRectangle(rect: Rect, color: Color = Color.Red) {
    drawRect(
        color = color,
        topLeft = rect.topLeft,
        size = Size(rect.width, rect.height)
    )
}
