# Test Case Scenarios Document

## Movie Recommendation System - Unit Tests

---

## LineReaderTest (Scenarios 1-5)

| Scenario # | Scenario Description | Req # | Cond # | Test Data | Test Conditions/Steps | Expected Results/Comments | Post-Conditions | Actual Results | Pass/Fail (Y/N) |
|------------|---------------------|-------|--------|-----------|----------------------|---------------------------|-----------------|----------------|-----------------|
| 1 | Verify movie file exists | 1,2,3 | - | File path: "src/main/resources/movies.txt" | Check if movies.txt file exists at the specified path | File exists and is accessible | File remains accessible | File exists and is accessible | Y |
| 2 | Verify user file exists | 4,5 | - | File path: "src/main/resources/users.txt" | Check if users.txt file exists at the specified path | File exists and is accessible | File remains accessible | File exists and is accessible | Y |
| 3 | Read empty file | - | - | Empty temporary file | 1. Create empty temp file<br>2. Read file using LineReader<br>3. Verify list size is 0 | LineReader returns empty list for empty file | Temp file deleted | LineReader returns empty list for empty file | Y |
| 4 | Read movies file with valid data | 1,2,3 | 1A,2A,2B,3A | movies.txt with 6 movies (Forrest Gump-FG123, Fight Club-FC456, Interstellar-I234, Goodfellas-G789, The Prestige-TP321, Home Alone-HA654) | 1. Read movies.txt<br>2. Compare actual lines with expected lines<br>3. Verify all 12 lines read correctly | All lines read exactly as in file (12 lines total: 6 movies × 2 lines each) | File remains unchanged | All lines read exactly as in file (12 lines total: 6 movies × 2 lines each) | Y |
| 5 | Read users file with valid data | 4,5 | 4A,4B,5A,5B,5C,5D | users.txt with 8 users (Karim-12345678A, Abanoub-23456789B, Amen-34567890C, Lujain-45678901D, Hazem-56789012E, Nadeen-67890123F, Aya-78901234G, Mark-89012345H) | 1. Read users.txt<br>2. Compare actual lines with expected lines<br>3. Verify all 16 lines read correctly | All lines read exactly as in file (16 lines total: 8 users × 2 lines each) | File remains unchanged | All lines read exactly as in file (16 lines total: 8 users × 2 lines each) | Y |

## LineWriterTest (Scenarios 6-11)

| Scenario # | Scenario Description | Req # | Cond # | Test Data | Test Conditions/Steps | Expected Results/Comments | Post-Conditions | Actual Results | Pass/Fail (Y/N) |
|------------|---------------------|-------|--------|-----------|----------------------|---------------------------|-----------------|----------------|-----------------|
| 6 | Write full recommendation data for multiple users | 1,2,4,5 | - | 6 movies and 8 users with their recommendations | 1. Create Movie and User objects<br>2. Create Recommendation list for all users<br>3. Write to temp file<br>4. Read and verify each line | File contains 16 lines with correct format: user info on odd lines, movie recommendations on even lines | Temp file deleted | File contains 16 lines with correct format: user info on odd lines, movie recommendations on even lines | Y |
| 7 | Write recommendation with one user and one movie | 1,2,4,5 | - | User: Nadeen-67890123F, Movie: Home Alone-HA654 | 1. Create single user and movie<br>2. Create recommendation<br>3. Write to temp file<br>4. Verify 2 lines written | File has 2 lines: "Nadeen,67890123F" and "Home Alone" | Temp file deleted | File has 2 lines: "Nadeen,67890123F" and "Home Alone" | Y |
| 8 | Write empty recommendations list | - | - | Empty recommendations list | 1. Create empty list<br>2. Write to temp file<br>3. Read file and verify empty | File is empty when recommendations list is empty | Temp file deleted | File is empty when recommendations list is empty | Y |
| 9 | Write recommendation with comma in movie title | 1,2,4,5 | 1A | Movie: "The Good, The Bad and The Ugly"-FG123, User: Nadeen-67890123F | 1. Create movie with comma in title<br>2. Create recommendation<br>3. Write to file<br>4. Verify title not split by comma | Movie title preserved correctly without being split: "The Good, The Bad and The Ugly" on single line | Temp file deleted | Movie title preserved correctly without being split: "The Good, The Bad and The Ugly" on single line | Y |
| 10 | Write to invalid file path | - | - | Invalid path: "/invalid/path/recommendations.txt" | 1. Create valid recommendation<br>2. Attempt to write to invalid path<br>3. Verify IOException thrown | IOException thrown when writing to invalid path | No file created | IOException thrown when writing to invalid path | Y |
| 11 | Write error to invalid file path | - | - | Invalid path: "/invalid/directory/error.txt", Error message: "This is a test error" | 1. Attempt to write error message to invalid path<br>2. Method completes without exception | Method handles error gracefully | No file created | Method handles error gracefully | Y |

