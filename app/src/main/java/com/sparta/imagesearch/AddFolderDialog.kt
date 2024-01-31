package com.sparta.imagesearch

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
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

    private lateinit var name: String
    private var colorId: Int = R.color.folder_color1

    var onAddConfirmListener: OnAddConfirmListener? = null

    fun show() {
        _binding = DialogAddFolderBinding.inflate(context.layoutInflater)

        dialog.run {
            setContentView(binding.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        initMoreButton()

        initCloseButton()
        initConfirmButton()

        dialog.show()
    }

    private fun initMoreButton(){
        binding.ivMore.setOnClickListener {
            ColorPickerDialog
                .Builder(context)
                .setDefaultColor(R.color.folder_color1)
                .setColorListener { color, colorHex ->
                    // Handle Color Selection
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
            onAddConfirmListener?.onAddConfirm(name, colorId)
            dialog.dismiss()
        }
    }
}
