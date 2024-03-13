/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.codecheck.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryDetailBinding

/** リポジトリ詳細画面 */
class RepositoryDetailFragment : Fragment(R.layout.fragment_repository_detail) {

    private val args: RepositoryDetailFragmentArgs by navArgs()
    private var _binding: FragmentRepositoryDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        _binding = FragmentRepositoryDetailBinding.bind(view)

        val repositoryItem = args.repositoryItem

        binding.ownerIconView.load(repositoryItem.ownerIconUrl)
        binding.nameView.text = repositoryItem.name
        binding.languageView.text = repositoryItem.language
        binding.starsView.text = "${repositoryItem.stargazersCount} stars"
        binding.watchersView.text = "${repositoryItem.watchersCount} watchers"
        binding.forksView.text = "${repositoryItem.forksCount} forks"
        binding.openIssuesView.text = "${repositoryItem.openIssuesCount} open issues"
    }
}
