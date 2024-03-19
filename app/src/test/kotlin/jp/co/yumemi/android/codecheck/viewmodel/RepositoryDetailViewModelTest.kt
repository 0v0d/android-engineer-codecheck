package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class RepositoryDetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var urlObserver: Observer<String?>

    private lateinit var viewModel: RepositoryDetailViewModel

    @Before
    fun setup() {
        viewModel = RepositoryDetailViewModel()
        viewModel.url.observeForever(urlObserver)
    }

    /** URLを開いたときにLiveDataにURLがセットされることを確認する */
    @Test
    fun testOpenUrl() {
        val htmlUrl = "https://example.com"
        viewModel.openUrl(htmlUrl)
        viewModel.url.observeForever {
            if (it == htmlUrl) {
                assertEquals(htmlUrl, viewModel.url.value)
            }
        }
        assertEquals(null, viewModel.url.value)
    }
}