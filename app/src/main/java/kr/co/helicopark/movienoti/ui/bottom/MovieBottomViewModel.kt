package kr.co.helicopark.movienoti.ui.bottom

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.AdminReservationMovie
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdateAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdatePersonalReservationMovieUseCase
import javax.inject.Inject

@HiltViewModel
class MovieBottomViewModel @Inject constructor(
    private val readPreferenceAuthUidUseCase: ReadPreferenceAuthUidUseCase,
    private val readPreferenceTokenUseCase: ReadPreferenceTokenUseCase,
    private val setAdminReservationMovieUseCase: SetAdminReservationMovieUseCase,
    private val setPersonalReservationMovieUseCase: SetPersonalReservationMovieUseCase,
    private val updateAdminReservationMovieUseCase: UpdateAdminReservationMovieUseCase,
    private val updatePersonalReservationMovieUseCase: UpdatePersonalReservationMovieUseCase
) : ViewModel() {
    private val _setAdminReservationMovieState: MutableSharedFlow<Resource<String>> = MutableSharedFlow()
    val setAdminReservationMovieState: SharedFlow<Resource<String>> = _setAdminReservationMovieState

    private val _updateAdminReservationMovieState: MutableSharedFlow<Resource<String>> = MutableSharedFlow()
    val updateAdminReservationMovieState: SharedFlow<Resource<String>> = _updateAdminReservationMovieState

    fun setAdminReservationMovie(
        coroutineScope: CoroutineScope,
        date: Long,
        reservationDate: Long,
        brand: String,
        movieTitle: String,
        movieFormat: String,
        areaCode: String,
        theaterCode: String,
        thumb: String
    ) {
        coroutineScope.launch {
            val primaryKey = readPreferenceAuthUidUseCase.invoke() + date
            val token = readPreferenceTokenUseCase.invoke()
            val adminReservationMovie = AdminReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, areaCode, theaterCode, token)

            setAdminReservationMovieUseCase.invoke(primaryKey, adminReservationMovie).collect {
                _setAdminReservationMovieState.emit(it)
            }
        }

        coroutineScope.launch {
            val authUid = readPreferenceAuthUidUseCase.invoke()

            setPersonalReservationMovieUseCase.invoke(
                authUid,
                PersonalReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, areaCode, theaterCode, thumb)
            ).collect()
        }
    }

    fun updateAdminReservationMovie(
        coroutineScope: CoroutineScope,
        date: Long,
        reservationDate: Long,
        brand: String,
        movieTitle: String,
        movieFormat: String,
        areaCode: String,
        theaterCode: String,
        thumb: String
    ) {
        coroutineScope.launch {
            val primaryKey = readPreferenceAuthUidUseCase.invoke() + date
            val token = readPreferenceTokenUseCase.invoke()
            val adminReservationMovie = AdminReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, areaCode, theaterCode, token)

            updateAdminReservationMovieUseCase.invoke(primaryKey, adminReservationMovie).collect {
                _updateAdminReservationMovieState.emit(it)
            }
        }

        coroutineScope.launch {
            val authUid = readPreferenceAuthUidUseCase.invoke()

            updatePersonalReservationMovieUseCase.invoke(
                authUid,
                PersonalReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, areaCode, theaterCode, thumb)
            ).collect()
        }
    }
}