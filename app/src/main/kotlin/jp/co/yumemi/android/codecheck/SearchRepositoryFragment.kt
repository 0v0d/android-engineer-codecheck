/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchRepositoryBinding

/** リポジトリー検索画面 */
class SearchRepositoryFragment : Fragment(R.layout.fragment_search_repository) {

    /**
     * ビュー生成時の処理
     * @param view ビュー
     * @param savedInstanceState 保存されたインスタンスの状態
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchRepositoryBinding.bind(view)

        val viewModel = SearchRepositoryViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(repositoryItem: RepositoryItem) {
                navigateToRepositoryFragment(repositoryItem)
            }
        })

        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        viewModel.searchRepositories(it).apply {
                            adapter.submitList(this)
                        }
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
    }

    /**
     * リポジトリー詳細画面へ遷移する
     * @param repositoryItem リポジトリーアイテム
     */
    fun navigateToRepositoryFragment(repositoryItem: RepositoryItem) {
        val action = SearchRepositoryFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(repositoryItem = repositoryItem)
        findNavController().navigate(action)
    }
}

/** リポジトリーアイテムの差分検出 */
val diff_util = object : DiffUtil.ItemCallback<RepositoryItem>() {
    override fun areItemsTheSame(
        oldRepositoryItem: RepositoryItem,
        newRepositoryItem: RepositoryItem
    ): Boolean {
        return oldRepositoryItem.name == newRepositoryItem.name
    }

    override fun areContentsTheSame(
        oldRepositoryItem: RepositoryItem,
        newRepositoryItem: RepositoryItem
    ): Boolean {
        return oldRepositoryItem == newRepositoryItem
    }

}

/**
 * カスタムアダプター
 * @param itemClickListener アイテムクリックリスナー
 */
class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryItem, CustomAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(repositoryItem: RepositoryItem)
    }

    /**
     * ビューホルダー生成時の処理
     * @param parent ビュー
     * @param viewType ビュータイプ
     * @return ビューホルダー
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * ビューホルダーのバインド時の処理
     * @param holder ビューホルダー
     * @param position 位置
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            item.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}