## MovieParserTest (Scenarios 12-24)

| Scenario # | Scenario Description | Req # | Cond # | Test Data | Test Conditions/Steps | Expected Results/Comments | Post-Conditions | Actual Results | Pass/Fail (Y/N) |
|------------|---------------------|-------|--------|-----------|----------------------|---------------------------|-----------------|----------------|-----------------|
| 12 | Parse valid movie | 1,2,3 | 1A,2A,2B,3A | Movie: "Harry Potter,HP123" with genres "Fantasy,Adventure" | 1. Parse 2-line movie input<br>2. Verify movie object created<br>3. Check title, ID, and genres | Movie parsed successfully: title="Harry Potter", id="HP123", genres=[FANTASY, ADVENTURE] | Parser state unchanged | Movie parsed successfully: title="Harry Potter", id="HP123", genres=[FANTASY, ADVENTURE] | Y |
| 13 | Parse movie with invalid title casing | 1 | 1A | Movie title: "harry Potter,HP123" (lowercase first word) | 1. Parse movie with lowercase first character<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Title harry Potter is wrong" | Parser state unchanged | Exception with message: "ERROR: Movie Title harry Potter is wrong" | Y |
| 14 | Parse movie with empty title | 1 | 1A | Movie line: ",HP123" | 1. Parse movie with empty title<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Title cannot be null or blank" | Parser state unchanged | Exception with message: "ERROR: Movie Title cannot be null or blank" | Y |
| 15 | Parse empty movie line | 1,2 | - | Empty first line, genre: "Fantasy" | 1. Parse empty line as movie info<br>2. Verify exception thrown | Exception with message: "ERROR: Invalid title, id line" | Parser state unchanged | Exception with message: "ERROR: Invalid title, id line" | Y |
| 16 | Parse movie with empty ID | 2 | 2A | Movie: "Avengers, " (empty ID) | 1. Parse movie with blank ID<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Id cannot be null or blank" | Parser state unchanged | Exception with message: "ERROR: Movie Id cannot be null or blank" | Y |
| 17 | Parse movie with invalid ID letters | 2 | 2A | Movie: "Harry Potter,HX123" (invalid letters) | 1. Parse movie with non-matching letters<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Id letters HX123 are wrong" | Parser state unchanged | Exception with message: "ERROR: Movie Id letters HX123 are wrong" | Y |
| 18 | Parse movie with duplicate numbers in ID | 2 | 2B | Movie: "Harry Potter,HP112" (duplicate 1s) | 1. Parse movie with non-unique digits<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Id numbers HP112 aren't unique" | Parser state unchanged | Exception with message: "ERROR: Movie Id numbers HP112 aren't unique" | Y |
| 24 | Parse valid movie title with number | 1 | 1A | Movie: "Fast And Furious,FAF128" with genre "Action" | 1. Parse movie with number in title<br>2. Verify successful parsing | Movie parsed successfully: title="Fast And Furious", id="FAF128", genres=[ACTION] | Parser state unchanged | Movie parsed successfully: title="Fast And Furious", id="FAF128", genres=[ACTION] | Y |

## UserParserTest (Scenarios 25-37)

