package com.sparta.imagesearch.data

import com.sparta.imagesearch.MainActivity
import com.sparta.imagesearch.R

object ImageFolderManager {
    val defaultFolder = ItemFolder("기본 폴더", R.color.folder_color1)

    private val folders = mutableListOf<ItemFolder>(defaultFolder)

    fun getFolders():MutableList<ItemFolder> = folders

    fun addFolder(name:String, colorId:Int){
        folders.add(ItemFolder(name, colorId))
    }

    fun deleteFolder(folder:ItemFolder){
        if(folder == defaultFolder) return

        MainActivity.savedItems.forEach {
            it.unsaveItem()
        }
        folders.remove(folder)
    }
}