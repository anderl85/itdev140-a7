# itdev140-a7

uses [sqlite jdbc](https://github.com/xerial/sqlite-jdbc/releases)
can be added to CLASSPATH or depending on ide settings, lib folder

[Totally Very Professional video](https://drive.google.com/file/d/1IpAM6MmKtyGjnqIhl_vae8SfpthIm4Q4/view?usp=sharing)

---

## table structure

```
CREATE TABLE employee (
    id        TEXT PRIMARY KEY,
    name      TEXT NOT NULL,
    hire_date TEXT
)
WITHOUT ROWID;
```
