package com.bookland.controller;

import com.bookland.controller.main.Main;
import com.bookland.model.Books;
import com.bookland.model.BooksDescription;
import com.bookland.model.Statistics;
import com.bookland.model.Users;
import com.bookland.model.enums.BookStatus;
import com.bookland.model.enums.Category;
import com.bookland.model.enums.Genre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BooksCont extends Main {
    @GetMapping
    public String books(Model model) {
        AddAttributes(model);
        model.addAttribute("books", booksRepo.findAll());
        return "books";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(defaultValue = "") String search) {
        AddAttributes(model);
        model.addAttribute("search", search);

        List<Books> books = booksRepo.findAll();

        List<Books> res = new ArrayList<>();

        for (Books i : books) {
            if (i.getName().toLowerCase().contains(search.toLowerCase())) {
                res.add(i);
            }
        }

        model.addAttribute("books", res);
        return "books";
    }

    @GetMapping("/genre/{genre}")
    public String genre(Model model, @PathVariable Genre genre) {
        AddAttributes(model);
        model.addAttribute("books", booksRepo.findAllByDescription_Genre(genre));
        return "books";
    }

    @GetMapping("/genre/{genre}/search")
    public String genreSearch(Model model, @PathVariable Genre genre, @RequestParam(defaultValue = "") String name, @RequestParam Category category) {
        AddAttributes(model);
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("books", booksRepo.findAllByNameContainingAndDescription_GenreAndDescription_Category(name, genre, category));
        return "books";
    }

    @GetMapping("/{id}")
    public String Book(Model model, @PathVariable Long id) {
        AddAttributes(model);
        model.addAttribute("book", booksRepo.getReferenceById(id));
        return "book";
    }

    @GetMapping("/add")
    public String BookAdd(Model model) {
        AddAttributes(model);
        return "book_add";
    }

    @GetMapping("/edit/{id}")
    public String BookEdit(Model model, @PathVariable Long id) {
        AddAttributes(model);
        model.addAttribute("book", booksRepo.getReferenceById(id));
        return "book_edit";
    }

    @GetMapping("/delete/{id}")
    public String BookDelete(@PathVariable Long id) {
        booksRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/my")
    public String BookMy(Model model) {
        AddAttributes(model);
        model.addAttribute("books", getUser().getBooks());
        return "books_my";
    }

    @PostMapping("/rent/{id}")
    public String rent(@PathVariable Long id, @RequestParam String date) {
        Books book = booksRepo.getReferenceById(id);

        Users user = getUser();
        user.addBook(book);
        usersRepo.save(user);

        book.setDateFree(date);
        book.setStatus(BookStatus.BOOKED);
        book.getStatistics().setDays(book.getStatistics().getDays() + 1);

        booksRepo.save(book);

        return "redirect:/books/{id}";
    }

    @GetMapping("/taken/{id}")
    public String taken(@PathVariable Long id) {
        Books book = booksRepo.getReferenceById(id);
        book.setStatus(BookStatus.TAKEN);
        booksRepo.save(book);
        return "redirect:/books/my";
    }

    @GetMapping("/return/{id}")
    public String BookReturn(@PathVariable Long id) {
        Users user = getUser();
        Books book = booksRepo.getReferenceById(id);
        book.setDateFree("");
        book.setStatus(BookStatus.ACTIVE);
        user.removeBook(book);
        usersRepo.save(user);
        return "redirect:/books/my";
    }

    @PostMapping("/add")
    public String BookAddNew(Model model, @RequestParam String name, @RequestParam String isbn, @RequestParam MultipartFile[] photos, @RequestParam String date, @RequestParam int quantity, @RequestParam String description, @RequestParam Category category, @RequestParam Genre genre, @RequestParam String author) {
        Books book;
        if (photos != null && !Objects.requireNonNull(photos[0].getOriginalFilename()).isEmpty()) {
            try {
                String[] result_photos;
                String result_screenshot;
                String uuidFile = UUID.randomUUID().toString();
                result_photos = new String[photos.length];
                for (int i = 0; i < result_photos.length; i++) {
                    result_screenshot = "books/" + uuidFile + "_" + photos[i].getOriginalFilename();
                    photos[i].transferTo(new File(uploadImg + "/" + result_screenshot));
                    result_photos[i] = result_screenshot;
                }

                book = new Books(name, isbn, date, result_photos);
                book.setStatistics(new Statistics(book));
                book.setDescription(new BooksDescription(category, genre, quantity, description, author));
                book = booksRepo.save(book);
            } catch (Exception e) {
                AddAttributes(model);
                model.addAttribute("message", "Ошибка, некорректный данные!");
                return "book_add";
            }
        } else {
            AddAttributes(model);
            model.addAttribute("message", "Ошибка, некорректный данные!");
            return "book_add";
        }
        return "redirect:/books/" + book.getId();
    }

    @PostMapping("/edit/{id}")
    public String BookEditOld(Model model, @RequestParam String name, @RequestParam String isbn, @RequestParam MultipartFile[] photos, @RequestParam String date, @RequestParam int quantity, @RequestParam String description, @RequestParam Category category, @RequestParam Genre genre, @RequestParam String author, @PathVariable Long id) {
        Books book = booksRepo.getReferenceById(id);
        String[] result_photos;
        if (photos != null && !Objects.requireNonNull(photos[0].getOriginalFilename()).isEmpty()) {
            try {
                String result_photo;
                String uuidFile = UUID.randomUUID().toString();
                result_photos = new String[photos.length];
                for (int i = 0; i < result_photos.length; i++) {
                    result_photo = "books/" + uuidFile + "_" + photos[i].getOriginalFilename();
                    photos[i].transferTo(new File(uploadImg + "/" + result_photo));
                    result_photos[i] = result_photo;
                }
                book.setPhotos(result_photos);
            } catch (Exception e) {
                AddAttributes(model);
                model.addAttribute("message", "Ошибка, некорректный данные!");
                return "book_edit";
            }
        }

        book.setName(name);
        book.setIsbn(isbn);
        book.setDate(date);
        booksRepo.save(book);

        BooksDescription booksDescription = book.getDescription();
        booksDescription.setDescription(description);
        booksDescription.setCategory(category);
        booksDescription.setGenre(genre);
        booksDescription.setQuantity(quantity);
        booksDescription.setAuthor(author);
        booksDescriptionRepo.save(booksDescription);

        return "redirect:/books/{id}";
    }
}
