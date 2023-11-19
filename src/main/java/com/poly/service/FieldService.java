package com.poly.service;

import com.poly.entity.Field;

import java.util.List;

public interface FieldService {
    List<Field> findAll();

    Field findById(Integer id);

    Field save(Field field);
}
