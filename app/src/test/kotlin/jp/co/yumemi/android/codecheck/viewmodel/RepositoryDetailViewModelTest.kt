package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

@ExperimentalCoroutinesApi
class RepositoryDetailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RepositoryDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = RepositoryDetailViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** URLを開いたときにLiveDataにURLがセットされることを確認する */
    @Test
    fun testOpenUrl() = runTest {
        val url = "https://example.com"
        viewModel.openUrl(url)
        assertEquals(url, viewModel.url.value)
    }
}