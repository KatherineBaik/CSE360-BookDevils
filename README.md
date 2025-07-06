# CSE360-BookDevils

Reminder to self:

VM Options:
--module-path <replace with path to your javafx-sdk\lib folder> --add-modules=javafx.controls,javafx.fxml

# Book Devils – CSE 360 Team 4

A Java / JavaFX prototype for a campus book–buy & sell system. Login supports **Buyer, Seller, Admin** roles and all data is saved to plain‑text files so it runs out‑of‑the‑box—no database required.

---

## Features

| Area | Feature | Description |
| --- | --- | --- |
| **Core** | **Role-Based Access** | Separate dashboards and functionality for `BUYER`, `SELLER`, and `ADMIN` roles. |
| | **Authentication** | Secure user registration and login system. |
| | **Data Persistence** | All application data (users, books, orders) is saved to local CSV-like `.txt` files in the `data/` directory. |
| **Buyer View** | **Browse & Filter** | View all available book listings. Filter books by category, condition, and price. Search by title or author. |
| | **Shopping Cart** | Add books to a shopping cart and proceed to checkout. |
| | **Wishlist** | Add books to a personal wishlist for later. |
| **Seller View** | **Listing Management** | Sellers can create new book listings, view their active listings, and edit or delete them. |
| **Admin View** | **Dashboard** | An overview dashboard displaying key statistics like total orders, books sold, and total products listed. |
| | **User Management** | View all registered users. Admins can suspend or delete user accounts. |
| | **Transaction Log** | View a complete log of all purchase transactions made in the system. |
| | **Analysis** | An analytics tab with insights on user demographics, book sales, total revenue, top-selling books, and highest-grossing sellers/categories. |

---

## Project Layout

```
BookDevils/
├───bin/                # Compiled .class files (auto-generated)
├───data/               # Plain-text data storage (created at runtime)
│   ├───books.txt
│   ├───orders.txt
│   ├───purchases.txt
│   ├───sales.txt
│   └───users.txt
├───lib/                # External libraries (e.g., JavaFX SDK)
└───src/                # Java source code
    ├───AdminView/      # Admin dashboard UI and logic
    ├───BuyerView/      # Buyer dashboard UI and logic
    ├───Data/           # Domain classes (Book, User, Order) + Store helpers
    ├───LoginPage/      # Login/Signup UI & Authentication
    └───SellerView/     # Seller dashboard UI and logic
```

---

## How to Run

1.  **Prerequisites:**
    *   Java JDK
    *   JavaFX SDK

2.  **Configure VM Options:**
    To run the application from an IDE like Eclipse, you need to configure the VM options to point to your JavaFX SDK library path. 
    Example VM arguments:
    ```
    --module-path "C:/path/to/your/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
    ```

3.  **Launch:**
    The main entry point for the application is `LoginPage.java`. Run this file to start the application, which will open the login window.
