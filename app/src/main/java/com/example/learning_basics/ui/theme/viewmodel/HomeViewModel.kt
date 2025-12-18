package com.example.learning_basics.ui.theme.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_basics.Repository.UrlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UrlRepository
) : ViewModel(){
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent : SharedFlow<UiEvent> = _uiEvent

    private val _urlInput = MutableStateFlow("")
    val urlInput = _urlInput.asStateFlow()

    fun onUrlChange(newUrl: String) {
        _urlInput.value = newUrl
    }

    fun validInput(input: String){
        val trimInput = input.trim()

        if (trimInput.isEmpty()){
            emitEvent(event = UiEvent.ShowError("Please enter a URL."))
            return
        }

        val finalInput = if (!trimInput.startsWith("http://") && !trimInput.startsWith("https://") ){
            "https://$trimInput"
        }else{
            trimInput
        }

        if (Patterns.WEB_URL.matcher(finalInput).matches()){
            viewModelScope.launch {
                repository.addUrl(url = finalInput)
                emitEvent(UiEvent.NavigateToWebView(finalInput))
            }
        }else{
            emitEvent(UiEvent.ShowError("Please enter a valid URL.."))
        }

    }

    private fun emitEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

}

sealed class UiEvent(){
    data class ShowError(val error : String) : UiEvent()
    data class NavigateToWebView(val url : String) : UiEvent()
}
