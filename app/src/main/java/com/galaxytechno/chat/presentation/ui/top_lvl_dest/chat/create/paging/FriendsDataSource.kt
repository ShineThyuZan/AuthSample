package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.model.vos.FriListVos
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FriendsDataSource @Inject constructor(
    private val api: ApiService,
    private val userId: Long,
    private val locale: Int,
    private val queryName: String,
) : PagingSource<Int, FriListVos>() {

    override fun getRefreshKey(state: PagingState<Int, FriListVos>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FriListVos> {

        val currentPage = params.key ?: Constant.INITIAL_PAGE

        return try {
            val response = api.getFriList(
                offset = currentPage,
                limit = Constant.PAGE_SIZE,
                userId = userId,
                searchedUsername = queryName,
                locale = locale
            )
            val pageable = response.body()?.data?.pageable

            val friends = response.body()?.data?.friendList

            LoadResult.Page(
                data = friends.orEmpty(),
                prevKey = pageable?.prev,
                nextKey = pageable?.next
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