package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
import kr.co.helicopark.movienoti.domain.model.Resource

interface AppRepository {
    fun getFirebaseAuth(): Flow<Resource<String>>
    fun getFirebaseToken(): Flow<Resource<String>>

    // 관리자용 영화 예약
    fun setAdminReservationMovieList(primaryKey: String, adminReservationMovieItem: Any): Flow<Resource<String>>

    // 개인용 영화 예약
    fun setPersonalReservationMovie(authUid: String, personalReservationMovieItem: Any): Flow<Resource<String>>
    fun getPersonalReservationMovieList(authUid: String): Flow<Resource<List<PersonalReservationMovie>>>

    // 영화 예약 삭제
    fun deleteAdminReservationMovieList(primaryKey: String): Flow<Resource<String>>
    fun deletePersonalReservationMovieList(authUid: String, date: Long): Flow<Resource<String>>

    // 최신 버전 가져오기
    fun getRemoteConfigVersion(): Flow<Resource<RemoteConfigVersion>>
}