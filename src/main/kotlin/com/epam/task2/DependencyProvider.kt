package com.epam.task2

import com.epam.task2.api.MySearchApi
import com.epam.task2.api.SearchApi
import com.epam.task2.api.factory.MyCrewFactory
import com.epam.task2.api.factory.MyLiveFactory
import com.epam.task2.api.factory.MyMovieFactory
import com.epam.task2.engine.SearchEngine
import com.epam.task2.repository.MySearchRepository
import com.epam.task2.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This is a simple realization of Service Locator pattern.
 * It uses 'fabric' pattern to provide all classes
 * you need in one place.
 * More to read : [link](https://ru.wikipedia.org/wiki/%D0%9B%D0%BE%D0%BA%D0%B0%D1%82%D0%BE%D1%80_%D1%81%D0%BB%D1%83%D0%B6%D0%B1)
 */
//TODO: add your realization of each contract in this task
@ExperimentalCoroutinesApi
object DependencyProvider {

    fun provideEngine(dispatcher: CoroutineDispatcher): SearchEngine = SearchEngine(provideRepository(), dispatcher)

    private fun provideRepository(): SearchRepository = MySearchRepository(provideApi())

    private fun provideApi(): SearchApi = MySearchApi(MyMovieFactory(), MyLiveFactory(), MyCrewFactory())
}