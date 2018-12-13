enum PlayerState { unknown, onPlay, onPause, onStop }

class PlayerStateHelper {
  static PlayerState create(String value) {
    PlayerState result;
    switch (value) {
      case 'unknown':
        result = PlayerState.unknown;
        break;
      case 'onPlay':
        result = PlayerState.onPlay;
        break;
      case 'onPause':
        result = PlayerState.onPause;
        break;
      case 'onStop':
        result = PlayerState.onStop;
        break;
    }
    return result;
  }
}

class PlaybackState {
  PlaybackState();

  PlayerState playerState = null;
  String trackId = null;

  setPlayerState(String state) {
    this.playerState = PlayerStateHelper.create(state);
  }

  setTrackId(String trackId) {
    this.trackId = trackId;
  }

  static PlaybackState create(String state) {
    var result = new PlaybackState();
    result.setPlayerState(state);
    return result;
  }

  static PlaybackState createForTrackId(String trackId) {
    var result = new PlaybackState();
    result.setTrackId(trackId);
    return result;
  }
}
