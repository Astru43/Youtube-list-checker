package com.astru43.youtube_checker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val lists = view?.findViewById<RecyclerView>(R.id.youtube_lists)
        val btn = view?.findViewById<Button>(R.id.save_btn)

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
                    viewModel.lists.observe(viewLifecycleOwner, {
                        Log.d("Selected2", saveListAdapter.selectedItem.toString())
                    })
                }
            }
        }
        viewModel.refresh()
    }
}