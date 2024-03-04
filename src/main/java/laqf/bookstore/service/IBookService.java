package laqf.bookstore.service;

import laqf.bookstore.model.Book;

import java.util.List;

public interface IBookService {

    public List<Book> showBooks();

    public Book findBookById(Integer idBook);

    public void saveBook(Book book);

    public void deleteBook(Book book);
}
