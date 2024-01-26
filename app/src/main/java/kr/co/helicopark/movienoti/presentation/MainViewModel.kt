package kr.co.helicopark.movienoti.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseTokenUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getFirebaseTokenUseCase: GetFirebaseTokenUseCase) : ViewModel() {
    private val _firebaseToken: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))

    fun initFirebaseToken(): MutableStateFlow<Resource<String>> {
        viewModelScope.launch {
            getFirebaseTokenUseCase.invoke().collectLatest {
                _firebaseToken.emit(it)
            }
        }

        return _firebaseToken
    }
}