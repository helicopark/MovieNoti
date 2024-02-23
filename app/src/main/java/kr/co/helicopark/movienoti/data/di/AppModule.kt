package kr.co.helicopark.movienoti.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.helicopark.movienoti.ADMIN_RESERVATION_MOVIE_LIST
import kr.co.helicopark.movienoti.BuildConfig
import kr.co.helicopark.movienoti.PERSONAL_RESERVATION_MOVIE_LIST
import kr.co.helicopark.movienoti.data.repository.AppRepository
import kr.co.helicopark.movienoti.data.repository.AppRepositoryImpl
import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import kr.co.helicopark.movienoti.data.repository.DataStoreRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("movinoti_datastore_preference")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /* Firebase */
    @Singleton
    @Provides
    fun provideFirebaseAuthTask() =
        Firebase.auth.signInAnonymously()

    @Singleton
    @Provides
    fun provideFirebaseMessagingTokenTask() =
        Firebase.messaging.token

    /**
     * 관리자용 영화 예약 리스트
     */
    @Singleton
    @Provides
    @Named("Admin")
    fun provideAdminReservationMoviesRef() =
        Firebase.firestore
            .collection(ADMIN_RESERVATION_MOVIE_LIST)

    /**
     * 개인용 영화 예약 리스트
     */
    @Singleton
    @Provides
    @Named("Personal")
    fun providePersonalReservationMoviesRef() =
        Firebase.firestore
            .collection(PERSONAL_RESERVATION_MOVIE_LIST)

    /**
     * 버전 정보 가져오기
     */
    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        if (BuildConfig.IS_DEBUG) {
            firebaseRemoteConfig.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build()
            )
        } else {
            firebaseRemoteConfig.setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build()
            )
        }

        val defaultMap = HashMap<String, Any>()
        defaultMap["versionCode"] = BuildConfig.VERSION_CODE.toString()
        defaultMap["versionName"] = BuildConfig.VERSION_NAME

        firebaseRemoteConfig.setDefaultsAsync(defaultMap)

        return firebaseRemoteConfig
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        firebaseAuthTask: Task<AuthResult>,
        firebaseMessagingTokenTask: Task<String>,
        firebaseRemoteConfig: FirebaseRemoteConfig,
        @Named("Admin") adminReservationMovieRef: CollectionReference,
        @Named("Personal") personalReservationMovieRef: CollectionReference
    ): AppRepository {
        return AppRepositoryImpl(firebaseAuthTask, firebaseMessagingTokenTask, firebaseRemoteConfig, adminReservationMovieRef, personalReservationMovieRef)
    }

    /* DataStore */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }
}