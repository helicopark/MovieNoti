package kr.co.helicopark.movienoti

import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream


// Auth
const val NO_ID = "NO_ID"

// Firestore
const val PERSONAL_RESERVATION_MOVIE_LIST = "personalReservationMovieList"
const val ADMIN_RESERVATION_MOVIE_LIST = "adminReservationMovieList"

//CGV
const val CGV_MOVIE_URL = "http://www.cgv.co.kr/movies/?lt=1&ft=0"
const val CGV_SEARCH_URL = "http://www.cgv.co.kr/theaters/?areacode=01&theaterCode=0001&date=20240117"

const val CGV_API_URL = "http://www.cgv.co.kr"
const val CGV_MORE_MOVIE_LIST_PATH = "/common/ajax/movies.aspx/GetMovieMoreList"

const val AREA_LIST =
    "[{\"areaCode\":\"01\", \"areaName\":\"서울\"},{\"areaCode\":\"02\", \"areaName\":\"경기\"},{\"areaCode\":\"03\", \"areaName\":\"인천\"},{\"areaCode\":\"04\", \"areaName\":\"강원\"},{\"areaCode\":\"05\", \"areaName\":\"대전/충청\"},{\"areaCode\":\"06\", \"areaName\":\"대구\"},{\"areaCode\":\"07\", \"areaName\":\"부산/울산\"},{\"areaCode\":\"08\", \"areaName\":\"경상\"},{\"areaCode\":\"09\", \"areaName\":\"광주/전라/제주\"}]"

const val SEOUL_THEATER_LIST =
    "[{\"theaterCode\":\"0056\",\"theaterName\":\"CGV강남\"},{\"theaterCode\":\"0001\",\"theaterName\":\"CGV강변\"},{\"theaterCode\":\"0229\",\"theaterName\":\"CGV건대입구\"},{\"theaterCode\":\"0010\",\"theaterName\":\"CGV구로\"},{\"theaterCode\":\"0063\",\"theaterName\":\"CGV대학로\"},{\"theaterCode\":\"0252\",\"theaterName\":\"CGV동대문\"},{\"theaterCode\":\"0230\",\"theaterName\":\"CGV등촌\"},{\"theaterCode\":\"0009\",\"theaterName\":\"CGV명동\"},{\"theaterCode\":\"0105\",\"theaterName\":\"CGV명동역 씨네라이브러리\"},{\"theaterCode\":\"0057\",\"theaterName\":\"CGV미아\"},{\"theaterCode\":\"0288\",\"theaterName\":\"CGV방학\"},{\"theaterCode\":\"0030\",\"theaterName\":\"CGV불광\"},{\"theaterCode\":\"0046\",\"theaterName\":\"CGV상봉\"},{\"theaterCode\":\"0300\",\"theaterName\":\"CGV성신여대입구\"},{\"theaterCode\":\"0088\",\"theaterName\":\"CGV송파\"},{\"theaterCode\":\"0276\",\"theaterName\":\"CGV수유\"},{\"theaterCode\":\"0150\",\"theaterName\":\"CGV신촌아트레온\"},{\"theaterCode\":\"0040\",\"theaterName\":\"CGV압구정\"},{\"theaterCode\":\"0112\",\"theaterName\":\"CGV여의도\"},{\"theaterCode\":\"0292\",\"theaterName\":\"CGV연남 with 토레타!\"},{\"theaterCode\":\"0059\",\"theaterName\":\"CGV영등포\"},{\"theaterCode\":\"0074\",\"theaterName\":\"CGV왕십리\"},{\"theaterCode\":\"0013\",\"theaterName\":\"CGV용산아이파크몰\"},{\"theaterCode\":\"0131\",\"theaterName\":\"CGV중계\"},{\"theaterCode\":\"0199\",\"theaterName\":\"CGV천호\"},{\"theaterCode\":\"0107\",\"theaterName\":\"CGV청담씨네시티\"},{\"theaterCode\":\"0223\",\"theaterName\":\"CGV피카디리1958\"},{\"theaterCode\":\"0164\",\"theaterName\":\"CGV하계\"},{\"theaterCode\":\"0191\",\"theaterName\":\"CGV홍대\"},{\"theaterCode\":\"P001\",\"theaterName\":\"CINE de CHEF 압구정\"},{\"theaterCode\":\"P013\",\"theaterName\":\"CINE de CHEF 용산아이파크몰\"}]"
