package edu.uw.nhyoung.photogram

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_upload.*
import java.text.SimpleDateFormat

import java.util.*

class UploadFragment : Fragment() {

    private val PICK_PHOTO_CODE = 1234
    private val TAG = "UPLOAD_FRAGMENT"
    private var chosenPhoto: Uri? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload, container, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.firebaseUserLiveData.observe(viewLifecycleOwner) { user ->
            if(user == null){
                findNavController().navigate(R.id.GalleryFragment)
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choose_photo.setOnClickListener {
            val pickPhotoIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            if (pickPhotoIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(pickPhotoIntent, PICK_PHOTO_CODE)
            }
        }
        val preference = PreferenceManager.getDefaultSharedPreferences(this.activity)
        val defaultCaption = preference.getString("default_caption", "0")
        input_caption.setText(defaultCaption)
        upload.setOnClickListener {
            uploadPhoto(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            Log.v(TAG, "Picked an image")
            data?.data?.let {
                val imageView = choose_photo
                imageView.setImageURI(it)
                chosenPhoto = it
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun uploadPhoto(v: View) {
        if(chosenPhoto == null) {
            Toast.makeText(context, "Must choose an image to upload!", Toast.LENGTH_LONG).show()
        } else if(FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_LONG).show()
        } else {

            var photoTitle = input_caption.text.toString()
            photoTitle = if (photoTitle == "") {
                "Untitled"
            } else {
                photoTitle
            }

            val storage = FirebaseStorage.getInstance()
            val imagesRef = storage.reference.child("images")
            val user = FirebaseAuth.getInstance().currentUser!!

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "${user.uid}_${timeStamp}.png" //use UID for unique urls

            val newImageRef = imagesRef.child(fileName)
            val uploadTask = newImageRef.putFile(chosenPhoto!!)


            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Failed to upload picture: ${it.message}", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    Log.v(TAG, "Uploaded file to URL $uri")
                    recordImage(photoTitle, uri)
                }
            }
        }
    }

    private fun recordImage(displayName: String, uri: Uri) {
        val database = FirebaseDatabase.getInstance()
        val photosRef = database.getReference("photos")

        val user = FirebaseAuth.getInstance().currentUser //record these details as well?

        val photoDetails = Photo(uri.toString(), displayName, user?.displayName, user?.uid)

        photosRef.push().setValue(photoDetails).addOnSuccessListener {
            findNavController().navigate(R.id.GalleryFragment)
        }

    }
}