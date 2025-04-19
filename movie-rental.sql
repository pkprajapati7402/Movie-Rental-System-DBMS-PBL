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
