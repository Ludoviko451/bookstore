package laqf.bookstore.presentation;

import laqf.bookstore.model.Book;
import laqf.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class BookForm extends JFrame {

    BookService bookService;
    private JPanel panel;
    private JTable booksTable;
    private JTextField bookText;

    private JTextField idText;
    private JTextField authorText;
    private JTextField priceText;
    private JTextField stockText;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    private DefaultTableModel tablebooksModel;

    @Autowired
    public BookForm(BookService bookService){
        this.bookService = bookService;
        startForm();
        addButton.addActionListener(e -> addBook());
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadSelectedBook();
            }
        });
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
    }



    private void addBook() {
        if (bookText.getText().isEmpty()){
            showMessage("Enter the name of the book");
            bookText.requestFocusInWindow();
            return;
        }
        var bookName = bookText.getText();
        var author = authorText.getText();
        var price = Double.parseDouble(priceText.getText());
        var stock = Integer.parseInt(stockText.getText());

        var book = new Book();
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setPrice(price);
        book.setStock(stock);

        this.bookService.saveBook(book);
        showMessage("The book was added");

        cleanForm();
        showBooks();
    }

    private void loadSelectedBook(){

        var row = booksTable.getSelectedRow();

        if (row != -1){
            String bookId = booksTable.getModel().getValueAt(row, 0).toString();
            idText.setText(bookId);

            String bookName = booksTable.getModel().getValueAt(row, 1).toString();
            bookText.setText(bookName);

            String bookAuthor = booksTable.getModel().getValueAt(row, 2).toString();
            authorText.setText(bookAuthor);

            String bookPrice = booksTable.getModel().getValueAt(row, 3).toString();
            priceText.setText(bookPrice);

            String bookStock = booksTable.getModel().getValueAt(row, 4).toString();
            stockText.setText(bookStock);
        }
    }

    private void updateBook(){
        if(this.idText.getText().isEmpty()){
            showMessage("Please select a book");
        }
        else{
            if(bookText.getText().isEmpty()){
                showMessage("Please, enter a name for the book");
                bookText.requestFocusInWindow();
                return;
            }
            int idBook = Integer.parseInt(idText.getText());
            var bookName = bookText.getText();
            var author = authorText.getText();
            var price = Double.parseDouble(priceText.getText());
            var stock = Integer.parseInt(stockText.getText());

            var book = new Book();

            book.setBookId(idBook);
            book.setBookName(bookName);
            book.setAuthor(author);
            book.setPrice(price);
            book.setStock(stock);

            bookService.saveBook(book);
            showMessage("The book has been updated");
            showBooks();
            cleanForm();
        }

    }

    private void deleteBook(){
        var row = booksTable.getSelectedRow();

        if (row != -1) {
            int idBook = Integer.parseInt(idText.getText());

            var bookName = bookText.getText();
            var book = new Book();
            book.setBookId(idBook);

            int respuesta = JOptionPane.showConfirmDialog(null, "You want delete this book?" + " " + bookName, "Confirmation", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                bookService.deleteBook(book);
                showMessage("The book has been deleted");
                showBooks();
                cleanForm();
            } else {
                cleanForm();
            }
        }
        else {
            showMessage("Please select a book");
        }

    }
    private void cleanForm(){
        bookText.setText("");
        authorText.setText("");
        priceText.setText("");
        stockText.setText("");
    }

    private void showMessage(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private void startForm(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int x = (screenSize.width - getWidth())/ 2;
        int y = (screenSize.height - getWidth()) / 2;

        setLocation(x, y);
    }

    private void createUIComponents() {

        idText = new JTextField("");

        idText.setVisible(false);

        // TODO: place custom component creation code here
        this.tablebooksModel = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };

        String[] headers = {"Id", "Book", "Author", "Price", "Stock"};

        tablebooksModel.setColumnIdentifiers(headers);

        this.booksTable = new JTable(tablebooksModel);

        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        showBooks();
    }

    private void showBooks(){

        tablebooksModel.setRowCount(0);

        var books = bookService.showBooks();

        books.forEach((book) -> {
            Object[] bookLine = {
                    book.getBookId(),
                    book.getBookName(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getStock()
            };
            this.tablebooksModel.addRow((bookLine));
        });
    }
}
