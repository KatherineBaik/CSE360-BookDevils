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

## What’s in This Release

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



