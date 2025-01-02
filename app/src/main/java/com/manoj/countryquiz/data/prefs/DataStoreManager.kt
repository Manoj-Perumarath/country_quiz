package com.manoj.countryquiz.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    private val QUESTION_TIME = longPreferencesKey("question_time")
    private val QUESTION_REAL_START_TIME = longPreferencesKey("question_real_start_time")

    private val QUESTIONNAIRE_IN_PROGRESS = booleanPreferencesKey("question_progress")


    suspend fun saveQuestionTime(questionTime: Long) {
        val startTime = System.currentTimeMillis()
        dataStore.edit { preferences ->
            preferences[QUESTION_TIME] = questionTime
            preferences[QUESTION_REAL_START_TIME] = startTime
        }
    }

    suspend fun saveQuestionnaireProgress(inProgress: Boolean) {
        dataStore.edit { preferences ->
            preferences[QUESTIONNAIRE_IN_PROGRESS] = inProgress
        }
    }

//    val questionTimeFlow: Flow<Long> = dataStore.data
//        .map { preferences ->
//            preferences[QUESTION_TIME] ?: ""
//        }

    suspend fun getQuestionTime(): Long {
        val preferences = dataStore.data.first()
        return preferences[QUESTION_TIME] ?: 20000L
    }

    suspend fun getQuestionnaireProgress(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[QUESTIONNAIRE_IN_PROGRESS] ?: false
    }

    suspend fun getQuestionRealStartTime(): Long {
        val preferences = dataStore.data.first()
        return preferences[QUESTION_REAL_START_TIME] ?: System.currentTimeMillis()
    }
}