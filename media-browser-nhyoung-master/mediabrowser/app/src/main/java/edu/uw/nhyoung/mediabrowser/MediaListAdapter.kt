package edu.uw.nhyoung.mediabrowser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.findNavController
//import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.uw.nhyoung.mediabrowser.network.Movie

class MediaListAdapter(val onClickCallback: (Movie) -> Unit) : ListAdapter<Movie, MediaListAdapter.ViewHolder>(MovieDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtView: TextView = view.findViewById(R.id.txt_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theItem = getItem(position)

        val year = if (theItem.releaseDate.length > 0) {
            theItem.releaseDate.substring(0,4)
        } else {
            ""
        }
        holder.txtView.text = "${theItem!!.title} ($year)"

        // click on a movie
        with(holder.itemView) {
            tag = theItem
            setOnClickListener {
                onClickCallback(theItem)
            }

        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}