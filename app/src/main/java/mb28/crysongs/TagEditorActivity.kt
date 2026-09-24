package mb28.crysongs

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mb28.crysongs.core.Settings
import mb28.crysongs.icons.arrow_back
import mb28.crysongs.icons.image_arrow_up
import mb28.crysongs.icons.save
import mb28.crysongs.ui.theme.CrySongsTheme
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.AndroidArtwork
import java.io.File

class TagEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        super.onCreate(savedInstanceState)

        val track = intent.getStringExtra(EXTRA_PATH)
        if (track == null) {
            finish()
        }
        val trackFile = AudioFileIO.read(File(track!!))

        setContent {
            CrySongsTheme {
                Tags(trackFile, this, track)
            }
        }
    }
}

@SuppressLint("SdCardPath")
@Composable
private fun Tags(file: AudioFile, activity: TagEditorActivity, path: String) {
    val shape = remember { RoundedCornerShape(25.dp) }

    var coverBytes by remember { mutableStateOf(
        file.tag.artworkList.firstOrNull()?.binaryData) }
    var coverChanged by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val stream = activity.contentResolver.openInputStream(uri)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                coverBytes = stream?.readAllBytes()
            } else {
                Toast.makeText(activity, "Cannot change cover on android <13",
                    Toast.LENGTH_SHORT).show()
            }
            stream?.close()
            coverChanged = true
        }
    }

    val title = rememberTextFieldState(file.tag.getFirst(FieldKey.TITLE))
    val artist = rememberTextFieldState(file.tag.getFirst(FieldKey.ARTIST))
    val album = rememberTextFieldState(file.tag.getFirst(FieldKey.ALBUM))
    val composer = rememberTextFieldState(file.tag.getFirst(FieldKey.COMPOSER))
    val genre = rememberTextFieldState(file.tag.getFirst(FieldKey.GENRE))

    val year = rememberTextFieldState(file.tag.getFirst(FieldKey.YEAR))
    val trackN = rememberTextFieldState(file.tag.getFirst(FieldKey.TRACK))
    val trackTotal = rememberTextFieldState(file.tag.getFirst(FieldKey.TRACK_TOTAL))
    val diskN = rememberTextFieldState(file.tag.getFirst(FieldKey.DISC_NO))
    val diskTotal = rememberTextFieldState(file.tag.getFirst(FieldKey.DISC_TOTAL))

    val lyricist = rememberTextFieldState(file.tag.getFirst(FieldKey.LYRICIST))
    val lyrics = rememberTextFieldState(file.tag.getFirst(FieldKey.LYRICS))
    val comment = rememberTextFieldState(file.tag.getFirst(FieldKey.COMMENT))

    val albumArtist = rememberTextFieldState(file.tag.getFirst(FieldKey.ALBUM_ARTIST))
    val mood = rememberTextFieldState(file.tag.getFirst(FieldKey.MOOD))
    val remixer = rememberTextFieldState(file.tag.getFirst(FieldKey.REMIXER))
    val recordLabel = rememberTextFieldState(file.tag.getFirst(FieldKey.RECORD_LABEL))

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                { Text("Tag Editor") },
                navigationIcon = {
                    IconButton(
                        { activity.finish() },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        modifier = Modifier.padding(horizontal = 15.dp)
                    ) { Icon(arrow_back, null) }
                },
                actions = {
                    if (coverBytes != null) {
                        IconButton(
                            {
                                val t = System.currentTimeMillis()
                                val f = File("/sdcard/Download/$t.png")
                                println(f.path)
                                f.createNewFile()
                                f.writeBytes(coverBytes!!)
                                Toast.makeText(activity, "Cover saved in 0/Download/$t.png",
                                    Toast.LENGTH_SHORT).show()
                            },
                        ) { Icon(image_arrow_up, null) }
                    }
                    IconButton(
                        {
                            if (title.text != file.tag.getFirst(FieldKey.TITLE)) {
                                file.tag.setField(FieldKey.TITLE, title.text.toString())
                            }
                            if (artist.text != file.tag.getFirst(FieldKey.ARTIST)) {
                                file.tag.setField(FieldKey.ARTIST, artist.text.toString())
                            }
                            if (album.text != file.tag.getFirst(FieldKey.ALBUM)) {
                                file.tag.setField(FieldKey.ALBUM, album.text.toString())
                            }
                            if (composer.text != file.tag.getFirst(FieldKey.COMPOSER)) {
                                file.tag.setField(FieldKey.COMPOSER, composer.text.toString())
                            }
                            if (genre.text != file.tag.getFirst(FieldKey.GENRE)) {
                                file.tag.setField(FieldKey.GENRE, genre.text.toString())
                            }
                            if (year.text != file.tag.getFirst(FieldKey.YEAR)) {
                                file.tag.setField(FieldKey.YEAR, year.text.toString())
                            }
                            if (trackN.text != file.tag.getFirst(FieldKey.TRACK)) {
                                file.tag.setField(FieldKey.TRACK, trackN.text.toString())
                            }
                            if (trackTotal.text != file.tag.getFirst(FieldKey.TRACK_TOTAL)) {
                                file.tag.setField(FieldKey.TRACK_TOTAL, trackTotal.text.toString())
                            }
                            if (diskN.text != file.tag.getFirst(FieldKey.DISC_NO)) {
                                file.tag.setField(FieldKey.DISC_NO, diskN.text.toString())
                            }
                            if (diskTotal.text != file.tag.getFirst(FieldKey.DISC_TOTAL)) {
                                file.tag.setField(FieldKey.DISC_TOTAL, diskTotal.text.toString())
                            }
                            if (lyricist.text != file.tag.getFirst(FieldKey.LYRICIST)) {
                                file.tag.setField(FieldKey.LYRICIST, lyricist.text.toString())
                            }
                            if (lyrics.text != file.tag.getFirst(FieldKey.LYRICS)) {
                                file.tag.setField(FieldKey.LYRICS, lyrics.text.toString())
                            }
                            if (comment.text != file.tag.getFirst(FieldKey.COMMENT)) {
                                file.tag.setField(FieldKey.COMMENT, comment.text.toString())
                            }
                            if (albumArtist.text != file.tag.getFirst(FieldKey.ALBUM_ARTIST)) {
                                file.tag.setField(FieldKey.ALBUM_ARTIST, albumArtist.text.toString())
                            }
                            if (mood.text != file.tag.getFirst(FieldKey.MOOD)) {
                                file.tag.setField(FieldKey.MOOD, mood.text.toString())
                            }
                            if (remixer.text != file.tag.getFirst(FieldKey.REMIXER)) {
                                file.tag.setField(FieldKey.REMIXER, remixer.text.toString())
                            }
                            if (recordLabel.text != file.tag.getFirst(FieldKey.RECORD_LABEL)) {
                                file.tag.setField(FieldKey.RECORD_LABEL, recordLabel.text.toString())
                            }
                            if (coverChanged) {
                                val artwork = File(Settings.appCacheThumbsFolder + "_LastPicked.png")
                                artwork.createNewFile()
                                artwork.writeBytes(coverBytes!!)
                                try {
                                    file.tag.deleteArtworkField()
                                    file.tag.setField(AndroidArtwork.createArtworkFromFile(artwork))
                                } catch (_: Exception) {}
                                val thumbnailFile = File("${Settings.appCacheThumbsFolder}/${path.hashCode()}.jpeg")
                                if (thumbnailFile.exists()) {
                                    thumbnailFile.delete()
                                }
                            }
                            file.commit()
                            Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show()
                            activity.finish()
                        },
                    ) { Icon(save, null) }
                }
            )
        }
    ) { p ->
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 300.dp)
        ) {
            item {
                Spacer(Modifier.padding(top = p.calculateTopPadding() + 10.dp))
                if (coverBytes != null) {
                    val img = BitmapFactory.decodeByteArray(coverBytes, 0,
                            coverBytes!!.size).asImageBitmap()
                    Box(
                        Modifier.fillMaxWidth(),
                        Alignment.Center
                    ) {
                        Image(
                            img, null,
                            Modifier
                                .size(250.dp)
                                .scale(1.4f)
                                .blur(80.dp, BlurredEdgeTreatment.Unbounded)
                        )
                        Image(
                            img, null,
                            Modifier
                                .size(250.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .clickable {
                                    picker.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                        )
                    }
                } else {
                    Text("No artwork data.")
                }
            }
            item {
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    title, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Title") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    artist, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Artist") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    album, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Album") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    composer, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Composer") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    genre, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Genre") }
                )
                HorizontalDivider(Modifier.padding(top = 10.dp))
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    year, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Year") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                Row {
                    OutlinedTextField(
                        trackN, Modifier.fillMaxWidth(0.5f), shape = shape,
                        label = { Text("Track Num.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        trackTotal, Modifier.fillMaxWidth(), shape = shape,
                        label = { Text("Track Total") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            item {
                Spacer(Modifier.height(10.dp))
                Row {
                    OutlinedTextField(
                        diskN, Modifier.fillMaxWidth(0.5f), shape = shape,
                        label = { Text("Disk Num") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Date)
                    )
                    OutlinedTextField(
                        diskTotal, Modifier.fillMaxWidth(), shape = shape,
                        label = { Text("Disk Total") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                HorizontalDivider(Modifier.padding(top = 10.dp))
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    lyricist, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Lyricist") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    lyrics, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Lyrics") },
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    comment, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Comment") }
                )
                HorizontalDivider(Modifier.padding(top = 10.dp))
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    albumArtist, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Album Artist") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    mood, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Mood") }
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    remixer, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Remixer") },
                )
            }
            item {
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    recordLabel, Modifier.fillMaxWidth(), shape = shape,
                    label = { Text("Record Label") }
                )
            }

        }
    }
}











