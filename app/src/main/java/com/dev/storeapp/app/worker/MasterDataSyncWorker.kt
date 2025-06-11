package com.dev.storeapp.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dev.storeapp.domain.repository.ProductRepository
import com.dev.storeapp.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

@HiltWorker
class MasterDataSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            supervisorScope {
                val productSyncDeferred = async { productRepository.sync() }
                val userSyncDeferred = async { userRepository.sync() }
                productSyncDeferred.await()
                userSyncDeferred.await()
            }
        }
        return Result.success()
    }
}