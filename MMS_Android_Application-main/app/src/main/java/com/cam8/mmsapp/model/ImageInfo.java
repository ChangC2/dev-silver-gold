package com.cam8.mmsapp.model;

public class ImageInfo {
    int id = 0;
    String urlPath = "";
    boolean fromServer = false;

    public ImageInfo(int id, String urlPath, boolean fromServer) {
        this.id = id;
        this.urlPath = urlPath;
        this.fromServer = fromServer;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrlPath() { return urlPath; }
    public void setUrlPath(String urlPath) { this.urlPath = urlPath; }

    public boolean isFromServer() { return fromServer; }
    public void setFromServer(boolean fromServer) { this.fromServer = fromServer; }
}
