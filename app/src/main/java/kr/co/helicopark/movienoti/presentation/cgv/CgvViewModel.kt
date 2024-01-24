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
import kr.co.helicopark.movienoti.presentation.model.CgvMoreMovieItem
import kr.co.helicopark.movienoti.presentation.model.CgvMovieItem
import javax.inject.Inject

@HiltViewModel
class CgvViewModel @Inject constructor(
    private val getCgvMovieListUseCase: GetCgvMovieListUseCase,
    private val getCgvMoreMovieListUseCase: GetCgvMoreMovieListUseCase
) : ViewModel() {
    private val _cgvMovieList: MutableStateFlow<Resource<List<CgvMovieItem>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))
    private val _cgvMoreMovieList: MutableStateFlow<Resource<List<CgvMoreMovieItem>>> = MutableStateFlow(Resource.Loading(UiStatus.LOADING))

    fun fetchCgvMovieList():StateFlow<Resource<List<CgvMovieItem>>> {
        viewModelScope.launch(Dispatchers.IO) {
            getCgvMovieListUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val cgvMovieItemList = it.data?.map { cgvMovie ->
                            CgvMovieItem(cgvMovie.thumb, cgvMovie.title, cgvMovie.reservationRate, cgvMovie.releaseDate)
                        }

                        if (!cgvMovieItemList.isNullOrEmpty()) {
                            _cgvMovieList.emit(Resource.Success(cgvMovieItemList, it.state))
                        } else {
                            _cgvMovieList.emit(Resource.Error("CgvViewModel, fetchCgvMovieList, cgvMovieItemList isNullOrEmpty", UiStatus.ERROR))
                        }
                    }

                    is Resource.Error -> {
                        _cgvMovieList.emit(Resource.Error(it.message, it.state))
                    }

                    else -> Unit
                }
            }
        }

        return _cgvMovieList
    }

    fun fetchCgvMoreMovieList():StateFlow<Resource<List<CgvMoreMovieItem>>> {
        viewModelScope.launch(Dispatchers.IO) {
            getCgvMoreMovieListUseCase.invoke().collectLatest {
                when(it) {
                    is Resource.Success -> {
                        val cgvMoreMovieItemList = it.data?.map { cgvMoreMovie ->
                            CgvMoreMovieItem(
                                cgvMoreMovie.thumb,
                                cgvMoreMovie.title,
                                cgvMoreMovie.reservationRate,
                                cgvMoreMovie.releasedDate,
                                cgvMoreMovie.openText,
                                cgvMoreMovie.dDay
                            )
                        }

                        if (!cgvMoreMovieItemList.isNullOrEmpty()) {
                            _cgvMoreMovieList.emit(Resource.Success(cgvMoreMovieItemList, UiStatus.SUCCESS))
                        } else {
                            _cgvMoreMovieList.emit(Resource.Error("CgvViewModel, fetchCgvMoreMovieList, cgvMoreMovieItemList isNullOrEmpty", UiStatus.ERROR))
                        }
                    }

                    is Resource.Error -> {
                        _cgvMovieList.emit(Resource.Error(it.message, it.state))
                    }

                    else -> Unit
                }
            }
        }

        return _cgvMoreMovieList
    }
}