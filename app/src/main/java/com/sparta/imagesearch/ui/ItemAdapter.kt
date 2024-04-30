package com.sparta.imagesearch.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.data.source.local.folder.FolderColor
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.entity.Item
import com.sparta.imagesearch.entity.ItemType
import com.sparta.imagesearch.databinding.RecyclerViewItemImageBinding


interface OnHeartClickListener {
    fun onHeartClick(item: Item)
}

interface OnHeartLongClickListener {
    fun onHeartLongClick(item: Item)
}

class ItemAdapter() : ListAdapter<Item, ItemAdapter.Holder>(ItemDiffCallback) {
    private val TAG = "ImageAdapter"

    var onHeartClickListener: OnHeartClickListener? = null
    var onHeartLongClickListener: OnHeartLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerViewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        when(val lastPayload = payloads.lastOrNull()){
            is ItemChangePayload.FolderIdChange ->
                holder.bindHeartImageView(lastPayload.folderId)

            else -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class Holder(val binding: RecyclerViewItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.ivImage
        private val sourceTextView = binding.tvImageSource
        private val timeTextView = binding.tvImageTime
        private val heartImageView = binding.ivHeart
        fun bind(item: Item) {
            setImageView(item.imageUrl)
            sourceTextView.text =
                (if (item.itemType == ItemType.IMAGE_TYPE) "[Image]" else "[Video]") + item.source
            timeTextView.text = item.time
            bindHeartImageView(item.folderId)

            setListeners(item)
        }
        private fun setImageView(imageUrl: String) {
            // TODO 이미지 비율에 따라 크기 조정하기
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(imageView)
            imageView.clipToOutline = true
        }

        private fun setListeners(item: Item) {
            heartImageView.setOnClickListener {
                Log.d(TAG, "heartImageView.onClick) called")
                onHeartClickListener?.onHeartClick(item)
            }
            heartImageView.setOnLongClickListener {
                Log.d(TAG, "heartImageView.onLongClick) called")
                onHeartLongClickListener?.onHeartLongClick(item)
                false
            }
        }

        fun bindHeartImageView(folderId: String) {
            Log.d(TAG, "bindHeartImageView) folderId: $folderId")

            // TODO 폴더 키로 폴더 색 가져오기
            heartImageView.imageTintList = ColorStateList.valueOf(
                Color.parseColor(
                    if (folderId == FolderId.NO_FOLDER.id) FolderColor.color0.colorHex
                    else FolderColor.color1.colorHex
                )
            )
        }
    }
}