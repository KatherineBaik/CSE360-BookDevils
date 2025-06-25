# CSE360-BookDevils

Reminder to self:

VM Options:
--module-path <replace with path to your javafx-sdk\lib folder> --add-modules=javafx.controls,javafx.fxml

### TODO

- Write documentation in the wiki pages
- Make LoginPage code work with the User class

# Book Devils – CSE 360 Team 4

A Java / JavaFX prototype for a campus book–buy & sell system. Login supports **Buyer, Seller, Admin** roles and all data is saved to plain‑text files so it runs out‑of‑the‑box—no database required.

---

## ✨ What’s in This Release

| Area               | Changes                                                                                           |
| ------------------ | ------------------------------------------------------------------------------------------------- |
| **Data layer**     | • `` simplified & now converts to / from CSV• `` added – loads/saves `data/users.txt`             |
| **Authentication** | • `` rewritten to use `UserStore`; provides `authenticate(...)` & `register(...)`                 |
| **UI**             | • `` keeps original two‑column layout, calls the new auth API, and shows success / error messages |

---

## Project Layout

```
BookDevils/
├─ bin/                 ← compiled classes (generated)
├─ lib/                 ← external jars (JavaFX etc.)
├─ src/                 ← Java sources
│  ├─ Data/             ← domain classes + *Store helpers*
│  ├─ LoginPage/        ← UI & auth façade
│  ├─ AdminView/ …      ← (stubs)
│  └─ SellerView/ …
├─ data/                ← **plain‑text storage** created at runtime
│  └─ users.txt
└─ resources/           ← images, fxml, etc. (logo.png lives here)
```

> **NOTE**  VS Code settings mark `src/` *and* `resources/` as `java.project.resourcePaths`, so everything non‑Java is copied onto the runtime class‑path.

---

## Prerequisites

| Tool       | Version                     |
| ---------- | --------------------------- |
| JDK        | 17 or newer                 |
| JavaFX SDK | 19.x                        |
| VS Code    | + “Extension Pack for Java” |

Make sure the `` in `.vscode/launch.json` points to your local JavaFX *lib* folder.

---

## Running in VS Code

1. **Clone** / unzip the project.
2. Open the *BookDevils* folder in VS Code.
3. The Java extension will detect the *Run ▸* button next to `LoginPage`.
4. Press *F5* (or the green ▶ Run LoginPage) to launch the application.

Default demo accounts:

| ASU ID     | Password    | Role   |
| ---------- | ----------- | ------ |
| 1111111111 | password123 | BUYER  |
| 2222222222 | admin123    | ADMIN  |
| 3333333333 | seller123   | SELLER |
| 4444444444 | buyer123    | BUYER  |

All accounts are seeded to `data/users.txt` the first time you run the app.

---

## Feature Guide

### 1 · Logging In

- Enter ASU ID, password, choose a role, hit **Sign In**.
- Success ⇒ a green banner; failure ⇒ red banner.
- (Switching to Buyer/Seller/Admin dashboards will be wired in future sprints.)

### 2 · Creating an Account

- Click **Sign up**.
- Fill ASU ID, password, pick role.
- The UI calls `AuthenticationService.register(…)`.
- On success a new line is appended to `data/users.txt`.

### 3 · Data Persistence

Plain‑text CSV files live under ``:

| File        | Managed By       | Columns                       |
| ----------- | ---------------- | ----------------------------- |
| `users.txt` | `Data.UserStore` | asuId,password,role,suspended |
| `books.txt` | *(TBD)*          | title,author,year,condition   |
| `sales.txt` | *(TBD)*          | title,amount,category         |
| `buys.txt`  | *(TBD)*          | title,amount,category         |

Each *Store* class offers `load()`, `save()`, and convenience helpers so higher layers never touch the file system directly.

---

## Extending the Project

| Task                      | Where to Start                                             |
| ------------------------- | ---------------------------------------------------------- |
| **Book inventory**        | create `Data/Book.java` + `Data/BookStore.java`            |
| **Sales / purchase logs** | mirror the pattern in `UserStore`                          |
| **Role dashboards**       | new scenes under `BuyerView/`, `SellerView/`, `AdminView/` |
| **Unit tests**            | add JUnit 5 tests in `src/test/`                           |

---

## License

This project is created for CSE 360 Spring 2025, Team 4 “Book Devils”. It is released for educational purposes only.

