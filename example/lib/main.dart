import 'dart:async';

import 'package:flutter/material.dart';
import 'package:audio_service/audio_service.dart';

import 'package:audio_service/player_state.dart';

import 'package:audio_service/source.dart';
import 'package:audio_service/tracks_listview.dart';
import 'package:flutter/widgets.dart';

var tracksList = [
  Track(
      "0",
      "SomaFM: Deep Space",
      "http://somafm.com/img3/deepspaceone-400.jpg",
      "http://ice1.somafm.com/deepspaceone-128-mp3"),
  Track("1", "SomaFM: Drone Zone", "http://somafm.com/img3/dronezone-400.jpg",
      "http://ice3.somafm.com/dronezone-256-mp3"),
  Track(
      "2",
      "Space Station Soma",
      "http://somafm.com/img3/spacestation-400.jpg",
      "http://ice1.somafm.com/spacestation-128-mp3")
];

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  IconData _playIcon = Icons.play_arrow;
  PlayerState _state = PlayerState.unknown;
  List<Track> _tracks = tracksList;

  @override
  void initState() {
    super.initState();
    init();
    AudioService.state().listen((data) {
      if (data.playerState != null) {
        updatePlayIcon(data.playerState);
      }
      if (data.trackId != null) {
        updateCurrentTrack(data.trackId);
      }
    });
  }

  void updateCurrentTrack(String newTrackId) {
    setState(() {
      _tracks.forEach((f) => f.isPlayning = (f.id == newTrackId));
    });
  }

  void updatePlayIcon(PlayerState state) {
    setState(() {
      _state = state;
      // ignore: missing_enum_constant_in_switch
      switch (_state) {
        case PlayerState.onPlay:
          _playIcon = Icons.pause;
          break;
        case PlayerState.onPause:
          _playIcon = Icons.play_arrow;
          break;
        case PlayerState.onStop:
          _playIcon = Icons.stop;
      }
    });
  }

  Future<String> playPause() async {
    if (_state == PlayerState.onPlay) {
        return await AudioService.pause();
    } else {
        return await AudioService.play();
    }
  }

  Future<String> next() async {
    String result = await AudioService.next();
  }

  Future<String> prev() async {
    String result = await AudioService.prev();
  }

  Future<String> init() async {
    await AudioService.init(new Source(_tracks));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Player app'),
        ),
        body: new Center(
            child: new Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
              new TrackListView(_tracks),
              new Column(crossAxisAlignment: CrossAxisAlignment.end, mainAxisAlignment: MainAxisAlignment.end, children: <Widget>[
                new Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    new RaisedButton(
                        onPressed: () {
                          playPause();
                        },
                        child: new Icon(_playIcon)),
                    new RaisedButton(
                      onPressed: () {
                        prev();
                      },
                      child: new Icon(Icons.skip_previous),
                    ),
                    new RaisedButton(
                        onPressed: () {
                          next();
                        },
                        child: new Icon(Icons.skip_next))
                  ],
                ),
              ]),
            ])),
      ),
    );
  }
}
