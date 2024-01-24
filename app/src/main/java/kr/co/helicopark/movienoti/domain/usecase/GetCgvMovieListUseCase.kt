package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMovie

interface GetCgvMovieListUseCase {
    operator fun invoke(): Flow<Resource<List<CgvMovie>>>
}