package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase firestore 에 개인 영화 예약 삭제하기
interface DeletePersonalReservationMovieUseCase {
    operator fun invoke(authUid: String, date: Long): Flow<Resource<String>>
}