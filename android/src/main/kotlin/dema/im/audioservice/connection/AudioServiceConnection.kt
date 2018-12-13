package dema.im.audioservice.connection

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import dema.im.audioservice.repository.MusicRepository
import dema.im.audioservice.repository.Source

class AudioServiceConnection(val context: Context, serviceComponent: ComponentName) : Control{

    override fun play_id(id: String) {
        transportControls.playFromMediaId(id, null)
    }

    override fun play() {
        transportControls.play()
    }

    override fun stop() {
        transportControls.stop()
    }

    override fun pause() {
        transportControls.pause()
    }

    override fun next_track() {
        transportControls.skipToNext()
    }

    override fun prev_track() {
        transportControls.skipToPrevious()
    }

    private lateinit var mediaControllerCallback : MediaControllerCompat.Callback

    companion object {

        @Volatile
        private var instance: AudioServiceConnection? = null

        fun getInstance(context: Context, serviceComponent: ComponentName) =
                instance ?: synchronized(this) {
                    instance
                            ?: AudioServiceConnection(context, serviceComponent)
                            .also { instance = it }
                }
    }

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private lateinit var mediaController: MediaControllerCompat

    inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            Log.d(TAG, "connected")
            mediaBrowser.subscribe("/", subscriptionCallback)
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(mediaControllerCallback)
            }
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            Log.d(TAG, "fail connection")
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            Log.d(TAG, "fail connection")
        }
    }

    fun init (source: Source,
              subscriptionCallback: MediaBrowserCompat.SubscriptionCallback,
              mediaControllerCallback: MediaControllerCompat.Callback) {
        MusicRepository.getInstance().setSource(source)
        this.subscriptionCallback = subscriptionCallback
        this.mediaControllerCallback = mediaControllerCallback
        mediaBrowser.connect()
        Log.d(TAG, "start connect")
    }

    private lateinit var subscriptionCallback: MediaBrowserCompat.SubscriptionCallback

    private val mediaBrowser =  MediaBrowserCompat(context, serviceComponent, ConnectionCallback(), null)

}

private const val TAG = "AudioServiceConnection"

@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
        .build()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
        .build()