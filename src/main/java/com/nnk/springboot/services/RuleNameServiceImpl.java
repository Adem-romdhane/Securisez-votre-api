package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RuleNameServiceImpl implements GenericService<RuleName, Integer> {

    private final RuleNameRepository ruleNameRepository;

    @Override
    public RuleName add(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    @Override
    public List<RuleName> getAll() {
        return ruleNameRepository.findAll();
    }

    @Override
    public RuleName update(Integer id, RuleName ruleName) {
        return ruleNameRepository.findById(id)
                .map(r -> {
                    r.setName(ruleName.getName());
                    r.setDescription(ruleName.getDescription());
                    r.setJson(ruleName.getJson());
                    r.setTemplate(ruleName.getTemplate());
                    r.setSqlStr(ruleName.getSqlStr());
                    r.setSqlPart(ruleName.getSqlPart());
                    return ruleNameRepository.save(r);
                }).orElseThrow(() -> new RuntimeException("rule name not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
        return "r name deleted";
    }

    @Override
    public RuleName findById(Integer id) {
        Optional<RuleName> optionalRuleName = ruleNameRepository.findById(id);
        RuleName ruleName = null;
        if (optionalRuleName.isPresent()){
            ruleName = optionalRuleName.get();
        }else {
            throw new RuntimeException("name not founded");
        }
        return ruleName;
    }
}
