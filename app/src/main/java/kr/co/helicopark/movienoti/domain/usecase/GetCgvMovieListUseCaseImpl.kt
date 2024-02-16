package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import javax.inject.Inject

// CGV 영화 리스트, JSoup 으로 CGV 홈페이지 크롤링
class GetCgvMovieListUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : GetCgvMovieListUseCase {
    override fun invoke(): Flow<Resource<List<CgvMovie>>> = movieRepository.getCgvMovieList()
}