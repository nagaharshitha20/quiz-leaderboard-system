# Quiz Leaderboard System

This is a Java application designed to poll the Quiz Validator API, deduplicate the retrieved event data, calculate scores for each participant, and submit the final leaderboard.

---

## 📌 Objective

The goal of this project is to:
- Fetch quiz event data from an external API
- Handle duplicate responses correctly
- Compute total scores for each participant
- Generate a sorted leaderboard
- Submit the final result

---

## 🚀 Features

- Polls the Validator API 10 times (poll=0 to poll=9)
- Maintains a mandatory 5-second delay between API calls
- Deduplicates events using (roundId + participant)
- Aggregates total scores per participant
- Generates a leaderboard sorted in descending order
- Submits final result using POST API
- Uses only standard Java libraries (no external dependencies)

---

## 🧠 Key Concept

The main challenge is handling duplicate API responses in a distributed system.

This solution ensures correctness by:
- Using a HashSet to track processed events
- Creating a unique key: (roundId + participant)
- Ignoring repeated entries across multiple API calls

This guarantees accurate score computation.

---

## 🛠️ Prerequisites

- Java Development Kit (JDK) 11 or higher

---

## ⚙️ Setup and Execution

1. Clone the repository:
git clone https://github.com/nagaharshitha20/quiz-leaderboard-system.git

 cd quiz-leaderboard-system

2. Update Registration Number in QuizLeaderboard.java:
private static final String REG_NO = "YOUR_REG_NO";

3. Compile the program:
javac QuizLeaderboard.java

4. Run the program:
java QuizLeaderboard

---

## 🔄 Workflow Explanation

1. Polling  
The application calls the API 10 times (poll=0 to poll=9) with a 5-second delay between each call.

2. Parsing & Deduplication  
The response is parsed and duplicate entries are removed using a HashSet with key (roundId + participant).

3. Aggregation  
Scores are stored and summed using a HashMap.

4. Sorting  
The leaderboard is sorted in descending order of total scores.

5. Submission  
The final leaderboard is converted to JSON and submitted using a POST request.

---

## 📊 Sample Output

Final Scores:
Diana = 470
Ethan = 455
Fiona = 440

Total Score = 1365

Leaderboard:
1. Diana
2. Ethan
3. Fiona

---

## 📁 Project Structure

quiz-leaderboard-system/
|-- QuizLeaderboard.java
|-- README.md

---

## 🏁 Conclusion

This project demonstrates:
- API integration
- Data deduplication
- Score aggregation
- Handling real-world distributed system challenges

---

## 👤 Author
Karra Naga Harshitha
