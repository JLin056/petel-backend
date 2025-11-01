package com.example.petel.service.impl;

import com.example.petel.dto.MERCH015Tranrs;
import com.example.petel.dto.MERCH025Tranrs;
import com.example.petel.repository.FacilitiesRepository;
import com.example.petel.service.MERCH025Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH025SvcImpl implements MERCH025Svc {

    private final FacilitiesRepository facilitiesRepository;

    @Override
    public List<MERCH025Tranrs> getAllFacilities() {
        return facilitiesRepository.findAll()
                .stream()
                .map(p -> new MERCH025Tranrs(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }
}
