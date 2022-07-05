package hr.altima.jsonparsing.pojo;

import java.util.List;

public class AuthorPojo {
    private String authorName;
    private List<BookPojo> books;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<BookPojo> getBooks() {
        return books;
    }

    public void setBooks(List<BookPojo> books) {
        this.books = books;
    }
}
