package com.waqas.animatedtictactoecompose


data class Winner(
    val combination: List<Int> = listOf(),
    val lineType: WinnerLineType = WinnerLineType.Row
)


class WinnerChecker {

    companion object {
        fun checkWinner(status: List<PlayerState>): Winner? {
//        fun checkWinner(status: List<PlayerState>): List<Int>? {

            val winningCombinations = listOf(

                //rows
                listOf(0, 1, 2),
                listOf(3, 4, 5),
                listOf(6, 7, 8),

                //columns
                listOf(0, 3, 6),
                listOf(1, 4, 7),
                listOf(2, 5, 8),

                //diagonal
                listOf(0, 4, 8),
                listOf(2, 4, 6),


                )


            winningCombinations.forEach { listOfCombinations ->

                //check if the player is none then skip it
                if (checkIfNone(combination = listOfCombinations, status = status)) return@forEach

                if (checkIfWon(listOfCombinations = listOfCombinations, status = status)) {
                    val indexOfWinner = winningCombinations.indexOf(listOfCombinations)
                    val lineType =
                        if (indexOfWinner in 0..2) WinnerLineType.Row else if (indexOfWinner in 3..6) WinnerLineType.Column else WinnerLineType.Diagonal
                    return Winner(combination = listOfCombinations, lineType = lineType)
                }
            }
            return null
        }

        private fun checkIfNone(combination: List<Int>, status: List<PlayerState>): Boolean {
            return status[combination[0]].player == Player.None && status[combination[1]].player == Player.None
        }

        private fun checkIfWon(listOfCombinations: List<Int>, status: List<PlayerState>): Boolean {
            return (status[listOfCombinations[0]].player == status[listOfCombinations[1]].player && status[listOfCombinations[1]].player == status[listOfCombinations[2]].player)
        }
    }
}
