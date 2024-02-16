package kr.co.helicopark.movienoti.ui.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.DeleteAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.DeletePersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val readFirebaseAuthUseCase: ReadPreferenceAuthUidUseCase,
    private val getPersonalReservationMovieListUseCase: GetPersonalReservationMovieListUseCase,
    private val deletePersonalReservationMovieUseCase: DeletePersonalReservationMovieUseCase,
    private val deleteAdminReservationMovieUseCase: DeleteAdminReservationMovieUseCase
) : ViewModel() {
    private val _reservationList: MutableStateFlow<Resource<List<PersonalReservationMovie>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val reservationList: StateFlow<Resource<List<PersonalReservationMovie>>> = _reservationList

    fun initReservationMovieList() {
        viewModelScope.launch {
            val authUid = readFirebaseAuthUseCase.invoke()
            getPersonalReservationMovieListUseCase.invoke(authUid).collect {
                _reservationList.emit(it)
            }
        }
    }

    fun deleteReservationMovie(authUid: String, date: Long) {
        viewModelScope.launch {
            deleteAdminReservationMovieUseCase.invoke(authUid + date).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        deletePersonalReservationMovieUseCase.invoke(authUid, date)
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}