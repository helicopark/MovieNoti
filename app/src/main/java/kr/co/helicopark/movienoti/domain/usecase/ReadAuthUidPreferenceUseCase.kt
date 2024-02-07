package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

interface ReadAuthUidPreferenceUseCase {
    suspend fun invoke(): Flow<Resource<String>>
}