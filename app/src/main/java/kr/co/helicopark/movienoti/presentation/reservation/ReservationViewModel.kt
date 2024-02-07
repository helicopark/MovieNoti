package kr.co.helicopark.movienoti.presentation.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCase
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(private val getPersonalReservationMovieListUseCase: GetPersonalReservationMovieListUseCase) : ViewModel() {
    private val _reservationList: MutableStateFlow<Resource<List<PersonalReservationMovie>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val reservationList: StateFlow<Resource<List<PersonalReservationMovie>>> = _reservationList

    fun getReservationMovieList() {
        viewModelScope.launch {
            getPersonalReservationMovieListUseCase.invoke().collect() {
                _reservationList.emit(it)
            }
        }
    }
//
//    fun getList(): Flow<Resource<List<ReservationItem>>> = flow {
//        Firebase.firestore.collection("private_list").document(Firebase.auth.currentUser?.uid ?: "1").collection("CGV").get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    document.documents[0]?.data?.get("date")
//
//                    document.documents.forEach {
//                        it.get("date")
//                    }
//
//
//                } else {
////                    _uiState.emit(Resource.Error("No such document", UiStatus.ERROR))
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e(ReservationFragment::class.java.simpleName, "get failed with ", exception)
//            }
//    }
}