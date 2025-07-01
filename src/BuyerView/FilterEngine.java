package BuyerView;                        // ← move out of demo1.*
import Data.Book;
import java.util.List;

public class FilterEngine {

    public List<Book> filterByCategory(List<Book> in, Book.Category c) {
        return in.stream().filter(b -> b.getCategory() == c).toList();
    }

    public List<Book> filterByCondition(List<Book> in, Book.Condition cond) {
        return in.stream().filter(b -> b.getCondition() == cond).toList();
    }

    public List<Book> filterByPrice(List<Book> in, double max) {
        return in.stream().filter(b -> b.getSellingPrice() <= max).toList();
    }

    public List<Book> search(List<Book> in, String keyword) {
        String k = keyword.toLowerCase();
        return in.stream().filter(b ->
            b.getTitle().toLowerCase().contains(k) ||
            b.getAuthor().toLowerCase().contains(k)
        ).toList();
    }
}
