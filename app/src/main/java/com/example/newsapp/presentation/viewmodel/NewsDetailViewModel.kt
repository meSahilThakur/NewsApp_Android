package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _newsDetailState = MutableStateFlow(NewsDetailState())
    val newsDetailState = _newsDetailState.asStateFlow()

    fun getNewsDetail(){
        viewModelScope.launch {
            getNewsUseCase().collectLatest{

            }
        }
    }
}

data class NewsDetailState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: Article? = null
)