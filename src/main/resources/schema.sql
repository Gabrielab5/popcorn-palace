-- (movies)
CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,            
    description TEXT,                        
    director VARCHAR(255),                   
    release_date DATE,                    
    duration INT,                        
    genre VARCHAR(100),                    
    rating DECIMAL(3,1)                       
);

-- (showtime)
CREATE TABLE IF NOT EXISTS showtime (
    id SERIAL PRIMARY KEY,
    movie_id INT NOT NULL,                   
    show_date DATE NOT NULL,               
    show_time TIME NOT NULL,          
    screen VARCHAR(50),                       
    price DECIMAL(10,2),                  
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- (booking)
CREATE TABLE IF NOT EXISTS booking (
    id SERIAL PRIMARY KEY,
    showtime_id INT NOT NULL,               
    customer_name VARCHAR(255),              
    seats INT,                             
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    total_price DECIMAL(10,2),               
    FOREIGN KEY (showtime_id) REFERENCES showtime(id)
);
