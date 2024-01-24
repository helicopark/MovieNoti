package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMoreMovie

interface CgvNetworkRepository {
    fun getCgvMoreMovieList(): Flow<Resource<List<CgvMoreMovie>>>
}