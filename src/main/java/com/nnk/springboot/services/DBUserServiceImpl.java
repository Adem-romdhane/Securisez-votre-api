package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.DBUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class DBUserServiceImpl implements GenericService<DBUser, Integer> {

    private final DBUserRepository DBUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public DBUser add(DBUser DBuser) {
        if (!isValid(DBuser.getPassword())){
            throw new IllegalArgumentException("password non valide");
        }
        String encodedPassword = passwordEncoder.encode(DBuser.getPassword());
        DBuser.setPassword(encodedPassword);
        return DBUserRepository.save(DBuser);
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
        } else {
            throw new RuntimeException("user not founded for id : " + id);
        }
        return dbUser;
    }

    public static boolean isValid(String password) {
        Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$");
        Matcher m = p.matcher(password);
        System.out.println("PASSWORD VALID : " + m);
        if (m.find()) return true;
        return false;
    }
}
