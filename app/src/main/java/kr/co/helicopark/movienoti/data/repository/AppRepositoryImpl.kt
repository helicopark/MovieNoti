package kr.co.helicopark.movienoti.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kr.co.helicopark.movienoti.NO_ID
import kr.co.helicopark.movienoti.domain.model.AdminReservationMovie
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.RemoteConfigUpdate
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class AppRepositoryImpl @Inject constructor(
    private val firebaseAuthTask: Task<AuthResult>,
    private val firebaseTokenTask: Task<String>,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    @Named("Admin") private val adminReservationMovieRef: CollectionReference,
    @Named("Personal") private val personalReservationMovieRef: CollectionReference
) : AppRepository {
    override fun getFirebaseAuth(): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("getFirebaseAuth, loading")

            firebaseAuthTask
                .addOnSuccessListener {
                    trySend(Resource.Success(it.user?.uid ?: NO_ID, UiStatus.SUCCESS))
                    Timber.d("getFirebaseAuth, success ${it.user?.uid}")
                }.addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                    Timber.e(e, "getFirebaseAuth, fail ${e.message}")
                }.addOnCanceledListener {
                    trySend(Resource.Error("정보 받기를 취소했어요", UiStatus.ERROR))
                    Timber.e("getFirebaseAuth, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "getFirebaseAuth, exception ${e.message}")
        }

        awaitClose {
            channel.close()
            Timber.d("getFirebaseAuth, awaitClose")
        }
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
    override fun setAdminReservationMovie(primaryKey: String, adminReservationMovie: AdminReservationMovie): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("setAdminReservationMovie, loading")

            adminReservationMovieRef.document(primaryKey).set(adminReservationMovie)
                .addOnSuccessListener { _ ->
                    trySend(Resource.Success("성공", UiStatus.SUCCESS))
                    Timber.d("setAdminReservationMovie, success")
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error("서버에 영화 오픈 알림 저장을 실패했어요.", UiStatus.ERROR))
                    Timber.e(e, "setAdminReservationMovie, fail ${e.message}")
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("서버에 영화 오픈 알림 저장을 취소했어요.", UiStatus.ERROR))
                    Timber.e("setAdminReservationMovie, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "setAdminReservationMovie, exception ${e.message}")
        }

        awaitClose { channel.close() }
    }

    override fun setPersonalReservationMovie(authUid: String, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("setPersonalReservationMovie, loading")

            val data = hashMapOf<String, Any>(
                personalReservationMovie.date.toString() to personalReservationMovie,
            )

            personalReservationMovieRef.document(authUid).set(data, SetOptions.merge())
                .addOnSuccessListener { _ ->
                    trySend(Resource.Success("success", UiStatus.SUCCESS))
                    Timber.d("setPersonalReservationMovie, success")
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error("영화 오픈 알림 저장을 실패했어요.", UiStatus.ERROR))
                    Timber.e(e, "setPersonalReservationMovie, fail ${e.message}")
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("영화 오픈 알림 저장을 취소했어요.", UiStatus.ERROR))
                    Timber.e("setPersonalReservationMovie, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "setAdminReservationMovie, exception ${e.message}")
        }

        awaitClose {
            channel.close()
        }
    }

    override fun getPersonalReservationMovieList(authUid: String): Flow<Resource<List<PersonalReservationMovie>>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("getPersonalReservationMovieList, loading")

            if (authUid.isEmpty()) {
                trySend(Resource.Success(arrayListOf(), UiStatus.SUCCESS))
                Timber.d("getPersonalReservationMovieList, success")
                close()
            }

            personalReservationMovieRef.document(authUid).get().addOnSuccessListener { documentSnapshot ->
                val personalReservationMovieList = documentSnapshot.data?.map {
                    val date = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["date"]?.toString()?.toLong() ?: 0L
                    val reservationDate = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["reservationDate"]?.toString()?.toLong() ?: 0L
                    val brand = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["brand"]?.toString() ?: ""
                    val movieTitle = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieTitle"]?.toString() ?: ""
                    val movieFormat = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["movieFormat"]?.toString() ?: ""
                    val areaCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["areaCode"]?.toString() ?: ""
                    val theaterCode = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["theaterCode"]?.toString() ?: ""
                    val thumb = (documentSnapshot.data?.get(it.key) as HashMap<*, *>)["thumb"]?.toString() ?: ""

                    PersonalReservationMovie(date, reservationDate, brand, movieTitle, movieFormat, areaCode, theaterCode, thumb)
                }

                val filteredPersonalReservationMovieList = personalReservationMovieList?.filter {
                    it.date > 0L
                            && it.reservationDate > 0L
                            && it.brand.isNotEmpty()
                            && it.movieTitle.isNotEmpty()
                            && it.movieFormat.isNotEmpty()
                            && it.areaCode.isNotEmpty()
                            && it.theaterCode.isNotEmpty()
                }?.map {
                    it
                }

                filteredPersonalReservationMovieList?.sortedBy {
                    it.date
                }

                if (filteredPersonalReservationMovieList != null) {
                    trySend(Resource.Success(filteredPersonalReservationMovieList, UiStatus.SUCCESS))
                    Timber.d("getPersonalReservationMovieList, success")
                } else {
                    trySend(Resource.Error("예약 영화 불러오기를 실패했어요", UiStatus.ERROR))
                    Timber.e("getPersonalReservationMovieList, filteredPersonalReservationMovieList is null")
                }
            }.addOnFailureListener {
                trySend(Resource.Error("예약 영화 불러오기를 실패했어요", UiStatus.ERROR))
                Timber.e(it, "getPersonalReservationMovieList, fail ${it.message}")
            }.addOnCanceledListener {
                trySend(Resource.Error("예약 영화 불러오기를 취소했어요", UiStatus.ERROR))
                Timber.e("getPersonalReservationMovieList, cancel")
            }
            awaitClose { channel.close() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "getPersonalReservationMovieList, fail ${e.message}")
        }
    }

    override fun updateAdminReservationMovie(primaryKey: String, adminReservationMovie: AdminReservationMovie): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("updateAdminReservationMovie, loading")

            adminReservationMovieRef.document(primaryKey).set(adminReservationMovie)
                .addOnSuccessListener {
                    trySend(Resource.Success("DocumentSnapshot successfully updated!", UiStatus.SUCCESS))
                    Timber.d("updateAdminReservationMovie, success")
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                    Timber.e(e, "updateAdminReservationMovie, fail, ${e.message}")
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("예약 영화 수정을 취소했어요", UiStatus.ERROR))
                    Timber.e("updateAdminReservationMovie, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "updateAdminReservationMovie, exception ${e.message}")
        }

        awaitClose { channel.close() }
    }

    override fun updatePersonalReservationMovie(authUid: String, personalReservationMovie: PersonalReservationMovie): Flow<Resource<String>> =
        callbackFlow {
            try {
                trySend(Resource.Loading(UiStatus.LOADING))
                Timber.d("updatePersonalReservationMovie, loading")

                val updates = hashMapOf<String, Any>(
                    personalReservationMovie.date.toString() to personalReservationMovie,
                )

                personalReservationMovieRef.document(authUid).update(updates)
                    .addOnSuccessListener {
                        trySend(Resource.Success("DocumentSnapshot successfully updated", UiStatus.SUCCESS))
                        Timber.d("updatePersonalReservationMovie, success")
                    }
                    .addOnFailureListener { e ->
                        trySend(Resource.Error("영화 예약 수정을 실패했어요.", UiStatus.ERROR))
                        Timber.e(e, "updatePersonalReservationMovie, fail ${e.message}")
                    }
                    .addOnCanceledListener {
                        trySend(Resource.Error("영화 예약 수정을 취소했어요.", UiStatus.ERROR))
                        Timber.e("updatePersonalReservationMovie, cancel")
                    }
            } catch (e: Exception) {
                trySend(Resource.Error(e.message, UiStatus.ERROR))
                Timber.e(e, "updatePersonalReservationMovie, exception ${e.message}")
            }

            awaitClose { channel.close() }
        }

    /**
     * primaryKey: authUid+date
     */
    override fun deleteAdminReservationMovie(primaryKey: String): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("deleteAdminReservationMovie, loading")

            adminReservationMovieRef.document(primaryKey).delete()
                .addOnSuccessListener {
                    trySend(Resource.Success("DocumentSnapshot successfully deleted!", UiStatus.SUCCESS))
                    Timber.d("deleteAdminReservationMovie, success")
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                    Timber.e(e, "deleteAdminReservationMovie, fail ${e.message}")
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("서버에 예약 영화 삭제를 취소했어요", UiStatus.ERROR))
                    Timber.e("deleteAdminReservationMovie, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "deleteAdminReservationMovie, exception ${e.message}")
        }

        awaitClose { channel.close() }
    }

    override fun deletePersonalReservationMovie(authUid: String, date: Long): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading(UiStatus.LOADING))
            Timber.d("deletePersonalReservationMovie, loading")

            val updates = hashMapOf<String, Any>(
                date.toString() to FieldValue.delete(),
            )

            personalReservationMovieRef.document(authUid).update(updates)
                .addOnSuccessListener {
                    trySend(Resource.Success("DocumentSnapshot successfully updated", UiStatus.SUCCESS))
                    Timber.d("deletePersonalReservationMovie, success")
                }
                .addOnFailureListener { e ->
                    trySend(Resource.Error(e.message, UiStatus.ERROR))
                    Timber.e(e, "deletePersonalReservationMovie, fail ${e.message}")
                }
                .addOnCanceledListener {
                    trySend(Resource.Error("영화 예약 삭제를 취소했어요.", UiStatus.ERROR))
                    Timber.e("deletePersonalReservationMovie, cancel")
                }
        } catch (e: Exception) {
            trySend(Resource.Error(e.message, UiStatus.ERROR))
            Timber.e(e, "deletePersonalReservationMovie, exception ${e.message}")
        }

        awaitClose { channel.close() }
    }

    override fun getRemoteConfigVersion(): Flow<Resource<RemoteConfigVersion>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))
        Timber.d("getRemoteConfigVersion, loading")

        firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
            val versionCode: Long = firebaseRemoteConfig.getLong("versionCode")
            val versionName: String = firebaseRemoteConfig.getString("versionName")

            trySend(Resource.Success(RemoteConfigVersion(versionCode, versionName), UiStatus.SUCCESS))
            Timber.d("getRemoteConfigVersion, success code: $versionCode name: $versionName")
        }.addOnFailureListener {
            trySend(Resource.Error("", UiStatus.ERROR))
            Timber.e(it, "getRemoteConfigVersion, fail ${it.message}")
        }.addOnCanceledListener {
            trySend(Resource.Error("", UiStatus.ERROR))
            Timber.e("getRemoteConfigVersion, cancel")
        }

        awaitClose { channel.close() }
    }

    override fun getRemoteConfigUpdate(): Flow<Resource<RemoteConfigUpdate>> = callbackFlow {
        trySend(Resource.Loading(UiStatus.LOADING))
        Timber.d("getRemoteConfigUpdateVersion, loading")

        firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
            val forcedUpdateVersionCode: Long = firebaseRemoteConfig.getLong("forcedUpdateVersionCode")
            val forcedUpdateMessage: String = firebaseRemoteConfig.getString("forcedUpdateMessage")
            val optionalUpdateVersionCode: Long = firebaseRemoteConfig.getLong("optionalUpdateVersionCode")
            val optionalUpdateMessage: String = firebaseRemoteConfig.getString("optionalUpdateMessage")

            trySend(
                Resource.Success(
                    RemoteConfigUpdate(forcedUpdateVersionCode, forcedUpdateMessage, optionalUpdateVersionCode, optionalUpdateMessage),
                    UiStatus.SUCCESS
                )
            )
            Timber.d("getRemoteConfigUpdateVersion, success forcedUpdateVersionCode: $forcedUpdateVersionCode, forcedUpdateMessage: $forcedUpdateMessage, optionalUpdateVersionCode: $optionalUpdateVersionCode, optionalUpdateMessage: $optionalUpdateMessage")
        }.addOnFailureListener {
            trySend(Resource.Error("", UiStatus.ERROR))
            Timber.e(it, "getRemoteConfigUpdateVersion, fail ${it.message}")
        }.addOnCanceledListener {
            trySend(Resource.Error("", UiStatus.ERROR))
            Timber.e("getRemoteConfigUpdateVersion, cancel")
        }

        awaitClose { channel.close() }
    }
}