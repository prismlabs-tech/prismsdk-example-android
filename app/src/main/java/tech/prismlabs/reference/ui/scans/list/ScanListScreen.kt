package tech.prismlabs.reference.ui.scans.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import tech.prismlabs.prismsdk.capture.CaptureSession
import tech.prismlabs.prismsdk.ui.session.PrismSession
import tech.prismlabs.prismsdk.ui.session.PrismSessionState
import tech.prismlabs.prismsdk.ui.session.PrismSessionView
import tech.prismlabs.reference.ui.NavigationItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanListScreen(
    navController: NavController,
    viewModel: ScanListViewModel = viewModel(),
) {
    val currentContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    /*
    val sampleConfiguration = remember {
        PrismSessionConfiguration(
            theme = PrismThemeConfiguration(
                primaryColor = Color(red = 0.125f, green = 0.141f, blue = 0.176f),
                secondaryColor = Color(0xFFE4499B),
                successColor = Color(red = 0.494f, green = 0.789f, blue = 0.497f),
                errorColor = Color(red = 0.881f, green = 0.32f, blue = 0.278f),
                primaryIconColor = Color.White,
                secondaryIconColor = Color(red = 1f, green = 1f, blue = 1f),
                iconBackgroundColor = Color(red = 0.769f, green = 0.776f, blue = 0.8f),
                backgroundColor = Color(red = 0.914f, green = 0.914f, blue = 0.922f),
                secondaryBackgroundColor = Color(red = 0.6f, green = 0.624f, blue = 0.675f),
                titleTextColor = Color(red = 0.125f, green = 0.141f, blue = 0.176f),
                textColor = Color(red = 0.125f, green = 0.141f, blue = 0.176f),
                buttonTextColor = Color.White,
                primaryButtonCornerRadius = 30.0f,
                smallButtonCornerRadius = 24.0f,
                cardCornerRadius = 24.0f,
                sheetCornerRadius = 24.0f,
                gradientColors = listOf(Color.Transparent, Color.Transparent),
            ),
            poseTheme = PoseTheme()
        )
    }
    */
    val captureSession = remember { CaptureSession(context = currentContext) }
    val prismSession = remember {
        PrismSession(
            context = currentContext,
            captureSession = captureSession,
            lifecycleOwner = lifecycleOwner,
//            configuration = sampleConfiguration
        )
    }
    val currentState by prismSession.currentState.value.collectAsStateWithLifecycle()
    val recordingFile by captureSession.recorder.recording.collectAsStateWithLifecycle()
    Log.i("ScanListScreen", "Current State: $currentState")

    // If we have finished recording, lets kick off the upload
    if (currentState == PrismSessionState.FINISHED && recordingFile != null) {
        recordingFile?.let {
            Log.i("ScanListScreen", "Recording file: $it")
            viewModel.createNewScan(it)
        }
    }

    var newCaptureSession by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Your Body Maps")  },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(NavigationItem.Onboarding.UserInfo.route) {
                                popUpTo(NavigationItem.Splash.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "More Options"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    Log.i("ScanListScreen", "Create a new Recording")
                    newCaptureSession = true
                },
                icon = {
                    Icon(
                        painter = painterResource(tech.prismlabs.prismsdk.R.drawable.prism_pose),
                        contentDescription = "Create a new scan"
                    )
                },
                text = { Text(text = "Begin New Scan") },
            )
        }
    ) {
        ScanListView(
            modifier = Modifier.padding(it),
            viewModel = viewModel,
            onClick = { scan ->
                navController.navigate("${NavigationItem.ScanDetails.route}/${scan.id}")
            }
        )

        if (newCaptureSession) {
            Dialog(
                onDismissRequest = {
                    newCaptureSession = false
                },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PrismSessionView(
                        prismSession = prismSession,
                        onDismiss = { newCaptureSession = false }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScanListScreenPreview() {
    val nav = rememberNavController()
    ScanListScreen(navController = nav)
}