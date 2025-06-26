package AdminView;

import Data.Order;
import Data.UserStore;

import java.io.IOException;
import java.util.List;

public class TransactionLog {
    private static List<Order> orderList;

    /** Loads all transaction data stored in file.
     * NOTE: Must be used before using this class. */
    public static void loadData(){
        //TODO
    }

    /** Saves all transaction data to file.
     * NOTE: Should be used at before application closes, or when updating data in the list. */
    public static void saveData() throws IOException {
        //TODO
    }

    //--------------------------

    /** Returns the underlying list used by this class */
    public static List<Order> getOrderList(){
        return orderList;
    }

    /** Returns the total number of orders. */
    public static int size(){
        return orderList.size();
    }

    //TODO: Search and sort methods?
}
