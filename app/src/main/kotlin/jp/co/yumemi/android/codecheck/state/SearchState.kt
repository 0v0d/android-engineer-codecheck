package jp.co.yumemi.android.codecheck.state

/** 検索の状態を表すenum */
enum class SearchState {
    SUCCESS,
    NETWORK_ERROR,
    EMPTY_RESULT,
    LOADING
}