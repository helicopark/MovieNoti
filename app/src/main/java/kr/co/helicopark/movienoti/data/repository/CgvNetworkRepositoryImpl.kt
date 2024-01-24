package kr.co.helicopark.movienoti.data.repository

import com.google.gson.JsonParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.data.network.ApiService
import kr.co.helicopark.movienoti.domain.model.CgvMoreMovie
import retrofit2.HttpException
import java.util.Date
import javax.inject.Inject

class CgvNetworkRepositoryImpl @Inject constructor(private val apiService: ApiService) : CgvNetworkRepository {
    override fun getCgvMoreMovieList(): Flow<Resource<List<CgvMoreMovie>>> = flow {
        try {
            emit(Resource.Loading(UiStatus.LOADING))
            val response = apiService.getCgvMoreMovieList(1, 1, 0, Date().time.toInt())

            if (response.isSuccessful) {
                val resultJsonElement = JsonParser.parseString(response.body()?.resultJsonString ?: "")

                val cgvMoreMovieList = resultJsonElement.asJsonObject["List"].asJsonArray.map {
                    val thumb = it.asJsonObject["PosterImage"].asJsonObject["ThumbNailImage"].asString
                    val title = it.asJsonObject["Title"].asString
                    val reservationRate = it.asJsonObject["TicketRate"].asString
                    val releaseDate = it.asJsonObject["OpenDate"].asString
                    val openText = it.asJsonObject["OpenText"].asString
                    val dDay = it.asJsonObject["D_Day"].asString

                    CgvMoreMovie(thumb, title, reservationRate, releaseDate, openText, dDay)
                }

                emit(Resource.Success(cgvMoreMovieList, UiStatus.SUCCESS))
            } else {
                emit(Resource.Error("getCgvMoreMovieList, response fail: response.isSuccessful false", UiStatus.ERROR))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("getCgvMoreMovieList, HttpException: ${e.message}", UiStatus.ERROR))
        } catch (t: Throwable) {
            emit(Resource.Error("getCgvMoreMovieList, Throwable: ${t.message}", UiStatus.ERROR))
        }
    }
}