package org.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchService<T> {
    private List<T> dataList;

    public SearchService(List<T> dataList) {
        this.dataList = dataList;
    }
    public T searchById(String id, Function<T, String> getId) {
        for (T item : dataList) {
            if (getId.apply(item).equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }
    public List<T> searchByName(String name, Function<T, String> getName) {
        List<T> result = new ArrayList<>();
        for (T item : dataList) {
            if (getName.apply(item).toLowerCase().contains(name.toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }
}
