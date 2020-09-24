package com.submission.core.utils

import com.submission.core.data.source.local.entity.DiscoverMovieEntity
import com.submission.core.data.source.local.entity.FavoriteMovieEntity
import com.submission.core.data.source.local.entity.TrendingMovieEntity
import com.submission.core.data.source.remote.response.*
import com.submission.core.domain.usecase.model.*

object DataMapper {
    fun mapDiscoverMovieResponsesToEntities(input: List<DiscoverMovieResult>): List<DiscoverMovieEntity> {
        val discoverList = ArrayList<DiscoverMovieEntity>()
        input.map {
            val discover = DiscoverMovieEntity(
                id = it.id,
                backdrop_path = it.backdrop_path,
                title = it.title,
                vote_average = it.vote_average
            )
            discoverList.add(discover)
        }
        return discoverList
    }

    fun mapTrendingMovieResponsesToEntities(input: List<TrendingMovieResult>): List<TrendingMovieEntity> {
        val trendingList = ArrayList<TrendingMovieEntity>()
        input.map {
            val trending = TrendingMovieEntity(
                id = it.id,
                poster_path = it.poster_path,
                release_date = it.release_date,
                title = it.title
            )
            trendingList.add(trending)
        }
        return trendingList
    }

    fun mapDetailMovieResponsesToDomain(input: DetailMovieResponse): DetailMovie {
        val genre = input.genres.map {
            mapGenreMovieResponseToDomain(it)
        }
        return DetailMovie(
            id = input.id,
            backdrop_path = input.backdrop_path,
            overview = input.overview,
            popularity = input.popularity,
            poster_path = input.poster_path,
            release_date = input.release_date,
            title = input.title,
            vote_average = input.vote_average,
            runtime = input.runtime,
            genreMovie = genre
        )
    }

    private fun mapGenreMovieResponseToDomain(input: DetailMovieGenre): GenreMovie {
        return GenreMovie(
            id = input.id,
            name = input.name
        )
    }

    fun mapCastMovieResponseToDomain(
        input: List<CastMovieResult>
    ): List<CastMovie> {
        val castList = ArrayList<CastMovie>()
        input.map {
            val cast = CastMovie(
                castId = it.id,
                character = it.character,
                name = it.name,
                profile_path = it.profile_path
            )
            castList.add(cast)
        }
        return castList
    }

    fun mapDiscoverMovieEntitiesToDomain(input: List<DiscoverMovieEntity>): List<DiscoverMovie> {
        val discoverList = ArrayList<DiscoverMovie>()
        input.map {
            val movie = DiscoverMovie(
                id = it.id,
                backdrop_path = it.backdrop_path,
                vote_average = it.vote_average,
                title = it.title
            )
            discoverList.add(movie)
        }
        return discoverList
    }

    fun mapTrendingMovieEntitiesToDomain(input: List<TrendingMovieEntity>): List<TrendingMovie> {
        val trendingList = ArrayList<TrendingMovie>()
        input.map {
            val movie = TrendingMovie(
                id = it.id,
                poster_path = it.poster_path,
                title = it.title,
                release_date = it.release_date
            )
            trendingList.add(movie)
        }
        return trendingList
    }

    fun mapSearchMovieResponseToDomain(input: List<SearchMovieResult>): List<SearchMovie> {
        val searchList = ArrayList<SearchMovie>()
        input.map {
            val movie = SearchMovie(
                id = it.id,
                poster_path = it.poster_path,
                title = it.title,
                release_date = it.release_date
            )
            searchList.add(movie)
        }
        return searchList
    }


    fun mapMovieFavoriteEntitiesToDomain(input: List<FavoriteMovieEntity>): List<FavoriteMovie> {
        val favoriteMovieList = ArrayList<FavoriteMovie>()
        input.map {
            val favorite = FavoriteMovie(
                id = it.id,
                title = it.title,
                poster_path = it.poster_path,
                release_date = it.release_date
            )
            favoriteMovieList.add(favorite)
        }
        return favoriteMovieList
    }

    fun mapMovieDomainToFavoriteEntities(input: FavoriteMovie) =
        FavoriteMovieEntity(
            id = input.id,
            poster_path = input.poster_path,
            release_date = input.release_date,
            title = input.title
        )

    fun mapDetailMovieToFavoriteMovie(input: DetailMovie) =
        FavoriteMovie(
            id = input.id,
            poster_path = input.backdrop_path,
            release_date = input.release_date,
            title = input.title
        )
}