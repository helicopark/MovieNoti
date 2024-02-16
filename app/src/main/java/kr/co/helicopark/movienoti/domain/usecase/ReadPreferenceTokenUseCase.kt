package kr.co.helicopark.movienoti.domain.usecase

// DataStore에 저장한  가져오기
interface ReadPreferenceTokenUseCase {
    suspend fun invoke(): String
}