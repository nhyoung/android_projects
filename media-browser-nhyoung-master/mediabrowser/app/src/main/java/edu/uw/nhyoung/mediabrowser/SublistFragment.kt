package edu.uw.nhyoung.mediabrowser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uw.nhyoung.mediabrowser.network.Movie

class SublistFragment : Fragment() {

    private var movieID: String? = null
    private var movieName: String? = null
    private var movieDate: String? = null

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MediaListAdapter


    fun getSimilar(id: String) {
        viewModel.getSimilar(id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sublist, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val movieObserver = Observer<List<Movie>> {
            adapter.submitList(it)
        }
        val args: SublistFragmentArgs by navArgs()
        viewModel.movieData.observe(viewLifecycleOwner, movieObserver)
        movieID = args.movie.id
        movieName = args.movie.title
        movieDate = args.movie.releaseDate.toString().substring(0,4)
        getSimilar(movieID!!)

        // Set listener for home button
        rootView.findViewById<Button>(R.id.go_home).setOnClickListener {
            findNavController().navigate(R.id.HomeFragment)
        }

        val year = if (movieDate.toString().length > 0) {
            movieDate.toString().substring(0,4)
        } else {
            ""
        }
        rootView.findViewById<TextView>(R.id.movie_title).text = "$movieName ($year)"
        Log.v("SublistActivity", "$movieName ($movieDate)")

        // initialize recycler view
        adapter = MediaListAdapter {
            val action = HomeFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
        val recycler = rootView.findViewById<RecyclerView>(R.id.recycler_list_sublist)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter

        return rootView
    }
}