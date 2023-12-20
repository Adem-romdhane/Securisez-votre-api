package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.DBUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class DBUserController {
    private final DBUserRepository DBUserRepository;

    public DBUserController(DBUserRepository DBUserRepository) {
        this.DBUserRepository = DBUserRepository;
    }

    @PostMapping("/create")
    public DBUser createUser(@RequestBody DBUser DBUser){
        return DBUserRepository.save(DBUser);
    }

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", DBUserRepository.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(DBUser DBUser) {

        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid DBUser DBUser, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            DBUser.setPassword(encoder.encode(DBUser.getPassword()));
            DBUserRepository.save(DBUser);
            model.addAttribute("users", DBUserRepository.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        DBUser DBUser = DBUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        DBUser.setPassword("");
        model.addAttribute("user", DBUser);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid DBUser DBUser,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        DBUser.setPassword(encoder.encode(DBUser.getPassword()));
        DBUser.setId(Long.valueOf(id));
        DBUserRepository.save(DBUser);
        model.addAttribute("users", DBUserRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        DBUser DBUser = DBUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        DBUserRepository.delete(DBUser);
        model.addAttribute("users", DBUserRepository.findAll());
        return "redirect:/user/list";
    }
}
