package ru.mail.polis.vahaev;

import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class Service implements KVService {
    @NotNull
    private final HttpServer server;

    @NotNull
    private final DAO dao;
    private static final String PREFIX = "id=";


    public Service(@NotNull DAO dao, int port) throws IOException {
        this.dao = dao;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/v0/entity", http -> {
            try {
                String id = getID(http.getRequestURI().getQuery());
                if ("".equals(id)) {
                    http.sendResponseHeaders(400, 0);
                    http.close();
                    return;
                }

                switch (http.getRequestMethod()) {
                    case "GET":
                        try {
                            byte value[] = dao.get(id);
                            http.sendResponseHeaders(200, value.length);
                            http.getResponseBody().write(value);

                        } catch (NoSuchElementException e) {
                            http.sendResponseHeaders(404, 0);
                        }

                        break;

                    case "PUT":
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        InputStream is = http.getRequestBody();
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                        final byte[] putValue = os.toByteArray();
                        dao.upsert(id, putValue);

                        http.sendResponseHeaders(201, 0);
                        break;

                    case "DELETE":
                        dao.delete(id);
                        http.sendResponseHeaders(202, 0);
                        break;

                    default:
                        http.sendResponseHeaders(405, 0);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
                http.sendResponseHeaders(404, 0);
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