const val GYEONGGI_THEATER_LIST =
    "[{\"theaterCode\":\"0260\",\"theaterName\":\"CGV경기광주\"},{\"theaterCode\":\"0270\",\"theaterName\":\"CGV고양백석\"},{\"theaterCode\":\"0255\",\"theaterName\":\"CGV고양행신\"},{\"theaterCode\":\"0257\",\"theaterName\":\"CGV광교\"},{\"theaterCode\":\"0266\",\"theaterName\":\"CGV광교상현\"},{\"theaterCode\":\"0348\",\"theaterName\":\"CGV광명역\"},{\"theaterCode\":\"0232\",\"theaterName\":\"CGV구리\"},{\"theaterCode\":\"0358\",\"theaterName\":\"CGV구리갈매\"},{\"theaterCode\":\"0344\",\"theaterName\":\"CGV기흥\"},{\"theaterCode\":\"0278\",\"theaterName\":\"CGV김포\"},{\"theaterCode\":\"0188\",\"theaterName\":\"CGV김포운양\"},{\"theaterCode\":\"0298\",\"theaterName\":\"CGV김포한강\"},{\"theaterCode\":\"0329\",\"theaterName\":\"CGV남양주 화도\"},{\"theaterCode\":\"0351\",\"theaterName\":\"CGV다산\"},{\"theaterCode\":\"0236\",\"theaterName\":\"CGV동두천\"},{\"theaterCode\":\"0124\",\"theaterName\":\"CGV동백\"},{\"theaterCode\":\"0041\",\"theaterName\":\"CGV동수원\"},{\"theaterCode\":\"0106\",\"theaterName\":\"CGV동탄\"},{\"theaterCode\":\"0359\",\"theaterName\":\"CGV동탄그랑파사쥬\"},{\"theaterCode\":\"0265\",\"theaterName\":\"CGV동탄역\"},{\"theaterCode\":\"0233\",\"theaterName\":\"CGV동탄호수공원\"},{\"theaterCode\":\"0226\",\"theaterName\":\"CGV배곧\"},{\"theaterCode\":\"0155\",\"theaterName\":\"CGV범계\"},{\"theaterCode\":\"0015\",\"theaterName\":\"CGV부천\"},{\"theaterCode\":\"0194\",\"theaterName\":\"CGV부천역\"},{\"theaterCode\":\"0287\",\"theaterName\":\"CGV부천옥길\"},{\"theaterCode\":\"0049\",\"theaterName\":\"CGV북수원\"},{\"theaterCode\":\"0242\",\"theaterName\":\"CGV산본\"},{\"theaterCode\":\"0196\",\"theaterName\":\"CGV서현\"},{\"theaterCode\":\"0304\",\"theaterName\":\"CGV성남모란\"},{\"theaterCode\":\"0143\",\"theaterName\":\"CGV소풍\"},{\"theaterCode\":\"0274\",\"theaterName\":\"CGV스타필드시티위례\"},{\"theaterCode\":\"0073\",\"theaterName\":\"CGV시흥\"},{\"theaterCode\":\"0055\",\"theaterName\":\"CGV신세계경기\"},{\"theaterCode\":\"0211\",\"theaterName\":\"CGV안산\"},{\"theaterCode\":\"0279\",\"theaterName\":\"CGV안성\"},{\"theaterCode\":\"0003\",\"theaterName\":\"CGV야탑\"},{\"theaterCode\":\"0262\",\"theaterName\":\"CGV양주옥정\"},{\"theaterCode\":\"0338\",\"theaterName\":\"CGV역곡\"},{\"theaterCode\":\"0004\",\"theaterName\":\"CGV오리\"},{\"theaterCode\":\"0305\",\"theaterName\":\"CGV오산\"},{\"theaterCode\":\"0307\",\"theaterName\":\"CGV오산중앙\"},{\"theaterCode\":\"0271\",\"theaterName\":\"CGV용인\"},{\"theaterCode\":\"0113\",\"theaterName\":\"CGV의정부\"},{\"theaterCode\":\"0187\",\"theaterName\":\"CGV의정부태흥\"},{\"theaterCode\":\"0205\",\"theaterName\":\"CGV이천\"},{\"theaterCode\":\"0054\",\"theaterName\":\"CGV일산\"},{\"theaterCode\":\"0320\",\"theaterName\":\"CGV정왕\"},{\"theaterCode\":\"0148\",\"theaterName\":\"CGV파주문산\"},{\"theaterCode\":\"0310\",\"theaterName\":\"CGV파주야당\"},{\"theaterCode\":\"0181\",\"theaterName\":\"CGV판교\"},{\"theaterCode\":\"0195\",\"theaterName\":\"CGV평촌\"},{\"theaterCode\":\"0052\",\"theaterName\":\"CGV평택\"},{\"theaterCode\":\"0334\",\"theaterName\":\"CGV평택고덕\"},{\"theaterCode\":\"0214\",\"theaterName\":\"CGV평택소사\"},{\"theaterCode\":\"0309\",\"theaterName\":\"CGV포천\"},{\"theaterCode\":\"0326\",\"theaterName\":\"CGV하남미사\"},{\"theaterCode\":\"0301\",\"theaterName\":\"CGV화성봉담\"},{\"theaterCode\":\"0145\",\"theaterName\":\"CGV화정\"},{\"theaterCode\":\"0365\",\"theaterName\":\"DRIVE IN 용인크랙사이드\"}]"
