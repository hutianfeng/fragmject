package com.example.fragment.project.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.fragment.project.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TabBar(
    data: List<T>?,
    textMapping: (T) -> String,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(R.color.white),
    selectedContentColor: Color = colorResource(R.color.theme),
    unselectedContentColor: Color = colorResource(R.color.text_999),
    onClick: (index: Int) -> Unit
) {
    if (!data.isNullOrEmpty()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = modifier,
            backgroundColor = backgroundColor,
            edgePadding = 0.dp,
            divider = { TabRowDefaults.Divider(color = colorResource(R.color.transparent)) }
        ) {
            data.forEachIndexed { index, item ->
                Tab(
                    text = { Text(textMapping(item)) },
                    onClick = { onClick(index) },
                    selected = pagerState.currentPage == index,
                    selectedContentColor = selectedContentColor,
                    unselectedContentColor = unselectedContentColor
                )
            }
        }
    }
}