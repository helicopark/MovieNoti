package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase firestore 에 개인 영화 예약 설정하기
interface SetPersonalReservationMovieUseCase {
    operator fun invoke(authUid: String, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>>
}