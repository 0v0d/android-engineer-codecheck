package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import jp.co.yumemi.android.codecheck.model.GitHubResponse
import jp.co.yumemi.android.codecheck.model.OwnerItem
import jp.co.yumemi.android.codecheck.model.RepositoryItem
import jp.co.yumemi.android.codecheck.repository.GithubRepository
import jp.co.yumemi.android.codecheck.state.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RepositoryListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RepositoryListViewModel

    @Mock
    private lateinit var repository: GithubRepository

    @Mock
    private lateinit var searchStateObserver: Observer<SearchState>

    @Mock
    private lateinit var repositoryItemsObserver: Observer<List<RepositoryItem>>

    private val response = GitHubResponse(
        listOf(
            RepositoryItem(
                3432266,
                "kotlin",
                "JetBrains/kotlin",
                OwnerItem(
                    "JetBrains",
                    "https://avatars.githubusercontent.com/u/878437?v=4",
                    "https://github.com/JetBrains",
                ),
                "https://github.com/JetBrains/kotlin",
                "The Kotlin Programming Language. ",
                "Kotlin",
                "47238",
                "2337",
                "5575",
                "171"
            )
        )
    )

    private val emptyResponse = GitHubResponse(
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = RepositoryListViewModel(repository)
        viewModel.searchState.observeForever(searchStateObserver)
        viewModel.repositoryItems.observeForever(repositoryItemsObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()

        viewModel.repositoryItems.removeObserver(repositoryItemsObserver)
        viewModel.searchState.removeObserver(searchStateObserver)
    }

    /** リポジトリ検索成功時のテスト */
    @Test
    fun searchRepositoriesSuccessResponse() =
        runTest {
            val keyword = "kotlin"
            `when`(repository.searchRepositoriesData(keyword)).thenReturn(Response.success(response))

            viewModel.repositoryItems.observeForever {
                if (it == response.items) {
                    assertEquals(response.items, viewModel.repositoryItems.value)
                }
            }

            viewModel.searchState.observeForever {
                if (it == SearchState.SUCCESS) {
                    assertEquals(SearchState.SUCCESS, viewModel.searchState.value)
                }
            }
            viewModel.searchRepositories(keyword)
        }

    /** リポジトリ検索失敗時のテスト */
    @Test
    fun searchRepositoriesNetWorkErrorResponse() =
        runTest {
            val keyword = "kotlin"
            val response = Response.error<GitHubResponse>(500, ResponseBody.create(null, ""))
            `when`(repository.searchRepositoriesData(keyword)).thenReturn(response)

            viewModel.searchState.observeForever {
                if (it == SearchState.NETWORK_ERROR) {
                    assertEquals(SearchState.NETWORK_ERROR, viewModel.searchState.value)
                }
            }
            viewModel.searchRepositories(keyword)
        }

    /** リポジトリ検索結果が空の場合のテスト */
    @Test
    fun searchRepositoriesEmptyResponse() =
        runTest {
            val keyword = "kotlin"
            `when`(repository.searchRepositoriesData(keyword)).thenReturn(
                Response.success(
                    emptyResponse
                )
            )

            viewModel.repositoryItems.observeForever {
                if (it == emptyResponse.items) {
                    assertEquals(emptyResponse.items.isEmpty(), viewModel.repositoryItems.value)
                }
            }

            viewModel.searchState.observeForever {
                if (it == SearchState.EMPTY_RESULT) {
                    assertEquals(SearchState.EMPTY_RESULT, viewModel.searchState.value)
                }
            }
            viewModel.searchRepositories(keyword)
        }
}
