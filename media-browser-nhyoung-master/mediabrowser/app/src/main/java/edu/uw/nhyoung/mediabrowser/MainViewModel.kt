package edu.uw.nhyoung.mediabrowser

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uw.nhyoung.mediabrowser.network.Movie
import edu.uw.nhyoung.mediabrowser.network.MovieDBApi
import edu.uw.nhyoung.mediabrowser.network.ResponseResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"
    private var _movieData = MutableLiveData<List<Movie>>()

    init {
        getNowPlaying()
    }

    val movieData: LiveData<List<Movie>>
        get() = _movieData

    fun getNowPlaying() {
        MovieDBApi.retrofitService.getNowPlaying().enqueue(object: Callback<ResponseResults> {
            override fun onResponse(call: Call<ResponseResults>, response: Response<ResponseResults>) {
                val body = response.body()
                val movies = body!!.results
                Log.v(TAG, "$movies")
                _movieData.value = movies
            }

            override fun onFailure(call: Call<ResponseResults>, t: Throwable) {
                Log.e(TAG, "Failure getNowPlaying(): ${t.message}")
            }
        })
    }

    fun searchMovies(query: String) {
        MovieDBApi.retrofitService.searchMovies(query).enqueue(object:
            Callback<ResponseResults> {
            override fun onResponse(call: Call<ResponseResults>, response: Response<ResponseResults>) {
                val body = response.body()
                val movies = body!!.results
                Log.v(TAG, "$movies")
                _movieData.value = movies
            }

            override fun onFailure(call: Call<ResponseResults>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    fun getSimilar(id: String) {
        MovieDBApi.retrofitService.getSimilar(id).enqueue(object:
            Callback<ResponseResults> {
            override fun onResponse(call: Call<ResponseResults>, response: Response<ResponseResults>) {
                val body = response.body()
                val movies = body!!.results
                Log.v(TAG, "$body")
                _movieData.value = movies
            }

            override fun onFailure(call: Call<ResponseResults>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }
}