package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import org.jsoup.Connection
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val cgvMovieListDocument: Connection,
) : MovieRepository {

    override fun getCgvMovieList(): Flow<Resource<List<CgvMovie>>> = flow {
        try {
            emit(Resource.Loading(UiStatus.LOADING))
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
            } else {
                emit(Resource.Error("영화 리스트 불러오기를 실패했어요", UiStatus.ERROR))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message, UiStatus.ERROR))
        }
    }
}