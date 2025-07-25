package com.example.musicplayer.entities

data class MusicTrack(
    // Campos essenciais
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int,
    val trackNumber: Int?,
    val genre: String?,

    // Extra visuais
    val albumArt: ByteArray?,

    // Informações de Arquivo
    val fileName: String,
    val filePath: String,

    // Informações opcionais
    val year: Int?,
)
