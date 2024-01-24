package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import javax.inject.Inject

class SetPersonalReservationMovieListUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : SetPersonalReservationMovieListUseCase {
    override fun invoke(personalReservationMOvieItem: Any): Flow<Resource<String>> = movieRepository.setPersonalReservationMovie(personalReservationMOvieItem)
}