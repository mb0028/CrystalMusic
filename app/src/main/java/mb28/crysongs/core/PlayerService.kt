package mb28.crysongs.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import mb28.crysongs.nowPlaying
import mb28.crysongs.playNextOrPrevious

private const val ADD_OR_REMOVE_FAVORITES = "ADD_OR_REMOVE_FAVORITES"

@SuppressLint("UnsafeOptInUsageError")
val favoriteAddButton = CommandButton.Builder(CommandButton.ICON_HEART_UNFILLED)
    .setSessionCommand(SessionCommand(ADD_OR_REMOVE_FAVORITES, Bundle.EMPTY))
    .setSlots(CommandButton.SLOT_OVERFLOW)
    .setDisplayName("Add to favorites")
    .build()

@SuppressLint("UnsafeOptInUsageError")
val favoriteRemoveButton = CommandButton.Builder(CommandButton.ICON_HEART_FILLED)
    .setSessionCommand(SessionCommand(ADD_OR_REMOVE_FAVORITES, Bundle.EMPTY))
    .setSlots(CommandButton.SLOT_OVERFLOW)
    .setDisplayName("Remove from favorite")
    .build()

@SuppressLint("UnsafeOptInUsageError")
val nextButton = CommandButton.Builder(CommandButton.ICON_NEXT)
    .setSessionCommand(SessionCommand(PLAY_NEXT, Bundle.EMPTY))
    .setSlots(CommandButton.SLOT_FORWARD)
    .setDisplayName("Next")
    .build()

@SuppressLint("UnsafeOptInUsageError")
val previousButton = CommandButton.Builder(CommandButton.ICON_PREVIOUS)
    .setSessionCommand(SessionCommand(PLAY_PREVIOUS, Bundle.EMPTY))
    .setSlots(CommandButton.SLOT_BACK)
    .setDisplayName("Previous")
    .build()

@SuppressLint("UnsafeOptInUsageError")
val aaa = CommandButton.Builder(CommandButton.ICON_SKIP_FORWARD_15)
    .setPlayerCommand(Player.COMMAND_SEEK_FORWARD)
    .setSlots(CommandButton.SLOT_OVERFLOW)
    .setDisplayName("Mute")
    .build()

var mediaSession: MediaSession? = null

class PlayerService : MediaSessionService() {

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()

        mediaSession = MediaSession.Builder(this, player)
            .setMediaButtonPreferences(listOf(favoriteAddButton, nextButton, aaa, previousButton))
            .setCallback(FavoriteMSC())
            .build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        NotificationManagerCompat.from(this).cancel(0)
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession
}

private const val PLAY_NEXT = "PLAY_NEXT"

private const val PLAY_PREVIOUS = "PLAY_PREVIOUS"

private class FavoriteMSC : MediaSession.Callback {
    @OptIn(UnstableApi::class)
    override fun onConnectAsync(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
    ): ListenableFuture<MediaSession.ConnectionResult> {
        val sessionCommands =
            MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(SessionCommand(ADD_OR_REMOVE_FAVORITES, Bundle.EMPTY))
                .add(SessionCommand(PLAY_NEXT, Bundle.EMPTY))
                .add(SessionCommand(PLAY_PREVIOUS, Bundle.EMPTY))
                .build()
        return Futures.immediateFuture(
            MediaSession.ConnectionResult.AcceptedResultBuilder(session, controller)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        )
    }

    @OptIn(UnstableApi::class)
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle,
    ): ListenableFuture<SessionResult> {
        if (customCommand.customAction == ADD_OR_REMOVE_FAVORITES) {

            if (Settings.favorites.contains(nowPlaying!!.path)) {
                Settings.favorites.remove(nowPlaying!!.path)
                mediaSession?.setMediaButtonPreferences(listOf(favoriteAddButton, nextButton, aaa, previousButton))
            } else {
                Settings.favorites.add(nowPlaying!!.path)
                mediaSession?.setMediaButtonPreferences(listOf(favoriteRemoveButton, nextButton, aaa, previousButton))
            }
            Settings.save()
        }
        else if (customCommand.customAction == PLAY_NEXT) {
            playNextOrPrevious()
        }
        else if (customCommand.customAction == PLAY_PREVIOUS) {
            playNextOrPrevious(false)
        }
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }
}
