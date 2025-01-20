package com.example.currencyapplication.sorting_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapplication.common.sorting_preferences.SortingOption
import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortingViewModel @Inject constructor(private val sortingPreferences: SortingPreferences) : ViewModel() {

    private val _selectedSorting = MutableStateFlow(SortingOption.CodeAZ)
    val selectedSorting: StateFlow<SortingOption> = _selectedSorting

    init {
        sortingPreferences.sortingOrder
            .onEach { _selectedSorting.value = it }
            .launchIn(viewModelScope)
    }

    fun saveSortingOrder(option: SortingOption) {
        viewModelScope.launch {
            sortingPreferences.saveSortingOrder(option)
        }
    }
}