package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 관리자 영화 예약 삭제하기
class DeleteAdminReservationMovieUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : DeleteAdminReservationMovieUseCase {
    override fun invoke(primaryKey: String): Flow<Resource<String>> =
        appRepository.deleteAdminReservationMovieList(primaryKey)
}