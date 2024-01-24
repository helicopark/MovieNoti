package kr.co.helicopark.movienoti.presentation.reservation

data class ReservationItem(
    val date: Long,
    val brand: String,    // CGV, LOTTE, MEGABOX
    val movieName: String,
    val movieFormat: String,   // 4DX, SCREENX, IMAX ...
    val theaterCode: String,   // 0001, 0110 ...
    val areaCode: String
)
