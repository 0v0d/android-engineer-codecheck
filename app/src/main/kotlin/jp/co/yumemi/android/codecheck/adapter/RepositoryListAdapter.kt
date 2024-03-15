package jp.co.yumemi.android.codecheck.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import jp.co.yumemi.android.codecheck.databinding.LayoutItemBinding
import jp.co.yumemi.android.codecheck.model.RepositoryItem

/**
 * リポジトリーリストアダプター
 * @param itemClickListener アイテムクリックリスナー
 */
class RepositoryListAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<RepositoryItem, RepositoryListViewHolder>(repositoryItemDiffCallback) {

    /**
     * ビューホルダー生成時の処理
     * @param parent 親ビュー
     * @param viewType ビュータイプ
     * @return リポジトリーリストビューホルダー
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemBinding.inflate(inflater, parent, false)
        return RepositoryListViewHolder(binding)
    }

    /**
     * ビューホルダーバインド時の処理
     * @param holder ビューホルダー
     * @param position ポジション
     */
    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }

    /** アイテムクリックリスナー */
    interface OnItemClickListener {
        /** アイテムクリック時の処理 */
        fun itemClick(repositoryItem: RepositoryItem)
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