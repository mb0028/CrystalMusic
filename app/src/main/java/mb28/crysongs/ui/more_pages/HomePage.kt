package mb28.crysongs.ui.more_pages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mb28.crysongs.EXTRA_PATH
import mb28.crysongs.LrcMakerActivity
import mb28.crysongs.SoundPickerActivity
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Track
import mb28.crysongs.core.pageAnimation
import mb28.crysongs.isReloading
import mb28.crysongs.memUsage
import mb28.crysongs.noCoverBitmap
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks
import mb28.crysongs.ui.other.CacheCoversCard

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomePage(activity: Activity) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    val name = remember { android.provider.Settings.Global.getString(
            activity.contentResolver, android.provider.Settings.Global.DEVICE_NAME) }

    AnimatedVisibility(
        visibleState = state,
        enter = pageAnimation,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 130.dp, bottom = 260.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    "Welcome, $name",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 36.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (Settings.tips_cacheThumbs) {
                item {
                    Spacer(Modifier.height(15.dp))
                    CacheCoversCard(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    )
                }
            }

            if (Settings.experimental) {
                item {
                    Spacer(Modifier.height(15.dp))
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            "Experimental features are on\nJVM memory usage: ${memUsage / 1024 / 1024}mb",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 18.sp
                        )
                    }
                }
            }

            if (Settings.recents.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(15.dp))
                    Card(Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "Recently played",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(5.dp))
                            LazyRow(Modifier.fillMaxWidth().height(100.dp)) {
                                items(Settings.recents.count()) {
                                    TrackTileCubic(Settings.recents[it], activity)
                                }
                            }
                        }
                    }
                }
            }

            val count = tracks.count()
            if (count != 0 && !isReloading) {
                item {
                    Spacer(Modifier.height(15.dp))
                    Card(Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "Recently added",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(5.dp))
                            LazyRow(Modifier.fillMaxWidth().height(100.dp)) {
                                items(count.coerceIn(0..10)) {
                                    TrackTileCubic(tracks[it], activity)
                                }
                            }
                        }

                    }
                }

                item {
                    Spacer(Modifier.height(15.dp))
                    Card(Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "Oldest tracks",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(5.dp))
                            LazyRow(Modifier.fillMaxWidth().height(100.dp)) {
                                items(count.coerceIn(0..10)) {
                                    TrackTileCubic(tracks[(count - 1) - it], activity)
                                }
                            }
                        }

                    }
                }
            }

            item {
                val result = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { activityResult ->
                    if (activityResult.resultCode == Activity.RESULT_OK) {
                        val intent = Intent(activity, LrcMakerActivity::class.java)
                            .putExtra(EXTRA_PATH, activityResult.data!!.getStringExtra(EXTRA_PATH))
                        activity.startActivity(intent)
                    }
                }

                Spacer(Modifier.height(15.dp))
                Card(
                    {
                        val intent = Intent(activity, SoundPickerActivity::class.java)
                            .putExtra(Intent.EXTRA_TITLE, "Select track to make lrc for")
                        result.launch(intent)
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    colors = CardDefaults.cardColors(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            "LRC Maker",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Create synced LRC from plain text with just clicking buttons. " +
                                    "Click to open LRC Maker",
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun TrackTileCubic(path: String, context: Context) {
    var t: Track? by remember { mutableStateOf(null) }
    var cover: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(path) {
        t = Track.getTags(path, context)
        withContext(Dispatchers.IO) {
            val coverPath = Track.createOrGetThumbnail(path)
            if (coverPath != null)
                cover = BitmapFactory.decodeFile(coverPath).asImageBitmap()
        }
    }

    Box(Modifier.clip(RoundedCornerShape(10.dp))) {
        Image(
            cover ?: noCoverBitmap!!,
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clickable { setAndPlay(path, true) }
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}
