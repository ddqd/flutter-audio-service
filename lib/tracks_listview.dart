import 'dart:async';

import 'package:audio_service/audio_service.dart';
import 'package:audio_service/source.dart';
import 'package:flutter/material.dart';

class TrackListView extends StatefulWidget {
  TrackListView(Iterable<Track> tracks) {
    this.tracklist = tracks;
  }

  @override
  State<StatefulWidget> createState() {
    return _TrackListViewState(tracklist);
  }

  List<Track> tracklist;
}

class _TrackListViewState extends State<TrackListView> {
  _TrackListViewState(this.tracklist);

  List<Track> tracklist;

  onTrackClick(Track track) {
    AudioService.play_track(track);
  }

  @override
  Widget build(BuildContext context) {
    ListTile makeListTile(Track track) => ListTile(
          onTap: () =>  onTrackClick(track),
          contentPadding: EdgeInsets.symmetric(vertical: 10.0),
          leading: Container(
            constraints: new BoxConstraints(maxHeight: 80),
            padding: EdgeInsets.only(right: 1.0, top: 1.0, bottom: 1.0),
            child: Image.network(track.imageUrl, fit: BoxFit.fitHeight),
          ),
          title: Text(
            track.title,
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
          subtitle: Padding(
            padding: EdgeInsets.only(right: 1.0),
            child: Text(
              track.streamUrl,
              style: TextStyle(color: Colors.white),
            ),
          ),
        );

    Card makeCard(Track track) => Card(
          elevation: 4.0,
          margin: new EdgeInsets.symmetric(horizontal: 6.0, vertical: 3.0),
          child: Container(
            decoration: BoxDecoration(
                color: track.isPlayning
                    ? Color.fromRGBO(22, 62, 70, 8)
                    : Color.fromRGBO(50, 50, 50, 7)),
            child: makeListTile(track),
          ),
        );

    final makeBody = Container(
      // decoration: BoxDecoration(color: Color.fromRGBO(58, 66, 86, 1.0)),
      child: ListView.builder(
        scrollDirection: Axis.vertical,
        shrinkWrap: true,
        itemCount: tracklist.length,
        itemBuilder: (BuildContext context, int index) {
          return makeCard(tracklist[index]);
        },
      ),
    );

    return makeBody;
  }
}
