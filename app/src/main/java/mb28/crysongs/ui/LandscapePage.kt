package mb28.crysongs.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mb28.crysongs.MainActivity
import mb28.crysongs.SettingsActivity
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.folder
import mb28.crysongs.icons.library_music
import mb28.crysongs.icons.music_note_2
import mb28.crysongs.icons.queue_music
import mb28.crysongs.icons.search
import mb28.crysongs.icons.settings
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.privateNowPlayingCover
import mb28.crysongs.ui.fullscreen_player.FSCover
import mb28.crysongs.ui.fullscreen_player.FSPlayerButtonsRow
import mb28.crysongs.ui.fullscreen_player.FSProgressBarRow
import mb28.crysongs.ui.more_pages.FoldersPage
import mb28.crysongs.ui.more_pages.PlaylistsPage
import mb28.crysongs.ui.more_pages.QueryPage
import mb28.crysongs.ui.more_pages.SearchPage

@Composable
fun LandscapePage(activity: MainActivity) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(Modifier.fillMaxSize()) { paddingValues ->
        Box {
            if (privateNowPlayingCover != null) {
                Image(
                    (privateNowPlayingCover as Bitmap).asImageBitmap(), null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(Settings.backgroundBlurRadius.dp)
                        .alpha(0.5f),
                )
            }

            Row(
                Modifier.padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                ),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                VerticalFloatingToolbar(
                    true,
                    modifier = Modifier.width(65.dp),
                    colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                        MaterialTheme.colorScheme.surfaceBright.copy(0.5f)
                    ),
                    leadingContent = {
                        FilledTonalIconButton(
                            {
                                activity.startActivity(Intent(activity,
                                    SettingsActivity::class.java))
                            }
                        ) { Icon(settings, null) }
                    }
                ) {
                    Spacer(Modifier.height(5.dp))
                    NavigationRailItem(
                        selectedTab == 0,
                        { selectedTab = 0 },
                        { Icon(music_note_2, null) },
                        label = { Text("Tracks") }
                    )
                    NavigationRailItem(
                        selectedTab == 1,
                        { selectedTab = 1 },
                        { Icon(queue_music, null) },
                        label = { Text("Query") }
                    )
                    NavigationRailItem(
                        selectedTab == 2,
                        { selectedTab = 2 },
                        { Icon(library_music, null) },
                        label = { Text("Playlists") }
                    )
                    NavigationRailItem(
                        selectedTab == 3,
                        { selectedTab = 3 },
                        { Icon(folder, null) },
                        label = { Text("Folders") }
                    )
                    NavigationRailItem(
                        selectedTab == 4,
                        { selectedTab = 4 },
                        { Icon(search, null) },
                        label = { Text("Search") }
                    )
                    Spacer(Modifier.height(5.dp))
                }

                Box(Modifier.weight(0.48f)) {
                    when(selectedTab) {
                        0 -> TracksList()
                        1 -> QueryPage()
                        2 -> PlaylistsPage()
                        3 -> FoldersPage()
                        else -> SearchPage(activity)
                    }

                }

                Column(
                    Modifier.weight(0.52f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(5.dp))
                        FSCover(Modifier.size(160.dp))
                        Spacer(Modifier.height(5.dp))
                        Text(nowPlayingTags?.title ?: "Play something", fontSize = 24.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(5.dp))
                        Text(nowPlayingTags?.artist ?: "🎵🎵🎵", fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    FSProgressBarRow()
                    Spacer(Modifier.height(5.dp))
                    FSPlayerButtonsRow()
                }
            }
        }
    }
}
