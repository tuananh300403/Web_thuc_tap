package com.poly.service.Impl;

import com.poly.entity.Field;
import com.poly.repository.FieldRepo;
import com.poly.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldImpl implements FieldService {

    @Autowired
    private FieldRepo fieldRepo;

    @Override
    public List<Field> findAll() {
        return fieldRepo.findAll();
    }

    @Override
    public Field findById(Integer id) {
        return fieldRepo.findById(id).get();
    }

    @Override
    public Field save(Field field) {
        return fieldRepo.save(field);
    }
}
