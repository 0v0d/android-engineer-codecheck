package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InputKeyWordViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: InputKeyWordViewModel

    @Before
    fun setup() {
        viewModel = InputKeyWordViewModel()
    }

    /** 入力が空でないことを確認する */
    @Test
    fun testValidateKeyword_ValidInput() {
        val inputText = "Hello"
        viewModel.setKeyword(inputText)

        assertEquals(inputText, viewModel.keyword.value)
    }

    /** キーワードの入力値をクリアできていることを確認する */
    @Test
    fun testOnNavigateComplete() {
        val inputText = ""
        viewModel.setKeyword(inputText)
        viewModel.onNavigateComplete()

        assertEquals("", viewModel.keyword.value)
    }
}