// import java.lang.*;
import java.util.*;
// import java.awt.*;
import java.io.*;
import java.util.stream.IntStream;


// TODO: Create student class (add avatar later)
// ! Add set notes
// TODO: Create question class (have possibles answers)
// ! Choose 4 random answers on 10 (or more)
// TODO: Create quizz class (qcm, manage question)
// Students can use quizz in challenge against other student
// TODO: Create create_quizz function (once at beginning)
// TODO: Add save_student_data function (in Main, save in file)
// TODO: Create classroom class (get all avatar in files)
// TODO: Add challenge_player function (in Student, choose a player, can't see stats until defy)
// TODO: Add getters, setters and javadoc (everywhere)
// TODO: Secure code (inputs, make as many vars private as possible)
// Upgrades (optional) :
// - Add quizz for student to practice (score add based on answers, unlock questions to defy and others quizz)
// - Add admin place (can add quizz from the app, modify student account, Student can ask questions to admins)
// - Add ask grade-book when new student created: scan pdf and auto-add notes (ask if correct)
// - Add check_location (use online site or something) to challenge near students
// - Add students can join each other into grouped challenge (if diff score not too high)
// - Add notifs (ask student email) and send mails when student is challenged, join group, new quizz...
// - Add stats for students (knowledges for each domains)
// - Add perfs for graphs (pv, domains mastership, player challenged, victory rate, etc)
// - Ass skills (see students stats, have more time, avoid questions (cost), remove words in other questions...)
public class Main implements Serializable {
    public static boolean running = true;
    public static boolean on_profil = false;
    public static boolean on_friends = false;
    public static boolean on_quizz = false;
    public static boolean on_class = false;
    public static boolean is_student_log_in = false;

    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();

    public static List<Student> AllMates = new ArrayList<>();                   // Classroom (students present today)
    public static List<Student> AllStudents = new ArrayList<>();                // School
    public static Student player;                                              // Player

    public static float time_sleep = 0.1f;                                      // Time to wait in sec

    public static void main(String[] args) {                                    // Launch all programs
        // Load game components
        create_quizz();
        create_students();
        fill_classroom();

        System.out.println("Welcome to " + Data.game_name + " ! (" + Data.version + ")");
        connect();
        wait_for_player();

        while (running) {                                                       // Main loop of game
            if (on_profil) menu_profil();
            else if (on_friends) menu_friends();
            else if (on_class) menu_class();
            else if (on_quizz) menu_quizz();
            else menu_school();
        }

        scanner.close();
    }

