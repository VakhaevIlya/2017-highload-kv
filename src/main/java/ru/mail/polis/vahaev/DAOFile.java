package ru.mail.polis.vahaev;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public final class DAOFile implements DAO {

    @NotNull
    private final String dir;
    private final NonBlockingHashMap<String,byte[]> cache;
    public DAOFile(@NotNull String dir) {
        this.dir = dir;
        cache = new NonBlockingHashMap<>(15000);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull String id) throws NoSuchElementException, IllegalArgumentException, IOException {
        checkId(id);
        byte[] value = cache.get(id);
        if (value!=null){
            return value;
        }
        if (Files.notExists(Paths.get(dir, id))) {
            throw new NoSuchElementException("Can't find file with id=" + id);
        }
        value= Files.readAllBytes(Paths.get(dir, id));
        cache.put(id,value);
        return cache.get(id);
    }

    @Override
    public void upsert(@NotNull String id, @NotNull byte[] value) throws IllegalArgumentException, IOException {
        checkId(id);
        Files.write(Paths.get(dir, id), value);
        cache.remove(id);
    }

    @Override
    public void delete(@NotNull String id) throws IllegalArgumentException, IOException {
        checkId(id);
        Files.deleteIfExists(Paths.get(dir, id));
        cache.remove(id);
    }

    private void checkId(@NotNull String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id is empty");
        }
    }

}