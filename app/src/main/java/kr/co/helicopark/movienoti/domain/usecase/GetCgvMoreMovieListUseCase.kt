package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMoreMovie

interface GetCgvMoreMovieListUseCase {
    fun invoke(): Flow<Resource<List<CgvMoreMovie>>>
}