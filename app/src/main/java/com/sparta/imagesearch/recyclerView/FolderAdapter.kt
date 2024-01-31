package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.data.FolderManager
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderBinding

interface OnFolderClickListener {
    fun onClick(folderId: String)
}

class FolderAdapter(var dataset: MutableList<Folder>) :
    RecyclerView.Adapter<FolderAdapter.Holder>() {

    var onFolderClickListener: OnFolderClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemFolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            onFolderClickListener?.onClick(dataset[position].id)
            notifyDataSetChanged()
        }
        holder.bind(position)
    }

    inner class Holder(private val binding: RecyclerViewItemFolderBinding) :
        ViewHolder(binding.root) {
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName
        private val dotImageView = binding.ivDot

        fun bind(position: Int) {
            val folder = dataset[position]

            folderImageView.imageTintList = ColorStateList.valueOf(
                binding.root.resources.getColor(folder.colorId)
            )
            nameTextView.text = folder.name
            dotImageView.isVisible = (folder.id == FolderManager.getSelectedFolderId())
        }
    }
}