package com.example.vtaxer.service;

import com.example.vtaxer.model.Revenue;
import com.example.vtaxer.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public List<Revenue> getRevenueByType(String type) {
        return revenueRepository.findByTypeOrderByNameAsc(type);
    }

    public Revenue saveRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    public List<Revenue> getAllRevenue() {
        return revenueRepository.findAll();
    }
}