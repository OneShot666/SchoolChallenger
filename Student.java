// import java.lang.*;
import java.text.DecimalFormat;
import java.util.*;
// import java.awt.*;
import java.io.*;

public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 123456789L;

    public boolean has_read_help = false;
    public boolean can_challenge = true;
    public boolean is_challenging = false;
    public boolean is_director = false;

    public String name;
    public List<Float> Notes = new ArrayList<>();
    public float average;
    public float life = 1;
    public String grade = Data.Grades.getFirst();                               // Start from the bottom

    private int nb_battle = 0;
    private int nb_victory = 0;

    public final List<Student> Friends = new ArrayList<>();

    private final List<String> BattleHistory = new ArrayList<>();
    public List<Question> UnlockedQuestions = new ArrayList<>();
    public List<Question> FinishedQuestions = new ArrayList<>();
    public List<Quizz> UnlockedQuizz = new ArrayList<>();
    public List<Quizz> FinishedQuizz = new ArrayList<>();

    /**
     * Constructor (no arg)
     */
    public Student() {                                                          // Ask name and notes of student
        while (this.name == null) ask_new_name();
        while (this.Notes.isEmpty()) ask_notes();
        calculate_unlocked_questions();
        calculate_unlocked_quizz();
        System.out.println("Bienvenue à " + Data.game_name + ", " + this.name + " !");
        System.out.println("Votre moyenne actuelle : " + this.average + " / 20");
        System.out.println("Vos points de vie : " + this.life + " pv");
    }

    /**
     * Constructor (5 args)
     *
     * @param name : name of student
     * @param notes : notes of student
     * @param grade_index : grade of student (position in list)
     * @param nb_battle : number of battles made by student
     * @param nb_victory : number of victories of student
     */
    public Student(String name, List<Float> notes, int grade_index, int nb_battle, int nb_victory) {
        set_name(name);
        set_notes(notes);
        set_grade(Data.Grades.get(grade_index));
        set_nb_battle(nb_battle);
        set_nb_victory(nb_victory);
    }

    /**
     * @return description student
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        double ratio = (double) nb_victory / nb_battle;
        double win_rate = (nb_battle > 0)? (ratio * 100) : 0;
        return name + " (" + grade + "), \t" + life + " pv, \tmoy " + average + "/20, \t" + df.format(win_rate) + "% de victoire";
    }

    /**
     * If student is actually the director of the school
     */
    public void player_is_director() {
        this.is_director = true;
    }

    /**
     * If director is no longer the director
     */
    public void director_is_fired() {
        this.is_director = false;
    }

    /**
     * @return name of student
     */
    public String get_name() {
        return this.name;
    }

    /**
     * @param new_name : new name of student
     */
    public void set_name(String new_name) {
        this.name = new_name;
    }

    /**
     * Ask student his name (once at beginning)
     */
    public void ask_new_name() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean is_correct = false;

        do {
            System.out.print("Entrez votre prénom ('q' pour annuler): ");
            String new_name = scanner.nextLine();

            if (new_name.equals("q")) is_correct = true;
            else if (Data.AllStudentsNames.contains(new_name)) System.out.println("Cet élève existe déjà !");
            else if (new_name.length() < Data.min_name_size) System.out.println("Votre nom est trop court.");
            else if (new_name.length() > Data.max_name_size) System.out.println("Votre nom est trop long.");
            else if (Data.ForbiddenNames.contains(new_name)) {
                int index = random.nextInt(Data.CommentsForbiddenNames.size());
                System.out.println(Data.CommentsForbiddenNames.get(index));     // Display random judge comment
            } else if (new_name.contains(Data.ForbiddenLetters.toString())) {
                int index = random.nextInt(Data.CommentsForbiddenLetters.size());
                System.out.println(Data.CommentsForbiddenLetters.get(index));   // Display random judge comment
            } else if (new_name.contains(Data.ForbiddenChar.toString())) {
                int index = random.nextInt(Data.CommentsForbiddenChar.size());
                System.out.println(Data.CommentsForbiddenChar.get(index));      // Display random judge comment
            } else {
                this.set_name(new_name);
                System.out.println("Nouveau nom : " + this.name);
                is_correct = true;
            }
        } while (!is_correct);
        scanner.close();
    }

    /**
     * @return notes of student
     */
    public List<Float> get_notes() {
        return this.Notes;
    }

    /**
     * @param new_notes : list of notes
     */
    public void set_notes(List<Float> new_notes) {
        this.Notes = new ArrayList<>();
        for (float note : new_notes) add_note(note);
        set_life(1);
        for (float note : this.Notes) change_life(note);
    }

    /**
     * Ask student his notes (once at beginning)
     */
    public void ask_notes() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Combien de notes avez-vous ? ");
        int nb = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= nb; i++) {
            System.out.print("Notes n°" + i + " sur " + nb + " : ");
            float new_note = Float.parseFloat(scanner.nextLine());
            if (new_note >= 19) {
                int index = random.nextInt(Data.CommentsGoodNotes.size());
                System.out.println(Data.CommentsGoodNotes.get(index));          // Display random judge comment
            } else if (new_note <= 1) {
                int index = random.nextInt(Data.CommentsBadNotes.size());
                System.out.println(Data.CommentsBadNotes.get(index));           // Display random judge comment
            }
            this.add_note(new_note);
        }
        scanner.close();
    }

    /**
     * @param note : note to check value
     * @return note with correct value
     */
    public float check_note_value(float note) {
        return (note > 20)? 20 : Math.max(note, 0);                             // Keep note between 0 and 20
    }

    /**
     * @param new_note : new note to add to notes
     */
    public void add_note(float new_note) {
        new_note = check_note_value(new_note);
        this.Notes.add(new_note);
        change_life(new_note);
        this.calculate_average();
    }

    /**
     * @param old_note : note to remove from notes
     */
    public void remove_note(float old_note) {
        try {
            this.Notes.remove(old_note);
            change_life(- old_note);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erreur : la note '" + old_note + "' n'existe pas !");
        }
    }

    /**
     * @return average grades of student
     */
    public float get_average() {
        return this.average;
    }

    /**
     * Calculate current average grades
     */
    public void calculate_average() {
        float somme = 0.0f;
        for (float note : Notes) {
            somme += note;
        }
        this.average = somme / Notes.size();
    }

    /**
     * @return life of student
     */
    public float get_life() {
        return this.life;
    }

    /**
     * @param new_life : new value of student life
     */
    public void set_life(float new_life) {
        this.life = new_life;
        check_life_value();
    }

    /**
     * @param add_life : value to add to life of student
     */
    public void change_life(float add_life) {
        this.life += add_life;
        check_life_value();
    }

    /**
     * Check if student life is not negative
     */
    public void check_life_value() {
        this.life = Math.max(this.life, 0);
        this.can_challenge = this.life != 0;
    }

    /**
     * @return grade of student
     */
    public String get_grade() {
        return this.grade;
    }

    /**
     * @param new_grade : new value of student grade
     */
    public void set_grade(String new_grade) {
        this.grade = new_grade;
        calculate_unlocked_quizz();
        calculate_unlocked_questions();
    }

    /**
     * Upgrade grade of student
     */
    public void is_promoted() {
        int grade_index = Data.Grades.indexOf(this.grade);
        set_grade(Data.Grades.get(grade_index + 1));
    }

    /**
     * Make student challenge another student
     */
    public void enter_battle() {
        this.is_challenging = true;
    }

    /**
     * @return number of battle student has participated
     */
    public int get_nb_battle() {
        return this.nb_battle;
    }

    /**
     * @param new_nb : new number of battles made by student (mostly use to create npc)
     */
    public void set_nb_battle(int new_nb) {
        this.nb_battle = new_nb;
    }

    /**
     * Increase number of battle student has participated (at end for security)
     */
    public void exit_battle() {
        this.nb_battle++;
        this.is_challenging = false;
    }

    /**
     * @return number of battle student has won
     */
    public int get_nb_victory() {
        return this.nb_victory;
    }

    /**
     * @param new_nb : new number of victories of student (mostly use to create npc)
     */
    public void set_nb_victory(int new_nb) {
        new_nb = Math.min(this.nb_battle, new_nb);
        this.nb_victory = new_nb;
    }

    /**
     * Increase number of battle student has won
     */
    public void win_battle() {
        this.nb_victory++;
    }

    /**
     * @return list if student's friends
     */
    public List<Student> get_friends() {
        return this.Friends;
    }

    /**
     * @param friend : student to add to friends
     */
    public void add_friend(Student friend) {
        if (!this.Friends.contains(friend)) this.Friends.add(friend);
    }

    /**
     * @param friend : student to remove from friends
     */
    public void remove_friend(Student friend) {
        this.Friends.remove(friend);
    }

    /**
     * @return history of battle student has participated (list of other students name)
     */
    public List<String> get_battle_history() {
        return this.BattleHistory;
    }

    /**
     * @param student : Student we wish to challenge
     */
    public void challenge_student(Student student) {
        this.BattleHistory.add(student.name);
    }

    /**
     * Add all unlocked questions to students' questions (to use during challenge)
     */
    public void calculate_unlocked_questions() {
        for (Quizz quiz: Data.AllQuizz) {
            if (quiz.score <= Data.Grades.indexOf(this.grade) + 1) {         // Only peak quizz under student grade
                for (Question question : quiz.Questions) {
                    this.add_unlocked_question(question);
                }
            }
        }
    }

    /**
     * @param question : question to add to list of unlocked questions
     */
    public void add_unlocked_question(Question question) {
        this.UnlockedQuestions.add(question);
    }

    /**
     * @param question : question to remove from list of unlocked questions
     */
    public void remove_unlocked_question(Question question) {
        this.UnlockedQuestions.remove(question);
    }

    /**
     * Display all unlocked questions
     */
    public void show_unlocked_questions() {
        int i = 1;
        for (Question question : this.UnlockedQuestions) {
            System.out.println((i + 1) + " - " + question);
            i += 1;
        }
    }

    /**
     * @param question : quiz to add to list of finished questions
     */
    public void add_finished_question(Question question) {
        this.FinishedQuestions.add(question);
    }

    /**
     * @param question : quiz to remove from list of finished questions
     */
    public void remove_finished_question(Question question) {
        this.FinishedQuestions.remove(question);
    }

    /**
     * Display all finished question
     */
    public void show_finished_questions() {
        int i = 1;
        for (Question question : this.FinishedQuestions) {
            System.out.println((i + 1) + " - " + question);
            i += 1;
        }
    }

    /**
     * Add all unlocked questions to students' questions (to use during challenge)
     */
    public void calculate_unlocked_quizz() {
        for (Quizz quiz: Data.AllQuizz) {
            if (quiz.score <= Data.Grades.indexOf(this.grade) + 1) {         // Only peak quizz under student grade
                this.add_unlocked_quiz(quiz);
            }
        }
    }

    /**
     * @param quiz : quiz to add to list of finished quizz
     */
    public void add_unlocked_quiz(Quizz quiz) {
        this.UnlockedQuizz.add(quiz);
    }

    /**
     * @param quiz : quiz to remove from list of finished quizz
     */
    public void remove_unlocked_quiz(Quizz quiz) {
        this.UnlockedQuizz.remove(quiz);
    }

    /**
     * Display all unlocked questions
     */
    public void show_unlocked_quizz() {
        int i = 1;
        for (Quizz quizz : this.UnlockedQuizz) {
            System.out.println((i + 1) + " - " + quizz);
            i += 1;
        }
    }

    /**
     * @return a chosen unlocked quiz
     */
    public Quizz choose_quiz() {
        System.out.println("Vos quizz : (x" + this.UnlockedQuizz.size() + ")");
        show_unlocked_quizz();
        int index = Main.ask_index(this.UnlockedQuizz.size()) + 1;
        return this.UnlockedQuizz.get(index);
    }

    /**
     * @param quiz : quiz to add to list of finished quizz
     */
    public void add_finished_quiz(Quizz quiz) {
        this.FinishedQuizz.add(quiz);
    }

    /**
     * @param quiz : quiz to remove from list of finished quizz
     */
    public void remove_finished_quiz(Quizz quiz) {
        this.FinishedQuizz.remove(quiz);
    }

    /**
     * Display all finished quizz
     */
    public void show_finished_quizz() {
        int i = 1;
        for (Quizz quizz : this.FinishedQuizz) {
            System.out.println((i + 1) + " - " + quizz);
            i += 1;
        }
    }

    /**
     * @param student : Student being asked a question
     * @param quizz : Quizz in which student choose a question to ask
     */
    public void defy_student(Student student, Quizz quizz) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Questions à poser : ");
        Question chosen = null;
        while (chosen == null) {
            int index = 0;
            for (Question question: Collections.unmodifiableList(quizz.get_all_questions())) {
                System.out.println(index + " - " + question + " (" + question.score + ")");
                index++;
            }
            System.out.println("Choix (choisir index) : ");
            int choice = Integer.parseInt(scanner.nextLine());
            chosen = quizz.get_question(choice);                                // Get question based on index
        }
        student.answer_question(chosen);
        scanner.close();
    }

    public void make_test(Quizz quiz) {
        int nb_right = 0;
        for (Question question : quiz.Questions) {
            if (answer_question(question)) nb_right += 1;
        }
        System.out.println("Votre score : " + nb_right + " / " + quiz.Questions.size());
    }

    /**
     * @param question : Question to answer
     */
    private boolean answer_question(Question question) {
        Scanner scanner = new Scanner(System.in);
        question.present();
        System.out.print("Choix (choisir index) : ");
        int answer = Integer.parseInt(scanner.nextLine()) - 1;
        boolean is_correct = question.answer_question(answer);
        scanner.close();
        if (is_correct) {
            System.out.println("Bonne réponse ! (+" + question.score + " pv)");
            this.change_life(question.score);
            return true;
        } else {                                                                // Reduce life by question score
            System.out.println("Mauvaise réponse. (-" + question.score + " pv)");
            this.change_life(- question.score);
            return false;
        }
    }
}
