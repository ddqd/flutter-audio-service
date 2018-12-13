import 'dart:async';

import 'package:flutter/services.dart';

import 'package:audio_service/player_state.dart';

import 'package:audio_service/source.dart';

import 'dart:convert' show json;


class AudioService {

  static const MethodChannel _plugin_channel =
  const MethodChannel('audio_service');

  static const BasicMessageChannel<dynamic> _audio_state =
  const BasicMessageChannel('audio_service_state_channel', JSONMessageCodec());

  static final StreamController<PlaybackState> ctrl = StreamController();

  
  static Future<String> getData(dynamic message) async {
    final Map<String, dynamic> jsonMessage = json.decode(message);
    print(jsonMessage);
    if (jsonMessage.containsKey("id")) {
      ctrl.sink.add(PlaybackState.createForTrackId(jsonMessage["id"]));
    }
    if (jsonMessage.containsKey("state")) {
      ctrl.sink.add(PlaybackState.create(jsonMessage["state"]));
    }
    return message;
  }

  static Stream<PlaybackState> state () {
    _audio_state.setMessageHandler(getData);
    return ctrl.stream;
  }

  static Future<String> play() async {
   String result = await _plugin_channel.invokeMethod('play');
   return result;
  }

  static Future<String> play_track(Track track) async {
    final trackJson = json.encode(track);
    final result = await _plugin_channel.invokeMethod('play', trackJson);
    return result;
  }

  static Future<String> pause() async {
    String result = await _plugin_channel.invokeMethod('pause');
    return result;
  }

  static Future<String> next() async {
    String result = await _plugin_channel.invokeMethod('next');
    return result;
  }

  static Future<String> prev() async {
    String result = await _plugin_channel.invokeMethod('prev');
    return result;
  }


  static Future<String> init(Source source) async {
    String j = json.encode(source);
    String result = await _plugin_channel.invokeMethod('init', j);
    return result;
  }
}
