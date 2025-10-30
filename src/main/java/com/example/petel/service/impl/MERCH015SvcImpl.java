package com.example.petel.service.impl;

import com.example.petel.dto.MERCH015Tranrs;
import com.example.petel.repository.PostalsRepository;
import com.example.petel.service.MERCH015Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MERCH015SvcImpl implements MERCH015Svc {

    private final PostalsRepository postalRepository;

    @Override
    public List<MERCH015Tranrs> getCityDistricts() {
        return postalRepository.findAll()
            .stream()
            .map(p -> new MERCH015Tranrs(p.getId(), p.getCity(), p.getDistrict()))
            .collect(Collectors.toList());
    }
}
