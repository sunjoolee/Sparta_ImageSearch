package com.sparta.imagesearch.data

import com.sparta.imagesearch.R

object ImageManager {
    private val savedImages: MutableList<Image> = mutableListOf()

    fun isSaved(image: Image): Boolean =
        savedImages.contains(image)

    fun saveImage(image: Image, folder: ImageFolder? = null) {
        image.folder = folder ?: ImageFolderManager.defaultFolder
        savedImages.add(image)
    }

    fun unsaveImage(image: Image) {
        savedImages.remove(image)
        image.folder = null
    }

    fun getSavedImages(folder:ImageFolder):MutableList<Image> =
        savedImages.filter { it.folder == folder}.toMutableList()
}