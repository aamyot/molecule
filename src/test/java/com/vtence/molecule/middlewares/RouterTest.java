package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.matchers.Anything;
import com.vtence.molecule.lib.matchers.Matcher;
import com.vtence.molecule.lib.matchers.Nothing;
import com.vtence.molecule.routing.Route;
import org.junit.Test;

import static com.vtence.molecule.testing.ResponseAssert.assertThat;

public class RouterTest {

    Router router = new Router(new NotFound());
    Request request = new Request();
    Response response = new Response();

    @Test public void
    routesToDefaultWhenNoRouteMatches() throws Exception {
        router.defaultsTo(route("default")).add(new StaticRoute(none(), route("other")));
        router.handle(request, response);
        assertRoutedTo("default");
    }

    @Test public void
    dispatchesToFirstRouteThatMatches() throws Exception {
        router.add(new StaticRoute(all(), route("preferred")));
        router.add(new StaticRoute(all(), route("alternate")));
        router.handle(request, response);
        assertRoutedTo("preferred");
    }

    private void assertRoutedTo(String route) {
        assertThat(response).hasBodyText(route);
    }

    private Application route(final String name) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(name);
            }
        };
    }

    public static Matcher<Request> all() {
        return new Anything<Request>();
    }

    public static Matcher<Request> none() {
        return new Nothing<Request>();
    }

    private class StaticRoute implements Route {
        private final Matcher<Request> requestMatcher;
        private final Application app;

        public StaticRoute(Matcher<Request> requestMatcher, Application app) {
            this.requestMatcher = requestMatcher;
            this.app = app;
        }

        public void handle(Request request, Response response) throws Exception {
            app.handle(request, response);
        }

        public boolean matches(Request actual) {
            return requestMatcher.matches(actual);
        }
    }
}