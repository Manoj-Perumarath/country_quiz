package com.manoj.countryquiz.data.repositoryimpl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manoj.countryquiz.data.model.QuestionsResponse
import com.manoj.countryquiz.data.prefs.DataStoreManager
import com.manoj.countryquiz.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(val context: Context) : DataRepository {

    private val dataStoreManager = DataStoreManager(context)

//    val userNameFlow: Flow<String> = dataStoreManager.questionTimeFlow

    override suspend fun loadItemsFromJson(): QuestionsResponse =
        withContext(Dispatchers.IO) {
            val jsonString =
                context.assets.open("questions.json").bufferedReader().use { it.readText() }
            val itemType = object : TypeToken<QuestionsResponse>() {}.type
            Gson().fromJson(jsonString, itemType)
        }

    override suspend fun saveQuestionTime(questionTime: Long) {
        withContext(Dispatchers.IO) {
            dataStoreManager.saveQuestionTime(questionTime)
        }
    }

    override suspend fun getQuestionnaireTime(): Long =
        withContext(Dispatchers.IO) {
            dataStoreManager.getQuestionTime()
        }

    override suspend fun getQuestionnaireRealTime(): Long =
        withContext(Dispatchers.IO) {
            return@withContext dataStoreManager.getQuestionRealStartTime()
        }

    override suspend fun saveQuestionnaireProgress(progress: Boolean) {
        withContext(Dispatchers.IO) {
            dataStoreManager.saveQuestionnaireProgress(progress)
        }
    }

    override suspend fun getQuestionnaireProgress(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext dataStoreManager.getQuestionnaireProgress()
        }

}
