package com.example.fragment.project.ui.search

import androidx.lifecycle.viewModelScope
import com.example.fragment.project.bean.ArticleBean
import com.example.fragment.project.bean.ArticleListBean
import com.example.miaow.base.http.HttpRequest
import com.example.miaow.base.http.post
import com.example.miaow.base.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    var refreshing: Boolean = false,
    var loading: Boolean = false,
    var finishing: Boolean = false,
    var articlesResult: MutableList<ArticleBean> = ArrayList(),
    var updateTime: Long = 0
)

class SearchViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())

    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun clearArticles() {
        _uiState.update {
            it.articlesResult.clear()
            it.copy(updateTime = System.nanoTime())
        }
    }

    fun getHome(key: String) {
        _uiState.update {
            it.copy(refreshing = true, loading = false, finishing = false)
        }
        getArticleQuery(key, getHomePage())
    }

    fun getNext(key: String) {
        _uiState.update {
            it.copy(refreshing = false, loading = false, finishing = false)
        }
        getArticleQuery(key, getNextPage())
    }

    /**
     * 搜索
     * k 搜索关键词
     * page 0开始
     */
    private fun getArticleQuery(key: String, page: Int) {
        //通过viewModelScope创建一个协程
        viewModelScope.launch {
            //构建请求体，传入请求参数
            val request = HttpRequest("article/query/{page}/json")
                .putParam("k", key)
                .putPath("page", page.toString())
            //以get方式发起网络请求
            val response = post<ArticleListBean>(request)
            //根据接口返回更新总页码
            updatePageCont(response.data?.pageCount?.toInt())
            _uiState.update { state ->
                response.data?.datas?.let { datas ->
                    if (isHomePage()) {
                        state.articlesResult.clear()
                    }
                    state.articlesResult.addAll(datas)
                }
                state.copy(refreshing = false, loading = hasNextPage(), finishing = !hasNextPage())
            }
        }
    }

}