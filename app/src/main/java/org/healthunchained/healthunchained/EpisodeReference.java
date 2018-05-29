package org.healthunchained.healthunchained;

/**
 * Created by User on 5/28/2018.
 */

public class EpisodeReference {

    private String body;
    private String title;
    private String url;

    public EpisodeReference() {
    }

    public EpisodeReference(String body, String title, String url) {
        this.body = body;
        this.title = title;
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