    /**
     * Set all quizz with all questions
     */
    public static void create_quizz() {
        List<String> domains = Data.Domains;
        List<String> diffs = Data.Difficulties;

        // Informatique
        Quizz quizz_info_easy_1 = new Quizz(domains.getFirst(), diffs.getFirst());
        Question question_info_easy_1 = new Question("Qu'est-ce que HTML ?", new String[] {"Un langage de programmation",
            "Un langage de balisage", "Un système d'exploitation", "Un logiciel de traitement de texte"}, 1);
        quizz_info_easy_1.add_question(question_info_easy_1);
        Data.AllQuizz.add(quizz_info_easy_1);

        Quizz quizz_info_middle_1 = new Quizz(domains.getFirst(), diffs.get(1));
        Question question_info_middle_1 = new Question("Qu'est-ce que signifie l'acronyme 'SQL' en informatique ?",
            new String[] {"Structured Query Language", "System Quality Level", "Simple Query Logic",
            "Software Quality License"}, 0);
        quizz_info_middle_1.add_question(question_info_middle_1);
        Data.AllQuizz.add(quizz_info_middle_1);

        Quizz quizz_info_inter_1 = new Quizz(domains.getFirst(), diffs.get(2));
        Question question_info_inter_1 = new Question("Quel est le principal objectif de Python ?",
            new String[] {"Créer des sites web interactifs", "Développer des applications mobiles",
            "Faciliter l'analyse de données et l'automatisation des tâches", "Concevoir des jeux vidéo"}, 2);
        quizz_info_inter_1.add_question(question_info_inter_1);
        Data.AllQuizz.add(quizz_info_inter_1);

        Quizz quizz_info_diff_1 = new Quizz(domains.getFirst(), diffs.get(3));
        Question question_info_diff_1 = new Question("Qu'est-ce qu'un algorithme de tri 'QuickSort' ?",
            new String[] {"Un algorithme de tri par fusion", "Un algorithme de tri par insertion",
            "Un algorithme de tri par sélection", "Un algorithme de tri rapide"}, 3);
        quizz_info_diff_1.add_question(question_info_diff_1);
        Data.AllQuizz.add(quizz_info_diff_1);

        Quizz quizz_info_expert_1 = new Quizz(domains.getFirst(), diffs.get(4));
        Question question_info_expert_1 = new Question("Quelle est la complexité temporelle de l'algorithme de " +
            "recherche binaire ?", new String[] {"O(n)", "O(log n)", "O(n log n)", "O(n²)"}, 1);
        quizz_info_expert_1.add_question(question_info_expert_1);
        Data.AllQuizz.add(quizz_info_expert_1);

        // Mathématiques
        Quizz quizz_math_easy_1 = new Quizz(domains.get(1), diffs.getFirst());
        Question question_math_easy_1 = new Question("Combien fait 5 + 7 ?", new String[] {"10", "12", "14", "16"}, 1);
        quizz_math_easy_1.add_question(question_math_easy_1);
        Data.AllQuizz.add(quizz_math_easy_1);

        Quizz quizz_math_middle_1 = new Quizz(domains.get(1), diffs.get(1));
        Question question_math_middle_1 = new Question("Comment calculer la circonférence d'un cercle ?",
            new String[] {"π * r", "2 * π * r", "π * r²", "2 * π * r²"}, 1);
        quizz_math_middle_1.add_question(question_math_middle_1);
        Data.AllQuizz.add(quizz_math_middle_1);

        Quizz quizz_math_inter_1 = new Quizz(domains.get(1), diffs.get(2));
        Question question_math_inter_1 = new Question("Quelle est la dérivée de la fonction f(x) = x² ?",
            new String[] {"f'(x) = 2x", "f'(x) = x", "f'(x) = 2", "f'(x) = 0"}, 0);
        quizz_math_inter_1.add_question(question_math_inter_1);
        Data.AllQuizz.add(quizz_math_inter_1);

        Quizz quizz_math_diff_1 = new Quizz(domains.get(1), diffs.get(3));
        Question question_math_diff_1 = new Question("Quelle est la valeur de π ?",
            new String[] {"3.1415", "3.1416", "3.1417", "3.1418"}, 0);
        quizz_math_diff_1.add_question(question_math_diff_1);
        Data.AllQuizz.add(quizz_math_diff_1);

        Quizz quizz_math_expert_1 = new Quizz(domains.get(1), diffs.get(4));
        Question question_math_expert_1 = new Question("Quelle est la somme des angles intérieurs d'un octogone ?",
            new String[] {"720 degrés", "800 degrés", "900 degrés", "1000 degrés"}, 0);
        quizz_math_expert_1.add_question(question_math_expert_1);
        Data.AllQuizz.add(quizz_math_expert_1);

        // Physique-chimie
        Quizz quizz_phch_easy_1 = new Quizz(domains.get(2), diffs.getFirst());
        Question question_phch_easy_1 = new Question("Quelle est la formule chimique de l'eau ?",
            new String[] {"H2O", "CO2", "NaCl", "CH4"}, 0);
        quizz_phch_easy_1.add_question(question_phch_easy_1);
        Data.AllQuizz.add(quizz_phch_easy_1);

        Quizz quizz_phch_middle_1 = new Quizz(domains.get(2), diffs.get(1));
        Question question_phch_middle_1 = new Question("Quelle est la formule de l'acide sulfurique ?",
            new String[] {"H2SO4", "HCl", "HNO3", "H2O2"}, 0);
        quizz_phch_middle_1.add_question(question_phch_middle_1);
        Data.AllQuizz.add(quizz_phch_middle_1);

        Quizz quizz_phch_inter_1 = new Quizz(domains.get(2), diffs.get(2));
        Question question_phch_inter_1 = new Question("Quel est le nombre de protons dans un atome d'hydrogène ?",
            new String[] {"0", "1", "2", "3"}, 1);
        quizz_phch_inter_1.add_question(question_phch_inter_1);
        Data.AllQuizz.add(quizz_phch_inter_1);

        Quizz quizz_phch_diff_1 = new Quizz(domains.get(2), diffs.get(3));
        Question question_phch_diff_1 = new Question("Quelle est la température de fusion de l'eau ?",
            new String[] {"0°C", "100°C", "20°C", "-20°C"}, 0);
        quizz_phch_diff_1.add_question(question_phch_diff_1);
        Data.AllQuizz.add(quizz_phch_diff_1);

        Quizz quizz_phch_expert_1 = new Quizz(domains.get(2), diffs.get(4));
        Question question_phch_expert_1 = new Question("Quelle est la formule de l'acide chlorhydrique ?",
            new String[] {"HCl", "H2SO4", "HNO3", "H2O2"}, 0);
        quizz_phch_expert_1.add_question(question_phch_expert_1);
        Data.AllQuizz.add(quizz_phch_expert_1);

        // SVT
        Quizz quizz_svt_easy_1 = new Quizz(domains.get(3), diffs.getFirst());
        Question question_svt_easy_1 = new Question("Quel est l'organe principal du système respiratoire chez l'Humain ?",
            new String[] {"Poumon", "Foie", "Cœur", "Cerveau"}, 0);
        quizz_svt_easy_1.add_question(question_svt_easy_1);
        Data.AllQuizz.add(quizz_svt_easy_1);

        Quizz quizz_svt_middle_1 = new Quizz(domains.get(3), diffs.get(1));
        Question question_svt_middle_1 = new Question("Quel est l'organe principal du système digestif chez l'Humain ?",
            new String[] {"Estomac", "Rein", "Cœur", "Cerveau"}, 0);
        quizz_svt_middle_1.add_question(question_svt_middle_1);
        Data.AllQuizz.add(quizz_svt_middle_1);

        Quizz quizz_svt_inter_1 = new Quizz(domains.get(3), diffs.get(2));
        Question question_svt_inter_1 = new Question("Quel est l'organe principal du système circulatoire chez l'Humain ?",
            new String[] {"Cœur", "Poumon", "Foie", "Cerveau"}, 0);
        quizz_svt_inter_1.add_question(question_svt_inter_1);
        Data.AllQuizz.add(quizz_svt_inter_1);

        Quizz quizz_svt_diff_1 = new Quizz(domains.get(3), diffs.get(3));
        Question question_svt_diff_1 = new Question("Quel est l'organe principal du système excréteur chez l'Humain ?",
            new String[] {"Rein", "Estomac", "Poumon", "Cerveau"}, 0);
        quizz_svt_diff_1.add_question(question_svt_diff_1);
        Data.AllQuizz.add(quizz_svt_diff_1);

        Quizz quizz_svt_expert_1 = new Quizz(domains.get(3), diffs.get(4));
        Question question_svt_expert_1 = new Question("Quelle est la plus grande cellule du corps humain ?",
            new String[] {"Ovule", "Gros intestin", "Neurone", "Spermatozoïde"}, 0);
        quizz_svt_expert_1.add_question(question_svt_expert_1);
        Data.AllQuizz.add(quizz_svt_expert_1);

        // Technologie
        Quizz quizz_tech_easy_1 = new Quizz(domains.get(4), diffs.getFirst());
        Question question_tech_easy_1 = new Question("Qu'est-ce que signifie USB ?",
            new String[] {"Universal Serial Bus", "User System Basics", "Underlying Software Backup", "Unified System Block"}, 0);
        quizz_tech_easy_1.add_question(question_tech_easy_1);
        Data.AllQuizz.add(quizz_tech_easy_1);

        Quizz quizz_tech_middle_1 = new Quizz(domains.get(4), diffs.get(1));
        Question question_tech_middle_1 = new Question("Quelle est la fonction principale d'un processeur ?",
            new String[] {"Effectuer des calculs", "Stockage des données", "Affichage des images", "Envoi des e-mails"}, 0);
        quizz_tech_middle_1.add_question(question_tech_middle_1);
        Data.AllQuizz.add(quizz_tech_middle_1);

        Quizz quizz_tech_inter_1 = new Quizz(domains.get(4), diffs.get(2));
        Question question_tech_inter_1 = new Question("Quel est le protocole de transfert de fichiers " +
            "utilisé pour télécharger des fichiers sur internet ?", new String[] {"FTP", "HTTP", "SMTP", "HTTPS"}, 0);
        quizz_tech_inter_1.add_question(question_tech_inter_1);
        Data.AllQuizz.add(quizz_tech_inter_1);

        Quizz quizz_tech_diff_1 = new Quizz(domains.get(4), diffs.get(3));
        Question question_tech_diff_1 = new Question("Quel est le rôle principal d'un pare-feu ?",
            new String[] {"Contrôler et filtrer le trafic réseau", "Augmenter la vitesse de connexion",
            "Gérer les serveurs", "Créer des sauvegardes automatiques"}, 0);
        quizz_tech_diff_1.add_question(question_tech_diff_1);
        Data.AllQuizz.add(quizz_tech_diff_1);

        Quizz quizz_tech_expert_1 = new Quizz(domains.get(4), diffs.get(4));
        Question question_tech_expert_1 = new Question("Quel est le langage de programmation utilisé principalement " +
            "pour les applications mobiles Android ?", new String[] {"Java", "C++", "Python", "Swift"}, 0);
        quizz_tech_expert_1.add_question(question_tech_expert_1);
        Data.AllQuizz.add(quizz_tech_expert_1);

        // Anglais
        Quizz quizz_EN_easy_1 = new Quizz(domains.get(5), diffs.getFirst());
        Question question_EN_easy_1 = new Question("Quelle est la traduction de 'Hello' en français ?",
            new String[] {"Bonjour", "S'il vous plaît", "Merci", "Au revoir" }, 0);
        quizz_EN_easy_1.add_question(question_EN_easy_1);
        Question question_EN_easy_2 = new Question("Quelle est la traduction de 'Please' en français ?",
            new String[] {"Bonjour", "S'il vous plaît", "Merci", "Au revoir" }, 1);
        quizz_EN_easy_1.add_question(question_EN_easy_2);
        Question question_EN_easy_3 = new Question("Quelle est la traduction de 'Thanks you' en français ?",
            new String[] {"Bonjour", "S'il vous plaît", "Merci", "Au revoir" }, 2);
        quizz_EN_easy_1.add_question(question_EN_easy_3);
        Question question_EN_easy_4 = new Question("Quelle est la traduction de 'Goodbye' en français ?",
            new String[] {"Bonjour", "S'il vous plaît", "Merci", "Au revoir" }, 3);
        quizz_EN_easy_1.add_question(question_EN_easy_4);
        Data.AllQuizz.add(quizz_EN_easy_1);

        Quizz quizz_EN_middle_1 = new Quizz(domains.get(5), diffs.get(1));
        Question question_EN_middle_1 = new Question("Quel est le passé simple du verbe 'to eat' en anglais ?",
            new String[] {"ate", "eated", "eat", "eaten"}, 0);
        quizz_EN_middle_1.add_question(question_EN_middle_1);
        Data.AllQuizz.add(quizz_EN_middle_1);

        Quizz quizz_EN_inter_1 = new Quizz(domains.get(5), diffs.get(2));
        Question question_EN_inter_1 = new Question("Quel est le participe passé du verbe 'to swim' en anglais ?",
            new String[] {"swam", "swimmed", "swim", "swum"}, 3);
        quizz_EN_inter_1.add_question(question_EN_inter_1);
        Data.AllQuizz.add(quizz_EN_inter_1);

        Quizz quizz_EN_diff_1 = new Quizz(domains.get(5), diffs.get(3));
        Question question_EN_diff_1 = new Question("Quel est le superlatif de 'good' en anglais ?",
            new String[] {"best", "better", "goodest", "gooder"}, 0);
        quizz_EN_diff_1.add_question(question_EN_diff_1);
        Data.AllQuizz.add(quizz_EN_diff_1);

        Quizz quizz_EN_expert_1 = new Quizz(domains.get(5), diffs.get(4));
        Question question_EN_expert_1 = new Question("Qui a écrit 'Romeo and Juliet' ?",
            new String[] {"William Shakespeare", "Jane Austen", "Charles Dickens", "Mark Twain"}, 0);
        quizz_EN_expert_1.add_question(question_EN_expert_1);
        Data.AllQuizz.add(quizz_EN_expert_1);

        // Français
        Quizz quizz_FR_easy_1 = new Quizz(domains.get(6), diffs.getFirst());
        Question question_FR_easy_1 = new Question("Quel est l'infinitif du verbe 'serait' en français ?",
                new String[] {"Être", "Sera", "Était", "Eûmes"}, 0);
        quizz_FR_easy_1.add_question(question_FR_easy_1);
        Data.AllQuizz.add(quizz_FR_easy_1);

        Quizz quizz_FR_middle_1 = new Quizz(domains.get(6), diffs.get(1));
        Question question_FR_middle_1 = new Question("Quel est le pluriel de 'chou' en français ?",
            new String[] {"Choux", "Chou", "Chous", "Chouxes"}, 0);
        quizz_FR_middle_1.add_question(question_FR_middle_1);
        Question question_FR_middle_2 = new Question("Quelle est la forme négative de la phrase 'Il vient' ?",
            new String[] {"Il ne vient pas", "Il viendra", "Il n'a pas venu", "Il ne vint pas"}, 0);
        quizz_FR_middle_1.add_question(question_FR_middle_2);
        Data.AllQuizz.add(quizz_FR_middle_1);

        Quizz quizz_FR_inter_1 = new Quizz(domains.get(6), diffs.get(2));
        Question question_FR_inter_1 = new Question("Qui a écrit 'Les Misérables' ?",
            new String[] {"Victor Hugo", "Gustave Flaubert", "Émile Zola", "Charles Baudelaire"}, 0);
        quizz_FR_inter_1.add_question(question_FR_inter_1);
        Question question_FR_inter_2 = new Question("Qui a écrit 'Madame Bovary' ?",
            new String[] {"Victor Hugo", "Gustave Flaubert", "Émile Zola", "Charles Baudelaire"}, 1);
        quizz_FR_inter_1.add_question(question_FR_inter_2);
        Question question_FR_inter_3 = new Question("Qui est l'auteur du roman 'Germinal' ?",
            new String[] {"Victor Hugo", "Gustave Flaubert", "Émile Zola", "Charles Baudelaire"}, 2);
        quizz_FR_inter_1.add_question(question_FR_inter_3);
        Question question_FR_inter_4 = new Question("Quel est le nom de l'auteur du roman 'Les Fleurs du mal' ?",
            new String[] {"Victor Hugo", "Gustave Flaubert", "Émile Zola", "Charles Baudelaire"}, 3);
        quizz_FR_inter_1.add_question(question_FR_inter_4);
        Question question_FR_inter_5 = new Question("Qui est l'auteur du roman 'Le Petit Prince' ?",
            new String[] {"Antoine de Saint-Exupéry", "Albert Camus", "Émile Zola", "Gustave Flaubert"}, 0);
        quizz_FR_inter_1.add_question(question_FR_inter_5);
        Question question_FR_inter_6 = new Question("Quel est le nom de l'auteur du roman 'Le Rouge et le Noir' ?",
            new String[] {"Stendhal", "Gustave Flaubert", "Honoré de Balzac", "Alexandre Dumas"}, 0);
        quizz_FR_inter_1.add_question(question_FR_inter_6);
        Data.AllQuizz.add(quizz_FR_inter_1);

        Quizz quizz_FR_diff_1 = new Quizz(domains.get(6), diffs.get(3));
        Question question_FR_diff_1 = new Question("Quelle est la figure de style consistant à répéter un même mot " +
            "au début de plusieurs phrases ou vers ?", new String[] {"Anaphore", "Antithèse", "Allégorie", "Paronomase"}, 0);
        quizz_FR_diff_1.add_question(question_FR_diff_1);
        Data.AllQuizz.add(quizz_FR_diff_1);

        Quizz quizz_FR_expert_1 = new Quizz(domains.get(6), diffs.get(4));
        Question question_FR_expert_1 = new Question("Quelle est la figure de style utilisée dans la phrase 'Il pleut des cordes' ?",
            new String[] {"Comparaison", "Métaphore", "Hyperbole", "Personnification"}, 0);
        quizz_FR_expert_1.add_question(question_FR_expert_1);
        Data.AllQuizz.add(quizz_FR_expert_1);

        // Histoire
        Quizz quizz_hist_easy_1 = new Quizz(domains.get(7), diffs.getFirst());
        Question question_hist_easy_1 = new Question("En quelle année a eu lieu la Révolution française ?",
            new String[] {"1789", "1689", "1889", "1799"}, 0);
        quizz_hist_easy_1.add_question(question_hist_easy_1);
        Data.AllQuizz.add(quizz_hist_easy_1);

        Quizz quizz_hist_middle = new Quizz(domains.get(7), diffs.get(1));
        Question question_hist_middle_1 = new Question("Qui était le premier empereur romain ?",
            new String[] {"Auguste", "Jules César", "Napoléon", "Claude"}, 0);
        quizz_hist_middle.add_question(question_hist_middle_1);
        Data.AllQuizz.add(quizz_hist_middle);

        Quizz quizz_hist_inter_1 = new Quizz(domains.get(7), diffs.get(2));
        Question question_hist_inter_1 = new Question("Quelle bataille a mis fin à la Guerre de Cent Ans ?",
            new String[] {"Bataille de Castillon", "Bataille d'Azincourt", "Bataille de Marignan", "Bataille de Crécy"}, 0);
        quizz_hist_inter_1.add_question(question_hist_inter_1);
        Data.AllQuizz.add(quizz_hist_inter_1);

        Quizz quizz_hist_diff_1 = new Quizz(domains.get(7), diffs.get(3));
        Question question_hist_diff_1 = new Question("Qui était le dernier roi de France avant la Révolution ?",
            new String[] {"Louis XVI", "Louis XIV", "Louis XV", "Louis XIII"}, 0);
        quizz_hist_diff_1.add_question(question_hist_diff_1);
        Data.AllQuizz.add(quizz_hist_diff_1);

        Quizz quizz_hist_expert_1 = new Quizz(domains.get(7), diffs.get(4));
        Question question_hist_expert_1 = new Question("Quelle est la capitale de l'Empire byzantin ?",
            new String[] {"Constantinople", "Rome", "Athènes", "Alexandrie"}, 0);
        quizz_hist_expert_1.add_question(question_hist_expert_1);
        Data.AllQuizz.add(quizz_hist_expert_1);

        // Géographie
        Quizz quizz_geo_easy_1 = new Quizz(domains.get(8), diffs.getFirst());
        Question question_geo_easy_1 = new Question("Quel est le plus grand océan sur Terre ?",
            new String[] {"Océan Pacifique", "Océan Atlantique", "Océan Indien", "Océan Arctique"}, 0);
        quizz_geo_easy_1.add_question(question_geo_easy_1);
        Data.AllQuizz.add(quizz_geo_easy_1);

        Quizz quizz_geo_middle_1 = new Quizz(domains.get(8), diffs.get(1));
        Question question_geo_middle_1 = new Question("Quel est le plus long fleuve d'Europe ?",
            new String[] {"Volga", "Danube", "Rhône", "Sena"}, 0);
        quizz_geo_middle_1.add_question(question_geo_middle_1);
        Data.AllQuizz.add(quizz_geo_middle_1);

        Quizz quizz_geo_inter_1 = new Quizz(domains.get(8), diffs.get(2));
        Question question_geo_inter_1 = new Question("Quel est le point culminant de l'Europe ?",
            new String[] {"Mont Blanc", "Mont Elbrouz", "Mont Everest", "Mont Kilimandjaro"}, 1);
        quizz_geo_inter_1.add_question(question_geo_inter_1);
        Data.AllQuizz.add(quizz_geo_inter_1);

        Quizz quizz_geo_diff_1 = new Quizz(domains.get(8), diffs.get(3));
        Question question_geo_diff_1 = new Question("Quel est le plus grand désert du monde ?",
            new String[] {"Désert du Sahara", "Désert de Gobi", "Désert d'Arabie", "Désert de l'Atacama"}, 0);
        quizz_geo_diff_1.add_question(question_geo_diff_1);
        Data.AllQuizz.add(quizz_geo_diff_1);

        Quizz quizz_geo_expert_1 = new Quizz(domains.get(8), diffs.get(4));
        Question question_geo_expert_1 = new Question("Quel pays possède la plus grande superficie au monde ?",
            new String[] {"Russie", "Canada", "Chine", "États-Unis"}, 0);
        quizz_geo_expert_1.add_question(question_geo_expert_1);
        Data.AllQuizz.add(quizz_geo_expert_1);

        // Culture générale
        Quizz quizz_cultG_easy_1 = new Quizz(domains.get(9), diffs.getFirst());
        Question question_cultG_easy_1 = new Question("Quel est la femelle du tigre ?",
                new String[] {"Chatte", "Lionne", "Tigresse", "Tigron"}, 2);
        quizz_FR_easy_1.add_question(question_cultG_easy_1);
        Question question_cultG_easy_2 = new Question("Qui a peint la Joconde ?",
            new String[] {"Léonard de Vinci", "Pablo Picasso", "Vincent van Gogh", "Claude Monet"}, 0);
        quizz_cultG_easy_1.add_question(question_cultG_easy_2);
        Data.AllQuizz.add(quizz_cultG_easy_1);

        Quizz quizz_cultG_middle = new Quizz(domains.get(9), diffs.get(1));
        Question question_cultG_middle_1 = new Question("Quel est le nom du premier homme à avoir marché sur la Lune ?",
            new String[] {"Neil Armstrong", "Buzz Aldrin", "Yuri Gagarine", "John Glenn"}, 0);
        quizz_cultG_middle.add_question(question_cultG_middle_1);
        Data.AllQuizz.add(quizz_cultG_middle);

        Quizz quizz_cultG_inter_1 = new Quizz(domains.get(9), diffs.get(2));
        Question question_cultG_inter_1 = new Question("Qui est l'auteur de la pièce de théâtre 'Cyrano de Bergerac' ?",
            new String[] {"Edmond Rostand", "Molière", "Victor Hugo", "Jean Racine"}, 0);
        quizz_cultG_inter_1.add_question(question_cultG_inter_1);
        Data.AllQuizz.add(quizz_cultG_inter_1);

        Quizz quizz_cultG_diff_1 = new Quizz(domains.get(9), diffs.get(3));
        Question question_cultG_diff_1 = new Question("Quel est le nom du plus grand lac d'eau douce d'Amérique du Nord ?",
            new String[] {"lac Supérieur", "lac Michigan", "lac Huron", "lac Érié"}, 0);
        quizz_cultG_diff_1.add_question(question_cultG_diff_1);
        Data.AllQuizz.add(quizz_cultG_diff_1);

        Quizz quizz_cultG_expert_1 = new Quizz(domains.get(9), diffs.get(4));
        Question question_cultG_expert_1 = new Question("Quelle est la capitale du Bhoutan ?",
            new String[] {"Thimphou", "Katmandou", "Dacca", "Kaboul"}, 0);
        quizz_cultG_expert_1.add_question(question_cultG_expert_1);
        Data.AllQuizz.add(quizz_cultG_expert_1);
    }

