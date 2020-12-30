package edu.uw.nhyoung.photogram

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.list_photo.view.*

class GalleryFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var user: FirebaseUser? = null
    private val TAG = "GALLERY_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_gallery, container, false)
        rootView.findViewById<View>(R.id.upload_button).setOnClickListener {
            findNavController().navigate(R.id.UploadFragment)
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // hide/show upload button based on user logged in
        val userObserver = Observer<FirebaseUser?> {
            user = if(it == null){
                upload_button.hide()
                null
            } else {
                upload_button.show()
                it
            }
        }
        viewModel.firebaseUserLiveData.observe(viewLifecycleOwner, userObserver)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseDatabase.getInstance().reference.child("photos").limitToLast(20)
        val options = FirebaseRecyclerOptions.Builder<Photo>()
            .setQuery(query, Photo::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build();
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        list_photo.layoutManager = layoutManager
        list_photo.adapter = FirebasePhotoAdapter(options)
    }

    inner class FirebasePhotoAdapter(private val options: FirebaseRecyclerOptions<Photo>) : FirebaseRecyclerAdapter<Photo, FirebasePhotoAdapter.ViewHolder>(options) {

        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.image
            val caption: TextView = view.caption
            val button: ImageButton = view.like
            val likes: TextView = view.number_likes
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebasePhotoAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_photo, parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: FirebasePhotoAdapter.ViewHolder, position: Int, photo: Photo) {
            // set photo
            Glide.with(this@GalleryFragment).load(photo.uri).into(holder.imageView)

            val likeCount = photo.likes?.size ?: 0
            holder.likes.text = likeCount.toString()

            if (user != null) {
                val userLiked = (photo.likes?.get(user!!.uid) != null)
                if (userLiked) {
                    holder.button.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.design_default_color_primary))
                } else {
                    holder.button.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.fui_bgTwitter))
                }
            }

            holder.caption.text = photo.title

            holder.button.setOnClickListener {
                if(user != null){
                    val userLiked = (photo.likes?.get(user!!.uid) != null)
                    val photoDetailsRef = getRef(position)
                    if(userLiked) {
                        photoDetailsRef.child("likes").child(user!!.uid).setValue(null)
                    } else {
                        photoDetailsRef.child("likes").child(user!!.uid).setValue(true)
                    }
                }
            }
        }

    }
}