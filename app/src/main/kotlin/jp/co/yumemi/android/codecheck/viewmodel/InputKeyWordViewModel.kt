/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** キーワード入力画面のViewModel */
class InputKeyWordViewModel : ViewModel() {
    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> = _keyword

    private val _showError = MutableLiveData<Boolean>()
    val showError: LiveData<Boolean> = _showError

    /** キーワードのバリデーション
     * @param inputText 入力されたテキスト
     */
    fun validateKeyword(inputText: String) {
        val isValid = isInputValid(inputText)
        _showError.value = !isValid

        if (isValid) {
            _keyword.value = inputText
        }
    }

    /** ナビゲーションが完了した時の処理 */
    fun onNavigateComplete() {
        _keyword.value = ""
    }

    /** 入力されたテキストが有効かどうか
     * @param inputText 入力されたテキスト
     * @return 有効ならtrue
     */
    fun isInputValid(inputText: String): Boolean {
        return inputText.isNotEmpty()
    }
}