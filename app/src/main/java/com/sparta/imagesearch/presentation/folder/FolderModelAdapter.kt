package com.sparta.imagesearch.presentation.folder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sparta.imagesearch.R
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderBinding

interface OnFolderModelClickListener {
    fun onFolderModelClick(folderModel: FolderModel)
}

class FolderModelAdapter() :
    ListAdapter<FolderModel, FolderModelAdapter.Holder>(FolderModelDiffCallback) {

    var onFolderModelClickListener: OnFolderModelClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemFolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        when (val lastPayload = payloads.lastOrNull()) {
            is FolderModelChangePayload.IsSelected -> holder.bindIsSelected(lastPayload.isSelected)
            else -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class Holder(private val binding: RecyclerViewItemFolderBinding) :
        ViewHolder(binding.root) {
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName
        private val dotImageView = binding.ivDot

        fun bind(folderModel: FolderModel) {
            folderImageView.imageTintList =
                ColorStateList.valueOf(Color.parseColor(folderModel.colorHex))
            nameTextView.text = folderModel.name
            bindIsSelected(folderModel.isSelected)

            setListener(folderModel)
        }

        fun bindIsSelected(isSelected: Boolean) {
            dotImageView.isVisible = isSelected
            nameTextView.setTextColor(
                itemView.context.getColor(
                    if (isSelected) R.color.white
                    else R.color.gray
                )
            )
        }

        private fun setListener(folderModel: FolderModel) {
            itemView.setOnClickListener {
                onFolderModelClickListener?.onFolderModelClick(folderModel)
            }
        }
    }
}