const val INCHON_THEATER_LIST =
    "[{\"theaterCode\":\"0043\",\"theaterName\":\"CGV계양\"},{\"theaterCode\":\"0198\",\"theaterName\":\"CGV남주안\"},{\"theaterCode\":\"0021\",\"theaterName\":\"CGV부평\"},{\"theaterCode\":\"0325\",\"theaterName\":\"CGV송도타임스페이스\"},{\"theaterCode\":\"0247\",\"theaterName\":\"CGV연수역\"},{\"theaterCode\":\"0002\",\"theaterName\":\"CGV인천\"},{\"theaterCode\":\"0296\",\"theaterName\":\"CGV인천가정(루원시티)\"},{\"theaterCode\":\"0346\",\"theaterName\":\"CGV인천논현\"},{\"theaterCode\":\"0340\",\"theaterName\":\"CGV인천도화\"},{\"theaterCode\":\"0352\",\"theaterName\":\"CGV인천시민공원\"},{\"theaterCode\":\"0258\",\"theaterName\":\"CGV인천연수\"},{\"theaterCode\":\"0269\",\"theaterName\":\"CGV인천학익\"},{\"theaterCode\":\"0308\",\"theaterName\":\"CGV주안역\"},{\"theaterCode\":\"0235\",\"theaterName\":\"CGV청라\"}]"
const val GANGWON_THEATER_LIST =
    "[{\"theaterCode\":\"0139\",\"theaterName\":\"CGV강릉\"},{\"theaterCode\":\"0355\",\"theaterName\":\"CGV기린\"},{\"theaterCode\":\"0144\",\"theaterName\":\"CGV원주\"},{\"theaterCode\":\"0354\",\"theaterName\":\"CGV원통\"},{\"theaterCode\":\"0281\",\"theaterName\":\"CGV인제\"},{\"theaterCode\":\"0070\",\"theaterName\":\"CGV춘천\"}]"
