package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderDialogBinding

class FolderDeleteAdapter(var dataset: MutableList<Folder>) :
    RecyclerView.Adapter<FolderDeleteAdapter.Holder>() {
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
            if(position == 0)
                return@setOnClickListener

            onFolderClickListener?.onFolderClick(dataset[position].id)
            holder.toggleCheckImageView()
        }
        holder.bind(position)
    }

    inner class Holder(private val binding: RecyclerViewItemFolderDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var checked = false
        private val checkImageView = binding.ivCheck
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName

        fun bind(position: Int) {
            val folder = dataset[position]

            folderImageView.imageTintList = ColorStateList.valueOf(
                binding.root.resources.getColor(folder.colorId)
            )
            nameTextView.text = folder.name

            if(position==0){
                binding.root.resources.getColor(R.color.gray).also {
                    checkImageView.imageTintList = ColorStateList.valueOf(it)
                    nameTextView.setTextColor(it)
                }
            }
        }

        fun toggleCheckImageView() {
            checked = !checked
            checkImageView.setImageResource(
                if (checked) R.drawable.icon_select_check
                else R.drawable.icon_select_empty
            )
        }
    }
}