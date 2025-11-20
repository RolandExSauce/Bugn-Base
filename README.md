## Setup Options

——————————————————————————————————————————————————————————————————————————————————————

After cloning the repository, user needs the following locally installed:

- JDK (at least 21)
- Maven
- PostgreSQL

The PostgreSQL driver should be up and running on port 5432. User needs to create a new databank and name it to 'bugnbass' with owner 'bugnbass'. The application will populate it later.

**Example:**

```sql
CREATE DATABASE bugnbass OWNER bugnbass;
```

### 1) To build this project and run the jar

Executing following at `/backend`:

```bash
mvn clean package
```

will create backend-0.0.1-SNAPSHOT.jar at `/backend/target`, which can be run by executing:

```bash
java -jar <jar-path>
```

Upon successful start, the databank will be populated. The app can be viewed on http://localhost:8080

### 2) To run it directly from the source code

User also needs following to start the frontend in development mode:

- Node.js
- npm

1. Executing following at `/backend`:

```bash
mvn spring-boot:run
```

starts the server on port 8080

2. Executing following at `/frontend`:

```bash
npm install
npm run dev
```

will install frontend dependencies and start it on http://localhost:5173/