const val DAEJEON_CHUNGCHEONG_THEATER_LIST =
    "[{\"theaterCode\":\"0261\",\"theaterName\":\"CGV논산\"},{\"theaterCode\":\"0207\",\"theaterName\":\"CGV당진\"},{\"theaterCode\":\"0007\",\"theaterName\":\"CGV대전\"},{\"theaterCode\":\"0286\",\"theaterName\":\"CGV대전가수원\"},{\"theaterCode\":\"0154\",\"theaterName\":\"CGV대전가오\"},{\"theaterCode\":\"0202\",\"theaterName\":\"CGV대전탄방\"},{\"theaterCode\":\"0127\",\"theaterName\":\"CGV대전터미널\"},{\"theaterCode\":\"0091\",\"theaterName\":\"CGV서산\"},{\"theaterCode\":\"0219\",\"theaterName\":\"CGV세종\"},{\"theaterCode\":\"0356\",\"theaterName\":\"CGV아산\"},{\"theaterCode\":\"0206\",\"theaterName\":\"CGV유성노은\"},{\"theaterCode\":\"0331\",\"theaterName\":\"CGV제천\"},{\"theaterCode\":\"0044\",\"theaterName\":\"CGV천안\"},{\"theaterCode\":\"0332\",\"theaterName\":\"CGV천안시청\"},{\"theaterCode\":\"0293\",\"theaterName\":\"CGV천안터미널\"},{\"theaterCode\":\"0110\",\"theaterName\":\"CGV천안펜타포트\"},{\"theaterCode\":\"0228\",\"theaterName\":\"CGV청주(서문)\"},{\"theaterCode\":\"0297\",\"theaterName\":\"CGV청주성안길\"},{\"theaterCode\":\"0282\",\"theaterName\":\"CGV청주율량\"},{\"theaterCode\":\"0142\",\"theaterName\":\"CGV청주지웰시티\"},{\"theaterCode\":\"0319\",\"theaterName\":\"CGV청주터미널\"},{\"theaterCode\":\"0284\",\"theaterName\":\"CGV충북혁신\"},{\"theaterCode\":\"0328\",\"theaterName\":\"CGV충주교현\"},{\"theaterCode\":\"0217\",\"theaterName\":\"CGV홍성\"}]"
const val DAEGU_THEATER_LIST =
    "[{\"theaterCode\":\"0345\",\"theaterName\":\"CGV대구\"},{\"theaterCode\":\"0157\",\"theaterName\":\"CGV대구수성\"},{\"theaterCode\":\"0108\",\"theaterName\":\"CGV대구스타디움\"},{\"theaterCode\":\"0185\",\"theaterName\":\"CGV대구아카데미\"},{\"theaterCode\":\"0343\",\"theaterName\":\"CGV대구연경\"},{\"theaterCode\":\"0216\",\"theaterName\":\"CGV대구월성\"},{\"theaterCode\":\"0256\",\"theaterName\":\"CGV대구죽전\"},{\"theaterCode\":\"0147\",\"theaterName\":\"CGV대구한일\"},{\"theaterCode\":\"0109\",\"theaterName\":\"CGV대구현대\"}]"
