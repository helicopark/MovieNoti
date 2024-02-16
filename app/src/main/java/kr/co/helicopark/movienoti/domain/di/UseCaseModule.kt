package kr.co.helicopark.movienoti.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kr.co.helicopark.movienoti.domain.usecase.DeleteAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.DeleteAdminReservationMovieUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.DeletePersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.DeletePersonalReservationMovieUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMoreMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMoreMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetCgvMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseAuthUidUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetFirebaseTokenUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetPersonalReservationMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SaveAuthUidPreferenceUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveAuthUidPreferenceUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SaveTokenPreferenceUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveTokenPreferenceUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieListUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieListUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindGetFirebaseAuthUseCase(getFirebaseAuthUseCase: GetFirebaseAuthUidUseCaseImpl): GetFirebaseAuthUidUseCase

    @Binds
    fun bindGetFirebaseTokenUseCase(getFirebaseTokenUseCase: GetFirebaseTokenUseCaseImpl): GetFirebaseTokenUseCase

    @Binds
    fun bindGetCgvMovieListUseCase(getCgvMovieListUseCase: GetCgvMovieListUseCaseImpl): GetCgvMovieListUseCase

    @Binds
    fun bindGetCgvMoreMovieListUseCase(getCgvMoreMovieListUseCase: GetCgvMoreMovieListUseCaseImpl): GetCgvMoreMovieListUseCase

    @Binds
    fun bindSetAdminReservationMovieListUseCase(setAdminReservationMovieListUseCase: SetAdminReservationMovieListUseCaseImpl): SetAdminReservationMovieListUseCase

    @Binds
    fun bindGetPersonalReservationMovieListUseCase(getPersonalReservationMovieListUseCase: GetPersonalReservationMovieListUseCaseImpl): GetPersonalReservationMovieListUseCase

    @Binds
    fun bindSetPersonalReservationMovieListUseCase(setPersonalReservationMovieListUseCase: SetPersonalReservationMovieListUseCaseImpl): SetPersonalReservationMovieListUseCase

    @Binds
    fun bindRemoveAdminReservationMovieUseCase(removeAdminReservationMovieUseCase: DeleteAdminReservationMovieUseCaseImpl): DeleteAdminReservationMovieUseCase

    @Binds
    fun bindRemovePersonalReservationMovieUseCase(removePersonalReservationMovieUseCase: DeletePersonalReservationMovieUseCaseImpl): DeletePersonalReservationMovieUseCase

    @Binds
    fun bindReadAuthUidPreferenceUseCase(readAuthUidPreferenceUseCase: ReadPreferenceAuthUidUseCaseImpl): ReadPreferenceAuthUidUseCase

    @Binds
    fun bindSaveAuthUidPreferenceUseCase(saveAuthUidPreferenceUseCase: SaveAuthUidPreferenceUseCaseImpl): SaveAuthUidPreferenceUseCase

    @Binds
    fun bindReadTokenPreferenceUseCase(readTokenPreferenceUseCase: ReadPreferenceTokenUseCaseImpl): ReadPreferenceTokenUseCase

    @Binds
    fun bindSaveTokenPreferenceUseCase(saveTokenPreferenceUseCase: SaveTokenPreferenceUseCaseImpl): SaveTokenPreferenceUseCase
}

