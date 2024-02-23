package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase RemoteConfig 버전 가져오기
class GetRemoteConfigVersionUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : GetRemoteConfigVersionUseCase {
    override fun invoke(): Flow<Resource<RemoteConfigVersion>> = appRepository.getRemoteConfigVersion()
}