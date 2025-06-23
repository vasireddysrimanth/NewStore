package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.repository.dataSource.FireBaseUserRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseUsersRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FireBaseUserRemoteDataSource {
    override suspend fun fetchAllUsers(): List<FireBaseUserEntity> {
        return try {
            val result = firestore.collection("users").get().await()
            AppLogger.d("FireStore", "Documents count: ${result.documents.size}")
            result.mapNotNull { it.toObject(FireBaseUserEntity::class.java) }
        } catch (e: Exception) {
            AppLogger.e("FireStore", "Error fetching users: ${e.localizedMessage}")
            emptyList()
        }
    }
}
