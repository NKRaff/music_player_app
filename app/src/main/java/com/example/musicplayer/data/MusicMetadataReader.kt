package com.example.musicplayer.data

import android.media.MediaMetadataRetriever
import com.example.musicplayer.entities.MusicTrack
import java.io.File

object MusicMetadataReader {
    fun extractMetadata(file: File): MusicTrack {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)

        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: file.name
        val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Desconhecido"
        val album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "Sem Álbum"
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toIntOrNull() ?: 0
        val trackNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)?.toIntOrNull()
        val genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
        val year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toIntOrNull()
        val albumArt = retriever.embeddedPicture

        retriever.release()

        return MusicTrack(
            title, artist, album, duration, trackNumber, genre,
            albumArt, file.name, file.absolutePath, year
        )
    }
}