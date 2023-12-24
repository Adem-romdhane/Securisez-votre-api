package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TradeServiceImpl implements GenericService<Trade, Integer> {

    private final TradeRepository tradeRepository;

    @Override
    public Trade add(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public List<Trade> getAll() {
        return tradeRepository.findAll();
    }

    @Override
    public Trade update(Integer id, Trade trade) {
        return tradeRepository.findById(id)
                .map(t -> {
                    t.setAccount(trade.getAccount());
                    t.setType(trade.getType());
                    t.setBuyQuantity(trade.getBuyQuantity());
                    return tradeRepository.save(t);
                }).orElseThrow(() -> new RuntimeException("trade not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        tradeRepository.deleteById(id);
        return "trade deleted";
    }

    @Override
    public Trade findById(Integer id) {
        Optional<Trade> optionalTrade = tradeRepository.findById(id);
        Trade trade = null;
        if (optionalTrade.isPresent()){
            trade = optionalTrade.get();
        }else {
            throw new RuntimeException("trade not founded ");
        }
        return trade;
    }
}
