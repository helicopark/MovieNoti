package kr.co.helicopark.movienoti.domain.model

data class RemoteConfigUpdate(
    val forcedUpdateVersionCode: Long,
    val forcedUpdateMessage: String,
    val optionalUpdateVersionCode: Long,
    val optionalUpdateMessage: String
)
