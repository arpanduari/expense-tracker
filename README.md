# Expense Tracker

---

Make sure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) (for local MySQL setup)
- Git

---

## Running MySQL with Docker

Spin up a local MySQL instance using:

```bash
docker run --name expense_tracker \
  -e MYSQL_ROOT_PASSWORD=your_root_password \
  -e MYSQL_DATABASE=expense_tracker_db \
  -e MYSQL_USER=your_username \
  -e MYSQL_PASSWORD=your_password \
  -p 3307:3306 \
  -d mysql:latest
```
