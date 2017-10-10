package ru.mail.polis.vahaev;

import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class Service implements KVService {
    @NotNull
    private final HttpServer server;

    @NotNull
    private final DAO Dao;
    private static final String PREFIX = "id=";


    public Service(@NotNull DAO Dao, int port) throws IOException {
this.Dao=Dao;
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/v0/entity", http -> {
            String id = getID(http.getRequestURI().getQuery());
            if(id.equals("")) http.sendResponseHeaders(400, 0);
            else {
                switch (http.getRequestMethod()) {
                    case "GET":
                        try {
                            byte value[] = Dao.get(id);
                            http.sendResponseHeaders(200, value.length);
                            http.getResponseBody().write(value);

                        } catch (NoSuchElementException e) {
                            http.sendResponseHeaders(404, 0);
                        }

                        break;

                    case "PUT":
                        byte[] value = new byte[http.getRequestBody().available()];

                        http.getRequestBody().read(value);
                        Dao.upsert(id, value);

                        http.sendResponseHeaders(201, 0);
                        break;

                    case "DELETE":
                        Dao.delete(id);
                        http.sendResponseHeaders(202, 0);
                        break;

                    default:
                        http.sendResponseHeaders(405, 0);
                }
            }
            http.close();
        });

        server.createContext("/v0/status", http -> {
            String response = "ONLINE";
            http.sendResponseHeaders(200, response.length());
            http.getResponseBody().write(response.getBytes());
            http.close();
        });
    }
    private static String getID(@NotNull final String query) {
        if (!query.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Error string");
        }
        return query.substring(PREFIX.length());
    }
    @Override
    public void start() {
server.start();
    }
    @Override
    public void stop() {
server.stop(0);
    }
}
