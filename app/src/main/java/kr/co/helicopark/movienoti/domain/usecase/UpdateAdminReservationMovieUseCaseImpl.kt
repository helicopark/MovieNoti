package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.AdminReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 관리자 영화 예약 수정하기
class UpdateAdminReservationMovieUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : UpdateAdminReservationMovieUseCase {
    override fun invoke(primaryKey: String, adminReservationMovie: AdminReservationMovie): Flow<Resource<String>> =
        appRepository.updateAdminReservationMovie(primaryKey, adminReservationMovie)
}