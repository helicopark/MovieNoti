package kr.co.helicopark.movienoti.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.BuildConfig
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigUpdateUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveAuthUidPreferenceUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveTokenPreferenceUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFirebaseAuthUidUseCase: GetFirebaseAuthUidUseCase,
    private val saveAuthUidPreferenceUseCase: SaveAuthUidPreferenceUseCase,
    private val getFirebaseTokenUseCase: GetFirebaseTokenUseCase,
    private val saveTokenPreferenceUseCase: SaveTokenPreferenceUseCase,
    private val getRemoteConfigUpdateUseCase: GetRemoteConfigUpdateUseCase
) : ViewModel() {
    private val _userInfoStatus: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val userInfoStatus: StateFlow<Resource<String>> = _userInfoStatus

    private val _forcedUpdate: MutableSharedFlow<String> = MutableSharedFlow()
    val forcedUpdate: SharedFlow<String> = _forcedUpdate

    private val _optionalUpdate: MutableSharedFlow<String> = MutableSharedFlow()
    val optionalUpdate: SharedFlow<String> = _optionalUpdate

    fun checkUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            getFirebaseAuthUidUseCase.invoke().zip(getFirebaseTokenUseCase.invoke()) { auth, token ->
                if (auth is Resource.Success) {
                    saveAuthUidPreferenceUseCase.invoke(auth.data ?: "")
                }

                if (token is Resource.Success) {
                    saveTokenPreferenceUseCase.invoke(token.data ?: "")
                }

                if (auth is Resource.Error || token is Resource.Error) {
                    Resource.Error("서버 연결을 실패했어요. 다시 시도해주세요.", UiStatus.ERROR)
                } else {
                    // 로딩 및 성공 -> 성공 처리
                    Resource.Success("", UiStatus.SUCCESS)
                }
            }.collect {
                _userInfoStatus.emit(it)
            }
        }
    }

    fun checkUpdate() {
        viewModelScope.launch {
            getRemoteConfigUpdateUseCase.invoke().collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        if (BuildConfig.VERSION_CODE < (it.data?.forcedUpdateVersionCode ?: 0)) {
                            _forcedUpdate.emit(it.data?.forcedUpdateMessage ?: "")
                            return@collect
                        }

                        if (BuildConfig.VERSION_CODE < (it.data?.optionalUpdateVersionCode ?: 0)) {
                            _optionalUpdate.emit(it.data?.optionalUpdateMessage ?: "")
                            return@collect
                        }
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}