package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 개인 영화 예약 삭제하기
class DeletePersonalReservationMovieUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : DeletePersonalReservationMovieUseCase {
    override fun invoke(authUid: String, date: Long): Flow<Resource<String>> =
        appRepository.deletePersonalReservationMovie(authUid, date)
}