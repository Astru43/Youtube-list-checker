package com.astru43.youtube_checker.ui.main.management

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astru43.youtube_checker.util.Account
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveViewModel : ViewModel() {

    private val _lists = MutableLiveData<List<Playlist>>()
    val lists: LiveData<List<Playlist>> = _lists

    init {
        request()
    }

    private fun request() {
        Log.i("Request", "In request")

        val credential = Account.instance.credential
        if (credential != null) {
            Log.i("Account", credential.selectedAccountName)


            val httpTransport = NetHttpTransport()
            val jasonFactory = JacksonFactory.getDefaultInstance()
            val youtube = YouTube.Builder(httpTransport, jasonFactory, credential)
                .setApplicationName("Youtube_list_checker").build()

            val request = youtube.Playlists().list("snippet")


            viewModelScope.launch(Dispatchers.IO) {
                val response = request.setMaxResults(50).setMine(true).execute()
                Log.i("youtube", response.items.size.toString())
                if (response.items != null) {
                    _lists.postValue(response.items)
                }
            }
        } else {
            _lists.value = emptyList()
        }
    }

    fun refresh() {
        request()
    }
}
