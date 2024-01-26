package com.sparta.imagesearch.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.Image
import com.sparta.imagesearch.databinding.RecyclerViewItemImageBinding
import com.sparta.imagesearch.util.fromDpToPx


interface OnClickListener{
    fun onClick(image: Image)
    fun onHeartClick(image: Image)
    fun onHeartLongClick(image: Image)
}
class ImageAdapter(var dataset: MutableList<Image>) :
    RecyclerView.Adapter<ImageAdapter.Holder>() {

    var onClickListener:OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerViewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(dataset[position])
        }
        holder.heartImageView.setOnClickListener {
            onClickListener?.onHeartClick(dataset[position])
        }
        holder.heartImageView.setOnLongClickListener {
            onClickListener?.onHeartLongClick(dataset[position])
            false
        }

        holder.bind(position)
    }

    private fun Holder.bind(position: Int) {
        with(dataset[position]) {
            Glide.with(binding.root.context)
                .load(this.thumbnailUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.icon_bad_wifi)
                        .error(R.drawable.icon_bad_wifi)
                        .override(160f.fromDpToPx()) //TODO 이미지 가로세로 비율 유지하며 크기 조정하기
                )
                .into(imageView)
            imageView.clipToOutline = true

            sourceTextView.text = this.source
            timeTextView.text = this.time
            //TODO 폴더 색에 따라 하트 색 바꾸기

        }
    }

    fun changeDataset(newDataset: MutableList<Image>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    inner class Holder(val binding: RecyclerViewItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.ivImage
        val sourceTextView = binding.tvImageSource
        val timeTextView = binding.tvImageTime
        val heartImageView = binding.ivHeart
    }
}