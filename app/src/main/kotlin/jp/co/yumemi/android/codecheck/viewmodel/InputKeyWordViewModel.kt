/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** キーワード入力画面のViewModel */
class InputKeyWordViewModel : ViewModel() {
    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    /** 入力されたテキストをセット
     * @param inputText 入力されたテキスト
     */
    fun setKeyword(inputText: String) {
        _keyword.value = inputText
    }

    /** ナビゲーションが完了した時の処理 */
    fun onNavigateComplete() {
        _keyword.value = ""
    }
}