package tech.prismlabs.reference.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import tech.prismlabs.reference.R

data class SectionItem(
    val id: String,
    val key: String = id,
    val sectionType: SectionType,
    val itemBuilder: @Composable () -> Unit
) {
    enum class SectionType {
        HEADER,
        ITEM
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionItemRow(
    item: SectionItem,
    removeItem: (SectionItem) -> Boolean = { false }
) {
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }

    when (item.sectionType) {
        SectionItem.SectionType.HEADER -> {
            // Render header item
            item.itemBuilder()
        }
        SectionItem.SectionType.ITEM -> {
            // Render regular item
            val dismissState = remember {
                SwipeToDismissBoxState(
                    initialValue = SwipeToDismissBoxValue.Settled,
                    density = Density(context),
                    positionalThreshold = { it / 2 },
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            // To Delete
                            openDialog = true
                            false
                        } else {
                            false
                        }
                    }
                )
            }
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete scan",
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                },
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        item.itemBuilder()
                    }
                }
            )

            if (openDialog) {
                AlertDialog(
                    onDismissRequest = { openDialog = false },
                    title = { Text(stringResource(R.string.delete_scan_confirmation_title)) },
                    text = { Text(stringResource(R.string.delete_scan_confirmation_message)) },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog = false
                                removeItem(item)
                            }
                        ) {
                            Text(stringResource(R.string.delete_scan_confirmation_button))
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { openDialog = false }
                        ) {
                            Text(stringResource(R.string.delete_scan_confirmation_cancel_button))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SectionList(
    modifier: Modifier = Modifier,
    sectionItems: List<SectionItem>,
    listState: LazyListState = rememberLazyListState(),
    removeItem: (SectionItem) -> Boolean = { false },
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        itemsIndexed(
            items = sectionItems,
            key = { _, item ->
                item.key
            }
        ) { _, item ->
            SectionItemRow(
                item = item,
                removeItem = removeItem
            )
        }
    }
}