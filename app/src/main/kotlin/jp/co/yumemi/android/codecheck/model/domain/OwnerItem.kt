package jp.co.yumemi.android.codecheck.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * オーナーのドメインモデル
 * @param login ログイン名
 * @param avatarUrl アバターのURL
 * @param htmlUrl GitHubのURL
 */
@Parcelize
data class OwnerItem(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
) : Parcelable