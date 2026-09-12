package mb28.crysongs.glance

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import mb28.crysongs.MainActivity
import mb28.crysongs.R
import mb28.crysongs.core.Track
import mb28.crysongs.nowPlaying

class TurntableReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TurntableWidget()
}

class TurntableWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Turntable()
        }
    }
}

@Composable
private fun Turntable() {
    val context = LocalContext.current

    Box(
        GlanceModifier
            .fillMaxSize()
            .background(androidx.glance.color.ColorProvider(
                Color.Transparent, Color.Transparent))
            .cornerRadius(10.dp)
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.Center
    ) {
        var coverPath: String? = null
        if (nowPlaying != null) {
            coverPath = Track.createOrGetThumbnail(nowPlaying!!.path)
        }
        Image(
            if (coverPath != null) ImageProvider(BitmapFactory.decodeFile(coverPath))
            else ImageProvider(R.drawable.null_track_cover),
            "Track cover",
            contentScale = ContentScale.FillBounds,
            modifier = GlanceModifier
                .background(GlanceTheme.colors.widgetBackground)
                .size(140.dp)
                .cornerRadius(100.dp)
        )

        Box(
            GlanceModifier.fillMaxSize(),
            Alignment.BottomStart
        ) {
            SquareIconButton(
                ImageProvider(R.drawable.play_pause_24px),
                null,
                {
                    controlPlayback(1, context)
                },
                modifier = GlanceModifier.size(50.dp)
            )
        }

        Box(
            GlanceModifier.fillMaxSize(),
            Alignment.TopEnd
        ) {
            CircleIconButton(
                ImageProvider(R.drawable.skip_next_24px),
                null,
                {
                    controlPlayback(3, context)
                },
                backgroundColor = GlanceTheme.colors.tertiary,
                contentColor = GlanceTheme.colors.onTertiary
            )
        }
    }
}
