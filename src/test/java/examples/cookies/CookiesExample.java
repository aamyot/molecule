package examples.cookies;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.WebServer;
import com.vtence.molecule.http.Cookie;
import com.vtence.molecule.lib.CookieJar;
import com.vtence.molecule.middlewares.Cookies;
import com.vtence.molecule.routing.DynamicRoutes;

import java.io.IOException;

public class CookiesExample {

    public void run(WebServer server) throws IOException {
        // Enable cookies
        server.add(new Cookies())
              .start(new DynamicRoutes() {{

                  get("/").to(new Application() {
                      public void handle(Request request, Response response) throws Exception {
                          CookieJar cookies = CookieJar.get(request);
                          // Read client cookie
                          Cookie customer = cookies.get("customer");
                          response.body("Welcome, " + valueOf(customer));
                      }
                  });

                  get("/weapon").to(new Application() {
                      public void handle(Request request, Response response) throws Exception {
                          CookieJar cookies = CookieJar.get(request);
                          // Send back a cookie to the client
                          cookies.add("weapon", "rocket launcher").path("/ammo");
                      }
                  });

                  get("/ammo").to(new Application() {
                      public void handle(Request request, Response response) throws Exception {
                          CookieJar cookies = CookieJar.get(request);
                          // Refresh client cookies max age
                          cookies.add("ammo", "riding rocket").path("/ammo").maxAge(30);
                      }
                  });

                  get("/backfire").to(new Application() {
                      public void handle(Request request, Response response) throws Exception {
                          CookieJar cookies = CookieJar.get(request);
                          // Expire client cookie
                          cookies.discard("weapon").path("/ammo");
                      }
                  });
              }});
    }

    private String valueOf(Cookie cookie) {
        return cookie != null ? cookie.value() : null;
    }

    public static void main(String[] args) throws IOException {
        CookiesExample example = new CookiesExample();
        // Run the default web server
        WebServer webServer = WebServer.create();
        example.run(webServer);
        System.out.println("Access at " + webServer.uri());
    }
}