package mb28.crysongs.glance

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import mb28.crysongs.MainActivity
import mb28.crysongs.R
import mb28.crysongs.canChangeTrack
import mb28.crysongs.isPlaying
import mb28.crysongs.lastLrcLine
import mb28.crysongs.nowPlayingTags
import mb28.crysongs.playNextOrPrevious
import mb28.crysongs.player
import mb28.crysongs.privateNowPlayingCover

class PlayerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PlayerWidget()
}

class PlayerWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            PlaybackControls()
        }
    }
}

@Composable
private fun PlaybackControls() {
    val context = LocalContext.current

    Row(
        GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.secondaryContainer)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            GlanceModifier
                .size(100.dp)
                .padding(10.dp)
                .cornerRadius(20.dp)
        ) {
            with(privateNowPlayingCover) {
                Image(
                    if (this != null) ImageProvider(this)
                    else ImageProvider(R.drawable.null_track_cover_small),
                    "Track cover",
                    contentScale = ContentScale.FillBounds,
                    modifier = GlanceModifier
                        .background(GlanceTheme.colors.widgetBackground)
                        .fillMaxSize()
                        .cornerRadius(20.dp)
                )
            }
        }

        Column (
            GlanceModifier.fillMaxWidth(),
            Alignment.CenterVertically,
            Alignment.CenterHorizontally
        ) {
            if (nowPlayingTags != null) {
                Text(nowPlayingTags?.title ?: "Nothing is playing", maxLines = 1,
                    style = TextStyle(color = GlanceTheme.colors.onSurface))
            }

            Row {
                CircleIconButton(
                    ImageProvider(R.drawable.skip_previous_24px),
                    null,
                    {
                        controlPlayback(2, context)
                    },
                    backgroundColor = GlanceTheme.colors.tertiary,
                    contentColor = GlanceTheme.colors.onTertiary,
                    enabled = canChangeTrack
                )
                Spacer(GlanceModifier.width(5.dp))
                SquareIconButton(
                    ImageProvider(if (isPlaying) R.drawable.pause_24px else R.drawable.play_arrow_24px),
                    null,
                    {
                        controlPlayback(1, context)
                    },
                    modifier = GlanceModifier.size(50.dp)
                )
                Spacer(GlanceModifier.width(5.dp))
                CircleIconButton(
                    ImageProvider(R.drawable.skip_next_24px),
                    null,
                    {
                        controlPlayback(3, context)
                    },
                    backgroundColor = GlanceTheme.colors.secondary,
                    contentColor = GlanceTheme.colors.onSecondary,
                    enabled = canChangeTrack
                )

                Spacer(GlanceModifier.width(10.dp))
            }

            if (lastLrcLine.isNotEmpty()) {
                Text(lastLrcLine, maxLines = 1,
                    style = TextStyle(color = GlanceTheme.colors.onSurface))
            }


        }
    }
}

fun controlPlayback(action: Int, context: Context) {
    try {
        when (action) {
            1 -> if (player.isPlaying) player.pause() else player.play()
            2 -> playNextOrPrevious(false)
            3 -> playNextOrPrevious()
        }
    } catch (_: Exception) {
        Toast.makeText(context,
            "Player is not started. open app\nor wait for next updates",
            Toast.LENGTH_SHORT
        ).show()
    }
}
