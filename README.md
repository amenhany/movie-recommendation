# Movie Recommendation System

A simple Java-based movie recommendation system that reads movies and users from text files, validates them, and generates personalized recommendations based on genre matching.

---

## 📁 Project Structure

```
amenhany-movie-recommendation/
├── build.gradle.kts
├── gradlew / gradlew.bat
├── settings.gradle.kts
├── gradle/wrapper/
├── src/
│   ├── main/
│   │   ├── java/org/testing/
│   │   │   ├── controller/Main.java
│   │   │   ├── io/
│   │   │   │   ├── LineReader.java
│   │   │   │   ├── LineWriter.java
│   │   │   │   ├── MovieParser.java
│   │   │   │   ├── Parser.java
│   │   │   │   └── UserParser.java
│   │   │   ├── model/
│   │   │   │   ├── Movie.java
│   │   │   │   ├── Recommendation.java
│   │   │   │   └── User.java
│   │   │   └── service/RecommendationEngine.java
│   │   └── resources/
│   │       ├── movies.txt
│   │       └── users.txt
│   └── test/java/org/testing/io/
│       ├── LineReaderTest.java
│       ├── LineWriterTest.java
│       ├── MovieParserTest.java
│       └── UserParserTest.java
```

---

## ⚙️ How It Works

1. **Read input files** using `LineReader` (`movies.txt` and `users.txt`).
2. **Parse and validate** inputs using `MovieParser` and `UserParser`.
3. **Build recommendations** in `RecommendationEngine` by:

   * Detecting genres liked by each user.
   * Finding all movies in those genres.
   * Removing already-watched movies.
4. **Write output** to `recommendations.txt` using `LineWriter`.

If an exception occurs during processing, an error message is written to the output file.

---

## 📄 Input Format

### movies.txt

Each movie uses **two lines**:

```
Movie Title, MOVIEID
Genre1, Genre2, ...
```

Example:

```
Forrest Gump, FG123
Drama, Romance
```

### users.txt

Each user uses **two lines**:

```
User Name, UserID
MOVIEID1, MOVIEID2, ...
```

Example:

```
Karim, 12345678A
FG123, I234, TP321
```

---

## ✅ Validation Rules

### Movie Rules

* Title must start with capital letters.
* ID format: `<InitialLetters><3 digits>` (e.g., FG123)
* Digits must be unique.
* Genres must match the enum in `Movie.Genre`.

### User Rules

* Name must only contain letters & spaces.
* ID must be 9 characters: 8 digits + a letter OR 9 digits.
* IDs must be unique.
* Movie IDs must be uppercase and valid.

---

## ▶️ Running the Project

### Using Gradle

```bash
./gradlew run
```

Or on Windows:

```bash
gradlew.bat run
```

### Running Tests

```bash
./gradlew test
```

---

## 🧪 Testing

JUnit 5 is used for testing all components:

* File Handling: `LineReaderTest`, `LineWriterTest`
* Parsing & Validation: `MovieParserTest`, `UserParserTest`

---

## 📦 Output Format (recommendations.txt)

Each user appears in **two lines**:

```
Name,ID
Movie1,Movie2,...
```

Example:

```
Karim,12345678A
Fight Club,Goodfellas
```

---

## 📜 License

This project is for learning use. No license specified.
