
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingsView(
    recordingsViewModel: RecordingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by recordingsViewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is RecordingsViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...")
            }
        }

        is RecordingsViewModel.State.Data -> {
            val data = (state as RecordingsViewModel.State.Data).data
            if (data.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No recordings")
                }
                return
            }
            LazyColumn(modifier = Modifier.padding(top = 64.dp)) {
                items(items = data, key = { it.fileName }) { recording ->
                    val dismissState = remember {
                        SwipeToDismissBoxState(
                            initialValue = SwipeToDismissBoxValue.Settled,
                            density = Density(context),
                            positionalThreshold = { it / 2 },
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    val success = recordingsViewModel.remove(recording.fileName)
                                    success
                                } else {
                                    false
                                }
                            }
                        )
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromEndToStart = true,
                        backgroundContent ={
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete recording",
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }
                        },
                        content = {
                            RecordingRow(recording)
                        }
                    )
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error")
            }
        }
    }
}

@Composable
fun RecordingRow(recordingViewModel: RecordingViewModel) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    recordingViewModel.lastModified.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(recordingViewModel.fileName, style = MaterialTheme.typography.labelSmall)
            }
            Column {
                Text("${recordingViewModel.size}mb", maxLines = 1)
            }
        }
        HorizontalDivider()
    }
}
