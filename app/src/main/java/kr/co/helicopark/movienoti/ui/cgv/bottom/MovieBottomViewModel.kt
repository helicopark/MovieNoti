package kr.co.helicopark.movienoti.ui.cgv.bottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.ui.model.AdminReservationMovieItem
import javax.inject.Inject

@HiltViewModel
class MovieBottomViewModel @Inject constructor(
    private val readPreferenceAuthUidUseCase: ReadPreferenceAuthUidUseCase,
    private val readPreferenceTokenUseCase: ReadPreferenceTokenUseCase,
    private val setAdminReservationMovieListUseCase: SetAdminReservationMovieListUseCase,
    private val setPersonalReservationMovieListUseCase: SetPersonalReservationMovieListUseCase
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
        theaterCode: String,
        areaCode: String
    ) {
        viewModelScope.launch {
            val primaryKey = readPreferenceAuthUidUseCase.invoke() + date
            val token = readPreferenceTokenUseCase.invoke()
            val item = AdminReservationMovieItem(date, reservationDate, brand, movieTitle, movieFormat, theaterCode, areaCode, token)

            setAdminReservationMovieListUseCase.invoke(primaryKey, item).collectLatest {
                _setAdminReservationMovieState.emit(it)
            }
        }
    }

    fun setPersonalReservationMovieList(personalReservationMovieItem: Any) {
        viewModelScope.launch {
            val authUid = readPreferenceAuthUidUseCase.invoke()

            setPersonalReservationMovieListUseCase.invoke(authUid, personalReservationMovieItem).collectLatest {
                _setPersonalReservationMovieState.emit(it)
            }
        }
    }
}