package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase firestore 에서 개인 영화 예약 리스트 가져오기
class GetPersonalReservationMovieListUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : GetPersonalReservationMovieListUseCase {
    override fun invoke(authUid: String): Flow<Resource<List<PersonalReservationMovie>>> = appRepository.getPersonalReservationMovieList(authUid)
}