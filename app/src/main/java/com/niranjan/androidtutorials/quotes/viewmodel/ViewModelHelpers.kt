package com.niranjan.androidtutorials.quotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Convenience factory to handle ViewModels with one parameter.
 *
 * Make a factory:
 * ```
 * // Make a factory
 * val FACTORY = viewModelFactory(::MyViewModel)
 * ```
 *
 * Use the generated factory:
 * ```
 * ViewModelProviders.of(this, FACTORY(argument))
 *
 * ```
 *
 * @param constructor A function (A) -> T that returns an instance of the ViewModel (typically pass
 * the constructor)
 * @return a function of one argument that returns ViewModelProvider.Factory for ViewModelProviders
 */
fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T):
            (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}