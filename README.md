# PetAlertServer

## Install and running

### Database
You will need a sql server runing. You have in `resources/PetAlert.sql` a file to init the database table structure.

### Server
Compile and deploy.

### Service method
Examples:
GET
User by id: http://localhost:8080/PetAlertServer/services/users/(id)

User by userName: http://localhost:8080/PetAlertServer/services/users/username/(userUsame)

Search alert by distance: http://localhost:8080//PetAlert/services/alerts/distance/{latitude}/{longitude}/{number}
                          http://localhost:8080//PetAlert/services/alerts/distance/41.3631301/2.1147764/5



