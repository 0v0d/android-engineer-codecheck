package jp.co.yumemi.android.codecheck.model.response

import android.os.Parcelable
import com.squareup.moshi.Json
import jp.co.yumemi.android.codecheck.model.domain.OwnerItem
import kotlinx.parcelize.Parcelize

/**
 * リポジトリのオーナー情報を保持するデータクラス
 * @param login ログイン名
 * @param avatarUrl アバターのURL
 * @param htmlUrl GitHubのURL
 */
@Parcelize
data class APIOwnerItem(
    @Json(name = "login")
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "html_url")
    val htmlUrl: String
) : Parcelable

/** APIOwnerItemをドメインモデルに変換する */
fun APIOwnerItem.toDomainModel() = OwnerItem(
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl
)