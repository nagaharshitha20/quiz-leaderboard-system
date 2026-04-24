# Quiz Leaderboard System

This is a Java application designed to poll the Quiz Validator API, deduplicate the retrieved event data, calculate the scores for each participant, and submit the final leaderboard.

## Features

- **Polls 10 Times**: Connects to the Validator API and fetches the event data for 10 iterations (`poll=0` to `poll=9`).
- **5-Second Delay**: Implements the mandatory 5-second delay between each API poll request.
- **Deduplication**: Effectively handles duplicate events across polls by deduplicating based on `roundId` and `participant`.
- **Aggregation & Sorting**: Aggregates total scores per participant and generates a leaderboard sorted in descending order of the total score.
- **One-Time Submission**: Submits the final leaderboard via a POST request to the Validator API upon completion.
- **No External Dependencies**: Built entirely with standard Java 11+ libraries (`java.net.http`, `java.util.regex`, etc.).

## Prerequisites

- **Java Development Kit (JDK) 11 or higher** is required to compile and run this project, as it leverages the native `java.net.http.HttpClient` API.

## Setup and Execution

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/your-repo.git
   cd your-repo
   ```

2. **Update Registration Number**:
   Open `QuizLeaderboard.java` and replace the placeholder Registration Number with your actual Registration Number.
   ```java
   private static final String REG_NO = "YOUR_REG_NO"; 
   ```

3. **Compile the Java File**:
   Open your terminal/command prompt and compile the application:
   ```bash
   javac QuizLeaderboard.java
   ```

4. **Run the Application**:
   Execute the compiled Java class:
   ```bash
   java QuizLeaderboard
   ```

## Workflow Explanation

1. **Polling**: The application iterates 10 times, executing a GET request each time with the corresponding `pollIndex`. A 5000ms delay is inserted after each poll (except the last one).
2. **Parsing & Deduplication**: The API response in JSON format is parsed using regex to avoid external dependency issues. Events are collected and a `HashSet` tracks duplicates based on a composite key (`roundId_participant`).
3. **Aggregation**: Processed events are used to sum up scores for each participant inside a `HashMap`.
4. **Submission**: The aggregated map is converted into a sorted list, then formatted back into JSON. A POST request is finally sent to `/quiz/submit` with the aggregated leaderboard data.

## Author
[Sai Teja Sri]
