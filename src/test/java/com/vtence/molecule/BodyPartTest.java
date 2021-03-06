package com.vtence.molecule;

import org.junit.Test;

import java.io.IOException;

import static com.vtence.molecule.helpers.Charsets.UTF_16;
import static com.vtence.molecule.helpers.Charsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BodyPartTest {

    @Test
    public void decodesTextContentAccordingToContentTypeCharset() throws IOException {
        String originalText = "Les naïfs ægithales hâtifs pondant à Noël où il gèle...";

        BodyPart part = new BodyPart().contentType("text/plain; charset=utf-16").content(originalText.getBytes(UTF_16));

        assertThat("decoded text", part.value(), equalTo(originalText));
    }

    @Test
    public void defaultsToUTF8DecodingWhenNoContentTypeIsSpecified() throws IOException {
        String originalText = "sont sûrs d'être déçus...";

        BodyPart part = new BodyPart().content(originalText.getBytes(UTF_8));

        assertThat("decoded text", part.value(), equalTo(originalText));
    }

    @Test
    public void defaultsToUTF8DecodingWhenNoCharsetIsSpecified() throws IOException {
        String originalText = "en voyant leurs drôles d'oeufs abîmés.";

        BodyPart part = new BodyPart().contentType("text/plain").content(originalText.getBytes(UTF_8));

        assertThat("decoded text", part.value(), equalTo(originalText));
    }
}