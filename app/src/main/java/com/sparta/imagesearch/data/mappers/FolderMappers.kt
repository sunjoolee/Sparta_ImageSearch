package com.sparta.imagesearch.data.mappers

import com.sparta.imagesearch.data.source.local.folder.FolderEntity
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder

fun Folder.toFolderEntity() = FolderEntity(
    name = name,
    colorHex = colorHex
)

fun DefaultFolder.toFolderEntity() = FolderEntity(
    id = id,
    name = name,
    colorHex = colorHex
)

fun FolderEntity.toFolder() = Folder(
    id = id,
    name = name,
    colorHex = colorHex
)

fun DefaultFolder.toFolder() = Folder(
    id = id,
    name = name,
    colorHex = colorHex
)