package com.sparta.imagesearch.presentation.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddFolderDialogState(
    val folderName: String = "",
    val folderColorHex: String = FolderColor.COLOR1.colorHex,
    val canAddFolder: Boolean = false
)


interface AddFolderDialogInputs {
    fun setFolderName(folderName: String)
    fun setFolderColorHex(folderColorHex: String)

    fun addFolder()
}

@HiltViewModel
class AddFolderDialogViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel(), AddFolderDialogInputs {
    private val TAG = "AddFolderDialogViewModel"

    private val _folderName = MutableStateFlow("")
    private val _folderColorHex = MutableStateFlow(FolderColor.COLOR1.colorHex)
    private val _nameValid = MutableStateFlow(false)

    private val _state = MutableStateFlow(AddFolderDialogState())
    val state = _state.asStateFlow()

    val inputs = this@AddFolderDialogViewModel

    init {
        viewModelScope.launch {
            _folderName.collect{
                _nameValid.value = it.isNotEmpty()
            }
        }

        combine(_folderName, _folderColorHex, _nameValid) { folderName, folderColorHex, nameValid ->
            _state.value = AddFolderDialogState(
                folderName, folderColorHex, nameValid
            )
        }.launchIn(viewModelScope)
    }

    override fun setFolderName(folderName: String) {
        _folderName.value = folderName
    }

    override fun setFolderColorHex(folderColorHex: String) {
        _folderColorHex.value = folderColorHex
    }

    override fun addFolder() {
        viewModelScope.launch {
            folderRepository.upsertFolder(
                Folder(name = _folderName.value, colorHex = _folderColorHex.value)
            )
        }
    }
}