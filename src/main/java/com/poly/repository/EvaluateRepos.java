package com.poly.repository;

import com.poly.entity.Evaluate;
import com.poly.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EvaluateRepos extends JpaRepository<Evaluate, Integer> {

    @Query(value = "select avg(e.point) from Evaluate e where e.product=?1")
    float avgPoint(Product product);
}
