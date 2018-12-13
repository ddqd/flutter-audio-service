package dema.im.audioservice

import android.content.ComponentName
import android.content.Context
import dema.im.audioservice.connection.AudioServiceConnection

object Injector {

    fun provideAudioSessionConnection(context: Context) : AudioServiceConnection {
        return AudioServiceConnection.getInstance(context.applicationContext,
                ComponentName(context.applicationContext, AudioService::class.java))
    }
}