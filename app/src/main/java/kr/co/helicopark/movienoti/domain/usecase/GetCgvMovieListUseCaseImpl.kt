package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import javax.inject.Inject

class GetCgvMovieListUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : GetCgvMovieListUseCase {
    override fun invoke(): Flow<Resource<List<CgvMovie>>> = movieRepository.getCgvMovieList()
}