    /**
     * Create all students in school
     */
    public static void create_students() {
        Student Adam = new Student("Adam", new ArrayList<>(Arrays.asList(19f, 15f, 10f, 16f, 13f)), 3, 14, 9);
        add_student_to_school(Adam);
        Student Alice = new Student("Alice", new ArrayList<>(Arrays.asList(12f, 16f, 9f, 14f, 11f)), 2, 23, 11);
        add_student_to_school(Alice);
        Student Bob = new Student("Bob", new ArrayList<>(Arrays.asList(12f, 14f, 11f, 12f, 13f)), 2, 9, 4);
        add_student_to_school(Bob);
        Student Bella = new Student("Bella", new ArrayList<>(Arrays.asList(12f, 15f, 17f, 17f, 11f)), 3, 31, 18);
        add_student_to_school(Bella);
        Student Charlie = new Student("Charlie", new ArrayList<>(Arrays.asList(11f, 9.5f, 10f, 12.5f, 11f)), 0, 15, 6);
        add_student_to_school(Charlie);
        Student Chloe = new Student("Chloe", new ArrayList<>(Arrays.asList(13f, 14.5f, 12f, 16f, 15f)), 3, 36, 19);
        add_student_to_school(Chloe);
        Student Dan = new Student("Dan", new ArrayList<>(Arrays.asList(17.5f, 19f, 20f, 16f, 18f)), 4, 56, 42);
        add_student_to_school(Dan);
        Student Daisy = new Student("Daisy", new ArrayList<>(Arrays.asList(14f, 14f, 12.5f, 17f, 15f)), 2, 25, 11);
        add_student_to_school(Daisy);
        Student Ethan = new Student("Ethan", new ArrayList<>(Arrays.asList(13f, 10f, 12f, 6f, 11.5f)), 0, 33, 13);
        add_student_to_school(Ethan);
        Student Emma = new Student("Emma", new ArrayList<>(Arrays.asList(20f, 18.5f, 15.5f, 17f, 16f)), 4, 44, 34);
        add_student_to_school(Emma);
        Student Fred = new Student("Fred", new ArrayList<>(Arrays.asList(11f, 13.5f, 8.5f, 14f, 13.5f)), 1, 18, 10);
        add_student_to_school(Fred);
        Student Fiona = new Student("Fiona", new ArrayList<>(Arrays.asList(11f, 14f, 15f, 12.5f, 12f)), 2, 32, 20);
        add_student_to_school(Fiona);
        Student George = new Student("George", new ArrayList<>(Arrays.asList(20f, 19f, 20f, 20f, 18f)), 7, 100, 86);
        add_student_to_school(George);
        Student Gabrielle = new Student("Gabrielle", new ArrayList<>(Arrays.asList(16f, 20f, 16f, 20f, 18f)), 4, 48, 39);
        add_student_to_school(Gabrielle);
        Student Harry = new Student("Harry", new ArrayList<>(Arrays.asList(23f, 07f, 19f, 8f, 9f)), 3, 8, 6);
        add_student_to_school(Harry);
        Student Hannah = new Student("Hannah", new ArrayList<>(Arrays.asList(11f, 13f, 11.5f, 10f, 12f)), 1, 27, 15);
        add_student_to_school(Hannah);
        Student Isaac = new Student("Isaac", new ArrayList<>(Arrays.asList(16f, 16f, 17f, 14f, 13f)), 5, 74, 64);
        add_student_to_school(Isaac);
        Student Isabella = new Student("Isabella", new ArrayList<>(Arrays.asList(15f, 15.5f, 17f, 14f, 16.5f)), 3, 48, 32);
        add_student_to_school(Isabella);
        Student James = new Student("James", new ArrayList<>(Arrays.asList(7f, 12f, 11f, 8f, 14f)), 0, 17, 5);
        add_student_to_school(James);
        Student Jessica = new Student("Jessica", new ArrayList<>(Arrays.asList(11f, 15.5f, 10f, 12f, 10.5f)), 1, 23, 11);
        add_student_to_school(Jessica);
        Student Liam = new Student("Liam", new ArrayList<>(Arrays.asList(13.5f, 15f, 12.5f, 12f, 15f)), 2, 35, 19);
        add_student_to_school(Liam);
        Student Lily = new Student("Lily", new ArrayList<>(Arrays.asList(18f, 20f, 16f, 17f, 16f)), 4, 66, 49);
        add_student_to_school(Lily);
        Student Kearan = new Student("Kearan", new ArrayList<>(Arrays.asList(18f, 20f, 16f, 19f, 17f)), 4, 75, 61);
        add_student_to_school(Kearan);
        Student Katherine = new Student("Katherine", new ArrayList<>(Arrays.asList(14f, 15.5f, 12f, 14f, 13.5f)), 2, 31, 17);
        add_student_to_school(Katherine);
        Student Matthew = new Student("Matthew", new ArrayList<>(Arrays.asList(9f, 13f, 10f, 12f, 11f)), 0, 15, 4);
        add_student_to_school(Matthew);
        Student Mia = new Student("Mia", new ArrayList<>(Arrays.asList(19f, 16f, 14f, 17.5f, 12f)), 3, 41, 22);
        add_student_to_school(Mia);
        Student Nathan = new Student("Nathan", new ArrayList<>(Arrays.asList(20f, 16f, 20f, 16f, 20f)), 6, 100, 96);
        add_student_to_school(Nathan);
        Student Natasha = new Student("Natasha", new ArrayList<>(Arrays.asList(12f, 16f, 9.5f, 14.5f, 15f)), 1, 16, 9);
        add_student_to_school(Natasha);
        Student Oliver = new Student("Oliver", new ArrayList<>(Arrays.asList(12.5f, 8f, 15f, 10f, 6.5f)), 0, 9, 4);
        add_student_to_school(Oliver);
        Student Olivia = new Student("Olivia", new ArrayList<>(Arrays.asList(18f, 20f, 15f, 12f, 17f)), 3, 52, 29);
        add_student_to_school(Olivia);
        Student Peter = new Student("Peter", new ArrayList<>(Arrays.asList(15f, 18f, 13f, 20f, 16f)), 3, 38, 22);
        add_student_to_school(Peter);
        Student Poppy = new Student("Poppy", new ArrayList<>(Arrays.asList(12f, 13.5f, 11f, 16f, 15f)), 1, 26, 11);
        add_student_to_school(Poppy);
        Student Quentin = new Student("Quentin", new ArrayList<>(Arrays.asList(17f, 15f, 18f, 14f, 17f)), 3, 44, 25);
        add_student_to_school(Quentin);
        Student Quinn = new Student("Quinn", new ArrayList<>(Arrays.asList(15f, 14f, 15.5f, 14f, 14.5f)), 2, 29, 16);
        add_student_to_school(Quinn);
        Student Ryan = new Student("Ryan", new ArrayList<>(Arrays.asList(20f, 17.5f, 14f, 8f, 11f)), 2, 33, 18);
        add_student_to_school(Ryan);
        Student Ruby = new Student("Ruby", new ArrayList<>(Arrays.asList(13f, 13f, 13f, 13f, 13f)), 1, 16, 8);
        add_student_to_school(Ruby);
        Student Samuel = new Student("Samuel", new ArrayList<>(Arrays.asList(10f, 14f, 12f, 9f, 11.5f)), 0, 7, 5);
        add_student_to_school(Samuel);
        Student Sophia = new Student("Sophia", new ArrayList<>(Arrays.asList(15f, 19f, 17f, 19f, 17f)), 4, 71, 46);
        add_student_to_school(Sophia);
        Student Thomas = new Student("Thomas", new ArrayList<>(Arrays.asList(14f, 17f, 13.5f, 15f, 16f)), 2, 32, 23);
        add_student_to_school(Thomas);
        Student Therese = new Student("Therese", new ArrayList<>(Arrays.asList(17.5f, 19.5f, 16.5f, 20f, 18.5f)), 5, 78, 63);
        add_student_to_school(Therese);
        Student Ulysse = new Student("Ulysse", new ArrayList<>(Arrays.asList(15f, 16f, 17f, 18f, 19f)), 4, 54, 31);
        add_student_to_school(Ulysse);
        Student Ursula = new Student("Ursula", new ArrayList<>(Arrays.asList(15.5f, 13f, 14f, 17f, 11.5f)), 1, 19, 7);
        add_student_to_school(Ursula);
        Student Valentin = new Student("Valentin", new ArrayList<>(Arrays.asList(14f, 16f, 12f, 18f, 10f)), 3, 37, 28);
        add_student_to_school(Valentin);
        Student Vanessa = new Student("Vanessa", new ArrayList<>(Arrays.asList(8f, 11f, 13.5f, 6.5f, 10f)), 0, 13, 6);
        add_student_to_school(Vanessa);
        Student William = new Student("William", new ArrayList<>(Arrays.asList(15f, 17f, 14f, 12.5f, 16f)), 2, 27, 15);
        add_student_to_school(William);
        Student Willow = new Student("Willow", new ArrayList<>(Arrays.asList(12f, 18f, 13f, 17f, 11.5f)), 1, 18, 9);
        add_student_to_school(Willow);
        Student Xavier = new Student("Xavier", new ArrayList<>(Arrays.asList(12f, 15f, 11f, 13f, 10.5f)), 1, 23, 17);
        add_student_to_school(Xavier);
        Student Xenia = new Student("Xenia", new ArrayList<>(Arrays.asList(20f, 14.5f, 18f, 18f, 16f)), 3, 49, 37);
        add_student_to_school(Xenia);
        Student Yusuf = new Student("Yusuf", new ArrayList<>(Arrays.asList(16f, 20f, 15f, 17f, 16.5f)), 3, 33, 13);
        add_student_to_school(Yusuf);
        Student Yasmin = new Student("Yasmin", new ArrayList<>(Arrays.asList(11f, 15f, 13f, 12.5f, 14f)), 1, 19, 12);
        add_student_to_school(Yasmin);
        Student Zachary = new Student("Zachary", new ArrayList<>(Arrays.asList(20f, 16f, 19.5f, 17f, 17.5f)), 4, 80, 62);
        add_student_to_school(Zachary);
        Student Zara = new Student("Zara", new ArrayList<>(Arrays.asList(13f, 18.5f, 14f, 17f, 15f)), 2, 54, 20);
        add_student_to_school(Zara);

        AllStudents.forEach(Main::save_student_data_from_class);
    }

