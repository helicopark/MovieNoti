package kr.co.helicopark.movienoti.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import javax.inject.Inject
import javax.inject.Named


class AppRepositoryImpl @Inject constructor(
    private val firebaseAuthTask: Task<AuthResult>,
    private val firebaseTokenTask: Task<String>,
    @Named("Admin") private val adminReservationMovieRef: CollectionReference,
    @Named("Personal") private val personalReservationMovieRef: CollectionReference
) : AppRepository {
    override fun getFirebaseAuth(): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            firebaseAuthTask
                .addOnSuccessListener {
                    trySend(Resource.Success(it.user?.uid ?: "", UiStatus.SUCCESS))
                }.addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }.addOnCanceledListener {
                    trySend(Resource.Error("정보 받기를 취소했어요", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }

    override fun getFirebaseToken(): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            firebaseTokenTask
                .addOnSuccessListener {
                    trySend(Resource.Success(it, UiStatus.SUCCESS))
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("알림 토큰 받기를 취소했어요", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }

    /**
     * primaryKey: authUid + date
     */
    override fun setAdminReservationMovieList(primaryKey: String, adminReservationMovieItem: Any): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            adminReservationMovieRef.document(primaryKey).set(adminReservationMovieItem)
                .addOnSuccessListener { documentReference ->
                    trySend(Resource.Success("DocumentSnapshot added with ID: $documentReference", UiStatus.SUCCESS))
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("서버에 예약 영화 저장을 취소했어요", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }

    override fun setPersonalReservationMovie(authUid: String, personalReservationMovieItem: Any): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            personalReservationMovieRef.document(authUid).set(personalReservationMovieItem, SetOptions.merge())
                .addOnSuccessListener { documentReference ->
                    trySend(Resource.Success("DocumentSnapshot added with ID: $documentReference", UiStatus.SUCCESS))
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("영화 예약 저장을 취소했어요.", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }

    override fun getPersonalReservationMovieList(authUid: String): Flow<Resource<List<PersonalReservationMovie>>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            personalReservationMovieRef.document(authUid).get().addOnSuccessListener { documentSnapshot ->
                val personalReservationMovieList = documentSnapshot.data?.map {
                    val date = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["date"]?.toString()?.toLong() ?: 0L
                    val reservationDate = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["reservationDate"]?.toString()?.toLong() ?: 0L
                    val brand = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["brand"]?.toString() ?: ""
                    val movieName = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieName"]?.toString() ?: ""
                    val movieFormat = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieFormat"]?.toString() ?: ""
                    val areaCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["areaCode"]?.toString() ?: ""
                    val theaterCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["theaterCode"]?.toString() ?: ""
                    val thumb = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["thumb"]?.toString() ?: ""

                    PersonalReservationMovie(date, reservationDate, brand, movieName, movieFormat, areaCode, theaterCode, thumb)
                }

                val filteredPersonalReservationMovieList = personalReservationMovieList?.filter {
                    it.date > 0L
                            && it.reservationDate > 0L
                            && it.brand.isNotEmpty()
                            && it.movieName.isNotEmpty()
                            && it.movieFormat.isNotEmpty()
                            && it.areaCode.isNotEmpty()
                            && it.theaterCode.isNotEmpty()
                }?.map {
                    it
                }

                if (!filteredPersonalReservationMovieList.isNullOrEmpty()) {
                    trySend(Resource.Success(filteredPersonalReservationMovieList, UiStatus.SUCCESS))
                } else {
                    trySend(Resource.Error("empty", UiStatus.ERROR))
                }
            }.addOnFailureListener {
                trySend(Resource.Error(it.message, UiStatus.ERROR))
            }.addOnCanceledListener {
                trySend(Resource.Error("예약 영화 불러오기를 실패했어요", UiStatus.ERROR))
            }

            awaitClose { channel.close() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }
    }

    /**
     * primaryKey: authUid+date
     */
    override fun deleteAdminReservationMovieList(primaryKey: String): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            adminReservationMovieRef.document(primaryKey).delete()
                .addOnSuccessListener {
                    trySend(Resource.Success("DocumentSnapshot successfully deleted!", UiStatus.SUCCESS))
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("서버에 예약 영화 삭제를 취소했어요", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }

    override fun deletePersonalReservationMovieList(authUid: String, date: Long): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))

            val updates = hashMapOf<String, Any>(
                date.toString() to FieldValue.delete(),
            )

            personalReservationMovieRef.document(authUid).update(updates)
                .addOnSuccessListener {
                    trySend(Resource.Success("DocumentSnapshot successfully updated", UiStatus.SUCCESS))
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("영화 예약 삭제를 취소했어요.", UiStatus.ERROR))
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
        }

        awaitClose { channel.close() }
    }
}