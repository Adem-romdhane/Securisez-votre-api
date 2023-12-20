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
                .map(rule -> {
                    rule.setName(rule.getName());
                    rule.setDescription(rule.getDescription());
                    rule.setJson(rule.getJson());
                    rule.setTemplate(rule.getTemplate());
                    rule.setSqlStr(rule.getSqlStr());
                    rule.setSqlPart(rule.getSqlPart());
                    return ruleNameRepository.save(rule);
                }).orElseThrow(() -> new RuntimeException("rule name not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
        return "rule name deleted";
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
