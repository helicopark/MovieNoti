package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import javax.inject.Inject

class GetPersonalReservationMovieListUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : GetPersonalReservationMovieListUseCase {
    override fun invoke(): Flow<Resource<List<PersonalReservationMovie>>> = movieRepository.getPersonalReservationMovieList()
}