package com.epam.task2.api

import com.epam.task2.api.factory.MyCrewFactory
import com.epam.task2.api.factory.MyLiveFactory
import com.epam.task2.api.factory.MyMovieFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

/**
 * Let's imagine that, this is some library's API with complied code,
 * and you have no access to this code. Class, witch will implement this
 * contract, should takes care about whole logic.
 *
 * Notice, that function with [Asset.Type] in arguments MUST search
 * only a specific (matching) [ContentFactory].
 * For example:
 * query with type [Asset.Type.VOD] must check only content factory which provides ONLY movies.
 */
/*
TODO:
 * add realization, which will interact with all content
   factories and returns the appropriate result.
 * functions with [Asset.Type] in arguments MUST search
   only in specific (matching) [ContentFactory].
   For example:
   query with type [Asset.Type.VOD] must check content factory which provides ONLY movies.
 */
interface SearchApi {

    /**
     * Searches for assets, whose poster **contains** the [query]
     * @return flow with results.
     */
    suspend fun searchByContains(query: String): Flow<Asset>

    /**
     * Searches for assets of the given [type], whose poster **contains** the [query]
     * @return flow with results.
     */
    suspend fun searchByContains(query: String, type: Asset.Type): Flow<Asset>

    /**
     * Searches for assets, whose poster **starts with** the [query]
     * @return flow with results.
     */
    suspend fun searchByStartWith(query: String): Flow<Asset>

    /**
     * Searches for assets of the given [type], whose poster **starts with** the [query]
     * @return flow with results.
     */
    suspend fun searchByStartWith(query: String, type: Asset.Type): Flow<Asset>
}


@ExperimentalCoroutinesApi
class MySearchApi(
    private val vodFactory: MyMovieFactory,
    private val liveFactory: MyLiveFactory,
    private val crewFactory: MyCrewFactory
) :
    SearchApi {


    override suspend fun searchByContains(query: String): Flow<Asset> =
        mergeAll().onEach { }.filter {
            it.getPoster().contains(query, ignoreCase = true)
        }


    override suspend fun searchByContains(query: String, type: Asset.Type): Flow<Asset> =
        type.toFlow().onEach { }.filter {
            it.getPoster().contains(query, ignoreCase = true)
        }


    override suspend fun searchByStartWith(query: String): Flow<Asset> =
        mergeAll().onEach { }.filter {
            it.getPoster().startsWith(query, ignoreCase = true)
        }


    override suspend fun searchByStartWith(query: String, type: Asset.Type): Flow<Asset> =
        type.toFlow().onEach { }.filter {
            it.getPoster().startsWith(query, ignoreCase = true)
        }

    private fun Asset.Type.toFlow(): Flow<Asset> =
        when (this) {
            Asset.Type.VOD -> vodFactory.provideContent()
            Asset.Type.LIVE -> liveFactory.provideContent()
            Asset.Type.CREW -> crewFactory.provideContent()
        }

    private fun mergeAll() = merge(
        vodFactory.provideContent(),
        liveFactory.provideContent(),
        crewFactory.provideContent()
    )
}