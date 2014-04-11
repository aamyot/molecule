package com.vtence.molecule.templating;

import com.vtence.molecule.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class ViewTemplate implements View {

    private final RenderingEngine renderer;
    private final String name;
    private final String mediaType;

    public ViewTemplate(RenderingEngine renderer, String name, String mediaType) {
        this.renderer = renderer;
        this.name = name;
        this.mediaType = mediaType;
    }

    public void render(Response response, Object context) throws IOException {
        response.contentType(mediaType);
        Writer out = new BufferedWriter(response.writer());
        renderer.render(out, name, context);
        out.flush();
    }
}