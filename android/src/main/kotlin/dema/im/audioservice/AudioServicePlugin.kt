package dema.im.audioservice

import android.content.Context
import android.media.session.PlaybackState
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.gson.Gson
import dema.im.audioservice.connection.AudioServiceConnection
import dema.im.audioservice.imageloader.ImageLoader
import dema.im.audioservice.repository.Source
import dema.im.audioservice.repository.Track
import io.flutter.plugin.common.*
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class AudioServicePlugin(val context: Context, private val mediaControllerCallback: MediaControllerCompat.Callback) : MethodCallHandler {

    private lateinit var audioServiceConnection: AudioServiceConnection


    companion object {
        private val gson: Gson = Gson()

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "audio_service")
            val audioServiceChannel = BasicMessageChannel(registrar.messenger(), "audio_service_state_channel", JSONMessageCodec.INSTANCE)
            channel.setMethodCallHandler(AudioServicePlugin(registrar.context(), MediaControllerCallback(audioServiceChannel)))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "pause" -> audioServiceConnection.pause()
            "play" -> {
                if (call.arguments != null) {
                    val track: Track = gson.fromJson<Track>(call.arguments.toString(), Track::class.java)
                    audioServiceConnection.play_id(track.id)
                    Log.d(TAG, "Play track id: ${track.id}")
                } else {
                    audioServiceConnection.play()
                }
            }
            "next" -> audioServiceConnection.next_track()
            "prev" -> audioServiceConnection.prev_track()
            "init" -> {
                audioServiceConnection = Injector.provideAudioSessionConnection(context)
                val source: Source = gson.fromJson<Source>(call.arguments.toString(), Source::class.java)
                Log.d(TAG, call.arguments.toString())
                audioServiceConnection.init(source, subscriptionCallback, mediaControllerCallback)
                result.success("init")
            }
            else -> result.notImplemented()
        }
    }

    private val mediaItems: MutableList<MediaItemData> = mutableListOf()

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

        val imageLoader = ImageLoader(context)

        override fun onError(parentId: String, options: Bundle) {
            super.onError(parentId, options)
            Log.d(TAG, "childLoad: ${parentId}")
        }

        override fun onError(parentId: String) {
            super.onError(parentId)
            Log.d(TAG, "childLoad: ${parentId}")
        }

        override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
            Log.d(TAG, "childLoad: ${children}")
            val itemsList = children.map { child ->
                Log.d(TAG, "child: ${child.description.toString()}")
                val items = children.map {
                    imageLoader.load(it.description.iconUri.toString())
                    MediaItemData(it.description.title.toString(), it.description.mediaUri.toString(), it.description.iconUri.toString(), it.mediaId)

                }.toList()
                mediaItems.addAll(items)
            }
        }
    }

    class MediaControllerCallback(private val messageChannel: BasicMessageChannel<Any>) : MediaControllerCompat.Callback() {


        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            val result = gson.toJson(state.toNativeState())
            messageChannel.send(result)
            Log.d(TAG, "state: ${state}")
            Log.d(TAG, "state.state: ${state?.state}")
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            Log.d(TAG, "metadata!: ${metadata?.description}")
            Log.d(TAG, "metadata: ${metadata}")
            Log.d(TAG, "metadata.uri: ${metadata?.description?.mediaUri}")
            Log.d(TAG, "metadata.uri2: ${metadata?.description?.mediaId}")
            Log.d(TAG, "metadata.id: ${metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)}")
            metadata?.keySet()?.forEach {
                Log.d(TAG, "key: " + it.toString())
            }

            val result = gson.toJson(PlayerState.Builder().id(metadata?.description?.mediaId).build())
            messageChannel.send(result)
        }
    }
}

fun PlaybackStateCompat.toNativeState(): PlayerState {
    val result =  when (state) {
        PlaybackState.STATE_PLAYING -> "onPlay"
        PlaybackState.STATE_PAUSED -> "onPause"
        PlaybackState.STATE_STOPPED -> "onStop"
        PlaybackState.STATE_BUFFERING -> "onBuffering"
        PlaybackState.STATE_NONE -> "unknown"
        else -> ""
    }
    return PlayerState.Builder().state(result).build()
}

private const val TAG = "AudioServicePlugin"