const val BUSAN_ULSAN_THEATER_LIST =
    "[{\"theaterCode\":\"0061\",\"theaterName\":\"CGV대연\"},{\"theaterCode\":\"0042\",\"theaterName\":\"CGV동래\"},{\"theaterCode\":\"0337\",\"theaterName\":\"CGV부산명지\"},{\"theaterCode\":\"0005\",\"theaterName\":\"CGV서면\"},{\"theaterCode\":\"0285\",\"theaterName\":\"CGV서면삼정타워\"},{\"theaterCode\":\"0303\",\"theaterName\":\"CGV서면상상마당\"},{\"theaterCode\":\"0089\",\"theaterName\":\"CGV센텀시티\"},{\"theaterCode\":\"0160\",\"theaterName\":\"CGV아시아드\"},{\"theaterCode\":\"0335\",\"theaterName\":\"CGV울산동구\"},{\"theaterCode\":\"0128\",\"theaterName\":\"CGV울산삼산\"},{\"theaterCode\":\"0264\",\"theaterName\":\"CGV울산신천\"},{\"theaterCode\":\"0246\",\"theaterName\":\"CGV울산진장\"},{\"theaterCode\":\"0306\",\"theaterName\":\"CGV정관\"},{\"theaterCode\":\"0245\",\"theaterName\":\"CGV하단아트몰링\"},{\"theaterCode\":\"0318\",\"theaterName\":\"CGV해운대\"},{\"theaterCode\":\"0159\",\"theaterName\":\"CGV화명\"},{\"theaterCode\":\"P004\",\"theaterName\":\"CINE de CHEF 센텀\"},{\"theaterCode\":\"0367\",\"theaterName\":\"DRIVE IN 영도\"}]"
const val GYEONGSANG_THEATER_LIST =
    "[{\"theaterCode\":\"0263\",\"theaterName\":\"CGV거제\"},{\"theaterCode\":\"0330\",\"theaterName\":\"CGV경산\"},{\"theaterCode\":\"0323\",\"theaterName\":\"CGV고성\"},{\"theaterCode\":\"0053\",\"theaterName\":\"CGV구미\"},{\"theaterCode\":\"0240\",\"theaterName\":\"CGV김천율곡\"},{\"theaterCode\":\"0028\",\"theaterName\":\"CGV김해\"},{\"theaterCode\":\"0311\",\"theaterName\":\"CGV김해율하\"},{\"theaterCode\":\"0239\",\"theaterName\":\"CGV김해장유\"},{\"theaterCode\":\"0033\",\"theaterName\":\"CGV마산\"},{\"theaterCode\":\"0097\",\"theaterName\":\"CGV북포항\"},{\"theaterCode\":\"0272\",\"theaterName\":\"CGV안동\"},{\"theaterCode\":\"0234\",\"theaterName\":\"CGV양산삼호\"},{\"theaterCode\":\"0324\",\"theaterName\":\"CGV진주혁신\"},{\"theaterCode\":\"0023\",\"theaterName\":\"CGV창원\"},{\"theaterCode\":\"0079\",\"theaterName\":\"CGV창원더시티\"},{\"theaterCode\":\"0283\",\"theaterName\":\"CGV창원상남\"},{\"theaterCode\":\"0156\",\"theaterName\":\"CGV통영\"}]"
const val GWANGJU_JEOLLA_JEJU_THEATER_LIST =
    "[{\"theaterCode\":\"0220\",\"theaterName\":\"CGV광양\"},{\"theaterCode\":\"0221\",\"theaterName\":\"CGV광양 엘에프스퀘어\"},{\"theaterCode\":\"0295\",\"theaterName\":\"CGV광주금남로\"},{\"theaterCode\":\"0193\",\"theaterName\":\"CGV광주상무\"},{\"theaterCode\":\"0210\",\"theaterName\":\"CGV광주용봉\"},{\"theaterCode\":\"0218\",\"theaterName\":\"CGV광주첨단\"},{\"theaterCode\":\"0244\",\"theaterName\":\"CGV광주충장로\"},{\"theaterCode\":\"0090\",\"theaterName\":\"CGV광주터미널\"},{\"theaterCode\":\"0215\",\"theaterName\":\"CGV광주하남\"},{\"theaterCode\":\"0237\",\"theaterName\":\"CGV나주\"},{\"theaterCode\":\"0289\",\"theaterName\":\"CGV목포\"},{\"theaterCode\":\"0280\",\"theaterName\":\"CGV목포평화광장\"},{\"theaterCode\":\"0225\",\"theaterName\":\"CGV서전주\"},{\"theaterCode\":\"0114\",\"theaterName\":\"CGV순천\"},{\"theaterCode\":\"0268\",\"theaterName\":\"CGV순천신대\"},{\"theaterCode\":\"0315\",\"theaterName\":\"CGV여수웅천\"},{\"theaterCode\":\"0020\",\"theaterName\":\"CGV익산\"},{\"theaterCode\":\"0213\",\"theaterName\":\"CGV전주고사\"},{\"theaterCode\":\"0336\",\"theaterName\":\"CGV전주에코시티\"},{\"theaterCode\":\"0179\",\"theaterName\":\"CGV전주효자\"},{\"theaterCode\":\"0186\",\"theaterName\":\"CGV정읍\"},{\"theaterCode\":\"0302\",\"theaterName\":\"CGV제주\"},{\"theaterCode\":\"0259\",\"theaterName\":\"CGV제주노형\"}]"

