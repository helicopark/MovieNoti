package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import org.jsoup.Connection
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val cgvMovieListDocument: Connection,
) : MovieRepository {

    override fun getCgvMovieList(): Flow<Resource<List<CgvMovie>>> = flow {
        try {
            emit(Resource.Loading(UiStatus.LOADING))
            Timber.d("getCgvMovieList, loading")
            val cgvMovieList = ArrayList<CgvMovie>()

            val movieThumbElements = cgvMovieListDocument.get().select("span.thumb-image")
            val movieInfoElements = cgvMovieListDocument.get().select("div.box-contents")

            movieInfoElements.forEachIndexed { index, movieInfoElement ->
                val thumb = movieThumbElements.select("img")[index].absUrl("src")
                val title = movieInfoElement.select("strong.title").html()
                val reservationRate = movieInfoElement.select("strong.percent").html()
                val releasedDate = movieInfoElement.select("span.txt-info").html()

                cgvMovieList.add(CgvMovie(thumb, title, reservationRate, releasedDate))
            }

            if (cgvMovieList.isNotEmpty()) {
                emit(Resource.Success(cgvMovieList, UiStatus.SUCCESS))
                Timber.d("getCgvMovieList, success")
            } else {
                emit(Resource.Error("영화 리스트 불러오기를 실패했어요.", UiStatus.ERROR))
                Timber.e("getCgvMovieList, cgvMovieList is empty")
            }
        } catch (unknownHostException: UnknownHostException) {
            emit(Resource.Error("네트워크 환경이 불안정해요. 다시 시도해주세요.", UiStatus.ERROR))
            Timber.e(unknownHostException, "getCgvMovieList, cgvMovieList exception ${unknownHostException.message}")
        } catch (e: Exception) {
            emit(Resource.Error("서버에 오류가 발생했어요. 다시 시도해주세요.", UiStatus.ERROR))
            Timber.e(e, "getCgvMovieList, cgvMovieList exception ${e.message}")
        }
    }
}