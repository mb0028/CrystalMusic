package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import mb28.crysongs.R
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
    val roundingAnim = animateDpAsState(
        if (isPlaying) 15.dp else 25.dp,
        SpringSpec(
            stiffness = Spring.StiffnessMedium
        )
    )
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
        FilledIconButton(
            {
                setAndPlay(
                    playerQuery[(nowPlayingI - 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            },
            enabled = playerQuery.isNotEmpty(),
            colors = IconButtonDefaults.filledIconButtonColors(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Icon(painterResource(R.drawable.skip_previous_24px), null)
        }
        FilledTonalIconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.start()
                }
                isPlaying = player.isPlaying
            },
            shape = RoundedCornerShape(roundingAnim.value),
            modifier = Modifier
                .scale(1.5f)
                .padding(horizontal = 8.dp)
        ) {
            Icon(if (isPlaying) painterResource(R.drawable.pause_24px)
                else painterResource(R.drawable.play_arrow_24px), null)
        }
        FilledIconButton(
            {
                setAndPlay(
                    playerQuery[(nowPlayingI + 1).coerceIn(0, playerQuery.count() - 1)],
                    false
                )
            },
            enabled = playerQuery.isNotEmpty()
        ) {
            Icon(painterResource(R.drawable.skip_next_24px), null)
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