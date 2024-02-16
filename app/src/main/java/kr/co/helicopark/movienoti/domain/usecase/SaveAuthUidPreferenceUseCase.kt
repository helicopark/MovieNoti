package kr.co.helicopark.movienoti.domain.usecase

// DataStore에 uid 저장하기
interface SaveAuthUidPreferenceUseCase {
    suspend fun invoke(authUid: String)
}