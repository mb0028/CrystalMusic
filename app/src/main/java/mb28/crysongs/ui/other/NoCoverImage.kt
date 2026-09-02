package mb28.crysongs.ui.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import mb28.crysongs.R

@Composable
fun NoCoverImage() {
    Box(
        Modifier
            .height(80.dp)
            .width(80.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                RoundedCornerShape(25.dp)
            )
    ) {
        Image(
            painterResource(R.drawable.null_track_cover),
            "Track cover",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )
    }
}