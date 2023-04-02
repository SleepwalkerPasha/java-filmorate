package ru.yandex.practicum.filmorate.storage.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractInMemoryStorage<T> {

    private final Map<Long, T> map;

    protected AbstractInMemoryStorage() {
        this.map = new HashMap<>();
    }

    public T put(long id, T data) {
        map.put(id, data);
        return data;
    }

    public void remove(long id) {
        map.remove(id);
    }

    public T getById(long id) {
        return map.get(id);
    }

    public List<T> getValues() {
        return new ArrayList<>(map.values());
    }
}
