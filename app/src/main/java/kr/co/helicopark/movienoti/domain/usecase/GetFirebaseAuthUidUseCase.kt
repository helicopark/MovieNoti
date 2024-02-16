package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase Auth 에서 Uid 가져오기
interface GetFirebaseAuthUidUseCase {
    fun invoke(): Flow<Resource<String>>
}