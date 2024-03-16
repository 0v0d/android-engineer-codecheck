/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Date
import javax.inject.Inject

/** リポジトリーの検索結果を返すためのViewModel */
@HiltViewModel
class SearchRepositoryViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {
    private val _lastSearchDate = MutableLiveData<Date>()
    val lastSearchDate: LiveData<Date> = _lastSearchDate

    private val _repositoryItems = MutableLiveData<List<RepositoryItem>>()
    val repositoryItems: LiveData<List<RepositoryItem>> = _repositoryItems

    /**
     * リポジトリーの検索
     * @param inputText 検索キーワード
     */
    fun searchRepositories(inputText: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.searchRepositoriesData(inputText)
                }
                handleResponse(response)
            } catch (e: Exception) {
                handleSearchError(e.toString())
            }
        }
    }

    /**
     * APIから検索結果を取得する
     * @param response 検索結果
     */
    private fun handleResponse(response: Response<GitHubResponse>?) {
        if (response?.body() == null) {
            handleSearchError("response is null")
            return
        }

        val isResultEmpty = response.body()?.items?.isEmpty() ?: true

        if (response.isSuccessful && !isResultEmpty) {
            _lastSearchDate.postValue(Date())
            _repositoryItems.postValue(response.body()?.items)
        } else {
            handleSearchError("response is empty")
        }
    }

    private fun handleSearchError(exception: String) {
        Log.e("SearchRepositoryViewModel", exception)
    }
}