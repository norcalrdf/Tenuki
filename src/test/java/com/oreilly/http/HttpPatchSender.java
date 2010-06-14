package com.oreilly.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpPatchSender {

	private URI uri;
	private String mimeType;
	private File file;
	public URI getUri() {
        return uri;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public HttpResponse send() throws ClientProtocolException, IOException
	{
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch();
        patch.setURI(uri);
        FileEntity fe = new FileEntity(file, mimeType);
        patch.setEntity(fe);
        return client.execute(patch);
    }
}
