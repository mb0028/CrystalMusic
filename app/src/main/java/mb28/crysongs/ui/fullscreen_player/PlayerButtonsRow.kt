package mb28.crysongs.ui.fullscreen_player

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.heart_plus
import mb28.crysongs.icons.pause_circle
import mb28.crysongs.icons.play_circle
import mb28.crysongs.icons.repeat
import mb28.crysongs.icons.repeat_on
import mb28.crysongs.icons.skip_next
import mb28.crysongs.icons.skip_previous
import mb28.crysongs.isPlaying
import mb28.crysongs.nowPlaying
import mb28.crysongs.nowPlayingI
import mb28.crysongs.player
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay

@Composable
fun FSPlayerButtonsRow() {
    Row(
        Modifier
            .scale(1.4f)
            .padding(vertical = 10.dp)
    ) {
        IconButton(
            {
                if (nowPlaying != null) {
                    Settings.addOrRemoveFavorite(nowPlaying!!.path)
                }
            },
            modifier = Modifier.scale(0.8f)
        ) {
            if (nowPlaying != null) {
                Icon(
                    if (Settings.favorites.contains(nowPlaying!!.path)) favorite else heart_plus,
                    null
                )
            } else {
                Icon(heart_plus, null, modifier = Modifier.alpha(0.5f))
            }

        }
        IconButton(
            {
                setAndPlay(
                    playerQuery[(nowPlayingI - 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            },
            enabled = playerQuery.isNotEmpty()
        ) {
            Icon(skip_previous, null)
        }
        IconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
                isPlaying = player.isPlaying
            },
            modifier = Modifier
                .scale(1.5f)
                .padding(horizontal = 8.dp)
        ) {
            Icon(if (isPlaying) pause_circle else play_circle, null)
        }
        IconButton(
            {
                setAndPlay(
                    playerQuery[(nowPlayingI + 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            },
            enabled = playerQuery.isNotEmpty()
        ) {
            Icon(skip_next, null)
        }
        IconButton(
            {
                Settings.loopTrack = !Settings.loopTrack
                player.isLooping = Settings.loopTrack
                Settings.save()
            },
            modifier = Modifier.scale(0.8f)
        ) {
            Icon(if (Settings.loopTrack) repeat_on else repeat, null)
        }
    }
}