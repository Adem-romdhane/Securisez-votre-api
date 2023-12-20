package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidListServiceImpl implements GenericService<BidList, Integer> {

    private final BidListRepository bidListRepository;

    @Override
    public BidList add(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    @Override
    public List<BidList> getAll() {
        return bidListRepository.findAll();
    }

    @Override
    public BidList update(Integer id, BidList bidList) {
        return bidListRepository.findById(id)
                .map(b -> {
                    b.setAccount(b.getAccount());
                    b.setType(b.getType());
                    b.setBidQuantity(b.getBidQuantity());
                    b.setAskQuantity(b.getAskQuantity());
                    b.setBid(b.getBid());
                    b.setAsk(b.getAsk());
                    b.setBenchmark(b.getBenchmark());
                    b.setBidListDate(b.getBidListDate());
                    b.setCommentary(b.getCommentary());
                    b.setSecurity(b.getSecurity());
                    b.setStatus(b.getStatus());
                    b.setTrader(b.getTrader());
                    b.setBook(b.getBook());
                    b.setCreationName(b.getCreationName());
                    b.setCreationDate(b.getCreationDate());
                    b.setRevisionName(b.getRevisionName());
                    b.setRevisionDate(b.getRevisionDate());
                    b.setDealName(b.getDealName());
                    b.setDealType(b.getDealType());
                    b.setSourceListId(b.getSourceListId());
                    b.setSide(b.getSide());
                    return bidListRepository.save(b);
                }).orElseThrow(() -> new RuntimeException("bid list not founded"));
    }

    @Override
    public String deleteById(Integer id) {
        bidListRepository.deleteById(id);
        return "bid list deleted";
    }

    @Override
    public BidList findById(Integer id) {
        Optional<BidList> optionalBidList = bidListRepository.findById(id);
        BidList bidList = null;
        if (optionalBidList.isPresent()){
            bidList = optionalBidList.get();
        }else {
            throw new RuntimeException("bidlist not founded : " + id);
        }
        return bidList;
    }
}