    /**
     * @param other : student to add to class
     */
    public static void add_student_to_school(Student other) {
        AllStudents.add(other);
        Data.AllStudentsNames.add(other.name);
    }

    /**
     * Load all students data in school (text files)
     */
    public static void load_students() {
        AllStudents.stream().map(other -> other.name).forEach(Main::load_student_data_to_class);
    }

    /**
     * Add random number of students in school
     */
    public static void fill_classroom() {
        AllMates = new ArrayList<>();                                           // Empty classroom first
        int nb_student = random.nextInt(AllStudents.size());                    // Random number between 0 and number of students
        IntStream.range(0, nb_student).map(i -> random.nextInt(AllStudents.size()))
            .filter(index -> !AllMates.contains(AllStudents.get(index)))
            .forEach(index -> AllMates.add(AllStudents.get(index)));            // Add random student if not already in class
    }

    /**
     * Ask player name
     */
    public static void connect() {
        do {
            int choix;
            System.out.print("Voulez-vous vous connecter (1) ou créer un profil (2) ? ");
            try { choix = Integer.parseInt(scanner.nextLine()); }
            catch (Exception e) { continue; }

            if (choix == 1) {
                System.out.print("Entrez votre prénom : ");
                String name = scanner.nextLine();
                if (name.equals(Data.get_director_name())) {
                    System.out.println("Bonjour Mr le directeur !");
                    System.out.print("Veuillez entrer votre mot de passe : ");
                    String password = scanner.nextLine();
                    // If password is correct and director exists, then promote student to director
                    if (Objects.equals(password, Data.get_password()) && load_student_data(Data.get_director_name())) {
                        player.player_is_director();
                        is_student_log_in = true;
                    }
                } else if (Data.AllStudentsNames.contains(name)) System.out.println("Cet élève existe déjà !");
                else if (load_student_data(name)) is_student_log_in = true;
            } else if (choix == 2) {
                player = new Student();                                        // Init new student
                is_student_log_in = true;
            }
        } while (!is_student_log_in);
    }

