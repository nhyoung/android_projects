package edu.uw.nhyoung.mediabrowser.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "a86d6bf9e679a3bf078db115e2f47416"

// API Interface
interface MovieDBApiService {
    @GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") api_key: String = API_KEY, @Query("page")page: Int = 1): Call<ResponseResults>

    @GET("search/movie")
    fun searchMovies(@Query("query") title: String, @Query("api_key") api_key: String = API_KEY): Call<ResponseResults>

    @GET("movie/{movie_id}/similar")
    fun getSimilar(@Path("movie_id") movie_id: String, @Query("api_key") api_key: String = API_KEY): Call<ResponseResults>
}

// Initialize Moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Initialize Retrofit
private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object MovieDBApi{
    val retrofitService: MovieDBApiService by lazy {
        retrofit.create(MovieDBApiService::class.java)
    }
}

@Parcelize
data class ResponseResults (
    val results: List<Movie>
) : Parcelable


@Parcelize
data class Movie(
    val id: String,
    val title: String,

    @Json(name="release_date")
    val releaseDate: String,

    val overview: String,

    @Json(name="backdrop_path")
    val backdropPath: String // image path
): Parcelable