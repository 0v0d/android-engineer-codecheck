package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.model.api.APIGitHubResponse
import jp.co.yumemi.android.codecheck.model.domain.RepositoryItem
import jp.co.yumemi.android.codecheck.model.api.toDomainModel
import jp.co.yumemi.android.codecheck.usecase.SearchRepositoriesUseCase
import jp.co.yumemi.android.codecheck.state.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Date
import javax.inject.Inject

/**
 * リポジトリーリスト画面のViewModel
 * @param searchRepositoriesUseCase リポジトリー検索UseCase
 */
@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase
) : ViewModel() {
    private val _lastSearchDate = MutableStateFlow(Date())

    /** 最後に検索した日時 */
    val lastSearchDate = _lastSearchDate.asStateFlow()

    private val _repositoryItems = MutableStateFlow<List<RepositoryItem>>(emptyList())

    /** リポジトリーの検索結果 */
    val repositoryItems = _repositoryItems.asStateFlow()

    private val _searchState = MutableStateFlow(SearchState.LOADING)

    /** 検索状態 */
    val searchState = _searchState.asStateFlow()

    /** 検索キーワード */
    private val _searchEvent = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchEvent.collect { query ->
                performSearch(query)
            }
        }
    }

    /**
     * リポジトリーの検索
     * @param inputText 検索キーワード
     */
    fun searchRepositories(inputText: String) {
        viewModelScope.launch {
            _searchEvent.emit(inputText)
        }
    }

    /**
     * 検索キーワードをセットする
     * @param query 検索キーワード
     */
    private suspend fun performSearch(query: String) {
        _searchState.value = SearchState.LOADING
        val response = searchRepositoriesUseCase.execute(query)
        handleResponse(response)
    }

    /**
     * APIから検索結果を取得する
     * @param response 検索結果
     */
    private fun handleResponse(response: Response<APIGitHubResponse>?) {
        if (response?.body() == null) {
            _searchState.value = SearchState.NETWORK_ERROR
            return
        }
        val repositories = response.body()?.items?.map { it.toDomainModel() }
        val isResultEmpty = repositories.isNullOrEmpty()

        if (response.isSuccessful && !isResultEmpty) {
            _searchState.value = SearchState.SUCCESS
            _lastSearchDate.value = Date()

            _repositoryItems.value = repositories ?: emptyList()
        } else {
            _searchState.value = SearchState.EMPTY_RESULT
        }
    }

    /** 検索結果を再取得する */
    fun retrySearch() {
        viewModelScope.launch {
            _searchEvent.value.let { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
            }
        }
    }
}