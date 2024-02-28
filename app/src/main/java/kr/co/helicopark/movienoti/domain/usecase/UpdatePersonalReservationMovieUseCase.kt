package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase firestore 에 개인 영화 예약 수정하기
interface UpdatePersonalReservationMovieUseCase {
    operator fun invoke(authUid: String, date: Long, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>>
}