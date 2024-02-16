package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMoreMovie

// 더보기 영화리스트, CGV 홈페이지 스크래핑
interface GetCgvMoreMovieListUseCase {
    fun invoke(): Flow<Resource<List<CgvMoreMovie>>>
}