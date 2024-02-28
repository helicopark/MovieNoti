package kr.co.helicopark.movienoti.domain.model

data class AdminReservationMovie(
    val date: Long,
    val reservationDate: Long,
    val brand: String,    // CGV, LOTTE, MEGABOX
    val movieTitle: String,
    val movieFormat: String,   // 4DX, SCREENX, IMAX ...
    val areaCode: String,
    val theaterCode: String,   // 0001, 0110 ...
    var token: String
)
