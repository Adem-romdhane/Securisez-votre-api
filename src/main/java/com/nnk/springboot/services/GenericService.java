package com.nnk.springboot.services;
import java.util.List;

public interface GenericService<T, Integer> {

    T add(T t);
    List<T> getAll();
    T update(Integer id, T t);
    String deleteById(Integer id);

    T findById(Integer id);
}
