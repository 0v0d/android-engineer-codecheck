package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** リポジトリ詳細画面のViewModel */
class RepositoryDetailViewModel : ViewModel() {
    private val _url = MutableStateFlow<String?>(null)
    val url: StateFlow<String?> = _url

    fun openUrl(htmlUrl: String) {
        _url.value = htmlUrl
    }

    fun clearUrl() {
        _url.value = null
    }
}