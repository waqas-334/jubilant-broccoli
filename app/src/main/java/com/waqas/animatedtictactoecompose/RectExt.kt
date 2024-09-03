package com.waqas.animatedtictactoecompose

import androidx.compose.ui.geometry.Rect

//this will add the padding by
//add the value to top and left
//and removing from bottom and right
fun Rect.addPadding(padding: Float): Rect {
    return Rect(
        top = this.top + padding,
        left = this.left + padding,
        bottom = this.bottom - padding,
        right = this.right - padding
    )
}

