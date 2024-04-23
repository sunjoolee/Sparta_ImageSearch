package com.sparta.imagesearch.ui.folder.add

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.FolderColor
import com.sparta.imagesearch.databinding.DialogAddFolderBinding

interface OnAddConfirmListener {
    fun onAddConfirm(name: String, colorHex: String)
}

class AddFolderDialog(private val context: AppCompatActivity) {
    private val TAG = "AddFolderDialog"

    private var _binding: DialogAddFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private var colorHex: String = FolderColor.color1.colorHex
    private val colorImageViewMap by lazy {
        mapOf<ImageView, String>(
            binding.ivColor1 to FolderColor.color1.colorHex,
            binding.ivColor2 to FolderColor.color2.colorHex,
            binding.ivColor3 to FolderColor.color3.colorHex,
            binding.ivColor4 to FolderColor.color4.colorHex,
            binding.ivColor5 to FolderColor.color5.colorHex
        )
    }
    private val dotImageViewMap by lazy {
        mapOf<ImageView, ImageView>(
            binding.ivColor1 to binding.ivDot1,
            binding.ivColor2 to binding.ivDot2,
            binding.ivColor3 to binding.ivDot3,
            binding.ivColor4 to binding.ivDot4,
            binding.ivColor5 to binding.ivDot5
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

    private fun initColorButtons() {
        val colorClickListener = OnClickListener { imageView ->
            setDotVisibility(imageView)

            colorHex = colorImageViewMap[imageView]!!
            setFolderImageViewTint()
        }
        binding.run {
            ivColor1.setOnClickListener(colorClickListener)
            ivColor2.setOnClickListener(colorClickListener)
            ivColor3.setOnClickListener(colorClickListener)
            ivColor4.setOnClickListener(colorClickListener)
            ivColor5.setOnClickListener(colorClickListener)
        }
    }

    private fun setFolderImageViewTint() {
        binding.ivFolder.imageTintList = ColorStateList.valueOf(
            Color.parseColor(colorHex)
        )
    }

    private fun setDotVisibility(imageView: View) {
        dotImageViewMap.values.forEach { it.isVisible = false }
        dotImageViewMap[imageView]!!.isVisible = true
    }

    private fun initMoreButton() {
        binding.ivMore.setOnClickListener {
            dotImageViewMap.values.forEach { it.isVisible = false }

            ColorPickerDialog
                .Builder(context)
                .setDefaultColor(R.color.folder_color1)
                .setColorListener { color, colorHex ->
                    this.colorHex = colorHex
                    setFolderImageViewTint()
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
            if (!isNameValid()) return@setOnClickListener

            val name = binding.etFolderName.text.toString()
            onAddConfirmListener?.onAddConfirm(name, colorHex)
            dialog.dismiss()
        }
    }

    private fun isNameValid(): Boolean {
        binding.etFolderName.run {
            return if (text.isNullOrBlank()) {
                text.clear()
                setHintTextColor(context.resources.getColor(R.color.theme_accent))
                false
            } else true
        }
    }
}
