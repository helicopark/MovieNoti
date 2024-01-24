package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

interface SetPersonalReservationMovieListUseCase {
    operator fun invoke(personalReservationMovieItem: Any): Flow<Resource<String>>
}