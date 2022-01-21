package com.example.zadanie9.Book;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Book {

    @SerializedName("title")
    private String title;
    @SerializedName("author_name")

    private List<String> authors;

    @SerializedName("cover_i")
    private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

}
