package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.RemoteConfigUpdate
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase RemoteConfig 업데이트 가져오기
interface GetRemoteConfigUpdateUseCase {
    operator fun invoke(): Flow<Resource<RemoteConfigUpdate>>
}