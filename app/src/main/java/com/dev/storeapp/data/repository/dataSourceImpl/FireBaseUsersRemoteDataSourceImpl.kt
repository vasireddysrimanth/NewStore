package com.dev.storeapp.data.repository.dataSourceImpl

import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.data.repository.dataSource.FireBaseUserRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseUsersRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
):FireBaseUserRemoteDataSource {
    override suspend fun fetchAllUsers(): List<FireBaseUserEntity> {
        return firestore.collection("users")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(FireBaseUserEntity::class.java) }
    }

}