    public static void menu_profil() {
        System.out.println("Profil : Que voulez-vous faire ?");
        display_with_timer_and_index(Data.ProfilActions, time_sleep);

        int choix = ask_index(Data.ProfilActions.size()) + 1;
        System.out.println();

        switch (choix) {
            case 1:
                player.ask_new_name();
                wait_for_player();
                break;
            case 2:
                System.out.println("Votre profil : ");
                System.out.println(player);
                wait_for_player();
                break;
            case 3:
                System.out.println("Vos questions : (x" + player.UnlockedQuestions.size() + ")");
                show_unlocked_questions();
                wait_for_player();
                break;
            case 4:
                System.out.println("Vos tests disponibles : (x" + player.UnlockedQuizz.size() + ")");
                show_unlocked_quizz();
                wait_for_player();
                break;
            case 5:
                on_profil = false;
                on_friends = true;
                break;
            case 6:
                System.out.println("Vos notes : ");
                show_notes();
                wait_for_player();
                break;
            case 7:
                back_to_school();
                break;
            default:
                System.out.println("Index non reconnu.");
        }
        System.out.println();
    }

    public static void menu_friends() {
        System.out.println("Amis : Que voulez-vous faire ?");
        display_with_timer_and_index(Data.FriendsActions, time_sleep);

        int choix = ask_index(Data.FriendsActions.size()) + 1;
        System.out.println();

        switch (choix) {
            case 1:
                System.out.println("Vos amis : ");
                show_player_friends();
                wait_for_player();
                break;
            case 2:
                add_mate_to_friend();
                wait_for_player();
                break;
            case 3:
                remove_friend();
                wait_for_player();
                break;
            case 4:
                if (!player.Friends.isEmpty()) {
                    System.out.println("Vos amis : (x" + player.Friends.size() + ")");
                    show_player_friends();
                }
                defy_friend();
                wait_for_player();
                break;
            case 5:
                back_to_profil();
                break;
            default:
                System.out.println("Index non reconnu.");
        }
        System.out.println();
    }

