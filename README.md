## The ĐAMN Project

```
Đata (Data, with Style(tm))
Access
Management 
NProject
```

A not-serious take on copying Hibernate, JPA and other state-of-the-art ORM libraries in Java.

Warning: not for serious people.



## Featuring

*   Initial support for SQLite File, SQLite In-Memory (untested) and PostgreSQL database (also untested)



## Getting started

-   Place a copy of `data.db` somewhere convenient (i.e. writable), then change the path to that file in `main`.

    -   `data.db` actually contains just an empty table for the Todo demo in `main`.

        ```
        CREATE TABLE todo (
            id INTEGER PRIMARY KEY,
            title TEXT,
            content TEXT,
            done BOOLEAN
        )
        ```

-   Run.

