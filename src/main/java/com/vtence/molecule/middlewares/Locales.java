package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.http.AcceptLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Locales extends AbstractMiddleware {

    private final List<Locale> supported;

    public Locales(String... supported) {
        this.supported = fromLanguageTags(supported);
    }

    private static List<Locale> fromLanguageTags(String... languageTags) {
        List<Locale> locales = new ArrayList<Locale>();
        for (String tag : languageTags) {
            locales.add(Locale.forLanguageTag(tag));
        }
        return locales;
    }

    public void handle(Request request, Response response) throws Exception {
        AcceptLanguage acceptedLanguages = AcceptLanguage.of(request);
        Locale best = acceptedLanguages.selectBest(supported);
        request.attribute(Locale.class, best != null ? best : Locale.getDefault());
        try {
            forward(request, response);
        } finally {
            request.removeAttribute(Locale.class);
        }
    }
}