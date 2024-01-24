package kr.co.helicopark.movienoti.data.network

import kr.co.helicopark.movienoti.CGV_MORE_MOVIE_LIST_PATH
import kr.co.helicopark.movienoti.data.dto.CgvMoreMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET(CGV_MORE_MOVIE_LIST_PATH)
    @Headers("Content-type: application/json")
    suspend fun getCgvMoreMovieList(
        @Query("listType") listType: Int,
        @Query("orderType") orderType: Int,
        @Query("filterType") filterType: Int,
        @Query("_") date: Int
    ): Response<CgvMoreMovieResponse>
}