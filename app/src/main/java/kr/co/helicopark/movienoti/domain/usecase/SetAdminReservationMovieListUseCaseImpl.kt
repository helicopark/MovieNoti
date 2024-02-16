package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 관리자 영화 예약 설정하기
class SetAdminReservationMovieListUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : SetAdminReservationMovieListUseCase {
    override fun invoke(primaryKey: String, adminReservationMovieItem: Any): Flow<Resource<String>> =
        appRepository.setAdminReservationMovieList(primaryKey, adminReservationMovieItem)
}