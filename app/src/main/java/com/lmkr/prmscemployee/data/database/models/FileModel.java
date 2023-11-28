package com.lmkr.prmscemployee.data.database.models;

import android.net.Uri;

public class FileModel {
    String fileName;   // = returnCursor.getString(nameIndex);
    String mimeType;   // = getContentResolver().getType(returnUri);
    String size;       // = Long.toString(returnCursor.getLong(sizeIndex));
    String path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    Uri uri;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
