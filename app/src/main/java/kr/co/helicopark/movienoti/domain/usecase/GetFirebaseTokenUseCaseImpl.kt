package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase 에서 message Token 가져오기
class GetFirebaseTokenUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : GetFirebaseTokenUseCase {
    override fun invoke(): Flow<Resource<String>> = appRepository.getFirebaseToken()
}