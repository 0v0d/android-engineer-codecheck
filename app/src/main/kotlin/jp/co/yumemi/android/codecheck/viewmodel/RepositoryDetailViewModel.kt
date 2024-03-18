package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** リポジトリ詳細画面のViewModel */
class RepositoryDetailViewModel : ViewModel() {
    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> get() = _url

    /**
     * URLを開く
     * @param htmlUrl URL
     */
    fun openUrl(htmlUrl: String) {
        _url.value = htmlUrl
        // 画面を回転させたときにURLを開き続けないようにnullをセットする
        _url.value = null
    }
}