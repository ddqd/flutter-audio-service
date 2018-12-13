package dema.im.audioservice.repository;

import android.net.Uri;

public class Track {
    private final String id;
    private final String streamUrl;
    private final String title;
    private final String imageUrl;

    Track(String id, String title, String imageUrl, String streamUrl) {
        this.id = id;
        this.title = title;
        this.streamUrl = streamUrl;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public Uri getStreamUri() {
        return Uri.parse(streamUrl);
    }

    public String getId() {
        return id;
    }

    public String getStreamUrl() {
        return streamUrl;
    }
}
