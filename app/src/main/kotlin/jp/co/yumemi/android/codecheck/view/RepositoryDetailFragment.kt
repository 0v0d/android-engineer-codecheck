/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.view

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentRepositoryDetailBinding
import jp.co.yumemi.android.codecheck.viewmodel.RepositoryDetailViewModel
import kotlinx.coroutines.launch

/** リポジトリ詳細画面 */
class RepositoryDetailFragment : Fragment(R.layout.fragment_repository_detail) {
    private val args: RepositoryDetailFragmentArgs by navArgs()
    private val viewModel: RepositoryDetailViewModel by viewModels()

    /**
     * ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", args.lastSearchDate)
        val binding = FragmentRepositoryDetailBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.repositoryItem = args.repositoryItem
        observeUrl()
    }

    /** urlの変更を監視 */
    private fun observeUrl() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.url.collect {
                    if (it != null) {
                        openWebBrowser(it)
                    }
                }
            }
        }
    }

    /**
     * WebViewでURLを開く
     * @param url 開くURL
     */
    private fun openWebBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.setComponent(
            ComponentName(
                "com.android.chrome",
                "com.google.android.apps.chrome.Main"
            )
        )
        startActivity(intent)
        viewModel.clearUrl()
    }
}
