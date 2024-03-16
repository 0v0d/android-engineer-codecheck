package jp.co.yumemi.android.codecheck.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * GitHubのリポジトリ情報を保持するデータクラス
 * @param items リポジトリ情報
 */
@Parcelize
data class GitHubResponse(
    @Json(name = "items")
    val items: List<RepositoryItem>
) : Parcelable