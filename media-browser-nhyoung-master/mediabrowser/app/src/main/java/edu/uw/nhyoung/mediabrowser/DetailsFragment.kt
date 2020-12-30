package edu.uw.nhyoung.mediabrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var movieName: String? = null
    private var movieYear: String? = null
    private var movieDescription: String? = null
    private var movieImage: String? = null
    private var movieID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailsFragmentArgs by navArgs()

        movieName = args.movieData.title
        movieYear = args.movieData.releaseDate.substring(0,4)
        movieDescription = args.movieData.overview
        movieImage = args.movieData.backdropPath
        movieID = args.movieData.id
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_details, container, false)
        rootView.findViewById<TextView>(R.id.txt_movie_name).text = "$movieName ($movieYear)"
        rootView.findViewById<TextView>(R.id.movie_description).text = "$movieDescription"
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/$movieImage").into(rootView.findViewById<ImageView>(R.id.movie_image));
        rootView.findViewById<Button>(R.id.find_similar).setOnClickListener {
            val args: DetailsFragmentArgs by navArgs()
            val action = DetailsFragmentDirections.actionToSublistFragment(args.movieData)
            findNavController().navigate(action)
        }
        return rootView
    }

}