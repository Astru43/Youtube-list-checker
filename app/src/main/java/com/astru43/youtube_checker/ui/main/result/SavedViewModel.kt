package com.astru43.youtube_checker.ui.main.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class SavedViewModel : ViewModel() {

    lateinit var titles: LiveData<MutableList<String>>

    fun populate_list(file: String) {
        //TODO: Implement title list loading.
    }


}