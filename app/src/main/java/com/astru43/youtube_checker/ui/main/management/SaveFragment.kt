package com.astru43.youtube_checker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astru43.youtube_checker.R
import com.astru43.youtube_checker.util.Account
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveFragment : Fragment() {

    companion object {
        fun newInstance() =
            SaveFragment()
    }

    private val viewModel: SaveViewModel by activityViewModels()
    private lateinit var saveListAdapter: SaveListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.save_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lists = view.findViewById<RecyclerView>(R.id.youtube_lists)
        val btn = view.findViewById<Button>(R.id.save_btn)

        lists?.layoutManager = LinearLayoutManager(requireContext())
        viewModel.lists.observe(viewLifecycleOwner, { list ->
            saveListAdapter = SaveListAdapter(list)
            lists?.adapter = saveListAdapter
        })

        btn?.setOnClickListener {
            Log.i("Button", "Pressed")
            if (this@SaveFragment::saveListAdapter.isInitialized) {
                Log.i("Selected", saveListAdapter.selectedItem.toString())
                if (saveListAdapter.selectedItem > -1) {
                    viewModel.lists.observe(viewLifecycleOwner, { lists ->
                        Log.d("ListId", lists[saveListAdapter.selectedItem].id)

                        val httpTransport = NetHttpTransport()
                        val jsonFactory = JacksonFactory.getDefaultInstance()
                        val youtube =
                            YouTube.Builder(httpTransport, jsonFactory, Account.instance.credential)
                                .setApplicationName("Youtube_list_checker").build()

                        val request = youtube.playlistItems().list("snippet")
                        val index = saveListAdapter.selectedItem
                        saveListAdapter.unselect()
                        lifecycleScope.launch(Dispatchers.IO) {
                            var response = request.setMaxResults(50)
                                .setPlaylistId(lists[index].id).execute()
                            while (response.nextPageToken != null) {
                                Log.d("Items", response.items.size.toString())
                                response = request.setPageToken(response.nextPageToken).execute()
                            }
                            Log.d("Items", response.items.size.toString())
                        }
                    })
                }
            }
        }
        viewModel.refresh()
    }

}