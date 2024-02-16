package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

// Firebase Auth 에서 Uid 가져오기
class GetFirebaseAuthUidUseCaseImpl @Inject constructor(private val appRepository: AppRepository) : GetFirebaseAuthUidUseCase {
    override fun invoke(): Flow<Resource<String>> = appRepository.getFirebaseAuth()
}