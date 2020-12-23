package com.submission.core.data.source.local.room

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.submission.core.data.source.local.entity.DiscoverMovieEntity
import com.submission.core.data.source.local.entity.FavoriteMovieEntity
import com.submission.core.data.source.local.entity.TrendingMovieEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FilmDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: FilmDatabase
    private lateinit var dao: FilmDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FilmDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.filmDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDiscoverMovieItem() = runBlockingTest {
        val list = listOf(
            DiscoverMovieEntity(1, "backdrop 1", "Testing 1", 9.9)
        )
        dao.insertDiscoverMovie(list)

        val allMovieItems = dao.getAllDiscoverMovie().asLiveData().getOrAwaitValue()

        assert(allMovieItems.isNotEmpty())
        assertThat(allMovieItems, equalTo(list))
    }

    @Test
    fun insertTrendingMovieItem() = runBlockingTest {
        val list = listOf(
            TrendingMovieEntity(1, "poster 1", "12-12-2020", "Testing 1")
        )
        dao.insertTrendingMovie(list)

        val allMovieItems = dao.getAllTrendingMovie().asLiveData().getOrAwaitValue()

        assert(allMovieItems.isNotEmpty())
        assertThat(allMovieItems, equalTo(list))
    }

    @Test
    fun insertFavoriteMovieItem() {
        val list =
            FavoriteMovieEntity(1, "poster 1", "12-12-2020", "Testing 1")

        dao.insertFavoriteMovie(list)

        val allMovieItems = dao.getAllFavoriteMovie().asLiveData().getOrAwaitValue()

        assert(allMovieItems.isNotEmpty())
        assertThat(allMovieItems[0], equalTo(list))
    }

    @Test
    fun deleteFavoriteMovieItem() = runBlockingTest {
        val list =
            FavoriteMovieEntity(1, "poster 1", "12-12-2020", "Testing 1")

        dao.insertFavoriteMovie(list)
        dao.deleteFavoriteMovie(list)

        val allMovieItems = dao.getAllFavoriteMovie().asLiveData().getOrAwaitValue()

        assert(allMovieItems.isEmpty())
    }
}