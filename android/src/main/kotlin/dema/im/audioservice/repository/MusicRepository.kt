package dema.im.audioservice.repository

import java.util.ArrayList

class MusicRepository private constructor() {

    companion object {
        private var instance: MusicRepository? = null
        fun getInstance() =
                instance ?: MusicRepository()
                        .also { instance = it }
    }

    private var trackList: List<Track> = ArrayList()

    private var currentItemIndex = 0

    val next: Track
        get() {
            if (currentItemIndex == trackCount)
                currentItemIndex = 0
            else
                currentItemIndex++
            return current
        }

    val previous: Track
        get() {
            if (currentItemIndex == 0)
                currentItemIndex = trackCount
            else
                currentItemIndex--
            return current
        }

    val current: Track
        get() = trackList[currentItemIndex]

    val trackCount: Int
        get() = trackList.size

    fun setSource(source: Source) {
        this.trackList = source.tracks
    }

    fun getTrackByIndex(index: Int): Track {
        return trackList[index]
    }

    fun getTrackById(trackId: String): Track {
        return trackList.first { it.id.equals(trackId) }
    }

    fun isFirst() : Boolean {
        return currentItemIndex == 0
    }

    fun isLast() : Boolean {
        return currentItemIndex == trackCount - 1
    }


}
