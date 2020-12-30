package edu.uw.nhyoung.mediabrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uw.nhyoung.mediabrowser.network.Movie
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var adapter: MediaListAdapter
    private lateinit var viewModel: MainViewModel

    fun getNowPlaying() {
        viewModel.getNowPlaying()
    }

    fun searchMovies() {
        val input = search_bar?.text.toString()
        if (input == "") {
            getNowPlaying()
        } else {
            viewModel.searchMovies(input)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val movieObserver = Observer<List<Movie>> {
            adapter.submitList(it)
        }
        viewModel.movieData.observe(viewLifecycleOwner, movieObserver)

        rootView.findViewById<Button>(R.id.search).setOnClickListener {
            searchMovies()
        }

        // initialize recycler view
        adapter = MediaListAdapter {
            val action = HomeFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
        val recycler = rootView.findViewById<RecyclerView>(R.id.recycler_list_home)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter

        return rootView
    }
}

