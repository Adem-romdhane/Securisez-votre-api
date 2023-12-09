package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    t.setAccount(t.getAccount());
                    t.setType(t.getType());
                    t.setBuyQuantity(t.getBuyQuantity());
                    t.setSellQuantity(t.getSellQuantity());
                    t.setBuyPrice(t.getBuyPrice());
                    t.setSellPrice(t.getSellPrice());
                    t.setBenchmark(t.getBenchmark());
                    t.setTradeDate(t.getTradeDate());
                    t.setSecurity(t.getSecurity());
                    t.setStatus(t.getStatus());
                    t.setTrader(t.getTrader());
                    t.setBook(t.getBook());
                    t.setCreationName(t.getCreationName());
                    t.setCreationDate(t.getCreationDate());
                    t.setRevisionName(t.getRevisionName());
                    t.setRevisionDate(t.getRevisionDate());
                    t.setDealName(t.getDealName());
                    t.setDealType(t.getDealType());
                    t.setSourceListId(t.getSourceListId());
                    t.setSide(t.getSide());
                    return tradeRepository.save(t);
                }).orElseThrow(() -> new RuntimeException("trade not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        tradeRepository.deleteById(id);
        return "trade deleted";
    }
}
