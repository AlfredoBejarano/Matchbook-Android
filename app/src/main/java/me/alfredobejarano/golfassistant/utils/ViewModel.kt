package me.alfredobejarano.golfassistant.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * Executes a suspend function in the [Main thread][Main] and returns the result of the suspend
 * function execution in a  [LiveData] object.
 * @param block The function to execute.
 * @param T Type of result to be reported.
 */
fun <T> ViewModel.execute(block: suspend () -> T) = liveData { emit(block()) }

/**
 * Executes a suspend function in the [IO thread][IO] and returns the result of the suspend
 * function execution in a  [LiveData] object.
 * @param block The function to execute.
 * @param T Type of result to be reported.
 */
fun <T> ViewModel.ioExecute(block: suspend () -> T) = liveData(IO) { emit(block()) }