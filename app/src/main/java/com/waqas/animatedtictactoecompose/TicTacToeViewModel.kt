package com.waqas.animatedtictactoecompose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.prefs.Preferences
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel @Inject constructor(
    private val data: Pair<String,String>
): ViewModel() {
}