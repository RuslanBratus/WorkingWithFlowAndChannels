package com.epam.task2.api.factory


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.epam.task2.api.Asset
import com.epam.task2.api.Cast
import com.epam.task2.api.Movie
import com.epam.task2.api.TvChannel

/** Represents base content factory, which provides concrete type of [Asset]s */
/*
TODO:
 * add three factories with given content below:
    - with Asset.Type.VOD content (Movies)
    - with Asset.Type.LIVE content (TvChannel)
    - with Asset.Type.CREW content (Cast)
 IMPORTANT: do not modify data because it uses in tests.
 */
abstract class ContentFactory<out T : Asset> {

    /** Represents movie, live or cast data */
    protected abstract val dataList: Array<*>

    /** Provides flow with concrete content: [Movie], [TvChannel] or [Cast] */
    abstract fun provideContent(): Flow<T>
}



class MyMovieFactory : ContentFactory<Movie>() {

    override fun provideContent(): Flow<Movie> = flow {
        dataList
            .map { it as Movie }
            .forEach { emit(it) }
    }

    override val dataList: Array<*>
        get() = arrayOf(
            Movie("Harry Potter and the Sorcerer's Stone", 1005861600000),
            Movie("28 Weeks Later", 1178830800000),
            Movie("Beowulf", 1195596000000),
            Movie("The Seven Deadly Sins", 1416088800000),
            Movie("Die Hard", 585345600000),
            Movie("Rocky", 217371600000),
            Movie("Doctor Strange", 1477342800000),
            Movie("Braveheart", 801262800000),
            Movie("Beauty and the Beast", 1487800800000),
            Movie("Seven", 811717200000)
        )
}

class MyLiveFactory : ContentFactory<TvChannel>() {

    override fun provideContent(): Flow<TvChannel> = flow {
        dataList
            .map { it as TvChannel }
            .forEach { emit(it) }
    }

    override val dataList: Array<*>
        get() = arrayOf(
            TvChannel(1, "1+1"),
            TvChannel(2, "Football 1"),
            TvChannel(3, "Inter"),
            TvChannel(4, "STB"),
            TvChannel(5, "5 channel"),
            TvChannel(6, "ICTV"),
            TvChannel(7, "National Geographic"),
            TvChannel(8, "Animal Planet"),
            TvChannel(9, "Ukraine HD"),
            TvChannel(10, "History HD")
        )
}

class MyCrewFactory : ContentFactory<Cast>() {

    override fun provideContent(): Flow<Cast> = flow {
        dataList
            .map { it as Cast }
            .forEach { emit(it) }
    }

    override val dataList: Array<*>
        get() = arrayOf(
            Cast("Adriana Ferdynand", 1),
            Cast("Walenty Kuba", 2),
            Cast("Jarek Franciszka", 3),
            Cast("Quintella Hayley", 4),
            Cast("Fraser Starr", 5),
            Cast("Wallis Chuck", 6),
            Cast("Nino Avksenti", 7),
            Cast("Daviti Ketevan", 8),
            Cast("Ioane Korneli", 9),
            Cast("Mariami Nika", 10)
        )
}

/*
    Movies data:
        | name                                 | release year in unix time
        "Harry Potter and the Sorcerer's Stone"| 1005861600000
        "28 Weeks Later"                       | 1178830800000
        "Beowulf"                              | 1195596000000
        "The Seven Deadly Sins"                | 1416088800000
        "Die Hard"                             | 585345600000
        "Rocky"                                | 217371600000
        "Doctor Strange"                       | 1477342800000
        "Braveheart"                           | 801262800000
        "Beauty and the Beast"                 | 1487800800000
        "Seven"                                | 811717200000

   Tv Channel data:
        | channel number | name
        №1               | "1+1"
        №2               | "Football 1"
        №3               | "Inter"
        №4               | "STB"
        №5               | "5 channel"
        №6               | "ICTV"
        №7               | "National Geographic"
        №8               | "Animal Planet"
        №9               | "Ukraine HD"
        №10              | "History HD"

   Cast data:
       | name              | film count
       "Adriana Ferdynand" | 1
       "Walenty Kuba"      | 2
       "Jarek Franciszka"  | 3
       "Quintella Hayley"  | 4
       "Fraser Starr"      | 5
       "Wallis Chuck"      | 6
       "Nino Avksenti"     | 7
       "Daviti Ketevan"    | 8
       "Ioane Korneli"     | 9
       "Mariami Nika"      | 10
 */