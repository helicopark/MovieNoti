package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase firestore 에 관리자 영화 예약 삭제하기
interface DeleteAdminReservationMovieUseCase {
    operator fun invoke(primaryKey: String): Flow<Resource<String>>
}