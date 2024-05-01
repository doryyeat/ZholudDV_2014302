package com.bookland.controller.main;

import com.bookland.model.Users;
import com.bookland.model.enums.Category;
import com.bookland.model.enums.Genre;
import com.bookland.repo.BooksDescriptionRepo;
import com.bookland.repo.BooksRepo;
import com.bookland.repo.StatisticsRepo;
import com.bookland.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

public class Main {
    @Autowired
    protected UsersRepo usersRepo;
    @Autowired
    protected BooksRepo booksRepo;
    @Autowired
    protected BooksDescriptionRepo booksDescriptionRepo;
    @Autowired
    protected StatisticsRepo statisticsRepo;

    @Value("${upload.img}")
    protected String uploadImg;

    protected Users getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return usersRepo.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getRole() {
        Users users = getUser();
        if (users == null) return "NOT";
        return users.getRole().toString();
    }

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
        model.addAttribute("categories", Category.values());
        model.addAttribute("genres", Genre.values());
    }
}