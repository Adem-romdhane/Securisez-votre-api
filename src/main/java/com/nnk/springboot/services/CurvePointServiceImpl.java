package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurvePointServiceImpl implements GenericService<CurvePoint, Integer> {

    private final CurvePointRepository curvePointRepository;

    @Override
    public CurvePoint add(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public List<CurvePoint> getAll() {
        return curvePointRepository.findAll();
    }

    @Override
    public CurvePoint update(Integer id, CurvePoint curvePoint) {
        return curvePointRepository.findById(id)
                .map(c -> {
                    c.setCurveId(c.getCurveId());
                    c.setAsOfDate(c.getAsOfDate());
                    c.setTerm(c.getTerm());
                    c.setValue(c.getValue());
                    c.setCreationDate(c.getCreationDate());
                    return curvePointRepository.save(c);
                }).orElseThrow(() -> new RuntimeException("curve point not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        curvePointRepository.deleteById(id);
        return "curve point deleted";
    }
}
