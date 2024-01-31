package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.Image
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.ItemType
import com.sparta.imagesearch.data.Video
import com.sparta.imagesearch.databinding.RecyclerViewItemImageBinding
import com.sparta.imagesearch.util.fromDpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


interface OnItemClickListener {
    fun onItemImageClick(item: Item)
    fun onItemHeartClick(position:Int, item: Item)
    fun onItemHeartLongClick(item: Item)
}

class ItemAdapter(var dataset: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.Holder>() {
    private val TAG = "ImageAdapter"

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerViewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemImageClick(dataset[position])
        }
        holder.heartImageView.setOnClickListener {
            onItemClickListener?.onItemHeartClick(position, dataset[position])
        }
        holder.heartImageView.setOnLongClickListener {
            onItemClickListener?.onItemHeartLongClick(dataset[position])
            true
        }

        holder.bind(position)
    }

    fun changeDataset(newDataset: MutableList<Item>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    inner class Holder(val binding: RecyclerViewItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.ivImage
        val sourceTextView = binding.tvImageSource
        val timeTextView = binding.tvImageTime
        val heartImageView = binding.ivHeart
        fun bind(position: Int) {
            val item = dataset[position]

            CoroutineScope(Dispatchers.Default).launch {
                setImageView(
                    if (item.type == ItemType.Image) (item as Image).imageUrl
                    else (item as Video).thumbnail
                )
            }

            sourceTextView.text =
                if (item.type == ItemType.Image) "[Image]" + (item as Image).source
                else "[Video]"
            timeTextView.text = item.time
            setHeartImageViewColor(item.folder)
        }

        private suspend fun Holder.setImageView(thumbnailUrl: String) {
            lateinit var sourceBitmap: Bitmap
            val job = CoroutineScope(Dispatchers.Default).launch {
                Log.d(TAG, "getting sourceBitmap...")
                sourceBitmap = try {
                    Glide.with(binding.root.context)
                        .asBitmap()
                        .load(thumbnailUrl)
                        .submit()
                        .get()

                } catch (e: Exception) {
                    e.printStackTrace()
                    BitmapFactory.decodeResource(binding.root.resources, R.drawable.icon_bad_wifi)
                }
            }
            runBlocking {
                job.join()
            }
            Log.d(TAG, "got sourceBitmap!")

            withContext(Dispatchers.Main) {
                val ratio = sourceBitmap.height.toDouble() / sourceBitmap.width.toDouble()
                val newWidth = 160f.fromDpToPx()
                val newHeight = (160f.fromDpToPx() * ratio).toInt()
                val scaledBitmap = createScaledBitmap(sourceBitmap, newWidth, newHeight, true)

                Log.d(TAG, "set scaledBitmap to imageView")
                imageView.setImageBitmap(scaledBitmap)
                imageView.clipToOutline = true
            }
        }


        private fun Holder.setHeartImageViewColor(folder: Folder?) {
            heartImageView.imageTintList = ColorStateList.valueOf(
                binding.root.resources.getColor(
                    folder?.colorId ?: R.color.gray
                )
            )
        }
    }
}