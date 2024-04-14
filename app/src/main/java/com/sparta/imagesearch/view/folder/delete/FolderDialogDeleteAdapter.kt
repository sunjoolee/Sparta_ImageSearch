package com.sparta.imagesearch.view.folder.delete

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.FolderId
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderDialogBinding
import com.sparta.imagesearch.view.folder.FolderDialogModel
import com.sparta.imagesearch.view.folder.FolderDialogModelChangePayload
import com.sparta.imagesearch.view.folder.FolderDialogModelDiffCallback
import com.sparta.imagesearch.view.folder.OnFolderDialogModelClickListener

class FolderDialogDeleteAdapter() :
    ListAdapter<FolderDialogModel, FolderDialogDeleteAdapter.Holder>(FolderDialogModelDiffCallback) {
    private val TAG = "FolderDialogDeleteAdapter"

    var onFolderDialogModelClickListener: OnFolderDialogModelClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemFolderDialogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(currentList[position]) {
            if (position == 0) holder.bindDefault(this)
            else holder.bind(this)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        when (val lastPayload = payloads.lastOrNull()) {
            is FolderDialogModelChangePayload.IsChecked ->
                holder.bindIsChecked(lastPayload.isChecked)

            else -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class Holder(private val binding: RecyclerViewItemFolderDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val checkImageView = binding.ivCheck
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName

        fun bindDefault(folderDialogModel: FolderDialogModel) {
            folderImageView.imageTintList = ColorStateList.valueOf(
                Color.parseColor(folderDialogModel.colorHex)
            )
            nameTextView.text = folderDialogModel.name

            with(itemView.context.getColor(R.color.gray)) {
                checkImageView.imageTintList = ColorStateList.valueOf(this)
                nameTextView.setTextColor(this)
            }
        }

        fun bind(folderDialogModel: FolderDialogModel) {
            folderImageView.imageTintList = ColorStateList.valueOf(
                Color.parseColor(folderDialogModel.colorHex)
            )
            nameTextView.text = folderDialogModel.name

            bindIsChecked(folderDialogModel.isChecked)

            setListener(folderDialogModel)
        }

        fun bindIsChecked(isChecked: Boolean) {
            Log.d(TAG, "bindIsChecked) isChecked: $isChecked")
            checkImageView.setImageResource(
                if (isChecked) R.drawable.icon_select_check
                else R.drawable.icon_select_empty
            )
        }

        private fun setListener(folderDialogModel: FolderDialogModel) {
            itemView.setOnClickListener {
                Log.d(TAG, "itemView.onClick) folderDialogModel.id: ${folderDialogModel.id}")
                if (folderDialogModel.id == FolderId.DEFAULT_FOLDER.id)
                    return@setOnClickListener

                onFolderDialogModelClickListener?.onFolderDialogModelClick(folderDialogModel)
            }
        }
    }
}