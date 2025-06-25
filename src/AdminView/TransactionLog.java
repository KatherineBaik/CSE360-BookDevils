package AdminView;

import Data.Order;

import java.util.List;

public class TransactionLog {
    private static List<Order> orderList;

    /** Loads all transaction data stored in file.
     * NOTE: Must be used before using this class. */
    public static void loadData(){
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
