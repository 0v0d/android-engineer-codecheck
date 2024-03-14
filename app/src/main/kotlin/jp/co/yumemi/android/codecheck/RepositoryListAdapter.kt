package jp.co.yumemi.android.codecheck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * リポジトリーリストアダプター
 * @param itemClickListener アイテムクリックリスナー
 */
class RepositoryListAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryItem, RepositoryListAdapter.ViewHolder>(diff_util) {

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

/** リポジトリーアイテムの差分検出 */
private val diff_util = object : DiffUtil.ItemCallback<RepositoryItem>() {
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