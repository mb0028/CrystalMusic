package mb28.crysongs.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.core.net.toUri

@SuppressLint("BatteryLife")
@Composable
fun PermissionsPage(modifier: Modifier = Modifier, activity: Activity) {
    val isFilesGranted = Environment.isExternalStorageManager()
    val isNotifGranted = activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
    val isBatteryGranted = activity.getSystemService<PowerManager>()!!.isIgnoringBatteryOptimizations(activity.packageName)
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Setup Permissions", fontSize = 28.sp)
        Spacer(Modifier.height(10.dp))
        Text("App needs these permissions to run:\n- All files access\n- Always run in background\n- Notifications (optional)")
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

        if (!isBatteryGranted) {
            Spacer(Modifier.height(5.dp))
            Button(
                {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        .setData("package:${activity.packageName}".toUri())
                    activity.startActivity(intent)
                }
            ) {
                Text("Show ignore battery optimization")
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