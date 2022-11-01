package com.niranjan.androidtutorials.plants.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.niranjan.androidtutorials.plants.db.PlantsDatabase
import com.niranjan.androidtutorials.plants.model.PlantsRepository
import com.niranjan.androidtutorials.plants.network.NetworkService
import com.niranjan.androidtutorials.plants.view.PlantsViewModelFactory

interface ViewModelFactoryProvider {
    fun providePlantsViewModelFactory(context: Context): PlantsViewModelFactory
}

val Injector: ViewModelFactoryProvider
    get() = currentInjector

private object DefaultViewModelProvider: ViewModelFactoryProvider {
    private fun getPlantRepository(context: Context): PlantsRepository {
        return PlantsRepository.getInstance(
            plantDao(context),
            plantService()
        )
    }

    private fun plantService() = NetworkService()

    private fun plantDao(context: Context) =
        PlantsDatabase.getInstance(context.applicationContext).plantDao()

    override fun providePlantsViewModelFactory(context: Context): PlantsViewModelFactory {
        val repository = getPlantRepository(context)
        return PlantsViewModelFactory(repository)
    }
}

private object Lock

@Volatile private var currentInjector: ViewModelFactoryProvider =
    DefaultViewModelProvider


@VisibleForTesting
private fun setInjectorForTesting(injector: ViewModelFactoryProvider?) {
    synchronized(Lock) {
        currentInjector = injector ?: DefaultViewModelProvider
    }
}

@VisibleForTesting
private fun resetInjector() =
    setInjectorForTesting(null)