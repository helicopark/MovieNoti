package kr.co.helicopark.movienoti.ui.cgv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMoreMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMovieListUseCase
import kr.co.helicopark.movienoti.ui.model.CgvMovieItem
import javax.inject.Inject

enum class CgvOrder {
    ReservationRateOrder,
    AbcOrder
}

@HiltViewModel
class CgvViewModel @Inject constructor(
    private val getCgvMovieListUseCase: GetCgvMovieListUseCase,
    private val getCgvMoreMovieListUseCase: GetCgvMoreMovieListUseCase
) : ViewModel() {
    private val _cgvMovieList: MutableStateFlow<Resource<List<CgvMovieItem>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val cgvMovieList: StateFlow<Resource<List<CgvMovieItem>>> = _cgvMovieList

    private val _cgvOrder: MutableStateFlow<CgvOrder> = MutableStateFlow(CgvOrder.ReservationRateOrder)
    val cgvOrder: StateFlow<CgvOrder> = _cgvOrder

    fun initCgvMovieList(order: CgvOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            _cgvOrder.emit(order)

            getCgvMovieListUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _cgvMovieList.emit(it)
                    }

                    is Resource.Success -> {
                        val cgvMovieItemList = it.data?.map { cgvMovie ->
                            CgvMovieItem(cgvMovie.thumb, cgvMovie.title, cgvMovie.reservationRate, cgvMovie.releaseDate)
                        }

                        if (!cgvMovieItemList.isNullOrEmpty()) {
                            fetchCgvMoreMovieList(cgvMovieItemList, order)
                        } else {
                            _cgvMovieList.emit(Resource.Error("CgvViewModel, fetchCgvMovieList, cgvMovieItemList isNullOrEmpty", it.state))
                        }
                    }

                    is Resource.Error -> {
                        _cgvMovieList.emit(Resource.Error(it.message, it.state))
                    }
                }
            }
        }
    }

    private fun fetchCgvMoreMovieList(cgvMovieList: List<CgvMovieItem>, order: CgvOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            getCgvMoreMovieListUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _cgvMovieList.emit(it)
                    }

                    is Resource.Success -> {
                        val cgvMovieArrayList = ArrayList<CgvMovieItem>(cgvMovieList)

                        it.data?.forEach { cgvMoreMovie ->
                            val reservationRate = String.format("예매율%s%%", cgvMoreMovie.reservationRate)

                            val dDay = if (cgvMoreMovie.dDay.contains("-")) {
                                ""
                            } else {
                                "D-${cgvMoreMovie.dDay}"
                            }

                            val releasedDate =
                                String.format(
                                    "<strong> %s <span>개봉</span> <em class=\"dday\">%s</em> </strong>",
                                    cgvMoreMovie.releasedDate,
                                    dDay
                                )

                            cgvMovieArrayList.add(
                                CgvMovieItem(
                                    cgvMoreMovie.thumb,
                                    cgvMoreMovie.title,
                                    reservationRate,
                                    releasedDate
                                )
                            )
                        }

                        if (cgvMovieList.isNotEmpty()) {
                            val sortedMovieList = when (order) {
                                // 기본값
                                CgvOrder.ReservationRateOrder -> {
                                    cgvMovieArrayList
                                }

                                CgvOrder.AbcOrder -> {
                                    cgvMovieArrayList.sortedBy { cgvMovieItem ->
                                        cgvMovieItem.title
                                    }
                                }
                            }

                            _cgvMovieList.emit(Resource.Success(sortedMovieList, UiStatus.SUCCESS))
                        } else {
                            _cgvMovieList.emit(Resource.Error("CgvViewModel, fetchCgvMoreMovieList, cgvMoreMovieItemList isNullOrEmpty", UiStatus.ERROR))
                        }
                    }

                    is Resource.Error -> {
                        _cgvMovieList.emit(Resource.Error(it.message, it.state))
                    }
                }
            }
        }
    }
}