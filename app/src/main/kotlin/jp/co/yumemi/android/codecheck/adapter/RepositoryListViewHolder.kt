package jp.co.yumemi.android.codecheck.adapter

import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.databinding.LayoutItemBinding
import jp.co.yumemi.android.codecheck.model.RepositoryItem

/**
 * リポジトリリストビューホルダー
 * @param binding レイアウトバインディング
 */
class RepositoryListViewHolder(private val binding: LayoutItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * データをバインドする
     * @param item リポジトリアイテム
     * @param listener リポジトリクリックリスナー
     */
    fun bind(item: RepositoryItem, listener: RepositoryListAdapter.OnItemClickListener) {
        binding.repositoryNameView.text = item.name
        binding.root.setOnClickListener { listener.itemClick(item) }
    }
}