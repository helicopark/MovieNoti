package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import kr.co.helicopark.movienoti.domain.model.Resource

interface MovieRepository {
    fun getCgvMovieList(): Flow<Resource<List<CgvMovie>>>
}