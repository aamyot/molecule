package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static com.vtence.molecule.http.HeaderNames.SERVER;

public class ServerHeader extends AbstractMiddleware {

    private final String serverName;

    public ServerHeader(String serverName) {
        this.serverName = serverName;
    }

    public void handle(Request request, Response response) throws Exception {
        response.header(SERVER, serverName);

        forward(request, response);
    }
}