
INSERT INTO movies (title, description, director, release_date, duration, genre, rating) VALUES
('The Great Adventure', 'An epic journey of discovery and adventure.', 'John Doe', '2022-07-15', 120, 'Adventure', 8.2),
('Mystery Manor', 'A suspenseful mystery set in an eerie mansion.', 'Jane Smith', '2021-10-31', 105, 'Mystery', 7.9),
('Romantic Escape', 'A heartwarming romantic tale of love and destiny.', 'Alice Johnson', '2023-02-14', 95, 'Romance', 8.0);


INSERT INTO showtime (movie_id, show_date, show_time, screen, price) VALUES
(1, '2023-08-01', '18:30:00', 'Screen 1', 12.50),
(1, '2023-08-01', '21:00:00', 'Screen 1', 12.50),
(2, '2023-08-02', '17:00:00', 'Screen 2', 10.00),
(3, '2023-08-03', '20:00:00', 'Screen 3', 15.00);


INSERT INTO booking (showtime_id, customer_name, seats, total_price) VALUES
(1, 'Michael Brown', 2, 25.00),
(2, 'Laura Green', 1, 12.50),
(3, 'Samuel Lee', 4, 40.00),
(4, 'Emily White', 2, 30.00);
