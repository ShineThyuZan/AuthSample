package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat.create.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.data.remote.ApiService
import com.galaxytechno.chat.model.vos.AppUserVo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppUsersDataSource @Inject constructor(
    private val api: ApiService,
    private val userId: Long,
    private val locale: Int,
    private val queryName: String,
) : PagingSource<Int, AppUserVo>() {

    override fun getRefreshKey(state: PagingState<Int, AppUserVo>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AppUserVo> {

        val currentPage = params.key ?: Constant.INITIAL_PAGE

        return try {
            val response = api.searchUser(
                userId = userId,
                locale = locale,
                query = queryName,
                page = currentPage,
                loadSize = Constant.PAGE_SIZE,
            )
            val pageable = response.body()?.data?.pageable

            val friends = response.body()?.data?.profileInfoList?.map { it.toAppUserVo() }
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