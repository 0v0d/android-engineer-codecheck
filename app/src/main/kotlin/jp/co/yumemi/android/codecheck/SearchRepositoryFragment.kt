/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchRepositoryBinding
import java.util.Date

/** リポジトリー検索画面 */
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
        val adapter = RepositoryListAdapter(object : RepositoryListAdapter.OnItemClickListener {
            override fun itemClick(repositoryItem: RepositoryItem) {
                navigateToRepositoryFragment(repositoryItem)
            }
        })

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                editText.text.toString().let {
                    viewModel.searchRepositories(it)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.recyclerView.also {
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
    fun navigateToRepositoryFragment(repositoryItem: RepositoryItem) {
        val date = viewModel.lastSearchDate.value ?: Date()
        val action = SearchRepositoryFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repositoryItem, date.toString())
        findNavController().navigate(action)
    }
}