package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.RemoteConfigUpdate
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase RemoteConfig 업데이트 가져오기
class GetRemoteConfigUpdateUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : GetRemoteConfigUpdateUseCase {
    override fun invoke(): Flow<Resource<RemoteConfigUpdate>> = appRepository.getRemoteConfigUpdate()
}