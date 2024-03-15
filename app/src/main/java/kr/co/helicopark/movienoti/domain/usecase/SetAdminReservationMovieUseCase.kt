package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.AdminReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase firestore 에 관리자 영화 예약 설정하기
interface SetAdminReservationMovieUseCase {
    operator fun invoke(primaryKey: String, adminReservationMovie: AdminReservationMovie): Flow<Resource<String>>
}