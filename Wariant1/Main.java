package Wariant1;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static BookManager bookManager;

    public static void main(String[] args) {
        List<Book> initialBooks = new ArrayList<>();
        initialBooks.add(new Book("Pan Tadeusz", "Adam Mickiewicz", "ISBN1", 1834));
        initialBooks.add(new Book("Lalka", "Bolesław Prus", "ISBN2", 1890));
        initialBooks.add(new Book("Ferdydurke", "Witold Gombrowicz", "ISBN3", 1937));
        initialBooks.add(new Book("Dziady cz. III", "Adam Mickiewicz", "ISBN4", 1832));
        initialBooks.add(new Book("Krzyzacy", "Henryk Sienkiewicz", "ISBN5", 1900));

        bookManager = new BookManager(initialBooks);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("[1] Dodaj książkę");
            System.out.println("[2] Usuń książkę");
            System.out.println("[3] Zaktualizuj książkę");
            System.out.println("[4] Wyświetl książki");
            System.out.println("[5] Wyjście");

            int choice = safeNextInt(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook(scanner);
                    break;
                case 2:
                    removeBook(scanner);
                    break;
                case 3:
                    updateBook(scanner);
                    break;
                case 4:
                    listBooks();
                    break;
                case 5:
                    System.out.println("Pomyślnie zamknięto program.");
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór.");
            }
        }
    }

    private static void addBook(Scanner scanner) {
        System.out.println("Podaj tytuł:");
        String title = scanner.nextLine();
        System.out.println("Podaj autora:");
        String author = scanner.nextLine();
        System.out.println("Podaj ISBN:");
        String isbn = scanner.nextLine();
        System.out.println("Podaj rok wydania:");
        int year = safeNextInt(scanner);
        scanner.nextLine();

        Book book = new Book(title, author, isbn, year);
        bookManager.addBook(book);
        System.out.println("Dodano książkę.");
    }

    private static void removeBook(Scanner scanner) {
        System.out.println("Podaj ISBN książki do usunięcia:");
        String isbn = scanner.nextLine();

        Book bookToRemove = null;
        for (Book book : bookManager.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                bookToRemove = book;
                break;
            }
        }

        if (bookToRemove != null) {
            bookManager.removeBook(bookToRemove);
            System.out.println("Usunięto książkę.");
        } else {
            System.out.println("Nie znaleziono książki o podanym ISBN.");
        }
    }

    private static void updateBook(Scanner scanner) {
        System.out.println("Podaj ISBN książki do zaktualizowania:");
        String isbn = scanner.nextLine();

        Book bookToUpdate = null;
        for (Book book : bookManager.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                bookToUpdate = book;
                break;
            }
        }

        if (bookToUpdate != null) {
            System.out.println("Podaj nowy tytuł:");
            String newTitle = scanner.nextLine();
            System.out.println("Podaj nowego autora:");
            String newAuthor = scanner.nextLine();
            System.out.println("Podaj nowy ISBN:");
            String newIsbn = scanner.nextLine();
            System.out.println("Podaj nowy rok wydania:");
            int newYear = safeNextInt(scanner);
            scanner.nextLine();

            Book newBook = new Book(newTitle, newAuthor, newIsbn, newYear);
            bookManager.updateBook(bookToUpdate, newBook);
            System.out.println("Zaktualizowano książkę.");
        } else {
            System.out.println("Nie znaleziono książki o podanym ISBN.");
        }
    }

    private static void listBooks() {
        List<Book> books = bookManager.getBooks();
        if (books.isEmpty()) {
            System.out.println("Brak książek w bibliotece.");
        } else {
            for (Book book : books) {
                System.out.println(book.toString());
            }
        }
    }

    private static int safeNextInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Podaj poprawną liczbę całkowitą:");
                scanner.next();
            }
        }
    }
}
