import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static BookManager bookManager;

    @Override
    public void start(Stage primaryStage) {
        List<Book> initialBooks = new ArrayList<>();
        initialBooks.add(new Book("Pan Tadeusz", "Adam Mickiewicz", "ISBN1", 1834));
        initialBooks.add(new Book("Lalka", "Bolesław Prus", "ISBN2", 1890));
        initialBooks.add(new Book("Ferdydurke", "Witold Gombrowicz", "ISBN3", 1937));
        initialBooks.add(new Book("Dziady cz. III", "Adam Mickiewicz", "ISBN4", 1832));
        initialBooks.add(new Book("Krzyżacy", "Henryk Sienkiewicz", "ISBN5", 1900));

        bookManager = new BookManager(initialBooks);

        primaryStage.setTitle("Menadżer Książek");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10)); 

        Button addButton = new Button("Dodaj książkę");
        Button removeButton = new Button("Usuń książkę");
        Button updateButton = new Button("Zaktualizuj książkę");
        Button listButton = new Button("Wyświetl książki");
        Button exitButton = new Button("Wyjście");

        addButton.setOnAction(e -> addBook());
        removeButton.setOnAction(e -> removeBook());
        updateButton.setOnAction(e -> updateBook());
        listButton.setOnAction(e -> listBooks());
        exitButton.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(10, addButton, removeButton, updateButton, listButton, exitButton);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10));

        vbox.getChildren().addAll(buttonBox);

        Scene scene = new Scene(vbox, 600, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dodaj książkę");
        dialog.setHeaderText("Dodaj nową książkę");
        dialog.setContentText("Podaj dane (tytuł, autor, ISBN, rok):");

        dialog.showAndWait().ifPresent(details -> {
            String[] bookDetails = details.split(",");
            if (bookDetails.length == 4) {
                try {
                    String title = bookDetails[0].trim();
                    String author = bookDetails[1].trim();
                    String isbn = bookDetails[2].trim();
                    int year = Integer.parseInt(bookDetails[3].trim());
                    Book book = new Book(title, author, isbn, year);
                    bookManager.addBook(book);
                    showAlert(Alert.AlertType.INFORMATION, "Książka dodana", "Książka została pomyślnie dodana.");
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Błędne dane", "Rok musi być liczbą całkowitą.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Błędne dane", "Podaj dane w poprawnym formacie.");
            }
        });
    }

    private void removeBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Usuń książkę");
        dialog.setHeaderText("Usuń książkę");
        dialog.setContentText("Podaj ISBN książki do usunięcia:");

        dialog.showAndWait().ifPresent(isbn -> {
            Book bookToRemove = null;
            for (Book book : bookManager.getBooks()) {
                if (book.getIsbn().equals(isbn.trim())) {
                    bookToRemove = book;
                    break;
                }
            }

            if (bookToRemove != null) {
                bookManager.removeBook(bookToRemove);
                showAlert(Alert.AlertType.INFORMATION, "Książka usunięta", "Książka została pomyślnie usunięta.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Nie znaleziono książki", "Nie znaleziono książki o podanym ISBN.");
            }
        });
    }

    private void updateBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zaktualizuj książkę");
        dialog.setHeaderText("Zaktualizuj książkę");
        dialog.setContentText("Podaj ISBN książki do zaktualizowania:");

        dialog.showAndWait().ifPresent(isbn -> {
            Book[] bookToUpdate = new Book[1];
            for (Book book : bookManager.getBooks()) {
                if (book.getIsbn().equals(isbn.trim())) {
                    bookToUpdate[0] = book;
                    break;
                }
            }

            if (bookToUpdate[0] != null) {
                TextInputDialog updateDialog = new TextInputDialog();
                updateDialog.setTitle("Zaktualizuj książkę");
                updateDialog.setHeaderText("Zaktualizuj dane książki");
                updateDialog.setContentText("Podaj nowe dane (tytuł, autor, ISBN, rok):");

                updateDialog.showAndWait().ifPresent(details -> {
                    String[] bookDetails = details.split(",");
                    if (bookDetails.length == 4) {
                        try {
                            String newTitle = bookDetails[0].trim();
                            String newAuthor = bookDetails[1].trim();
                            String newIsbn = bookDetails[2].trim();
                            int newYear = Integer.parseInt(bookDetails[3].trim());
                            Book newBook = new Book(newTitle, newAuthor, newIsbn, newYear);
                            bookManager.updateBook(bookToUpdate[0], newBook);
                            showAlert(Alert.AlertType.INFORMATION, "Książka zaktualizowana", "Książka została pomyślnie zaktualizowana.");
                        } catch (NumberFormatException e) {
                            showAlert(Alert.AlertType.ERROR, "Błędne dane", "Rok musi być liczbą całkowitą.");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Błędne dane", "Podaj dane w poprawnym formacie.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Nie znaleziono książki", "Nie znaleziono książki o podanym ISBN.");
            }
        });
    }

    private void listBooks() {
        List<Book> books = bookManager.getBooks();
        if (books.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Biblioteka jest pusta", "Brak książek w bibliotece.");
        } else {
            StringBuilder content = new StringBuilder();
            for (Book book : books) {
                content.append(book).append("\n");
            }

            TextArea textArea = new TextArea(content.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(550); 

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lista książek");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
