package mb28.crysongs.ui.fullscreen_player

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import mb28.crysongs.R
import mb28.crysongs.canChangeTrack
import mb28.crysongs.core.Settings
import mb28.crysongs.core.Settings.loopTrack
import mb28.crysongs.icons.favorite
import mb28.crysongs.icons.heart_plus
import mb28.crysongs.icons.repeat
import mb28.crysongs.icons.repeat_on
import mb28.crysongs.isPlaying
import mb28.crysongs.nowPlaying
import mb28.crysongs.playNextOrPrevious
import mb28.crysongs.player
import mb28.crysongs.playerQuery

@Composable
fun FSPlayerButtonsRow() {
    val roundingAnim = animateDpAsState(
        if (isPlaying) 12.dp else 20.dp,
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
                    Settings.addOrRemoveFavorite(nowPlaying!!)
                }
            },
            modifier = Modifier.scale(0.8f)
        ) {
            if (nowPlaying != null) {
                Icon(
                    if (Settings.favorites.contains(nowPlaying!!)) favorite else heart_plus,
                    null
                )
            } else {
                Icon(heart_plus, null, modifier = Modifier.alpha(0.5f))
            }

        }
        FilledIconButton(
            {
                playNextOrPrevious(false)
            },
            enabled = playerQuery.isNotEmpty() && canChangeTrack,
            colors = IconButtonDefaults.filledIconButtonColors(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Icon(painterResource(R.drawable.skip_previous_24px), null)
        }
        FilledIconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
                isPlaying = player.isPlaying
            },
            shape = RoundedCornerShape(roundingAnim.value),
            modifier = Modifier.scale(1.5f).padding(horizontal = 8.dp)
        ) {
            Icon(
                if (isPlaying) painterResource(R.drawable.pause)
                    else painterResource(R.drawable.play),
                "Play / Pause",
                Modifier.padding(5.dp),
            )
        }
        FilledIconButton(
            {
                playNextOrPrevious()
            },
            enabled = playerQuery.isNotEmpty() && canChangeTrack,
            colors = IconButtonDefaults.filledIconButtonColors(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(painterResource(R.drawable.skip_next_24px), null)
        }
        IconButton(
            {
                loopTrack = !loopTrack
                player.repeatMode = if (loopTrack) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                Settings.save()
            },
            modifier = Modifier.scale(0.8f)
        ) {
            Icon(if (loopTrack) repeat_on else repeat, null)
        }
    }
}

@Composable
fun FSPlayerButtonsRowMaterial() {
    val roundingAnim by animateDpAsState(
        if (isPlaying) 12.dp else 20.dp,
        SpringSpec(
            stiffness = Spring.StiffnessMedium
        )
    )
    val widthAnim by animateDpAsState(
        if (isPlaying) 170.dp else 90.dp,
        SpringSpec(
            Spring.DampingRatioMediumBouncy,
            Spring.StiffnessLow
        )
    )
    Column(
        Modifier.fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        FilledIconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
                isPlaying = player.isPlaying
            },
            shape = RoundedCornerShape(roundingAnim),
            modifier = Modifier.scale(1.5f).width(widthAnim)
        ) {
            Icon(
                if (isPlaying) painterResource(R.drawable.pause)
                else painterResource(R.drawable.play),
                "Play / Pause",
                Modifier.padding(5.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.padding(vertical = 10.dp).scale(1.4f)) {
            FilledTonalIconButton({
                if (nowPlaying != null) {
                    Settings.addOrRemoveFavorite(nowPlaying!!)
                }
            }) {
                if (nowPlaying != null) {
                    Icon(
                        if (Settings.favorites.contains(nowPlaying!!)) favorite else heart_plus,
                        null
                    )
                } else {
                    Icon(heart_plus, null, modifier = Modifier.alpha(0.5f))
                }

            }

            FilledIconButton(
                {
                    playNextOrPrevious(false)
                },
                enabled = playerQuery.isNotEmpty() && canChangeTrack,
                colors = IconButtonDefaults.filledIconButtonColors(
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Icon(painterResource(R.drawable.skip_previous_24px), null)
            }

            FilledIconButton(
                { playNextOrPrevious() },
                enabled = playerQuery.isNotEmpty() && canChangeTrack,
                colors = IconButtonDefaults.filledIconButtonColors(
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onTertiary
                )
            ) { Icon(painterResource(R.drawable.skip_next_24px), null) }

            FilledTonalIconButton(
                {
                    loopTrack = !loopTrack
                    player.repeatMode = if (loopTrack) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                    Settings.save()
                },
            ) {
                Icon(if (loopTrack) repeat_on else repeat, null)
            }

        }
    }
}

@Composable
fun FSPlayerButtonsRowClear() {
    Row(
        Modifier.scale(1.4f).padding(vertical = 10.dp),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
        IconButton(
            {
                if (nowPlaying != null) {
                    Settings.addOrRemoveFavorite(nowPlaying!!)
                }
            },
        ) {
            if (nowPlaying != null) {
                Icon(
                    if (Settings.favorites.contains(nowPlaying!!)) favorite else heart_plus,
                    null,
                    Modifier.scale(0.8f)
                )
            } else {
                Icon(heart_plus, null, modifier = Modifier.scale(0.8f).alpha(0.5f))
            }
        }
        IconButton(
            {
                playNextOrPrevious(false)
            },
            enabled = playerQuery.isNotEmpty() && canChangeTrack
        ) {
            Icon(painterResource(R.drawable.skip_previous_24px), null)
        }
        IconButton(
            {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
                isPlaying = player.isPlaying
            },
        ) {
            Icon(
                if (isPlaying) painterResource(R.drawable.pause)
                else painterResource(R.drawable.play),
                "Play / Pause",
                Modifier.padding(5.dp),
            )
        }
        IconButton(
            {
                playNextOrPrevious()
            },
            enabled = playerQuery.isNotEmpty() && canChangeTrack
        ) {
            Icon(painterResource(R.drawable.skip_next_24px), null)
        }
        IconButton(
            {
                loopTrack = !loopTrack
                player.repeatMode = if (loopTrack) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                Settings.save()
            },
            Modifier.scale(0.8f)
        ) {
            Icon(if (loopTrack) repeat_on else repeat, null)
        }
    }
}
