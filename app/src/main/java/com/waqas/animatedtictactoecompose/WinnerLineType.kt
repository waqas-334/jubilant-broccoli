package com.waqas.animatedtictactoecompose

//This is required to make calculations to draw the final line
sealed class WinnerLineType {
    data object Row : WinnerLineType()
    data object Column : WinnerLineType()
    data object Diagonal : WinnerLineType()
}