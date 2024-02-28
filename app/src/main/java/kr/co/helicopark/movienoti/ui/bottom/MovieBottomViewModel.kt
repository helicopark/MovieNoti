package kr.co.helicopark.movienoti.ui.bottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.AdminReservationMovie
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdateAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdatePersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.ui.cgv.CgvOrder
import kr.co.helicopark.movienoti.ui.model.CgvMovieItem
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
    private val _setAdminReservationMovieState: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val setAdminReservationMovieState: StateFlow<Resource<String>> = _setAdminReservationMovieState

    private val _setPersonalReservationMovieState: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val setPersonalReservationMovieState: StateFlow<Resource<String>> = _setPersonalReservationMovieState

    fun setAdminReservationMovie(
        date: Long,
        reservationDate: Long,
        brand: String,
        movieTitle: String,
        movieFormat: String,
        areaCode: String,
        theaterCode: String
    ) {
        viewModelScope.launch {
            val primaryKey = readPreferenceAuthUidUseCase.invoke() + date
            val token = readPreferenceTokenUseCase.invoke()
            val item = AdminReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, theaterCode, areaCode, token)

            setAdminReservationMovieUseCase.invoke(primaryKey, item).collect {
                _setAdminReservationMovieState.emit(it)
            }
        }
    }

    fun setPersonalReservationMovieList(personalReservationMovieItem: Any) {
        viewModelScope.launch {
            val authUid = readPreferenceAuthUidUseCase.invoke()

            setPersonalReservationMovieUseCase.invoke(authUid, personalReservationMovieItem).collect {
                when (it) {
                    is Resource.Loading -> {
                        _setPersonalReservationMovieState.emit(it)
                    }

                    is Resource.Success -> {
                        _setPersonalReservationMovieState.emit(it)
                    }

                    is Resource.Error -> {
                        _setPersonalReservationMovieState.emit(it)
                    }
                }
            }
        }
    }

    fun updateAdminReservationMovie(
        date: Long,
        reservationDate: Long,
        brand: String,
        movieTitle: String,
        movieFormat: String,
        areaCode: String,
        theaterCode: String
    ) {
        viewModelScope.launch {
            val primaryKey = readPreferenceAuthUidUseCase.invoke() + date
            val token = readPreferenceTokenUseCase.invoke()
            val item = AdminReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, theaterCode, areaCode, token)

            updateAdminReservationMovieUseCase.invoke(primaryKey, item)
        }
    }

    fun updatePersonalReservationMovieList(date: Long, personalReservationMovie: PersonalReservationMovie) {
        viewModelScope.launch {
            val authUid = readPreferenceAuthUidUseCase.invoke()

            updatePersonalReservationMovieUseCase.invoke(authUid, date, personalReservationMovie)
        }
    }
}