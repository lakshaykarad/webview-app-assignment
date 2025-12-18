package com.example.learning_basics.ui.theme.viewmodel

import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_basics.Repository.UrlRepository
import com.example.learning_basics.data.UserUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: UrlRepository
) : ViewModel() {

    private val _historyList = MutableStateFlow<List<UserUrl>>(emptyList())
    val history : StateFlow<List<UserUrl>> = _historyList

    private val _uploadLinks = MutableStateFlow<String?>(null)
    val uploadLinks : StateFlow<String?> = _uploadLinks

    init {
        loadHistory()
    }

    fun loadHistory(){
        viewModelScope.launch {
            _historyList.value = repository.getHistory()
        }
    }

    fun clearHistory(){
        viewModelScope.launch {
            repository.clearHistory()
            loadHistory()
        }
    }

    fun uploadHistory(){
        viewModelScope.launch {
            _uploadLinks.value = "Uploading..."
            val result = repository.uploadHistory()
            if (result.isSuccess) {
                _uploadLinks.value = "Upload Successful"
            } else {
                _uploadLinks.value = "Upload Failed: ${result.exceptionOrNull()?.message}"
            }
        }
    }

}