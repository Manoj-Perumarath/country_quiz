package com.manoj.countryquiz.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manoj.countryquiz.data.constants.AppConstants
import com.manoj.countryquiz.data.model.QuestionsResponse
import com.manoj.countryquiz.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {
    private val _items = MutableLiveData<QuestionsResponse>()
    val items: LiveData<QuestionsResponse> get() = _items
    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> = _timeLeft

    private val _questionTimeLeft = MutableLiveData<String>()
    val questionTimeLeft: LiveData<String> = _questionTimeLeft

    private val _answerTimeLeft = MutableLiveData<String>()
    val answerTimeLeft: LiveData<String> = _answerTimeLeft

    val _selectedAnswer = MutableLiveData<String>("")
    val selectedAnswer: LiveData<String> = _selectedAnswer
    private val _continueQuiz = MutableLiveData<Boolean>()
    val continueQuiz: LiveData<Boolean> = _continueQuiz

    val selectedOption = MutableLiveData<String>()

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private var timer: CountDownTimer? = null
    private var questionTimer: CountDownTimer? = null
    private var answerTimer: CountDownTimer? = null
    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress: LiveData<Boolean> = _isProgress
    private val _questionnaireTime = MutableLiveData<Long>()
    val questionnaireTime: LiveData<Long> = _questionnaireTime
    private val _remainingTime = MutableLiveData<Long>(0)

    init {
        loadItems()
        getQuestionnaireTime()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _items.value = dataRepository.loadItemsFromJson()
        }
    }

    private fun getQuestionnaireTime() {
        viewModelScope.launch {
            _questionnaireTime.value = dataRepository.getQuestionnaireTime()
        }
    }

    fun startCountDown() {
        timer?.cancel()
        timer = object : CountDownTimer(AppConstants.DefaultTime.CHALLENGE_START_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                _timeLeft.value = String.format(Locale.ENGLISH, "%02d", seconds)
            }

            override fun onFinish() {
//                _timeLeft.value = "0"
            }
        }
        timer?.start()
    }

    fun checkQuizExpired() {
        viewModelScope.launch(Dispatchers.IO) {
            val questionnaireTime = dataRepository.getQuestionnaireTime()
            val questionnaireStartTime = dataRepository.getQuestionnaireRealTime()
            Log.d("qT", questionnaireTime.toString())
            val totalTimeForQuestions = 15 * (10000 + questionnaireTime)
            val timeToFinish = totalTimeForQuestions + questionnaireStartTime
            val currentRealtime = System.currentTimeMillis()
            Log.d("time", timeToFinish.toString() + "rema" + currentRealtime)
            if (timeToFinish > currentRealtime) {
                _continueQuiz.postValue(true)
                val questionNumber =
                    (currentRealtime - questionnaireStartTime) / (questionnaireTime + 10000)
                val remainingTime =
                    ((questionnaireTime + 10000) * (questionNumber + 1)) - (currentRealtime - questionnaireStartTime)
                Log.d("questions can be continued", "progress $remainingTime")
                if (remainingTime > 10000) {
                    _remainingTime.postValue(remainingTime - 10000)
                    _currentIndex.postValue(questionNumber.toInt())
                } else {
                    _currentIndex.postValue(questionNumber.toInt() + 1)
                }
            } else {
                _continueQuiz.postValue(false)
                Log.d("questions finished", "stop")
                saveQuestionnaireProgress(false)
            }
        }
    }

    fun saveQuestionTime(questionTime: Long) {
        viewModelScope.launch {
            dataRepository.saveQuestionTime(questionTime)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        questionTimer?.cancel()
        answerTimer?.cancel()
    }

    fun startQuestionCounter() {
        questionTimer?.cancel()
        questionTimer =
            object : CountDownTimer(
                if (_remainingTime.value != 0L) _remainingTime.value!! else questionnaireTime.value!!,
                1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    _questionTimeLeft.value = String.format(Locale.ENGLISH, "%02d", seconds)
                }

                override fun onFinish() {
                    _remainingTime.value = 0L
//                _timeLeft.value = "0"
                }
            }
        questionTimer?.start()
    }

    fun cancelQuestionCounter() {
        questionTimer?.cancel()
    }

    fun startAnswerValidatorCounter() {
        answerTimer?.cancel()
        answerTimer =
            object : CountDownTimer(AppConstants.DefaultTime.CHALLENGE_ANSWER_TIME, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    _answerTimeLeft.value = String.format(Locale.ENGLISH, "%02d", seconds)
                }

                override fun onFinish() {
//                _timeLeft.value = "0"
                }
            }
        answerTimer?.start()
    }

    fun incrementQuestionIndex() {
        _currentIndex.value?.let { i ->
            _currentIndex.value = i + 1
        }
    }

    fun setSelectedAnswer(selectedAnswer: String) {
        _selectedAnswer.value = selectedAnswer
    }

    fun onOptionSelected(group: RadioGroup, checkedId: Int) {
        val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
        selectedOption.value = selectedRadioButton?.tag?.toString()
    }

    fun saveQuestionnaireProgress(progress: Boolean) {
        viewModelScope.launch {
            dataRepository.saveQuestionnaireProgress(progress)
        }
    }

    fun getQuestionnaireProgress() {
        viewModelScope.launch {
            _isProgress.value = dataRepository.getQuestionnaireProgress()
        }
    }

    fun calculateScore(isRight: Boolean) {
        if (isRight) {
            _score.value?.let { i ->
                _score.value = i + 1
            }
        }
    }
}