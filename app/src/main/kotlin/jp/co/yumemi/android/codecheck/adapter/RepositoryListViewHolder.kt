package jp.co.yumemi.android.codecheck.adapter

import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.databinding.LayoutRepositoryListItemBinding
import jp.co.yumemi.android.codecheck.model.domain.RepositoryItem

/**
 * リポジトリリストビューホルダー
 * @param binding レイアウトバインディング
 */
class RepositoryListViewHolder (
    private val binding: LayoutRepositoryListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * データをバインドする
     * @param item リポジトリアイテム
     * @param listener リポジトリクリックリスナー
     */
    fun bind(item: RepositoryItem, listener: (RepositoryItem) -> Unit) {
        binding.repositoryItem = item
        binding.root.setOnClickListener {
            listener(item)
        }
    }
}