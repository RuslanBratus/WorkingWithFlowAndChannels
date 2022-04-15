package com.epam.task2.repository

import com.epam.task2.api.Asset
import com.epam.task2.api.SearchApi
import kotlinx.coroutines.flow.Flow

/**
 * Represent data layer, which designed to use with user's raw input.
 * All inputs should be converted to [Query]
 */
/*
TODO: add realization, which will interact with SearchApi and returns the result.
 * your realization should hold reference to [SearchApi]
*/
interface SearchRepository {

    suspend fun searchContentAsync(query: Query): Flow<Asset>
}

class MySearchRepository(private val api: SearchApi) : SearchRepository {
    override suspend fun searchContentAsync(query: Query): Flow<Asset> =
        when (query) {
            is StartWithQuery -> if (query.type == null)
                api.searchByStartWith(query.input)
            else
                api.searchByStartWith(query.input, query.type)

            is JustQuery -> if (query.type == null)
                api.searchByContains(query.input)
            else
                api.searchByContains(query.input, query.type)
        }
}