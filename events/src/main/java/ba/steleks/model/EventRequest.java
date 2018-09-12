package ba.steleks.model;

import java.util.List;

public class EventRequest {

    private String title;
    private String shortText;
    private String longText;
    private List<Media> medias;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    @Override
    public String toString() {
        return "EventRequest{" +
                "title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", longText='" + longText + '\'' +
                ", medias=" + medias +
                '}';
    }
}
