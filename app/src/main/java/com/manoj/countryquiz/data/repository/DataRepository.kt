package com.manoj.countryquiz.data.repository

import com.manoj.countryquiz.data.model.QuestionsResponse

interface DataRepository {
    suspend fun loadItemsFromJson(): QuestionsResponse

    suspend fun saveQuestionTime(questionTime: Long)

    suspend fun getQuestionnaireTime(): Long

    suspend fun getQuestionnaireRealTime(): Long

    suspend fun saveQuestionnaireProgress(progress: Boolean)

    suspend fun getQuestionnaireProgress(): Boolean
}