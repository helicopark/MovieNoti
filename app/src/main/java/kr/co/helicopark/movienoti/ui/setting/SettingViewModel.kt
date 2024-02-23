package kr.co.helicopark.movienoti.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigVersionUseCase
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val getRemoteConfigVersionUseCase: GetRemoteConfigVersionUseCase) : ViewModel() {
    private val _versionInfo: MutableStateFlow<Resource<RemoteConfigVersion>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val versionInfo: StateFlow<Resource<RemoteConfigVersion>> = _versionInfo

    init {
        viewModelScope.launch {
            getRemoteConfigVersionUseCase.invoke().collectLatest {
                _versionInfo.emit(it)
            }
        }
    }
}