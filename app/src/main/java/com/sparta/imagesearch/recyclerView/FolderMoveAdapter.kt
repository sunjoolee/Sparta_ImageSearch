package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderDialogBinding

class FolderMoveAdapter(var dataset: MutableList<Folder>, var checkedFolderId: String) :
    RecyclerView.Adapter<FolderMoveAdapter.Holder>() {
    private val TAG = "FolderDialogAdapter"

    var onFolderClickListener: OnFolderClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemFolderDialogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            checkedFolderId = dataset[position].id
            onFolderClickListener?.onFolderClick(checkedFolderId)
            notifyDataSetChanged()
        }
        holder.bind(position)
    }

    inner class Holder(private val binding: RecyclerViewItemFolderDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val checkImageView = binding.ivCheck
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName

        fun bind(position: Int) {
            val folder = dataset[position]
            checkImageView.setImageResource(
                if (folder.id == checkedFolderId) R.drawable.icon_select_full
                else R.drawable.icon_select_empty
            )
            folderImageView.imageTintList = ColorStateList.valueOf(folder.color)
            nameTextView.text = folder.name
        }
    }
}