    public static void menu_class() {
        System.out.println("Classe : Que voulez-vous faire ?");
        display_with_timer_and_index(Data.ClassActions, time_sleep);

        int choix = ask_index(Data.ClassActions.size()) + 1;
        System.out.println();

        switch (choix) {
            case 1:
                System.out.println("Vos camarades : (x" + AllMates.size() + ")");
                show_mates();
                wait_for_player();
                break;
            case 2:
                search_mate();
                wait_for_player();
                break;
            case 3:
                search_student();
                wait_for_player();
                break;
            case 4:
                defy_mate();
                wait_for_player();
                break;
            case 5:
                back_to_school();
                break;
            default:
                System.out.println("Index non reconnu.");
        }
        System.out.println();
    }

    public static void menu_quizz() {
        System.out.println("Quizz : Que voulez-vous faire ?");
        display_with_timer_and_index(Data.QuizzActions, time_sleep);

        int choix = ask_index(Data.QuizzActions.size()) + 1;
        System.out.println();

        switch (choix) {
            case 1:
                System.out.println("Vos test : ");
                show_unlocked_quizz();
                make_test();
                break;
            case 2:
                System.out.println("Tous les quizz : (x" + Data.AllQuizz.size() + ")");
                show_all_quizz();
                wait_for_player();
                break;
            case 3:
                System.out.println("Les catégories : (x" + Data.Domains.size() + ")");
                show_all_domains();
                wait_for_player();
                break;
            case 4:
                System.out.println("Les difficultés : (x" + Data.Difficulties.size() + ")");
                show_all_difficulties();
                wait_for_player();
                break;
            case 5:
                back_to_school();
                break;
            default:
                System.out.println("Index non reconnu.");
        }
        System.out.println();
    }

    /**
     * Main function of game
     */
    public static void menu_school() {
        System.out.println("Ecole : Que voulez-vous faire ?");
        display_with_timer_and_index(Data.SchoolActions, time_sleep);

        int choix = ask_index(Data.SchoolActions.size()) + 1;
        System.out.println();

        switch (choix) {
            case 1:
                on_profil = true;
                break;
            case 2:
                make_test();
                break;
            case 3:
                defy_mate();
                wait_for_player();
                break;
            case 4:
                on_class = true;
                break;
            case 5:
                System.out.println("Tous les tests : (x" + Data.AllQuizz.size() + ")");
                show_all_quizz();
                wait_for_player();
                break;
            case 6:
                System.out.println("Vocabulaire : ");
                show_vocabulary();
                wait_for_player();
                break;
            case 7:
                show_help();
                wait_for_player();
                break;
            case 8:
                show_parameters();
                wait_for_player();
                break;
            case 9:
                close_game();
                break;
            default:
                System.out.println("Index non reconnu.");
        }
        System.out.println();
    }