const val THEATER_LIST =
    "{\"01\":${SEOUL_THEATER_LIST}, \"02\":${GYEONGGI_THEATER_LIST}, \"03\":${INCHON_THEATER_LIST}, \"04\":${GANGWON_THEATER_LIST}, \"05\":${DAEJEON_CHUNGCHEONG_THEATER_LIST}, \"06\":${DAEGU_THEATER_LIST}, \"07\":${BUSAN_ULSAN_THEATER_LIST}, \"08\":${GYEONGSANG_THEATER_LIST}, \"09\":${GWANGJU_JEOLLA_JEJU_THEATER_LIST}}"

const val MOVIE_FORMAT =
    "[{\"movieFormat\":\"전체\"}" +
            ", {\"movieFormat\":\"2D\"}" +
//            ", {\"movieFormat\":\"4DX\"}" +
//            ", {\"movieFormat\":\"얼터너티브\"}" +
            ", {\"movieFormat\":\"IMAX\"}]"

const val SEOUL_CODE = "01"                     // 서울
const val GYEONGGI_CODE = "02"                  // 경기
const val INCHON_CODE = "03"                    // 인천
const val GANGWON_CODE = "04"                   // 강원
const val DAEJEON_CHUNGCHEONG_CODE = "05"       // 대전, 충청
const val DAEGU_CODE = "06"                     // 대구
const val BUSAN_ULSAN_CODE = "07"               // 부산, 울산
const val GYEONGSANG_CODE = "08"                // 경상
const val GWANGJU_JEOLLA_JEJU_CODE = "09"       // 광주, 전라, 제주

//서울
enum class SEOUL(private val location: String, private val code: String) {
    ERROR("에러", "0000"),
    GANGBEYON("강변", "0001"),
    GUNDAE("건대", "0229"),
    GURO("구로", "0010"),
    DAEHACKRO("대학로", "0063"),
    DONGDAEMUN("동대문", "0252"),
    DEOUNGCHON("등촌", "0230"),
    MYEONGDONG("명동", "0009"),
    MYEONGDONG_CINE("명동역 시네라이브러리", "0105"),
    MIA("미아", "0057"),
    BANGHAK("방학", "0288"),
    BULGWANG("불광", "0030"),
    SANGBONG("상봉", "0046"),
    SUNGSIN("성신여대입구", "0300"),
    SONGPA("0088", "송파"),
    SUYU("0276", "수유"),
    SINCHON_ATREON("0150", "신촌아트레온"),
    APGUJEONG("0040", "압구정"),
    YEOUIDO("0112", "여의도"),
    YEONNAM("0292", "연남 with 토레타!"),
    YEONGDEUNGPO("0059", "영등포"),
    WANGSIMNI("0074", "왕십리"),
    YONGSAN("0013", "용산"),
    JUNGGYE("0131", "중계"),
    CUNHO("0199", "천호"),
    CHUNGDAM("0107", "청담씨네시티"),
    PICCADILLY("0223", "피카디리1958"),
    HAGYE("0164", "하계"),
    HONGDAE("0191", "홍대");

