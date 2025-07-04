package AdminView;

import Data.Book;
import Data.Order;
import Data.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisTool {

    //---------------
    //User analysis
    //---------------

    /** Returns the total number of all currently registered users in UserManager. */
    public static int getTotalUsers(){
        return UserManager.getNumUsers();
    }

    /** Returns the total number of users matching role in UserManager. */
    public static int getTotalUsers(User.Role role){
        Collection<User>list = UserManager.getUserList().values();
        int total = 0;

        for(User user: list){
            if(user.getRole() == role){
                total++;
            }
        }

        return total;
    }

    /** Returns the number of suspended users in UserManager. */
    public static int getTotalSuspendedUsers(){
        Collection<User>list = UserManager.getUserList().values();
        int total = 0;

        for(User user: list){
            if(user.isSuspended()){
                total++;
            }
        }

        return total;
    }
    //---------------
    //Sales analysis
    //---------------

    /** Returns the number of orders in TransactionLog. */
    public static int getTotalOrders(){
        return TransactionLog.size();
    }

    /** Calculates the total revenue gained from all sales orders in TransactionLog. */
    public static double getTotalRevenue(){
        double total = 0.0;

        for(Order order : TransactionLog.getOrderList()){
            total += order.getTotalPrice();
        }

        return total;
    }

    /** Calculates the average revenue gained from a sales order in TransactionLog. */
    public static double getAverageRevenue(){
        return getTotalRevenue() / getTotalOrders();
    }

    /** Finds the best-selling book. */
    public static Book getBestSellingBook(){
        Book bestSelling = null;
        int bestSellingTotal  = 0;

        //holds the total amounts of each book
        Map<Book, Integer> totals = new HashMap<>();

        //go through all orders in TransactionLog
        List<Order> orderList = TransactionLog.getOrderList();
        List<Book> bookList = null;

        for(Order o : orderList){
            //loop through the booklist of each order
            bookList = o.getBooks();
            for(Book b : bookList){
                //if a book does not exist in the map, add it and set the value to 1
                if(!totals.containsKey(b)){
                    totals.put(b, 1);

                    if(bestSelling == null){
                        bestSelling = b;
                        bestSellingTotal = 1;
                    }
                }
                //otherwise, increment the value corresponding to the book
                else{
                    int val = totals.get(b) + 1;

                    //check if the value is larger than the current bestSellingTotal
                    if(val > bestSellingTotal){
                        bestSelling = b;
                        bestSellingTotal = val;
                    }

                    totals.replace(b, val);
                }
            }
        }

        return bestSelling;
    }

    /** Calculate the best-selling book category */
    public static Book.Category getHighestGrossingCategory(){
        Book.Category bestSelling = null;

        //holds the total amounts of each book category
        Map<Book.Category, Double> totals = new HashMap<>();
        for(Book.Category category : Book.Category.values()){
            totals.put(category, 0.0);
        }

        //go through all orders in TransactionLog
        List<Order> orderList = TransactionLog.getOrderList();
        List<Book> bookList = null;

        for(Order o : orderList){
            //loop through the booklist of each order
            bookList = o.getBooks();
            for(Book b : bookList){
                double value = totals.get(b.getCategory()) + b.getSellingPrice();

                if(bestSelling == null || value > totals.get(bestSelling)){
                    bestSelling = b.getCategory();
                }

                totals.replace(b.getCategory(), value);
            }
        }

        return bestSelling;
    }

    //---------------
    //Book analysis
    //---------------

    /** Get the total number of books in BookListingManager */
    public static int getTotalBooks(){
        //TODO
        return 0;
    }

    /** Get the number of books that are sold/available in BookListingManager */
    public static int getTotalBooks(boolean sold){
        //TODO
        return 0;
    }

    /** Calculates the average price of a book in BookListingManager. */
    public static double getAvgBookPrice(){
        //TODO

        //Get all books
        //Total up prices
        //Divide by total books

        return 0.0;
    }

}
