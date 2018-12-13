
class Source {
  List<Track> tracks;
  Source(this.tracks);

  Map<String, dynamic> toJson() => {
    'tracks': tracks.map((e) => e.toJson()).toList()
  };

}

class Track extends Object {
  final String id;
  final String title;
  final String imageUrl;
  final String streamUrl;
  bool isPlayning = false;

  Map<String, dynamic> toJson() => {
    'id' : id,
    'title': title,
    'imageUrl': imageUrl,
    'streamUrl': streamUrl
  };

  Track(this.id, this.title, this.imageUrl, this.streamUrl);

  static Track empty() {
    return Track("", null, null, null);
  }
}
