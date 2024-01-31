package com.sparta.imagesearch

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.sparta.imagesearch.databinding.DialogAddFolderBinding

class AddFolderDialog(private val context: AppCompatActivity) {
    private val TAG = "AddFolderDialog"

    interface OnAddConfirmListener {
        fun onAddConfirm(name: String, colorId: Int)
    }

    private var _binding: DialogAddFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private var colorId: Int = R.color.folder_color1
    private val colorImageViewMap by lazy{
        mapOf<ImageView, Int>(
            binding.ivColor1 to R.color.folder_color1,
            binding.ivColor2 to R.color.folder_color2,
            binding.ivColor3 to R.color.folder_color3,
            binding.ivColor4 to R.color.folder_color4,
            binding.ivColor5 to R.color.folder_color5
        )
    }

    var onAddConfirmListener: OnAddConfirmListener? = null

    fun show() {
        _binding = DialogAddFolderBinding.inflate(context.layoutInflater)

        dialog.run {
            setContentView(binding.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        initColorButtons()
        initMoreButton()

        initCloseButton()
        initConfirmButton()

        dialog.show()
    }

    private fun initColorButtons(){
        val colorClickListener = OnClickListener { imageView ->
            colorId = colorImageViewMap[imageView]!!
            setFolderColorTint(colorId)
        }
        binding.run{
            ivColor1.setOnClickListener(colorClickListener)
            ivColor2.setOnClickListener(colorClickListener)
            ivColor3.setOnClickListener(colorClickListener)
            ivColor4.setOnClickListener(colorClickListener)
            ivColor5.setOnClickListener(colorClickListener)
        }
    }

    private fun setFolderColorTint(colorId:Int){
        binding.ivFolder.imageTintList = ColorStateList.valueOf(
            binding.root.resources.getColor(colorId)
        )
    }
    private fun initMoreButton(){
        binding.ivMore.setOnClickListener {
            ColorPickerDialog
                .Builder(context)
                .setDefaultColor(R.color.folder_color1)
                .setColorListener { color, colorHex ->
                    Log.d(TAG, "color: ${color}, colorHex: ${colorHex}")
                }
                .show()
        }
    }

    private fun initCloseButton() {
        binding.bntNegative.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initConfirmButton() {
        binding.bntPositive.setOnClickListener {
            if(!isNameValid()) return@setOnClickListener

            val name = binding.etFolderName.text.toString()
            onAddConfirmListener?.onAddConfirm(name , colorId)
            dialog.dismiss()
        }
    }

    private fun isNameValid():Boolean{
        binding.etFolderName.run {
            return if (text.isNullOrBlank()) {
                text.clear()
                setHintTextColor(context.resources.getColor(R.color.theme_accent))
                false
            } else true
        }
    }
}
