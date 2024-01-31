package com.sparta.imagesearch.data

import com.sparta.imagesearch.R

object ImageFolderManager {
    val defaultFolder = ImageFolder("기본 폴더", R.color.folder_color1)

    private val folders = mutableListOf<ImageFolder>(defaultFolder)

    fun getFolders():MutableList<ImageFolder> = folders

    fun addFolder(name:String, colorId:Int){
        folders.add(ImageFolder(name, colorId))
    }

    fun deleteFolder(folder:ImageFolder){
        if(folder == defaultFolder) return

        ImageManager.getSavedImages(folder).forEach {
            ImageManager.unsaveImage(it)
        }
        folders.remove(folder)
    }
}