package tech.prismlabs.reference.ui.scans.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.reference.scans.scanDate
import tech.prismlabs.reference.ui.components.SectionItem
import tech.prismlabs.reference.ui.components.SectionList

@Composable
fun ScanListView(
    modifier: Modifier = Modifier,
    viewModel: ScanListViewModel,
    onClick: (Scan) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uploadProgress by viewModel.uploadProgress.collectAsStateWithLifecycle()

    when (state) {
        is ScanListViewModel.State.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...")
            }
        }

        is ScanListViewModel.State.Data -> {
            val data = (state as ScanListViewModel.State.Data).data
            if (data.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Scans")
                }
                return
            }
            val items = data.map { group ->
                listOf(
                    SectionItem(
                        id = group.scanDate,
                        sectionType = SectionItem.SectionType.HEADER,
                        itemBuilder = {
                            ScanListHeader(group.scanDate)
                        }
                    ),
                    *group.scans.map { scan ->
                        SectionItem(
                            id = scan.id,
                            key = scan.id + scan.createdAt.toString() + scan.updatedAt.toString(),
                            sectionType = SectionItem.SectionType.ITEM,
                            itemBuilder = {
                                ScanListCell(
                                    scan = scan,
                                    onClick = { onClick(scan) }
                                )
                            }
                        )
                    }.toTypedArray()
                )
            }
            val listState = rememberLazyListState()
//            listState.scrollToItem(0, 0)
            val canPaginate by remember {
                derivedStateOf {
                    viewModel.canPaginate && (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (listState.layoutInfo.totalItemsCount - 6)
                }
            }

            LaunchedEffect(canPaginate) {
                if (canPaginate && viewModel.listState == ScanListViewModel.ListState.IDLE || viewModel.listState == ScanListViewModel.ListState.LOADED)
                    viewModel.fetch()
            }

            Column {
                if (uploadProgress in 1..99) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                    ) {
                        Text(text = "Uploading Scan")
                        LinearProgressIndicator(
                            progress = { uploadProgress / 100f },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        )
                    }
                }
                SectionList(
                    modifier = if (uploadProgress in 1..99) Modifier else modifier,
                    listState = listState,
                    sectionItems = items.flatten(),
                    removeItem = {
                        // Show the confirmation dialog
                        viewModel.remove(it.id)
                    },
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScanListViewPreview() {
    ScanListView(
        viewModel = viewModel(),
    )
}