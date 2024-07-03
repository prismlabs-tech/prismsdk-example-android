package tech.prismlabs.reference.ui.scans.details.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import tech.prismlabs.reference.R
import tech.prismlabs.reference.extensions.ScanCreatedAt
import tech.prismlabs.reference.ui.scans.details.data.ScanDetailsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDetailsScreen(
    navController: NavController,
    viewModel: ScanDetailsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val avatar by viewModel.avatar.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var moreButtonExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    if (state is ScanDetailsViewModel.State.Data) {
        title = (state as ScanDetailsViewModel.State.Data).scan.createdAt.format(ScanCreatedAt)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title)  },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { moreButtonExpanded = !moreButtonExpanded }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        DropdownMenu(
                            expanded = moreButtonExpanded,
                            onDismissRequest = { moreButtonExpanded = false },
                            properties = PopupProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true
                            )
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.scan_details_delete_scan),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    moreButtonExpanded = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is ScanDetailsViewModel.State.Loading -> {
                    Text(text = "Loading...")
                }

                is ScanDetailsViewModel.State.Data -> {
                    val scan = (state as ScanDetailsViewModel.State.Data).scan
                    ScanDetailsView(
                        scan = scan,
                        avatar = avatar
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_scan_confirmation_title)) },
            text = { Text(stringResource(R.string.delete_scan_confirmation_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            if (state is ScanDetailsViewModel.State.Data) {
                                val scan = (state as ScanDetailsViewModel.State.Data).scan
                                viewModel.delete(scan)
                                navController.navigateUp()
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_scan_confirmation_button))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.delete_scan_confirmation_cancel_button))
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScanDetailsScreenPreview() {
    val nav = rememberNavController()
    ScanDetailsScreen(
        navController = nav,
        viewModel = viewModel()
    )
}
