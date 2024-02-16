package kr.co.helicopark.movienoti.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.helicopark.movienoti.CGV_MOVIE_URL
import kr.co.helicopark.movienoti.data.network.ApiService
import kr.co.helicopark.movienoti.data.repository.CgvNetworkRepository
import kr.co.helicopark.movienoti.data.repository.CgvNetworkRepositoryImpl
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.data.repository.MovieRepositoryImpl
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {
    /**
     * CGV 영화 리스트, CGV 홈페이지 크롤링
     */
    @Provides
    @Singleton
    fun provideCgvMovieList(): Connection {
        return Jsoup.connect(CGV_MOVIE_URL)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        cgvMovieListDocument: Connection,
    ): MovieRepository {
        return MovieRepositoryImpl(cgvMovieListDocument)
    }

    /**
     * CGV 영화 리스트 더보기, CGV 홈페이지 스크래핑
     */
    @Singleton
    @Provides
    fun provideCgvNetworkRepository(apiService: ApiService): CgvNetworkRepository {
        return CgvNetworkRepositoryImpl(apiService)
    }
}