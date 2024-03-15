package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 개인 영화 예약 수정하기
class UpdatePersonalReservationMovieUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : UpdatePersonalReservationMovieUseCase {
    override fun invoke(authUid: String, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>> =
        appRepository.updatePersonalReservationMovie(authUid, personalReservationMovie)
}