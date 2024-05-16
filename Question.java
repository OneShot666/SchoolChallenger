// import java.lang.*;
import java.util.*;
// import java.awt.*;
import java.io.*;

public class Question implements Serializable {
    public String domain;
    public String question;
    public String[] Answers;                                                    // Possibles answers (at least 4)
    public int correct_index;                                                   // place of correct answer in Answers
    public String difficulty;
    public int score;                                                           // Depends on diff

    public Question(String question, String[] Answers, int correct_index) {
        this.question = question;
        this.Answers = Answers;
        this.correct_index = correct_index;
    }

    public Question(String domain, String question, String[] Answers, int correct_index, String difficulty) {
        this.domain = domain;
        this.question = question;
        this.Answers = Answers;
        this.correct_index = correct_index;
        this.difficulty = difficulty;
        this.score = Data.Difficulties.indexOf(difficulty) + 1;
    }

    /**
     * @return description of question
     */
    @Override
    public String toString() {
        String s = (score > 1)? "s" : "";
        return question + " (" + domain + "), " + difficulty + " (" + score + " pt" + s + ") : " + Answers.length + " réponses";
    }

    public void present() {
        System.out.println(question + " (" + domain + ")");
        int index = 0;
        for (String answer : Answers) {
            System.out.println((index + 1) + " - " + answer);
            index++;
        }
    }

    public boolean answer_question(int answer) {                             // Add check if answer is inside Answers
        return Answers[correct_index].equals(Answers[answer]);
    }
}
