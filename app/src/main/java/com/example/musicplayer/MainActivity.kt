package com.example.musicplayer

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.musicplayer.adapters.MusicCustomAdapter
import com.example.musicplayer.data.MusicMetadataReader
import com.example.musicplayer.data.MusicRepository
import com.example.musicplayer.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

private  lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var musicRepository: MusicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        agendarAtualizacaoMusicas(this)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        musicRepository = MusicRepository(contentResolver)

        val musicFiles = musicRepository.getMusicFiles()
        val musicTracks = musicFiles.mapNotNull {
            try {
                MusicMetadataReader.extractMetadata(it)
            } catch (e: Exception) {
                Log.e("MusicMetadataError", "Erro inesperado ao ler metadados do arquivo ${it.path}: ${e.message}", e)
                null
            }
        }

        Log.d("MusicTracks", "Encontradas ${musicTracks.size} músicas")

        val recyclerView = binding.musicsRecyclerView
        val musicAdapter = MusicCustomAdapter(musicTracks)
        recyclerView.adapter = musicAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 1001)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
        }
    }

    class UpdateMusicLibraryWorker(
        context: Context,
        workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

        private val repository = MusicRepository(context.contentResolver)

        override fun doWork(): Result {
            try {
                val musicFiles = repository.getMusicFiles()

                val musicTracks = musicFiles.map { file ->
                    MusicMetadataReader.extractMetadata(file)
                }

                println("Encontradas ${musicTracks.size} músicas.")

                return Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                return Result.failure()
            }
        }
    }

    fun agendarAtualizacaoMusicas(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<UpdateMusicLibraryWorker>(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "update_music_library",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

}