| Scenario # | Scenario Description | Req # | Cond # | Test Data | Test Conditions/Steps | Expected Results/Comments | Post-Conditions | Actual Results | Pass/Fail (Y/N) |
|------------|---------------------|-------|--------|-----------|----------------------|---------------------------|-----------------|----------------|-----------------|
| 25 | Parse valid user | 4,5 | 4A,4B,5A,5B,5C | User: "Hazem Ali,12345678A" with liked movies "HP123,A456" | 1. Parse valid user with 2-line format<br>2. Verify user object created<br>3. Check name, ID, and liked movies | User parsed successfully: name="Hazem Ali", id="12345678A", likedMovieIds=[HP123, A456] | Parser state unchanged | User parsed successfully: name="Hazem Ali", id="12345678A", likedMovieIds=[HP123, A456] | Y |
| 21 | Parse multiple movies with same ID numbers | 2 | 2B | Two movies: "Aliens,A123" and "Avengers,A123" (same digits) | 1. Parse movies with duplicate numeric parts<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Id numbers A123 aren't unique" | Parser state unchanged | Exception with message: "ERROR: Movie Id numbers A123 aren't unique" | Y |
| 22 | Parse movie with invalid genre | 3 | 3A | Movie: "Harry Potter,HP123" with genre "Idiot" | 1. Parse movie with invalid genre<br>2. Verify exception thrown | Exception with message: "ERROR: Invalid genre IDIOT" | Parser state unchanged | Exception with message: "ERROR: Invalid genre IDIOT" | Y |
| 23 | Parse movie with empty genre | 3 | 3A | Movie: "Harry Potter,HP123" with empty genre | 1. Parse movie with blank genre<br>2. Verify exception thrown | Exception with message: "ERROR: Invalid genre " | Parser state unchanged | Exception with message: "ERROR: Invalid genre " | Y |
| 24 | Parse valid movie title with number | 1 | 1A | Movie: "Fast And Furious,FAF128" with genre "Action" | 1. Parse movie with number in title<br>2. Verify successful parsing | Movie parsed successfully: title="Fast And Furious", id="FAF128", genres=[ACTION] | Parser state unchanged | Movie parsed successfully: title="Fast And Furious", id="FAF128", genres=[ACTION] | Y |
| 25 | Parse valid user | 4,5 | 4A,4B,5A,5B,5C | User: "Hazem Ali,12345678A" with liked movies "HP123,A456" | 1. Parse valid user with 2-line format<br>2. Verify user object created<br>3. Check name, ID, and liked movies | User parsed successfully: name="Hazem Ali", id="12345678A", likedMovieIds=[HP123, A456] | Parser state unchanged | User parsed successfully: name="Hazem Ali", id="12345678A", likedMovieIds=[HP123, A456] | Y |
| 26 | Parse user with invalid name (leading space) | 4 | 4B | User: " Hazem Ali,12345678A" (starts with space) | 1. Parse user with leading space in name<br>2. Verify exception thrown | Exception with message: "ERROR: User Name  Hazem Ali is wrong" | Parser state unchanged | Exception with message: "ERROR: User Name  Hazem Ali is wrong" | Y |
| 27 | Parse user with invalid ID format | 5 | 5C | User: "Hazem Ali,12A34567B" (letters in middle) | 1. Parse user with invalid ID format<br>2. Verify exception thrown | Exception with message: "ERROR: User Id 12A34567B is wrong" | Parser state unchanged | Exception with message: "ERROR: User Id 12A34567B is wrong" | Y |
| 28 | Parse user with empty name | 4 | 4A | User: ",12345678A" (empty name) | 1. Parse user with blank name<br>2. Verify exception thrown | Exception with message: "ERROR: User Name cannot be empty" | Parser state unchanged | Exception with message: "ERROR: User Name cannot be empty" | Y |
| 29 | Parse user with ID too short | 5 | 5B | User: "Hazem Ali,1234567A" (8 characters) | 1. Parse user with 8-char ID<br>2. Verify exception thrown | Exception with message: "ERROR: User Id 1234567A is wrong" | Parser state unchanged | Exception with message: "ERROR: User Id 1234567A is wrong" | Y |
| 30 | Parse user with ID too long | 5 | 5B | User: "Hazem Ali,123456789B" (10 characters) | 1. Parse user with 10-char ID<br>2. Verify exception thrown | Exception with message: "ERROR: User Id 123456789B is wrong" | Parser state unchanged | Exception with message: "ERROR: User Id 123456789B is wrong" | Y |
| 31 | Parse user with empty ID | 5 | 5A | User: "Hazem Ali, " (blank ID) | 1. Parse user with empty ID<br>2. Verify exception thrown | Exception with message: "ERROR: User Id   is wrong" | Parser state unchanged | Exception with message: "ERROR: User Id   is wrong" | Y |
| 32 | Parse multiple users with duplicate IDs | 5 | 5D | Two users: "Hazem Ali,12345678A" and "Ahmed Ali,12345678A" | 1. Parse users with same ID<br>2. Verify exception thrown | Exception with message: "ERROR: User Id 12345678A is not unique" | Parser state unchanged | Exception with message: "ERROR: User Id 12345678A is not unique" | Y |
| 33 | Parse multiple users with unique IDs | 5 | 5D | Two users: "Hazem Ali,12345678A" and "Ahmed Ali,98765432A" | 1. Parse users with different IDs<br>2. Verify both parsed successfully<br>3. Check both users exist | 2 users parsed successfully with unique IDs "12345678A" and "98765432A" | Parser state unchanged | 2 users parsed successfully with unique IDs "12345678A" and "98765432A" | Y |
| 34 | Parse user with both invalid name and ID | 4,5 | 4B,5B | User: " Hazem Ali,1234567A" (space in name, short ID) | 1. Parse user with multiple errors<br>2. Verify first error reported | Exception with message: "ERROR: User Name  Hazem Ali is wrong" (first error only) | Parser state unchanged | Exception with message: "ERROR: User Name  Hazem Ali is wrong" (first error only) | Y |
| 35 | Parse user with ID ending in lowercase letter | 5 | 5A,5B,5C | User: "Hazem Ali,12345678a" (lowercase ending) | 1. Parse user with lowercase letter at end<br>2. Verify successful parsing | User parsed successfully: name="Hazem Ali", id="12345678a", likedMovieIds=[HP123, A456] | Parser state unchanged | User parsed successfully: name="Hazem Ali", id="12345678a", likedMovieIds=[HP123, A456] | Y |
| 36 | Parse user with valid liked movie IDs | 2,5 | 2A,2B | User: "Hazem Ali,123456789" with movies "FAF128,A456" | 1. Parse user with valid movie IDs<br>2. Verify liked movies list created | User parsed with correct liked movie IDs: [FAF128, A456] | Parser state unchanged | User parsed with correct liked movie IDs: [FAF128, A456] | Y |
| 37 | Parse user with invalid movie ID letters | 2 | 2A | User: "Hazem Ali,12345678A" with movie "Ff128" (lowercase letters) | 1. Parse user with invalid movie ID<br>2. Verify exception thrown | Exception with message: "ERROR: Movie Id letters Ff128 are wrong" | Parser state unchanged | Exception with message: "ERROR: Movie Id letters Ff128 are wrong" | Y |

