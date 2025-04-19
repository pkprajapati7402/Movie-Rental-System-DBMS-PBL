# 🎥 Movie Rental System 📚

Welcome to the **Movie Rental System**, a mini-project developed using **Java Swing** for the frontend and **MySQL** for the backend. This project demonstrates the integration of a **DBMS** with a Java application, providing functionalities for both **Admin** and **User** roles.


## ✨ Features

### 👩‍💼 Admin Panel
- ➕ **Add Movies**: Add new movies to the database.
- ✏️ **Update Movie Genre**: Modify the genre of an existing movie.
- ❌ **Delete Movies**: Remove movies from the database.
- 👥 **View Users Who Rented**: See a list of users who have rented movies.
- 🎞️ **View All Movies**: Display all movies in the database.
- 🔒 **Logout**: Return to the main menu.

### 👤 User Panel
- 🆕 **Register**: Create a new user account.
- 🔑 **Login**: Log in with your user ID.
- 🎥 **Rent Movies**: Rent a movie by entering its ID.
- 🔄 **Return Movies**: Return a rented movie by entering its ID.
- 📋 **My Rented Movies**: View a list of movies you have rented.
- 🎞️ **Show Available Movies**: Display all available movies for rent.
- 🔒 **Logout**: Return to the main menu.


## 🛠️ Technologies Used
- **Java Swing**: For the GUI
- **MySQL**: For the backend database
- **JDBC**: For database connectivity


## 📂 Database Schema

```sql
CREATE DATABASE movierental;
USE movierental;

CREATE TABLE Customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Insert initial values
INSERT INTO Customers (name, email) VALUES
('John Doe', 'john@example.com'),
('Jane Smith', 'jane@example.com');

CREATE TABLE Movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    available BOOLEAN DEFAULT TRUE
);

-- Insert initial values
INSERT INTO Movies (title, genre, available) VALUES
('Inception', 'Sci-Fi', TRUE),
('The Godfather', 'Crime', TRUE),
('The Dark Knight', 'Action', TRUE),
('Pulp Fiction', 'Drama', TRUE),
('The Matrix', 'Sci-Fi', TRUE);

CREATE TABLE Rentals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    rented_on DATE NOT NULL,
    due_date DATE NOT NULL,
    returned_on DATE,
    FOREIGN KEY (user_id) REFERENCES Customers(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES Movies(id) ON DELETE CASCADE
);

-- Insert initial values
INSERT INTO Rentals (user_id, movie_id, rented_on, due_date) VALUES
(1, 1, '2025-04-01', '2025-04-08'),
(2, 2, '2025-04-02', '2025-04-09');
```


## 🚀 How to Run the Project

### Prerequisites
- Install **Java JDK** (version 8 or higher)
- Install **MySQL**
- Add the **MySQL JDBC Driver** to your project

### Steps

```bash
git clone https://github.com/your-username/movie-rental-system.git
cd movie-rental-system
```

1. Import the project into your IDE (e.g., IntelliJ IDEA, Eclipse, VS Code).
2. Set up the database:
   - Create a database named `MovieRental`
   - Run the SQL scripts from the **Database Schema** section
3. Update the database connection in your code:
```java
conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MovieRental", "root", "your-password");
```
4. Run the `MovieRentalSystem` class.


## 🎮 How to Use

### Admin Login
- Select **Admin Login** from the main menu.
- Use credentials:
  - **Username**: `admin`
  - **Password**: `admin123`
- Access the admin panel to manage movies and view rentals.

### User Login/Register
- Select **User Login/Register** from the main menu.
- Register or log in with your user ID.
- Access the user panel to rent, return, and view movies.


## 🌟 Future Enhancements
- 🔍 Add search functionality by movie title or genre
- 🔐 Implement password-based authentication for users
- 📧 Add email notifications for due dates
- 🎨 Enhance UI using JavaFX


## 🤝 Contributing

Contributions are welcome!  
Feel free to **fork** the repository and submit a **pull request**.


## 📄 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.
