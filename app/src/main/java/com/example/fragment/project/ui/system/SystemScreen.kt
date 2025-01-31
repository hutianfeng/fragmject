package com.example.fragment.project.ui.system

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragment.project.R
import com.example.fragment.project.bean.TreeBean
import com.example.fragment.project.components.ArticleCard
import com.example.fragment.project.components.LoadingContent
import com.example.fragment.project.components.SwipeRefresh
import com.example.fragment.project.components.TabBar
import com.example.fragment.project.components.TitleBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SystemScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    title: String = "体系",
    index: Int = 0,
    tree: List<TreeBean>,
    systemViewModel: SystemViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToSystem: (cid: String) -> Unit = {},
    onNavigateToUser: (userId: String) -> Unit = {},
    onNavigateToWeb: (url: String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(index) { tree.size }
    Column(modifier = Modifier.systemBarsPadding()) {
        TitleBar(title) { onNavigateUp() }
        TabBar(
            data = tree,
            textMapping = { it.name },
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            },
        )
        TabRowDefaults.Divider(color = colorResource(R.color.line))
        HorizontalPager(state = pagerState) { page ->
            val pageCid = tree[page].id
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        systemViewModel.init(pageCid)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            val systemUiState by systemViewModel.uiState.collectAsStateWithLifecycle()
            val listState = rememberLazyListState()
            LoadingContent(systemUiState.getRefreshing(pageCid) && !systemUiState.getLoading(pageCid)) {
                SwipeRefresh(
                    items = systemUiState.getResult(pageCid),
                    refreshing = systemUiState.getRefreshing(pageCid),
                    loading = systemUiState.getLoading(pageCid),
                    finishing = systemUiState.getFinishing(pageCid),
                    onRefresh = { systemViewModel.getHome(pageCid) },
                    onLoad = { systemViewModel.getNext(pageCid) },
                    modifier = Modifier.fillMaxSize(),
                    listState = listState,
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    key = { _, item -> item.id },
                ) { _, item ->
                    ArticleCard(
                        data = item,
                        onNavigateToLogin = onNavigateToLogin,
                        onNavigateToUser = onNavigateToUser,
                        onNavigateToSystem = onNavigateToSystem,
                        onNavigateToWeb = onNavigateToWeb
                    )
                }
            }
        }
    }
}