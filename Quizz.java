// import java.lang.*;
import java.util.*;
// import java.awt.*;
import java.io.*;

public class Quizz implements Serializable {
    public List<Question> Questions = new ArrayList<>();
    public String domain;
    public String difficulty;
    public int score;                                                           // Depends on diff

    /**
     * Constructor (2 args)
     *
     * @param domain : domain of quizz (general subjects)
     * @param difficulty : how hard the questions will be to answer
     */
    public Quizz(String domain, String difficulty) {
        this.domain = domain;
        this.difficulty = difficulty;
        this.score = Data.Difficulties.indexOf(difficulty) + 1;
    }

    /**
     * @return description of quizz
     */
    @Override
    public String toString() {
        String pts = (score > 1)? "s" : "";
        String qs = (Questions.size() > 1)? "s" : "";
        return domain + ", " + difficulty + " (" + score + " pt" + pts + ") : " + Questions.size() + " question" + qs;
    }

    /**
     * Display quiz questions
     */
    public void present_questions() {
        for (int i = 1; i < Questions.size(); i++) {
            Question question = Questions.get(i);
            System.out.println(i + " - " + question);
        }
    }

    /**
     * Make all questions present themselves (question + possibles answers)
     */
    public void present_questions_with_answers() {
        for (Question question : Questions) {
            question.present();
            System.out.println();
        }
    }

    /**
     * @return questions of quizz
     */
    public List<Question> get_all_questions() {
        return this.Questions;
    }

    /**
     * @param index : place of required question in questions list
     * @return required question
     */
    public Question get_question(int index) {
        return this.Questions.get(index);
    }

    /**
     * @param New_Questions : new set of questions
     */
    public void set_all_questions(List<Question> New_Questions) {
        this.Questions = New_Questions;
    }

    /**
     * @param question : question to add to list of questions
     */
    public void add_question(Question question) {                               // Adapt question to quizz
        question.domain = this.domain;
        question.difficulty = this.difficulty;
        question.score = this.score;
        this.Questions.add(question);
    }
}
