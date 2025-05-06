package com.alpha.features.profile.presentation.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpha.features.profile.presentation.viewmodels.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    email: String, profileViewModel: ProfileViewModel = hiltViewModel(), onLogout: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val user by profileViewModel.user.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(email) {
        if (email.isNotBlank()) {
            Log.d("ProfileScreen", "Fetching user for email: $email")
            profileViewModel.getUser(email)
        }
    }

    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading user data...")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Name: ${user!!.name}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Email: ${user!!.email}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Allow Camera Permission", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = cameraPermissionState.status.isGranted,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            // Request permission only if not granted
                            if (!cameraPermissionState.status.isGranted) {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "To revoke camera permission, go to App Settings.",
                                Toast.LENGTH_LONG
                            ).show()

                            // Optional: open app settings directly
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        }
                    })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}
