package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class InputKeyWordViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var keywordObserver: Observer<String>

    @Mock
    private lateinit var showErrorObserver: Observer<Boolean>

    private lateinit var viewModel: InputKeyWordViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = InputKeyWordViewModel()
        viewModel.keyword.observeForever(keywordObserver)
        viewModel.showError.observeForever(showErrorObserver)
    }

    @After
    fun tearDown() {
        viewModel.keyword.removeObserver(keywordObserver)
        viewModel.showError.removeObserver(showErrorObserver)
    }

    /** 入力が空でないことを確認する */
    @Test
    fun testValidateKeyword_ValidInput() {
        val inputText = "Hello"
        viewModel.validateKeyword(inputText)

        assertEquals(inputText, viewModel.keyword.value)
        assertFalse(viewModel.showError.value ?: true)
    }

    /** 入力が空であることを確認する */
    @Test
    fun testValidateKeyword_InvalidInput() {
        val inputText = ""
        viewModel.validateKeyword(inputText)

        assertTrue(viewModel.showError.value ?: false)
    }

    /** キーワードの入力値をクリアできていることを確認する */
    @Test
    fun testOnNavigateComplete() {
        val inputText = ""
        viewModel.validateKeyword(inputText)
        viewModel.onNavigateComplete()

        assertEquals("", viewModel.keyword.value)
    }

    /** 入力が空でないことを確認する */
    @Test
    fun testIsInputValid_ValidInput() {
        val inputText = "Hello"
        val isValid = viewModel.isInputValid(inputText)

        assertTrue(isValid)
    }

    /** 入力が空であることを確認する */
    @Test
    fun testIsInputValid_InvalidInput() {
        val inputText = ""
        val isValid = viewModel.isInputValid(inputText)

        assertFalse(isValid)
    }
}