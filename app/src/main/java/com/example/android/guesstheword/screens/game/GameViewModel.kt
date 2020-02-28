package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel:ViewModel() {

    // The current word
    private var _word = MutableLiveData<String>()
    val word:LiveData<String> get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score:LiveData<Int>get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished:LiveData<Boolean> get() = _eventGameFinished

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long> get() = _currentTime

    val currentTimeString = Transformations.map(currentTime){time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer : CountDownTimer

    fun onGameFinished()
    {
        _eventGameFinished.value = true
    }

    fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        //score--
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        //score++
        _score.value = score.value?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    fun nextWord() {
        if (wordList.isEmpty()) {

            //onGameFinished()
            resetList()
        }
        else{
            //Select and remove a word from the list
            //word = wordList.removeAt(0)
            _word.value = wordList.removeAt(0)
        }
    }


    /** Methods for updating the UI **/

    init {
        _word.value = ""
        _score.value =0
        resetList()
        nextWord()
        Log.i("GameViewModel", "GameViewModel created!")

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinished()
            }

            override fun onTick(millisUntilFinished: Long) {

                _currentTime.value = millisUntilFinished/ ONE_SECOND
            }

        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun onGameFinishedComplete()
    {
        _eventGameFinished.value = false
    }

    companion object {

        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L

    }
}