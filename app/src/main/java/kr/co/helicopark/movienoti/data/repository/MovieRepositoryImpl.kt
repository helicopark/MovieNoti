package kr.co.helicopark.movienoti.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kr.co.helicopark.movienoti.NO_ID
import kr.co.helicopark.movienoti.domain.model.CgvMovie
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import org.jsoup.Connection
import javax.inject.Inject
import javax.inject.Named


class MovieRepositoryImpl @Inject constructor(
    private val firebaseTokenTask: Task<String>,
    private val cgvMovieListDocument: Connection,
    @Named("Admin") private val adminReservationMovieRef: CollectionReference,
    @Named("Personal") private val personalReservationMovieRef: CollectionReference
) : MovieRepository {
    override fun getFirebaseToken(): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))
        firebaseTokenTask.addOnSuccessListener {

        }

    }

    override fun getCgvMovieList(): Flow<Resource<List<CgvMovie>>> = flow {
        try {
            emit(Resource.Loading(UiStatus.LOADING))
            val cgvMovieList = ArrayList<CgvMovie>()

            val movieThumbElements = cgvMovieListDocument.get().select("span.thumb-image")
            val movieInfoElements = cgvMovieListDocument.get().select("div.box-contents")

            movieInfoElements.forEachIndexed { index, movieInfoElement ->
                val thumb = movieThumbElements.select("img")[index].absUrl("src")
                val title = movieInfoElement.select("strong.title").html()
                val reservationRate = movieInfoElement.select("strong.percent").html()
                val releasedDate = movieInfoElement.select("span.txt-info").html()

                cgvMovieList.add(CgvMovie(thumb, title, reservationRate, releasedDate))
            }

            if (cgvMovieList.isNotEmpty()) {
                emit(Resource.Success(cgvMovieList, UiStatus.SUCCESS))
            } else {
                emit(Resource.Error("MovieRepositoryImpl, getCgvMovieThumbList, cgvMovieList is Empty", UiStatus.ERROR))
            }
        } catch (e: Exception) {
            emit(Resource.Error("MovieRepositoryImpl, Exception, ${e.message}", UiStatus.ERROR))
        }
    }

    override fun setAdminReservationMovieList(adminReservationMovieItem: Any): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))

        adminReservationMovieRef.add(adminReservationMovieItem)
            .addOnSuccessListener { documentReference ->
                trySend(Resource.Success("DocumentSnapshot added with ID: $documentReference", UiStatus.SUCCESS))
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error("setReservationMovieList, addOnFailureListener:${e.message}", UiStatus.ERROR))
            }
            .addOnCanceledListener {
                trySend(Resource.Error("setReservationMovieList, addOnCanceledListener", UiStatus.ERROR))
            }

        awaitClose { }
    }

    override fun setPersonalReservationMovie(personalReservationMovieItem: Any): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))
        personalReservationMovieRef.document(Firebase.auth.currentUser?.uid ?: NO_ID).set(personalReservationMovieItem, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                trySend(Resource.Success("DocumentSnapshot added with ID: $documentReference", UiStatus.SUCCESS))
            }
            .addOnFailureListener { e ->
                trySend(Resource.Error("setReservationMovieList, addOnFailureListener:${e.message}", UiStatus.ERROR))
            }
            .addOnCanceledListener {
                trySend(Resource.Error("setReservationMovieList, addOnCanceledListener", UiStatus.ERROR))
            }

        awaitClose { }
    }

    override fun getPersonalReservationMovieList(): Flow<Resource<List<PersonalReservationMovie>>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))

        personalReservationMovieRef.document(Firebase.auth.currentUser?.uid ?: NO_ID).get().addOnSuccessListener { documentSnapshot ->
            val personalReservationMovieList = documentSnapshot.data?.map {
                val date = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["date"]?.toString()?.toLong() ?: 0L
                val brand = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["brand"]?.toString() ?: ""
                val movieName = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieName"]?.toString() ?: ""
                val movieFormat = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieFormat"]?.toString() ?: ""
                val areaCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["areaCode"]?.toString() ?: ""
                val theaterCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["theaterCode"]?.toString() ?: ""

                PersonalReservationMovie(date, brand, movieName, movieFormat, areaCode, theaterCode)
            }

            val isRightInfo = personalReservationMovieList?.any {
                it.date > 0L
                        && it.brand.isNotEmpty()
                        && it.movieName.isNotEmpty()
                        && it.movieFormat.isNotEmpty()
                        && it.areaCode.isNotEmpty()
                        && it.theaterCode.isNotEmpty()
            }

            if (isRightInfo == true) {
                trySend(Resource.Success(personalReservationMovieList, UiStatus.SUCCESS))
            } else {
                trySend(Resource.Error("getPersonalReservationMovieList, isRightInfo: false", UiStatus.ERROR))
            }
        }.addOnFailureListener {
            trySend(Resource.Error("getPersonalReservationMovieList, addOnFailureListener:${it.message}", UiStatus.ERROR))
        }.addOnCanceledListener {
            trySend(Resource.Error("getPersonalReservationMovieList, addOnCanceledListener", UiStatus.ERROR))
        }

        awaitClose { }
    }
}