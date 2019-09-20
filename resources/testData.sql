/**
 * Author:  Luis Gómez
 * Created: 05-may-2019
 * Project: Pet Alert
 */

insert into users (id, name, username, password, rol, mail)
values (1, 'Luis', 'userLuis', 'f561aaf6ef0bf14d4208bb46a4ccb3ad', 'user', 'holaqtal@gmail.com'),
       (2, 'Pepe', 'userPepe', 'f561aaf6ef0bf14d4208bb46a4ccb3ad', 'user', 'holaYadios@gmail.com'),
       (3, 'Admin', 'admin', 'admin', 'admin', 'admin@gmail.com');

insert into coordinates (id, latitude, longitude)
values (1, 41.3644985, 2.1138928),
       (2, 41.374329, 2.146365);

insert into animals (id, name, kind, hair_color, race, sex, age)
values (1, 'Perrete', 'dog', 'black', 'labrador', 'M', 6),
       (2, 'Misi', 'cat', 'white', 'siames', 'F', 2);

insert into descriptions (id, title, lost_day_hour, description, phone)
values (1, 'Perro perdido', '2019-05-05 10:30:00', 'Perro de raza Labrador perdido la mañana del domingo 5 de mayo, última vez visto en la calle Sant Pius X', '677892304'),
       (2, 'Gato perdido', '2019-05-06 17:15:00', 'Gata de raza siames de color blanca perdida en las próximidades de plaza españa', '677123951');

insert into alerts (id, creation_date, user_id, animal_id, desc_id, coord_id)
values (1, '2019-05-05 15:25:18', 1, 1, 1, 1),
       (2, '2019-05-06 19:48:42', 2, 2, 2, 2);

insert into messages (id, title, content, sender_active, receiver_active, send_date, receipt_date, sender_id, receiver_id)
values (1, 'Hola!', 'Yabadabadú', true, true, '2019-04-22 18:30:00', '2019-04-22 18:31:00', 1, 2),
       (2, '(Respuesta) Hola!', 'Ahora repitelo tú!', true, false, '2019-04-23 10:15:00', '2019-04-23 11:00:00', 2, 1);