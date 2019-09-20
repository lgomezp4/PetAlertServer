DELIMITER //

CREATE PROCEDURE calcDistance (latitude LONG, longitude LONG) 
BEGIN
    SELECT a.id, 
    (acos(sin(radians(c.latitude)) * sin(radians(latitude)) + 
    cos(radians(c.latitude)) * cos(radians(latitude)) * 
    cos(radians(c.longitude) - radians(longitude))) * 6371)
    AS distance
    FROM alerts a, coordinates c WHERE a.coord_id=c.id 
    ORDER by distance;
END;
//


