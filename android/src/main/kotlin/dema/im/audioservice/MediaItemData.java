package dema.im.audioservice;

public class MediaItemData {

    public String title;

    public MediaItemData(String title, String url, String image, String id) {
        this.title = title;
        this.url = url;
        this.image = image;
        this.id = id;
    }

    public String url;
    public String image;
    public String id;
}
