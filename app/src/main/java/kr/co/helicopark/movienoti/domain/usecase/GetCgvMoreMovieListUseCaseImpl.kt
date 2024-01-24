package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.data.repository.CgvNetworkRepository
import kr.co.helicopark.movienoti.domain.model.CgvMoreMovie
import javax.inject.Inject

class GetCgvMoreMovieListUseCaseImpl @Inject constructor(private val cgvNetworkRepository: CgvNetworkRepository) : GetCgvMoreMovieListUseCase {
    override fun invoke(): Flow<Resource<List<CgvMoreMovie>>> = cgvNetworkRepository.getCgvMoreMovieList()
}