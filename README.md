# CineVision – Multi-Cinema Booking & Management System

## Overview
CineVision is a JavaFX-based desktop application that allows users to browse movies across multiple cinemas, select seats, and make bookings. It also includes an admin module for managing movies and viewing system statistics.

The system follows the **MVC architecture** and uses the **DAO pattern** for database operations with SQLite.

---

## Features

### User Features
- User Registration & Login (with password hashing)
- Cinema selection
- View movies by cinema
- Seat selection with real-time availability check
- Booking confirmation
- View booking history
- Cancel booking

### Admin Features
- Admin login
- Add new movies
- Delete movies
- View system stats (total bookings, revenue)

---

## Tech Stack
- **Language:** Java  
- **UI:** JavaFX  
- **Database:** SQLite  
- **Build Tool:** Maven  
- **Architecture:** MVC + DAO Pattern  
- **IDE:** IntelliJ IDEA  

---

## Project Structure

```
src/
└── main/
├── java/org/example/
│ ├── controllers/
│ ├── database/
│ ├── booking/
│ ├── models/
│ ├── session/
│ └── Main.java
└── resources/
├── views/ (FXML files)
├── images/
├── media/
└── cinema-style.css
```

---

## Setup & Run Instructions

### 1. Clone the repository
```bash
git clone https://github.com/your-username/cinevision.git
cd cinevision
```

### 2. Open in IntelliJ IDEA
- Open IntelliJ
- Click **Open Project**
- Select the cloned folder

### 3. Configure Maven
- Wait for Maven dependencies to load automatically  
- If not:
  - Right-click project → **Maven → Reload Project**

### 4. Setup JavaFX
- Make sure JavaFX SDK is configured in IntelliJ  
- Add VM options (if required):

```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

### 5. Run the Application
- Run `Main.java`

---

## Database Setup (SQLite)

- The project uses **SQLite**
- Database file will be:
  - Automatically created OR
  - Located in project directory

### Tables:
- Users  
- Cinemas  
- Movies  
- Bookings  

---

## System Flow

1. User registers/logs in  
2. Selects cinema  
3. Views available movies  
4. Selects seat  
5. Confirms booking  
6. Views/cancels bookings  

### Admin:
- Logs in  
- Adds/deletes movies  
- Views stats  

---

## Testing
- Black-box testing (EP, BVA, error guessing)  
- White-box testing (coverage techniques)  
- Bug tracking and fixes included  

---

## Future Improvements
- Advanced reporting dashboard  
- Online payment integration  
- Movie search & filtering  
- UI enhancements  

---

## Contributors
- Aliza Yasser  
- Navaira Azmat
- Madiha Shahzad 

---
