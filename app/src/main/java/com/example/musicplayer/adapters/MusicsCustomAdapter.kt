package com.example.musicplayer.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.entities.MusicTrack

class MusicCustomAdapter (private val musicList: List<MusicTrack>) :
    RecyclerView.Adapter<MusicCustomAdapter.MusicsViewHolder>() {

    class MusicsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumArt: ImageView = itemView.findViewById(R.id.albumArt)
        val title: TextView = itemView.findViewById(R.id.title)
        val artist: TextView = itemView.findViewById(R.id.artist)
        val album: TextView = itemView.findViewById(R.id.album)
        val duration: TextView = itemView.findViewById(R.id.duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: MusicsViewHolder, position: Int) {
        val music = musicList[position]

        if (music.albumArt != null) {
            val bitmap = BitmapFactory.decodeByteArray(music.albumArt, 0, music.albumArt.size)
            holder.albumArt.setImageBitmap(bitmap)
        } else
            holder.albumArt.setImageResource(R.drawable.music_placeholder)

        holder.title.text = music.title
        holder.artist.text = music.artist
        holder.album.text = music.album
        holder.duration.text = formatDuration(music.duration)

    }

    fun formatDuration(duration: Int): String {
        val minutes = (duration / 1000) / 60 // Converte para minutos
        val seconds = (duration / 1000) % 60 // Converte para segundos

        // Formata como mm:ss, garantindo que o número de segundos tenha 2 dígitos
        return String.format("%02d:%02d", minutes, seconds)
    }
}