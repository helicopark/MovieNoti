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
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigUpdateUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigUpdateUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigVersionUseCase
import kr.co.helicopark.movienoti.domain.usecase.GetRemoteConfigVersionUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceAuthUidUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCase
import kr.co.helicopark.movienoti.domain.usecase.ReadPreferenceTokenUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SaveAuthUidPreferenceUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveAuthUidPreferenceUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SaveTokenPreferenceUseCase
import kr.co.helicopark.movienoti.domain.usecase.SaveTokenPreferenceUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetAdminReservationMovieUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.SetPersonalReservationMovieUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.UpdateAdminReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdateAdminReservationMovieUseCaseImpl
import kr.co.helicopark.movienoti.domain.usecase.UpdatePersonalReservationMovieUseCase
import kr.co.helicopark.movienoti.domain.usecase.UpdatePersonalReservationMovieUseCaseImpl

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
    fun bindSetAdminReservationMovieListUseCase(setAdminReservationMovieListUseCase: SetAdminReservationMovieUseCaseImpl): SetAdminReservationMovieUseCase

    @Binds
    fun bindGetPersonalReservationMovieListUseCase(getPersonalReservationMovieListUseCase: GetPersonalReservationMovieListUseCaseImpl): GetPersonalReservationMovieListUseCase

    @Binds
    fun bindSetPersonalReservationMovieListUseCase(setPersonalReservationMovieListUseCase: SetPersonalReservationMovieUseCaseImpl): SetPersonalReservationMovieUseCase

    @Binds
    fun bindRemoveAdminReservationMovieUseCase(removeAdminReservationMovieUseCase: DeleteAdminReservationMovieUseCaseImpl): DeleteAdminReservationMovieUseCase

    @Binds
    fun bindRemovePersonalReservationMovieUseCase(removePersonalReservationMovieUseCase: DeletePersonalReservationMovieUseCaseImpl): DeletePersonalReservationMovieUseCase

    @Binds
    fun bindGetRemoteConfigVersionUseCase(getRemoteConfigVersionUseCase: GetRemoteConfigVersionUseCaseImpl): GetRemoteConfigVersionUseCase

    @Binds
    fun bindGetRemoteConfigUpdateUseCase(getRemoteConfigUpdateUseCase: GetRemoteConfigUpdateUseCaseImpl): GetRemoteConfigUpdateUseCase

    @Binds
    fun bindReadAuthUidPreferenceUseCase(readAuthUidPreferenceUseCase: ReadPreferenceAuthUidUseCaseImpl): ReadPreferenceAuthUidUseCase

    @Binds
    fun bindSaveAuthUidPreferenceUseCase(saveAuthUidPreferenceUseCase: SaveAuthUidPreferenceUseCaseImpl): SaveAuthUidPreferenceUseCase

    @Binds
    fun bindReadTokenPreferenceUseCase(readTokenPreferenceUseCase: ReadPreferenceTokenUseCaseImpl): ReadPreferenceTokenUseCase

    @Binds
    fun bindSaveTokenPreferenceUseCase(saveTokenPreferenceUseCase: SaveTokenPreferenceUseCaseImpl): SaveTokenPreferenceUseCase

    @Binds
    fun bindUpdateAdminReservationMovieUseCase(updateAdminReservationMovieUseCase: UpdateAdminReservationMovieUseCaseImpl): UpdateAdminReservationMovieUseCase

    @Binds
    fun bindUpdatePersonalReservationMovieUseCase(updatePersonalReservationMovieUseCase: UpdatePersonalReservationMovieUseCaseImpl): UpdatePersonalReservationMovieUseCase
}

