package com.example.gojekassignment.trendingRepositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gojekassignment.R
import com.example.gojekassignment.databinding.RepositoryItemBinding
import com.example.gojekassignment.domain.Repository

class RepositoriesAdapter() : RecyclerView.Adapter<RepositoriesViewHolder>() {

    var repositories: List<Repository> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoriesViewHolder {
        val withDataBinding: RepositoryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            RepositoriesViewHolder.LAYOUT,
            parent,
            false)
        return RepositoriesViewHolder(withDataBinding)
    }

    override fun getItemCount() = repositories.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: RepositoriesViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.repository = repositories[position]
        }
    }

}

/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class RepositoriesViewHolder(val viewDataBinding: RepositoryItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.repository_item
    }
}
