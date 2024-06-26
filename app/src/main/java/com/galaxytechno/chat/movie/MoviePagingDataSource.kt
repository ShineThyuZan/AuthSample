package com.galaxytechno.chat.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.galaxytechno.chat.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviePagingDataSource @Inject constructor(
    private val apiService: ApiService,
) : PagingSource<Int, Movie>() {

    companion object {
        const val INIT_PAGE = 1 //start index to load
        const val PAGE_SIZE = 30 //items per load
//        const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2 //initial size of one load
//        const val MAX_SIZE = (PAGE_SIZE + INITIAL_LOAD_SIZE) * 10 //cache of the page to hold
    }

    //The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

//        return state.anchorPosition

        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val currentPage = params.key ?: INIT_PAGE

        return try {
            val response = apiService.fetchMovies(
                currentPage,
            )
            val pageResponse = response.body()
            val data = pageResponse?.results
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val endOfPaginationReached = data.isNullOrEmpty()
            val nextPage =
                if (endOfPaginationReached) null else currentPage + (params.loadSize / PAGE_SIZE)

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null, //nextPage ( we use forward only )prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}