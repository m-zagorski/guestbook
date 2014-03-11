package com.appunite.guestbook.api.content;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamingGsonHttpContent extends GsonHttpContent {

    public StreamingGsonHttpContent(Gson gson) {
        super(gson);
    }

    @Override
    public long getLength() throws IOException {
        return -1;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        final Object object = getObject();
        final OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        try {
            mGson.toJson(object, writer);
        } finally {
            writer.close();
        }
    }
}
