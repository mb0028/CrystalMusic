package mb28.crysongs.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun PermissionsPage(modifier: Modifier = Modifier, activity: Activity) {
    val isFilesGranted = Environment.isExternalStorageManager()
    val isNotifGranted = activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Setup Permissions", fontSize = 28.sp)
        Spacer(Modifier.height(10.dp))
        Text("App needs these permissions to run:\n- All files access\n- Notifications (optional)")
        Spacer(Modifier.height(60.dp))

        if (!isFilesGranted) {
            Button(
                {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        .setData("package:${activity.packageName}".toUri())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                }
            ) {
                Text("Open all files access settings")
            }
        }

        if (isNotifGranted != PackageManager.PERMISSION_GRANTED) {
            Spacer(Modifier.height(5.dp))
            Button(
                {
                    activity.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
                }
            ) {
                Text("Grant (Notifications)")
            }
        }

        Spacer(Modifier.height(80.dp))
        Button(
            {
                activity.finish()
            }
        ) {
            Text("Restart")
        }
    }
}