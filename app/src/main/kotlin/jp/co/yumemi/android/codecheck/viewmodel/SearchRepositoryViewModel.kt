/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import jp.co.yumemi.android.codecheck.model.RepositoryItem
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

/** リポジトリーの検索結果を返すためのViewModel */
class SearchRepositoryViewModel : ViewModel() {
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
                val response = executeSearchQuery(inputText)
                val repositoryItems = parseResponse(response)
                updateSearchResults(repositoryItems)
            } catch (e: Exception) {
                handleSearchError(e)
            }
        }
    }

    private suspend fun executeSearchQuery(inputText: String): HttpResponse {
        val client = HttpClient(Android)
        return client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }
    }

    private suspend fun parseResponse(response: HttpResponse): List<RepositoryItem> {
        val jsonBody = JSONObject(response.receive<String>())
        val jsonItems = jsonBody.optJSONArray("items") ?: JSONArray()
        val repositoryItems = mutableListOf<RepositoryItem>()
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.optJSONObject(i) ?: continue
            val name = jsonItem.optString("full_name")
            val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: ""
            val language = jsonItem.optString("language")
            val stargazersCount = jsonItem.optLong("stargazers_count")
            val watchersCount = jsonItem.optLong("watchers_count")
            val forksCount = jsonItem.optLong("forks_count")
            val openIssuesCount = jsonItem.optLong("open_issues_count")
            repositoryItems.add(
                RepositoryItem(
                    name = name,
                    ownerIconUrl = ownerIconUrl,
                    language = language,
                    stargazersCount = stargazersCount,
                    watchersCount = watchersCount,
                    forksCount = forksCount,
                    openIssuesCount = openIssuesCount
                )
            )
        }
        return repositoryItems
    }

    private fun updateSearchResults(repositoryItems: List<RepositoryItem>) {
        _lastSearchDate.postValue(Date())
        _repositoryItems.postValue(repositoryItems)
    }

    private fun handleSearchError(e: Exception) {
        Log.d("SearchRepositoryViewModel", "searchRepositories failed", e)
    }
}