    /**
     * @param max : maximum value for index
     * @return index between 1
     */
    public static int ask_index(int max) {
        int index;

        while (true) {
            System.out.print("Choix (choisir index) : ");
            String input = scanner.nextLine();
            if (input.isEmpty()) continue;                                      // Ask again if has entered nothing

            try {
                index = Integer.parseInt(input) - 1;
                if (!(index < 0 || max <= index)) break;
            } catch (NumberFormatException _) {}
        }

        return index;
    }

    /**
     * Show unlocked questions of student
     */
    public static void show_unlocked_questions() {
        int i = 1;
        for (Question question: player.UnlockedQuestions) {
            System.out.println(i + " - " + question);
            sleep(0.1f);
            i += 1;
        }
    }

    /**
     * Show unlocked quizz of student
     */
    public static void show_unlocked_quizz() {
        int i = 1;
        for (Quizz quiz: player.UnlockedQuizz) {
            System.out.println(i + " - " + quiz);
            sleep(0.1f);
            i += 1;
        }
    }

    /**
     * Display all quizz
     */
    public static void show_all_quizz() {
        int i = 1;
        for (Quizz quiz : Data.AllQuizz) {
            System.out.println(i + " - " + quiz);
            sleep(0.1f);
            i += 1;
        }
    }

    /**
     * Display all quizz domains
     */
    public static void show_all_domains() {
        int i = 1;
        for (String domain : Data.Domains) {
            System.out.println(i + " - " + capitalize(domain));
            sleep(0.1f);
            i += 1;
        }
    }

    /**
     * Display all difficulties
     */
    public static void show_all_difficulties() {
        int i = 1;
        for (String diff : Data.Difficulties) {
            System.out.println(i + " - " + diff);
            sleep(0.1f);
            i += 1;
        }
    }

    /**
     * @param index : index of chosen quiz to show
     */
    public static void show_chosen_quiz(int index) {
        Quizz quiz = Data.AllQuizz.get(index);
        System.out.println(quiz);
        quiz.present_questions_with_answers();
    }

    /**
     * @return domain chose by student
     */
    public static int ask_domain() {
        System.out.println("Il y a " + Data.AllQuizz.size() + " quizz au total.");

        System.out.println("Catégories : ");
        show_all_domains();

        return ask_index(Data.Domains.size());
    }

    /**
     * Where student can choose test to play it
     */
    public static void make_test() {
        int domain_index = ask_domain();
        String chosen_domain = Data.Domains.get(domain_index);
        List <Quizz> ChosenQuizz = Data.AllQuizz.stream().filter(quiz -> chosen_domain.equals(quiz.domain)).toList();
        int i = 1;
        for (Quizz quiz : ChosenQuizz) {
            System.out.println(i + " - " + quiz);
            sleep(time_sleep);
            i += 1;
        }
        int choix = ask_index(ChosenQuizz.size());
        player.make_test(ChosenQuizz.get(choix));
    }

    /**
     * Display all player notes
     */
    public static void show_notes() {
        if (!player.Notes.isEmpty()) {
            for (float note : player.Notes) System.out.print(note + ", ");
            sleep(0.1f);
            System.out.println("\nVotre moyenne : " + player.average + " / 20");
        } else System.out.println("Vous n'avez pas encore de notes pour le moment");
    }

    /**
     * Show student's friends
     */
    public static void show_player_friends() {
        if (!player.Friends.isEmpty()) player.Friends.forEach(System.out::println);
        else System.out.println("Vous n'avez aucun ami pour le moment");
    }

    /**
     * Show profil of mates (students in class today)
     */
    public static void show_mates() {
        System.out.println("1 - " + player + " (vous)");
        if (!AllMates.isEmpty()) {
            int i = 2;
            for (Student other : AllMates)
                if (!player.name.equals(other.name)) {
                    System.out.println(i + " - " + other);
                    sleep(0.1f);
                    i += 1;
                }
        } else System.out.println("Il n'y a aucun autre élève pour le moment...");
    }

    /**
     * Display all students in school
     */
    public static void show_all_students() {
        int i = 1;
        System.out.println(i + " - " + player + " (vous)");
        i += 1;
        for (Student other : AllStudents) if (!player.name.equals(other.name)) System.out.println(i++ + " - " + other);
    }

    /**
     * Look for a mate chose by student
     */
    public static void search_mate() {
        if (!AllMates.isEmpty()) {
            System.out.print("Entrez le nom du camarade à chercher : ");
            String name = scanner.nextLine();
            Student student = is_name_into_list(AllMates, name);
            if (name.equals(player.name)) System.out.println("C'est vous-même.");
            else if (student == null) System.out.println("Il n'y a aucun élève de ce nom pour le moment...");
            else {
                System.out.println(student);
                if (ask_yes_no_question("Ajouter " + student.name + " en ami ? (y/n) ")) {
                    player.add_friend(student);
                    System.out.println(student.name + " ajouté.e à vos amis !");
                }
            }
        } else System.out.println("Il n'y a aucun autre camarade en classe aujourd'hui...");
    }

    /**
     * Look for a student chose by student
     */
    public static void search_student() {
        System.out.print("Entrez le nom de l'élève à chercher : ");
        String name = scanner.nextLine();
        Student student = is_name_into_list(AllStudents, name);
        if (name.equals(player.name)) System.out.println("C'est vous-même.");
        else if (student == null) System.out.println("Il n'y a aucun élève de ce nom pour le moment...");
        else System.out.println(student + ((AllMates.contains(student))? "\t(en classe)" : ""));
    }

    /**
     * Add a mate to player's list of friends
     */
    public static void add_mate_to_friend() {
        if (!AllMates.isEmpty()) {
            System.out.print("Entrez le nom du camarade à ajouter à vos amis : ");
            String name = scanner.nextLine();
            Student student = is_name_into_list(player.Friends, name);
            if (name.equals(player.name))
                System.out.println("Vous ne pouvez pas vous ajouter vous-même à vos amis (c'est triste...)");
            else if (student == null) System.out.println("Il n'y a aucun élève de ce nom à l'école...");
            else {
                player.add_friend(student);
                System.out.println(student.name + " ajouté.e à votre liste d'amis !");
            }
        } else System.out.println("Il n'y a aucun autre camarade en classe aujourd'hui...");
    }

    /**
     * Remove a mate to player's list of friends
     */
    public static void remove_friend() {
        if (!player.Friends.isEmpty()) {
            System.out.print("Entrez le nom de la personne don't vous ne voulez plus être ami avec : ");
            String name = scanner.nextLine();
            Student student = is_name_into_list(player.Friends, name);
            if (name.equals(player.name)) System.out.println("Vous ne pouvez pas vous retirer vous-même de vos amis (c'est bizarre...)");
            else if (student == null) System.out.println("Il n'y a aucun élève de ce nom à l'école...");
            else {
                player.remove_friend(student);
                System.out.println(student.name + " retiré.e de votre liste d'amis !");
            }
        } else System.out.println("Vous n'avez aucun ami pour le moment");
    }

