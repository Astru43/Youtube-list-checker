package com.astru43.youtube_checker.ui.main.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.astru43.youtube_checker.R

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SaveViewModel::class.java)
        // TODO: Use the ViewModel
    }
}