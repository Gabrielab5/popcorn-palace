-- Insert sample movies
INSERT INTO movie (title, genre, duration, rating, release_year) VALUES 
  ('Inception', 'Sci-Fi', 148, 8.8, 2010),
  ('Interstellar', 'Sci-Fi', 169, 8.6, 2014),
  ('The Dark Knight', 'Action', 152, 9.0, 2008);

-- Insert sample showtimes
INSERT INTO showtime (movie_id, theater, start_time, end_time, price) VALUES
  (1, 'Theater 1', '2025-04-01 18:00:00', '2025-04-01 20:30:00', 10.0),
  (2, 'Theater 2', '2025-04-02 20:00:00', '2025-04-02 22:45:00', 12.0),
  (3, 'Theater 3', '2025-04-03 19:00:00', '2025-04-03 21:45:00', 11.0);

-- Insert sample bookings
INSERT INTO booking (showtime_id, seat_number, customer_name) VALUES
  (1, 10, 'John Doe'),
  (2, 15, 'Jane Smith'),
  (3, 8, 'Alice Johnson');
