package com.rsetiapp.core.basecomponent


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
class BaseRecyclerAdapter<T, VB : ViewBinding>(
    private var items: List<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val onBind: (item: T, binding: VB, position: Int) -> Unit,
    private val onItemClick: ((item: T, position: Int) -> Unit)? = null,
    // view-wise click listener
    private val onViewClick: ((view: View, item: T, position: Int) -> Unit)? = null,
    private val diffChecker: ((old: T, new: T) -> Boolean)? = null
) : RecyclerView.Adapter<BaseRecyclerAdapter<T, VB>.BaseViewHolder>() {

    inner class BaseViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            bindingInflater(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]

        // whole item click
        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(item, position)
        }
        onBind(item, holder.binding, position)
    }

    fun triggerViewClick(view: View, item: T, position: Int) {
        onViewClick?.invoke(view, item, position)
    }

    override fun getItemCount(): Int = items.size

    fun update(newList: List<T>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                return diffChecker?.invoke(items[oldPos], newList[newPos])
                    ?: (items[oldPos] == newList[newPos])
            }

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
                return items[oldPos] == newList[newPos]
            }
        })

        items = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
