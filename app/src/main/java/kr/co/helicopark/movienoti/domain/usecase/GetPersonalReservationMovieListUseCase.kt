package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie

interface GetPersonalReservationMovieListUseCase {
    operator fun invoke(): Flow<Resource<List<PersonalReservationMovie>>>
}