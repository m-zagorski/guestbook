package com.appunite.guestbook.api.content;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MemoryGsonHttpContent extends GsonHttpContent {
    private byte[] content;

    public MemoryGsonHttpContent(Gson gson) {
        super(gson);
        content = null;
    }

    private byte[] getContent() throws IOException {
        if (content != null) {
            return content;
        }

        final Object object = getObject();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        try {
            mGson.toJson(object, writer);
        } finally {
            writer.close();
        }
        content = out.toByteArray();
        return content;
    }

    @Override
    public long getLength() throws IOException {
        return getContent().length;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(getContent());
    }
}

