package jp.co.yumemi.android.codecheck.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.model.GitHubResponse
import jp.co.yumemi.android.codecheck.model.RepositoryItem
import jp.co.yumemi.android.codecheck.repository.GithubRepository
import jp.co.yumemi.android.codecheck.state.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Date
import javax.inject.Inject

/**
 * リポジトリーリスト画面のViewModel
 * @param repository リポジトリー
 */
@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {
    private val _lastSearchDate = MutableLiveData<Date>()

    /** 最後に検索した日時 */
    val lastSearchDate: LiveData<Date> = _lastSearchDate

    private val _repositoryItems = MutableLiveData<List<RepositoryItem>>()

    /** リポジトリーの検索結果 */
    val repositoryItems: LiveData<List<RepositoryItem>> = _repositoryItems

    private val _searchState = MutableLiveData<SearchState>()

    /** 検索状態 */
    val searchState: LiveData<SearchState> = _searchState

    /** 検索キーワード */
    private lateinit var inputKeyWord: String

    /**
     * リポジトリーの検索
     * @param inputText 検索キーワード
     */
    fun searchRepositories(inputText: String) {
        _searchState.value = SearchState.LOADING
        viewModelScope.launch {
            try {
                inputKeyWord = inputText
                performSearch(inputKeyWord)
            } catch (e: Exception) {
                handleSearchError(e.toString())
            }
        }
    }

    /**
     * APIから検索結果を取得する
     * @param inputText 検索キーワード
     */
    private suspend fun performSearch(inputText: String) {
        val response = withContext(Dispatchers.IO) {
            repository.searchRepositoriesData(inputText)
        }
        handleResponse(response)
    }

    /**
     * APIから検索結果を取得する
     * @param response 検索結果
     */
    private fun handleResponse(response: Response<GitHubResponse>?) {
        if (response?.body() == null) {
            _searchState.postValue(SearchState.NETWORK_ERROR)
            handleSearchError("response is null")
            return
        }

        val isResultEmpty = response.body()?.items?.isEmpty() ?: true

        if (response.isSuccessful && !isResultEmpty) {
            _searchState.postValue(SearchState.SUCCESS)
            _lastSearchDate.postValue(Date())
            _repositoryItems.postValue(response.body()?.items)
            Log.d("RepositoryListViewModel", "response is successful")
        } else {
            _searchState.postValue(SearchState.EMPTY_RESULT)
            handleSearchError("response is empty")
        }
    }

    /** 検索結果を再取得する */
    fun retrySearch() {
        if (inputKeyWord.isEmpty()) {
            return
        }
        _searchState.value = SearchState.LOADING
        searchRepositories(inputKeyWord)
    }

    /**
     * エラーを表示する
     * @param exception エラーメッセージ
     */
    private fun handleSearchError(exception: String) {
        Log.e("RepositoryListViewModel", exception)
    }
}