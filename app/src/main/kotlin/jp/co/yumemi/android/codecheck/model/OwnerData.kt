package jp.co.yumemi.android.codecheck.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * リポジトリのオーナー情報を保持するデータクラス
 * @param login ログイン名
 * @param avatarUrl アバターのURL
 * @param htmlUrl GitHubのURL
 */
@Parcelize
data class OwnerItem(
    @Json(name = "login")
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "html_url")
    val htmlUrl: String
) : Parcelable