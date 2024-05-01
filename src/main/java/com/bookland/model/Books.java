package com.bookland.model;

import com.bookland.model.enums.BookStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Books {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String isbn;
    private String date;
    private String dateFree = "";
    private String[] photos;

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.ACTIVE;

    @OneToOne(cascade = CascadeType.ALL)
    private BooksDescription description;
    @OneToOne(cascade = CascadeType.ALL)
    private Statistics statistics;

    public Books(String name, String isbn, String date, String[] photos) {
        this.name = name;
        this.isbn = isbn;
        this.date = date;
        this.photos = photos;
    }

    public String getPhotosFirst() {
        return photos[0];
    }

    public String[] getPhotosOther() {
        String[] res = new String[photos.length - 1];
        System.arraycopy(photos, 1, res, 0, photos.length - 1);
        return res;
    }


    public void update(Books update) {
        this.name = update.getName();
        this.isbn = update.getIsbn();
        this.date = update.getDate();
        this.photos = update.getPhotos();
    }
}
