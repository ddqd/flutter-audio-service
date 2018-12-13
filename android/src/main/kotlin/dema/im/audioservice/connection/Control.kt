package dema.im.audioservice.connection

interface Control {
    fun play_id(id: String)
    fun play()
    fun stop()
    fun pause()
    fun next_track()
    fun prev_track()

}