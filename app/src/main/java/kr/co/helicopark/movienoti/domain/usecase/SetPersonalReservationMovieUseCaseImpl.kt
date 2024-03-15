package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에 개인 영화 예약 설정하기
class SetPersonalReservationMovieUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : SetPersonalReservationMovieUseCase {
    override fun invoke(authUid: String, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>> =
        appRepository.setPersonalReservationMovie(authUid, personalReservationMovie)
}