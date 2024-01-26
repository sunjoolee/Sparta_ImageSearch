package com.sparta.imagesearch.recyclerView

import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.RecyclerViewImage
import com.sparta.imagesearch.databinding.RecyclerViewItemImageBinding

class RecyclerViewImageAdapter(var dataset:MutableList<RecyclerViewImage>) : RecyclerView.Adapter<RecyclerViewImageAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    private fun Holder.bind(position:Int){
        with(dataset[position]) {
            Glide.with(binding.root.context)
                .load(this.thumbnailUrl)
                .apply(
                    RequestOptions().error(R.drawable.image_sample1)
                )
                .into(imageView)

            sourceTextView.text =this.source
            timeTextView.text = this.time
            //TODO 폴더 색에 따라 하트 색 바꾸기
        }
    }

    fun changeDataset(newDataset:MutableList<RecyclerViewImage>){
        dataset = newDataset
        notifyDataSetChanged()
    }

    inner class Holder(val binding:RecyclerViewItemImageBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.ivImage
        val sourceTextView = binding.tvImageSource
        val timeTextView = binding.tvImageTime
        val heartImageView = binding.ivHeart
    }
}