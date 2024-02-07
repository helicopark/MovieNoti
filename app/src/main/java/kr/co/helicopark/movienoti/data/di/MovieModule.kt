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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.helicopark.movienoti.ADMIN_RESERVATION_MOVIE_LIST
import kr.co.helicopark.movienoti.CGV_MOVIE_URL
import kr.co.helicopark.movienoti.PERSONAL_RESERVATION_MOVIE_LIST
import kr.co.helicopark.movienoti.data.network.ApiService
import kr.co.helicopark.movienoti.data.repository.CgvNetworkRepository
import kr.co.helicopark.movienoti.data.repository.CgvNetworkRepositoryImpl
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.data.repository.MovieRepositoryImpl
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Named
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("movinoti_datastore_preference")

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {
    @Singleton
    @Provides
    fun provideFirebaseAuthTask() =
        Firebase.auth.signInAnonymously()

    @Singleton
    @Provides
    fun provideFirebaseMessagingTokenTask() =
        Firebase.messaging.token

    @Singleton
    @Provides
    @Named("Admin")
    fun provideAdminReservationMoviesRef() =
        Firebase.firestore
            .collection(ADMIN_RESERVATION_MOVIE_LIST)

    @Singleton
    @Provides
    @Named("Personal")
    fun providePersonalReservationMoviesRef() =
        Firebase.firestore
            .collection(PERSONAL_RESERVATION_MOVIE_LIST)

    @Singleton
    @Provides
    fun provideMovieRepository(
        firebaseAuthTask: Task<AuthResult>,
        firebaseMessagingTokenTask: Task<String>,
        cgvMovieListDocument: Connection,
        @Named("Admin") adminReservationMovieRef: CollectionReference,
        @Named("Personal") personalReservationMovieRef: CollectionReference
    ): MovieRepository {
        return MovieRepositoryImpl(firebaseAuthTask, firebaseMessagingTokenTask, cgvMovieListDocument, adminReservationMovieRef, personalReservationMovieRef)
    }

    @Singleton
    @Provides
    fun provideCgvNetworkRepository(apiService: ApiService): CgvNetworkRepository {
        return CgvNetworkRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCgvMovieList(): Connection {
        return Jsoup.connect(CGV_MOVIE_URL)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}