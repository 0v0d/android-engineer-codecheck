package jp.co.yumemi.android.codecheck.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.co.yumemi.android.codecheck.model.api.APIGitHubResponse
import jp.co.yumemi.android.codecheck.model.api.APIOwnerItem
import jp.co.yumemi.android.codecheck.model.api.APIRepositoryItem
import jp.co.yumemi.android.codecheck.model.api.toDomainModel
import jp.co.yumemi.android.codecheck.model.domain.OwnerItem
import jp.co.yumemi.android.codecheck.model.domain.RepositoryItem
import jp.co.yumemi.android.codecheck.repository.GithubRepository
import jp.co.yumemi.android.codecheck.state.SearchState
import jp.co.yumemi.android.codecheck.usecase.SearchRepositoriesUseCase
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RepositoryListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RepositoryListViewModel

    @Mock
    private lateinit var useCase: SearchRepositoriesUseCase

    private val response = APIGitHubResponse(
        listOf(
            APIRepositoryItem(
                3432266,
                "kotlin",
                "JetBrains/kotlin",
                APIOwnerItem(
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


    private val emptyResponse = APIGitHubResponse(
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = RepositoryListViewModel(useCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()

    }

    /** リポジトリ検索成功時のテスト */
    @Test
    fun searchRepositoriesSuccessResponse() = runTest {
        val keyword = "kotlin"
        `when`(useCase.execute(keyword)).thenReturn(Response.success(response))

        viewModel.searchRepositories(keyword)

        verify(useCase).execute(keyword)
        val responseItems = response.items.map { it.toDomainModel() }
        assertEquals(responseItems, viewModel.repositoryItems.value)
        assertEquals(SearchState.SUCCESS, viewModel.searchState.value)
    }

    /** リポジトリ検索失敗時のテスト */
    @Test
    fun searchRepositoriesNetWorkErrorResponse() =
        runTest {
            val keyword = "kotlin"
            val response = Response.error<APIGitHubResponse>(500, ResponseBody.create(null, ""))
            `when`(useCase.execute(keyword)).thenReturn(response)

            assertEquals(SearchState.NETWORK_ERROR, viewModel.searchState.value)
            viewModel.searchRepositories(keyword)
        }

    /** リポジトリ検索結果が空の場合のテスト */
    @Test
    fun searchRepositoriesEmptyResponse() = runTest {
        val keyword = "kotlin"
        `when`(useCase.execute(keyword)).thenReturn(Response.success(emptyResponse))

        viewModel.searchRepositories(keyword)

        assertEquals(emptyList<RepositoryItem>(), viewModel.repositoryItems.value)
        assertEquals(SearchState.EMPTY_RESULT, viewModel.searchState.value)
    }
}