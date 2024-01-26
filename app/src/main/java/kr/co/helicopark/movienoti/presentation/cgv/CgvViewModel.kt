package kr.co.helicopark.movienoti.presentation.cgv

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
import kr.co.helicopark.movienoti.presentation.model.CgvMovieItem
import javax.inject.Inject

@HiltViewModel
class CgvViewModel @Inject constructor(
    private val getCgvMovieListUseCase: GetCgvMovieListUseCase,
    private val getCgvMoreMovieListUseCase: GetCgvMoreMovieListUseCase
) : ViewModel() {
    private val _cgvMovieList: MutableStateFlow<Resource<List<CgvMovieItem>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    val cgvMovieList: StateFlow<Resource<List<CgvMovieItem>>> = _cgvMovieList

    fun initCgvMovieList(searchText: String, order: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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
                            fetchCgvMoreMovieList(cgvMovieItemList, searchText, order)
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

    private fun fetchCgvMoreMovieList(cgvMovieList: List<CgvMovieItem>, searchText: String, order: Int) {
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
                            val sortedMovieList = if (searchText.isNotEmpty()) {
                                cgvMovieArrayList.sortedBy { cgvMovieItem ->
                                    cgvMovieItem.title.contains(searchText)
                                }
                            } else {
                                if (order == 0) {
                                    cgvMovieArrayList.sortedByDescending { cgvMovieItem ->
                                        cgvMovieItem.reservationRate
                                    }
                                } else {
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