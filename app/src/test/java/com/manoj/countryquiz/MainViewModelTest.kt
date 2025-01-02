package com.manoj.countryquiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.manoj.countryquiz.data.model.QuestionsResponse
import com.manoj.countryquiz.data.repository.DataRepository
import com.manoj.countryquiz.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val mockDataRepository: DataRepository = mock()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(mockDataRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }

    @Test
    fun `loadItems update items LiveData`() = runTest {
        val mockResponse = QuestionsResponse(emptyList())
        whenever(mockDataRepository.loadItemsFromJson()).thenReturn(mockResponse)

        viewModel.loadItems()

        assertEquals(mockResponse, viewModel.items.getOrAwaitValue())
    }

    @Test
    fun `checkQuizExpired should update continueQuiz`() = runTest {
        whenever(mockDataRepository.getQuestionnaireTime()).thenReturn(10_000L)
        whenever(mockDataRepository.getQuestionnaireRealTime()).thenReturn(System.currentTimeMillis() - 50_000)

        viewModel.checkQuizExpired()

        assertEquals(true, viewModel.continueQuiz.getOrAwaitValue())
    }

    @Test
    fun `increment QuestionIndex increases currentIndex`() {
        viewModel.incrementQuestionIndex()
        assertEquals(1, viewModel.currentIndex.getOrAwaitValue())
    }

    @Test
    fun `calculateScore increments score when answer is correct`() {
        viewModel.calculateScore(true)
        assertEquals(1, viewModel.score.getOrAwaitValue())
    }
}

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    return data ?: throw KotlinNullPointerException("No value was captured.")
}
