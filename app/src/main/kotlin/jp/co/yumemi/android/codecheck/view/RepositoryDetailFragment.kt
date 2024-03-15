/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryDetailBinding

/** リポジトリ詳細画面 */
class RepositoryDetailFragment : Fragment(R.layout.fragment_repository_detail) {

    private val args: RepositoryDetailFragmentArgs by navArgs()

    /**
     * ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", args.lastSearchDate)
        val binding = FragmentRepositoryDetailBinding.bind(view)
        val repositoryItem = args.repositoryItem

        binding.ownerIconView.load(repositoryItem.ownerIconUrl)
        binding.nameView.text = repositoryItem.name
        binding.languageView.text = getString(R.string.written_language, repositoryItem.language)
        binding.starsView.text = getString(R.string.stars, repositoryItem.stargazersCount)
        binding.watchersView.text = getString(R.string.watchers, repositoryItem.watchersCount)
        binding.forksView.text = getString(R.string.forks, repositoryItem.forksCount)
        binding.openIssuesView.text =
            getString(R.string.open_issues, repositoryItem.openIssuesCount)
    }
}
