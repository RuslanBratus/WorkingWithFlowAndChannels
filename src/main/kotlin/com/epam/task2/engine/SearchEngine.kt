package com.epam.task2.engine

import com.epam.task2.api.Asset
import com.epam.task2.repository.JustQuery
import com.epam.task2.repository.Query
import com.epam.task2.repository.SearchRepository
import com.epam.task2.repository.StartWithQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList

/**
 * In Clean Architecture this class should be named as UseCase or Interactot.
 * All necessary logic should be implemented here.
 */
/*
TODO: implement all business logic which converts input stream to
      and then used to in [SearchRepository]
 * your realization should receive CoroutineDispatcher in constructor
   (it gives possibility to write tests)
 * flow should executed on provided CoroutineDispatcher
 */


class SearchEngine(
    private val repository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun searchContentAsync(rawInput: String): Flow<SearchResult> = flow {
        repository.searchContentAsync(getQuery(rawInput)).toList(mutableListOf())
            .groupBy { it.type }
            .map { result -> SearchResult(result.value, result.key, result.key.toGroupName()) }
            .forEach { emit(it) }
    }.flowOn(dispatcher)

    private fun getQuery(rawInput: String): Query {
        val assetType = """w*[?](VOD|LIVE|CREW)$""".toRegex().find(rawInput)?.groupValues?.first()?.removePrefix("?")
            ?.let { type -> Asset.Type.valueOf(type.capitalize()) }

        var assetString = if (assetType == null)
            rawInput
        else
            rawInput.removeSuffix("?${assetType.toString()}")

        val isStart = rawInput.startsWith("@")

        if (isStart)
            assetString = assetString.removePrefix("@")

        if (assetString.isEmpty() || assetString.isBlank())
            throw IllegalStateException()

        return if (isStart)
            StartWithQuery(assetString, assetType)
        else
            JustQuery(assetString, assetType)
    }
}

private fun Asset.Type.toGroupName() =
    when (this) {
        Asset.Type.VOD -> com.epam.task2.VOD
        Asset.Type.LIVE -> com.epam.task2.LIVE
        Asset.Type.CREW -> com.epam.task2.CREW
    }
