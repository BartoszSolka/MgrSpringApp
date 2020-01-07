
#Uruchomienie bazy postgres

```
docker run -p 5432:5432 -d -e POSTGRES_PASSWORD=footwitter -e POSTGRES_USER=footwitter -v pgdata:/var/lib/postgresql/data postgres
```