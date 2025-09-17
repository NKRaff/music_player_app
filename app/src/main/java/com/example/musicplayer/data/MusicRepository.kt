package com.example.musicplayer.data

import android.content.ContentResolver
import android.provider.MediaStore
import java.io.File

class MusicRepository(private val contentResolver: ContentResolver) {

    fun getMusicFiles(): List<File> {
        val musicFiles = mutableListOf<File>()

        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(uri, projection, selection, null, null)

        cursor?.use {
            val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val filePath = cursor.getString(dataIndex)
                musicFiles.add(File(filePath))
            }
        }

        return musicFiles
    }
}