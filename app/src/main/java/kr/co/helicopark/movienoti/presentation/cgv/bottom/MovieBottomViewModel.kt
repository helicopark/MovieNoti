package kr.co.helicopark.movienoti.presentation.cgv.bottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCase
import javax.inject.Inject

@HiltViewModel
class MovieBottomViewModel @Inject constructor(
    private val setAdminReservationMovieListUseCase: SetAdminReservationMovieListUseCase,
    private val setPersonalReservationMovieListUseCase: SetPersonalReservationMovieListUseCase
) : ViewModel() {
    private val _setAdminReservationMovieState: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val setAdminReservationMovieState: StateFlow<Resource<String>> = _setAdminReservationMovieState

    private val _setPersonalReservationMovieState: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val setPersonalReservationMovieState: StateFlow<Resource<String>> = _setPersonalReservationMovieState

    fun setAdminReservationMovie(adminReservationMovieItem: Any) {
        viewModelScope.launch {
            setAdminReservationMovieListUseCase.invoke(adminReservationMovieItem).collectLatest {
                _setAdminReservationMovieState.emit(it)
            }
        }
    }

    fun setPersonalReservationMovieList(personalReservationMovieItem: Any) {
        viewModelScope.launch {
            setPersonalReservationMovieListUseCase.invoke(personalReservationMovieItem).collectLatest {
                _setPersonalReservationMovieState.emit(it)
            }
        }
    }
}