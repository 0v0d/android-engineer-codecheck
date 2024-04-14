package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** リポジトリ詳細画面のViewModel */
class RepositoryDetailViewModel : ViewModel() {
    private val _url = MutableStateFlow<String?>(null)
    val url = _url.asStateFlow()

    fun openUrl(htmlUrl: String) {
        _url.value = htmlUrl
    }

    fun clearUrl() {
        _url.value = null
    }
}