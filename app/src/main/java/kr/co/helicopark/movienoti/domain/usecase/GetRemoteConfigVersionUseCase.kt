package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase RemoteConfig 버전 가져오기
interface GetRemoteConfigVersionUseCase {
    operator fun invoke(): Flow<Resource<RemoteConfigVersion>>
}