    /**
     * @param Students : list of students
     * @param name : name of student looked for
     * @return student with this name
     */
    // TODO: Use in every function where name are looking in student list
    @org.jetbrains.annotations.Nullable
    @org.jetbrains.annotations.Contract(pure = true)
    public static Student is_name_into_list(List<Student> Students, String name) {
        return Students.stream().filter(other -> other.name.equals(name)).findFirst().orElse(null);
    }

    /**
     * Defy a friend of player (chose by student)
     */
    public static void defy_friend() {
        if (!player.Friends.isEmpty()) {
            System.out.print("Entrez le nom de l'ami à défier : ");
            String name = scanner.nextLine();
            Student student = is_name_into_list(player.Friends, name);
            if (name.equals(player.name)) System.out.println("Vous ne pouvez pas vous défier vous-même !");
            else if (student != null) {
                System.out.println(student);
                Quizz quiz = player.choose_quiz();
                player.defy_student(student, quiz);
            } else System.out.println("Il n'y a aucun camarade de ce nom aujourd'hui...");
        } else System.out.println("Vous n'avez aucun ami pour le moment...");
    }

    /**
     * Defy a mate (chose by student)
     */
    public static void defy_mate() {
        if (!AllMates.isEmpty()) {
            System.out.print("Entrez le nom du camarade à défier : ");
            String name = scanner.nextLine();
            Student student = is_name_into_list(AllMates, name);
            if (name.equals(player.name)) System.out.println("Vous ne pouvez pas vous défier vous-même !");
            else if (student != null) {
                System.out.println(student);
                Quizz quiz = player.choose_quiz();
                player.defy_student(student, quiz);
            } else System.out.println("Il n'y a aucun camarade de ce nom aujourd'hui...");
        } else System.out.println("Il n'y a aucun autre camarade en classe aujourd'hui...");
    }

    /**
     * Go back to main menu
     */
    public static void back_to_profil() {
        on_profil = true;
        on_friends = false;
        on_class = false;
        on_quizz = false;
    }

    /**
     * Go back to main menu
     */
    public static void back_to_school() {
        on_profil = false;
        on_friends = false;
        on_class = false;
        on_quizz = false;
    }

    /**
     * @param to_display : list of string to display with index (start to 1)
     * @param timer : timer in sec
     */
    public static void display_with_timer_and_index(List<String> to_display, float timer) {
        int i = 1;
        for (String line : to_display) {
            System.out.println(i + " - " + line);
            sleep(timer);
            i += 1;
        }
    }

    /**
     * @param to_display : list of string to display
     * @param timer : timer in sec
     */
    public static void display_with_timer(List<String> to_display, float timer) {
        for (String line : to_display) {
            System.out.println(line);
            sleep(timer);
        }
    }

    /**
     * @param question : question to ask student
     * @return if answer is yes or no
     */
    public static boolean ask_yes_no_question(String question) {
        System.out.print(question);
        String answer = scanner.nextLine().toLowerCase();
        return Arrays.asList(Data.AnswersPositives).contains(answer);
    }

    /**
     * @param str : string to be capitalize
     * @return capitalized string
     */
    public static String capitalize(String str) {
        return (str == null || str.isEmpty())? str : str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Game is paused until student presses enter
     */
    public static void wait_for_player() {
        System.out.print("");
        scanner.nextLine();
    }

    /**
     * @param time_sec : time in seconds
     */
    public static void sleep(float time_sec) {
        try { Thread.sleep((long) (time_sec * 1000L)); }
        catch (InterruptedException e) { System.out.println("Timer interrompu !"); }
    }

    /**
     * Display help notes
     */
    public static void show_vocabulary() {
        display_with_timer(Data.Vocabulary, 0.5f);
    }

    /**
     * Display help notes
     */
    public static void show_help() {
        display_with_timer(Data.Helps, 0.5f);

        if (!player.has_read_help) {
            System.out.println("Merci d'avoir lu les anti-sèches !");
            System.out.println("Voici un cadeau de remerciement : +5 pv");
            player.change_life(5);
            player.has_read_help = true;
        }
    }

    /**
     * Display all parameters
     */
    public static void show_parameters() {
        System.out.println("Paramètres : ");
        System.out.println("En construction...");
    }

    public static void load_student_data_to_class(String name) {
        try {
            FileInputStream file_in = new FileInputStream(Data.saves_students_path + "/" + name + ".txt");
            ObjectInputStream object_in = new ObjectInputStream(file_in);
            AllStudents.add((Student) object_in.readObject());
            object_in.close();
        } catch (FileNotFoundException e) {
            System.out.println(name + " n'est pas enregistré !");
        } catch (ClassNotFoundException e) {
            System.out.println("La classe de " + name + " n'a pas été trouvé !");
        } catch (IOException e) {
            System.out.println("Un problème est survenu lors du chargement du profil de " + name + " !");
        }
    }

    public static void save_student_data_from_class(Student other_student) {
        File file = new File(Data.saves_students_path);
        if (!file.exists()) {                                                   // Try to create student file if doesn't exist
            if (file.mkdirs()) System.out.println("Création automatique du dossier de l'élève...");
            else System.out.println("Erreur lors de la création automatique du dossier de l'élève.");
        }

        try {
            FileOutputStream file_out = new FileOutputStream(Data.saves_students_path + "/" + other_student.name + ".txt");
            ObjectOutputStream object_out = new ObjectOutputStream(file_out);
            object_out.writeObject(other_student);                              // Write student data in file
            object_out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier de l'élève n'a pas été trouvé !");
        } catch (IOException e) {
            System.out.println("Un problème est survenu lors de la sauvegarde des données de " + other_student.name + " !");
        }
    }

    public static boolean load_student_data(String name) {
        try {
            FileInputStream file_in = new FileInputStream(Data.saves_students_path + "/" + name + ".txt");
            ObjectInputStream object_in = new ObjectInputStream(file_in);
            player = (Student) object_in.readObject();                         // Load file data into object
            System.out.println("Votre profil a été chargé avec succès !");
            System.out.println(player);
            object_in.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Cet élève n'est pas enregistré !");
        } catch (ClassNotFoundException e) {
            System.out.println("La classe de l'élève n'a pas été trouvé !");
        } catch (IOException e) {
            System.out.println("Un problème est survenu lors du chargement du profil de l'élève !");
        }
        return false;
    }

    public static boolean save_student_data() {
        File file = new File(Data.saves_students_path);
        if (!file.exists()) {                                                   // Try to create student file if doesn't exist
            if (file.mkdirs()) System.out.println("Création automatique du dossier de l'élève...");
            else System.out.println("Erreur lors de la création automatique du dossier de l'élève.");
        }

        System.out.println("Sauvegarde...");
        try {
            FileOutputStream file_out = new FileOutputStream(Data.saves_students_path + "/" + player.name + ".txt");
            ObjectOutputStream object_out = new ObjectOutputStream(file_out);
            object_out.writeObject(player);                                    // Write student data in file
            object_out.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier de l'élève n'a pas été trouvé !");
        } catch (IOException e) {
            System.out.println("Un problème est survenu lors de la sauvegarde des données de l'élève !");
        }
        return false;
    }

    /**
     * Quit the game
     */
    public static void close_game() {
        if (save_student_data()) {
            System.out.println("Bonnes vacances, " + player.name + " !");
            System.exit(0);
        } else if (ask_yes_no_question("Voulez-vous quitter le jeu quand même ? (y/n) ")) System.exit(0);
    }
}
