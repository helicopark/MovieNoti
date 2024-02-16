package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMovie

// CGV 영화 리스트, JSoup 으로 CGV 홈페이지 크롤링
interface GetCgvMovieListUseCase {
    operator fun invoke(): Flow<Resource<List<CgvMovie>>>
}