package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.DBUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBUserServiceImpl implements GenericService<DBUser, Integer>{

    private final DBUserRepository DBUserRepository;

    @Override
    public DBUser add(DBUser DBUser) {
        return DBUserRepository.save(DBUser);
    }

    @Override
    public List<DBUser> getAll() {
        return DBUserRepository.findAll();
    }

    @Override
    public DBUser update(Integer id, DBUser DBUser) {
        return DBUserRepository.findById(id)
                .map(u -> {
                    u.setFullname(u.getFullname());
                    u.setUsername(u.getUsername());
                    u.setPassword(u.getPassword());
                    return DBUserRepository.save(u);
                }).orElseThrow(() -> new RuntimeException("User non trouvé !"));
    }

    @Override
    public String deleteById(Integer id) {
        DBUserRepository.deleteById(id);
        return "user deleted";
    }

    @Override
    public DBUser findById(Integer id) {
        Optional<DBUser> optionalDBUser = DBUserRepository.findById(id);
        DBUser dbUser = null;
        if (optionalDBUser.isPresent()) {
            dbUser = optionalDBUser.get();
        }else {
            throw new RuntimeException("user not founded for id : " + id);
        }
        return dbUser;
    }

}
