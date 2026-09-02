package mb28.crysongs.ui.other

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import mb28.crysongs.icons.shuffle
import mb28.crysongs.playerQuery
import mb28.crysongs.setAndPlay
import mb28.crysongs.tracks

@Composable
fun ShuffleButton(modifier: Modifier = Modifier) {
    FilledTonalIconButton(
        {
            playerQuery = tracks.shuffled().toMutableStateList()
            setAndPlay(playerQuery.first(), false)
        }
    ) {
        Icon(shuffle, null)
    }
}