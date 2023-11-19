package com.poly.service;

import com.poly.entity.Evaluate;
import com.poly.dto.EvaluateRes;
import org.springframework.data.domain.Page;

public interface EvaluateService {

    public Page<Evaluate> getAll(EvaluateRes evaluateRes);

    public Evaluate add(EvaluateRes evaluate);

    public void delete(Integer id);

    public Evaluate edit(Integer id);

    Evaluate findById(Integer id);
}
