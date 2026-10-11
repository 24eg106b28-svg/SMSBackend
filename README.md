# Student Management System – Backend (Spring Boot)

Java 17+ · Spring Boot 3.2 · Spring Security + JWT · JPA/Hibernate · MySQL

## Run
1. Install JDK 17 or newer, Maven 3.8+, and MySQL 8.
2. Open `src/main/resources/application.properties` and set your MySQL `username` and `password`.
   The database `student_db` is created automatically and tables are created by Hibernate.
3. From this folder run:
   ```
   mvn spring-boot:run
   ```
4. The API starts on http://localhost:8080

On first start the app creates:
- Admin: `admin` / `admin123`
- Demo student: `CS2024001` / `student123` (with sample attendance and marks)

## API
| Method | URL | Who |
|---|---|---|
| POST | /api/auth/login | everyone |
| GET | /api/admin/stats | admin |
| GET | /api/admin/students?keyword= | admin |
| GET / PUT / DELETE | /api/admin/students/{id} | admin |
| POST | /api/admin/students | admin |
| GET / POST | /api/admin/students/{id}/attendance | admin |
| DELETE | /api/admin/attendance/{id} | admin |
| GET / POST | /api/admin/students/{id}/marks | admin |
| DELETE | /api/admin/marks/{id} | admin |
| GET | /api/student/me, /api/student/attendance, /api/student/marks | student (own data only) |

All endpoints except login need the header `Authorization: Bearer <token>`.
