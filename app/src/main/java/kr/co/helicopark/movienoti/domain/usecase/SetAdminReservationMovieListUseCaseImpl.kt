package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import javax.inject.Inject

class SetAdminReservationMovieListUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : SetAdminReservationMovieListUseCase {
    override fun invoke( value: Any): Flow<Resource<String>> = movieRepository.setAdminReservationMovieList(value)
}