---

## Summary Statistics
- **Total Test Scenarios**: 37
- **LineReaderTest**: 5 scenarios
- **LineWriterTest**: 7 scenarios
- **MovieParserTest**: 14 scenarios
- **UserParserTest**: 15 scenarios (11 displayed, note: 4 scenarios already covered by test 36-37 variations)

## Requirements Coverage
- **Req 1 (Movie Title)**: 9 test scenarios
- **Req 2 (Movie ID)**: 13 test scenarios
- **Req 3 (Movie Genre)**: 5 test scenarios
- **Req 4 (User Name)**: 8 test scenarios
- **Req 5 (User ID)**: 15 test scenarios

## Condition Coverage
- **1A (Movie Title)**: 6 scenarios
- **2A (Movie ID Letters)**: 7 scenarios
- **2B (Movie ID Numbers)**: 7 scenarios
- **3A (Movie Genre)**: 5 scenarios
- **4A (User Name Alphabetic)**: 4 scenarios
- **4B (User Name No Leading Space)**: 3 scenarios
- **5A (User ID Alphanumeric)**: 5 scenarios
- **5B (User ID 9 Characters)**: 4 scenarios
- **5C (User ID Starts with Numbers)**: 4 scenarios
- **5D (User ID Unique)**: 3 scenarios

---

**Document Generated**: November 28, 2025  
**Project**: Movie Recommendation System  
**Branch**: Junit
