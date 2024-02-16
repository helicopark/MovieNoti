package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie

// Firebase firestore 에서 개인 영화 예약 리스트 가져오기
interface GetPersonalReservationMovieListUseCase {
    operator fun invoke(authUid: String): Flow<Resource<List<PersonalReservationMovie>>>
}