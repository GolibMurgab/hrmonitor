package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.SlaDto;
import com.myproject.hrmonitor.entity.SLA;
import com.myproject.hrmonitor.repository.SlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlaService {
    @Autowired
    private SlaRepository slaRepository;

    public void save(SlaDto slaDto){
        SLA sla = new SLA();
        sla.setDuration(slaDto.getTotalDuration());
        sla.setStage(slaDto.getStage());
        slaRepository.save(sla);
    }

    public List<SLA> getAllSla() {
        return slaRepository.findAll();
    }
}
