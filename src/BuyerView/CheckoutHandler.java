package BuyerView;

import Data.Book;
import Data.Order;
import Data.OrderStore;
import SellerView.BookListingManager;

import java.util.List;

public class CheckoutHandler {

    public Order process(Cart cart) {
        if (cart.getBooks().isEmpty()) return null;

        // create immutable order & mark items sold
        Order order = new Order(cart.getBuyer(), List.copyOf(cart.getBooks()));
    cart.getBooks().forEach(b -> {
        b.markAsSold();
        BookListingManager.markSold(b.getId());
    });
        cart.clear();
        OrderStore.add(order); 
        return order;
    }
}
