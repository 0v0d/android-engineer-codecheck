/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.viewmodel.SearchRepositoryViewModel
import jp.co.yumemi.android.codecheck.adapter.RepositoryListAdapter
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchRepositoryBinding
import jp.co.yumemi.android.codecheck.model.RepositoryItem
import java.util.Date

/** リポジトリー検索画面 */
@AndroidEntryPoint
class SearchRepositoryFragment : Fragment(R.layout.fragment_search_repository) {
    private val viewModel: SearchRepositoryViewModel by viewModels()

    /**
     * ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchRepositoryBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val repositoryItemClickListener = { it: RepositoryItem ->
            navigateToRepositoryFragment(it)
        }
        val adapter = RepositoryListAdapter(repositoryItemClickListener)

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                val text = editText.text.toString()
                viewModel.searchRepositories(text)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.repositoryRecyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.repositoryItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    /**
     * リポジトリー詳細画面へ遷移する
     * @param repositoryItem リポジトリーアイテム
     */
    private fun navigateToRepositoryFragment(repositoryItem: RepositoryItem) {
        val date = viewModel.lastSearchDate.value ?: Date()
        val action =
            SearchRepositoryFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(
                repositoryItem,
                date.toString()
            )
        findNavController().navigate(action)
    }
}