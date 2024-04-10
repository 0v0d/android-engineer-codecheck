package jp.co.yumemi.android.codecheck.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import jp.co.yumemi.android.codecheck.databinding.LayoutRepositoryListItemBinding
import jp.co.yumemi.android.codecheck.model.domain.RepositoryItem

/**
 * リポジトリーリストアダプター
 * @param onRepositoryItemClick リポジトリーアイテムクリック時の処理
 */
class RepositoryListAdapter(
    private val onRepositoryItemClick: (RepositoryItem) -> Unit
) : ListAdapter<RepositoryItem, RepositoryListViewHolder>(repositoryItemDiffCallback) {

    /**
     * ビューホルダー生成時の処理
     * @param parent 親ビュー
     * @param viewType ビュータイプ
     * @return リポジトリーリストビューホルダー
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutRepositoryListItemBinding.inflate(inflater, parent, false)
        return RepositoryListViewHolder(binding)
    }

    /**
     * ビューホルダーバインド時の処理
     * @param holder ビューホルダー
     * @param position ポジション
     */
    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        holder.bind(getItem(position), onRepositoryItemClick)
    }
}

/** リポジトリーアイテムの差分検出 */
private val repositoryItemDiffCallback = object : DiffUtil.ItemCallback<RepositoryItem>() {
    override fun areItemsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
        return oldItem == newItem
    }
}