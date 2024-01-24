package kr.co.helicopark.movienoti.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMoreMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMoreMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetCgvMovieListUseCase(getCgvMovieListUseCase: GetCgvMovieListUseCaseImpl): GetCgvMovieListUseCase

    @Binds
    fun bindGetCgvMoreMovieListUseCase(getCgvMoreMovieListUseCase: GetCgvMoreMovieListUseCaseImpl): GetCgvMoreMovieListUseCase

    @Binds
    fun bindSetAdminReservationMovieListUseCase(setAdminReservationMovieListUseCase: SetAdminReservationMovieListUseCaseImpl): SetAdminReservationMovieListUseCase

    @Binds
    fun bindSetPersonalReservationMovieListUseCase(setPersonalReservationMovieListUseCase: SetPersonalReservationMovieListUseCaseImpl): SetPersonalReservationMovieListUseCase

    @Binds
    fun bindGetPersonalReservationMovieListUseCase(getPersonalReservationMovieListUseCase: GetPersonalReservationMovieListUseCaseImpl): GetPersonalReservationMovieListUseCase
}

