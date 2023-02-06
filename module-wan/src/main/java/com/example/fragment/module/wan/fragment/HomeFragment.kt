package com.example.fragment.module.wan.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragment.library.base.compose.Banner
import com.example.fragment.library.base.compose.SwipeRefresh
import com.example.fragment.library.base.compose.theme.WanTheme
import com.example.fragment.library.base.model.BaseViewModel
import com.example.fragment.library.common.compose.ArticleCard
import com.example.fragment.library.common.constant.Keys
import com.example.fragment.library.common.constant.Router
import com.example.fragment.library.common.fragment.RouterFragment
import com.example.fragment.module.wan.model.HomeViewModel

class HomeFragment : RouterFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WanTheme {
                    HomeScreen()
                }
            }
        }
    }

    override fun initView() {}

    override fun initViewModel(): BaseViewModel? {
        return null
    }

    @Composable
    fun HomeScreen() {
        val viewModel: HomeViewModel = viewModel()
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            refreshing = viewModel.refreshing,
            loading = viewModel.loading,
            onRefresh = {
                viewModel.getArticleHome()
            },
            onLoad = {
                viewModel.getArticleNext()
            },
            onNoData = {
                viewModel.getArticleHome()
            },
            data = viewModel.result,
        ) { _, item ->
            if (item.viewType == 0) {
                Banner(
                    data = item.banners,
                    pathMapping = { it.imagePath },
                    onClick = { _, banner ->
                        navigation(
                            Router.WEB,
                            bundleOf(Keys.URL to Uri.encode(banner.url))
                        )
                    }
                )
            } else {
                ArticleCard(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    item = item
                )
            }
        }
    }

    @Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
    @Composable
    fun HomeScreenPreview() {
        WanTheme {
            HomeScreen()
        }
    }

}