    companion object {
        private val locations: Map<String, SEOUL> = Collections.unmodifiableMap(
//            Stream.of(values()).collect(Collectors.toMap(description,java.util.function.Function.identity()))
            Stream.of(*values()).collect(Collectors.toMap(SEOUL::getCode, Function.identity()))
        )

        fun find(location: String): SEOUL {
            return Optional.ofNullable(locations[location]).orElse(GURO)
        }
    }

    fun getCode(): String {
        return location
    }
}


class Const {
    val GANGNAM = "0056"          // 강남
    val GANGBEYON = "0001"        // 강변
    val GUNDAE = "0229"           // 건대
    val GURO = "0010"             // 구로
    val DAEHACKRO = "0063"        // 대학로
    val DONGDAEMUN = "0252"       // 동대문
    val DEOUNGCHON = "0230"       // 등촌
    val MYEONGDONG = "0009"       // 명동
    val MYEONGDONG_CINE = "0105"  // 명동역 시네라이브러리
    val MIA = "0057"              // 미아
    val BANGHAK = "0288"          // 방학
    val BULGWANG = "0030"         // 불광
    val SANGBONG = "0046"         // 상봉
    val SUNGSIN = "0300"          // 성신여대입구
    val SONGPA = "0088"           // 송파
    val SUYU = "0276"             // 수유
    val SINCHON_ATREON = "0150"   // 신촌아트레온
    val APGUJEONG = "0040"        // 압구정
    val YEOUIDO = "0112"          // 여의도
    val YEONNAM = "0292"          // 연남 with 토레타!
    val YEONGDEUNGPO = "0059"     // 영등포
    val WANGSIMNI = "0074"        // 왕십리
    val YONGSAN = "0013"          // 용산
    val JUNGGYE = "0131"          // 중계
    val CUNHO = "0199"            // 천호
    val CHUNGDAM = "0107"         // 청담씨네시티
    val PICCADILLY = "0223"       // 피카디리1958
    val HAGYE = "0164"            // 하계
    val HONGDAE = "0191"          // 홍대
}

const val GANGNAM = "0056"          // 강남
const val GANGBEYON = "0001"        // 강변
const val GUNDAE = "0229"           // 건대
const val GURO = "0010"             // 구로
const val DAEHACKRO = "0063"        // 대학로
const val DONGDAEMUN = "0252"       // 동대문
const val DEOUNGCHON = "0230"       // 등촌
const val MYEONGDONG = "0009"       // 명동
const val MYEONGDONG_CINE = "0105"  // 명동역 시네라이브러리
const val MIA = "0057"              // 미아
const val BANGHAK = "0288"          // 방학
const val BULGWANG = "0030"         // 불광
const val SANGBONG = "0046"         // 상봉
const val SUNGSIN = "0300"          // 성신여대입구
const val SONGPA = "0088"           // 송파
const val SUYU = "0276"             // 수유
const val SINCHON_ATREON = "0150"   // 신촌아트레온
const val APGUJEONG = "0040"        // 압구정
const val YEOUIDO = "0112"          // 여의도
const val YEONNAM = "0292"          // 연남 with 토레타!
const val YEONGDEUNGPO = "0059"     // 영등포
const val WANGSIMNI = "0074"        // 왕십리
const val YONGSAN = "0013"          // 용산
const val JUNGGYE = "0131"          // 중계
const val CUNHO = "0199"            // 천호
const val CHUNGDAM = "0107"         // 청담씨네시티
const val PICCADILLY = "0223"       // 피카디리1958
const val HAGYE = "0164"            // 하계
const val HONGDAE = "0191"          // 홍대
//const val APGUJEONG_CINE = "0040"//CINE de CHEF 압구정
//const val YONGSAN_CINE ="0013"//CINE de CHEF 용산아이파크몰