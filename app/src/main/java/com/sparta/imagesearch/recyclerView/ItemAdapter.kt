package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.Image
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.ItemType
import com.sparta.imagesearch.data.Video
import com.sparta.imagesearch.databinding.RecyclerViewItemImageBinding
import com.sparta.imagesearch.databinding.RecyclerViewItemProgressBinding
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
    RecyclerView.Adapter<ViewHolder>() {
    private val TAG = "ImageAdapter"

    var onItemClickListener: OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return when(dataset[position].type){
            ItemType.Image -> 0
            ItemType.Video -> 1
            ItemType.ProgressBar -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if((viewType==0)||(viewType==1)){
            val itemBinding =
                RecyclerViewItemImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ItemHolder(itemBinding)
        }
        else {
            val progressBinding = RecyclerViewItemProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ProgressHolder(progressBinding)
        }
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType){
            0 -> {
                (holder as ItemHolder).run {
                    setListeners()
                    bindImage(position)
                }
            }
            1 -> {
                (holder as ItemHolder).run{
                    setListeners()
                    bindVideo(position)
                }
            }
            2 -> {
                //no binding needed
            }
        }
    }
    fun changeDataset(newDataset: MutableList<Item>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    inner class ItemHolder(val binding: RecyclerViewItemImageBinding) :
        ViewHolder(binding.root) {
        private val imageView = binding.ivImage
        private val sourceTextView = binding.tvImageSource
        private val timeTextView = binding.tvImageTime
        private val heartImageView = binding.ivHeart

        fun setListeners(){
            itemView.setOnClickListener {
                onItemClickListener?.onItemImageClick(dataset[adapterPosition])
            }
            heartImageView.setOnClickListener {
                onItemClickListener?.onItemHeartClick(adapterPosition, dataset[adapterPosition])
            }
            heartImageView.setOnLongClickListener {
                onItemClickListener?.onItemHeartLongClick(dataset[adapterPosition])
                true
            }
        }
        fun bindImage(position: Int) {
            val item = dataset[position] as Image

            CoroutineScope(Dispatchers.Default).launch {
                setImageView(item.imageUrl)
            }
            sourceTextView.text ="[Image]" + item.source
            timeTextView.text = item.time
            setHeartImageViewColor(item.folder)
        }

        fun bindVideo(position:Int){
            val item = dataset[position] as Video

            CoroutineScope(Dispatchers.Default).launch {
                setImageView(item.thumbnail)
            }
            sourceTextView.text = "[Video]"
            timeTextView.text = item.time
            setHeartImageViewColor(item.folder)
        }

        private suspend fun setImageView(thumbnailUrl: String) {
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


        private fun setHeartImageViewColor(folder: Folder?) {
            heartImageView.imageTintList = ColorStateList.valueOf(
                folder?.color ?:binding.root.resources.getColor(R.color.gray)
            )
        }
    }

    inner class ProgressHolder(val binding: RecyclerViewItemProgressBinding) :
        ViewHolder(binding.root) {}
}