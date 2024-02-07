package kr.co.helicopark.movienoti.data.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie

interface MovieRepository {
    fun getFirebaseAuth(): Flow<Resource<AuthResult>>

    fun getFirebaseToken(): Flow<Resource<String>>

    fun getCgvMovieList(): Flow<Resource<List<CgvMovie>>>

    fun setAdminReservationMovieList(adminReservationMovieItem: Any): Flow<Resource<String>>

    fun setPersonalReservationMovie(personalReservationMovieItem: Any): Flow<Resource<String>>
    fun getPersonalReservationMovieList(): Flow<Resource<List<PersonalReservationMovie>>>
}