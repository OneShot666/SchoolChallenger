// import java.lang.*;
import java.io.Serializable;
import java.util.*;
// import java.awt.*;

public class Data implements Serializable {
    public final static String game_name = "School Challenger";
    public final static String version = "v0.1.6";
    private final static String director_name = "Nathan";
    private final static String password = "oneshot666";

    public final static String saves_students_path = "Saves/students";

    public final static String[] AnswersPositives = new String[] {"y", "yes", "yep", "o", "oui", "ouais", "je confirme",
        "absolument", "effectivement", "chocolate rain", "est-ce que c'est bon pour vous ?"};
    public final static String[] AnswersNegatives = new String[] {"n", "no", "nope", "non", "pas vraiment", "je ne veux pas"};

    public final static List<Quizz> AllQuizz = new ArrayList<>();
    public final static List<String> AllStudentsNames = new ArrayList<>();

    // TODO: Move "Défier un camarade" in ClassActions (change menu in main.java)
    public final static List<String> SchoolActions = Arrays.asList("Aller sur mon profil", "Faire un test",
        "Défier un camarade", "Aller en classe", "Voir les quizz", "Vocabulaire", "Anti-sèches", "Paramètres", "Quitter l'école");
    public final static List<String> ProfilActions = Arrays.asList("Modifier mon nom", "Voir mon profil",
        "Voir mes questions", "Voir mes tests", "Voir mes amis", "Voir mon carnet de notes", "Retour");
    public final static List<String> FriendsActions = Arrays.asList("Voir ma liste d'amis", "Ajouter un ami",
        "Retirer un ami", "Défier un ami", "Retour");
    public final static List<String> ClassActions = Arrays.asList("Voir mes camarades", "Chercher un camarade",
        "Chercher un élève", "Défier un camarade", "Retour");
    public final static List<String> QuizzActions = Arrays.asList("Faire un test", "Voir tous les quizz",
        "Voir les catégories", "Voir difficultés", "Retour");
    public final static List<String> Vocabulary = Arrays.asList("Ecole : Le lieu où vous pouvez faire toutes les actions du jeu",
        "Classe : Le lieu où vos camarades présents aujourd'hui se réunissent", "Directeur : Il n'y en a qu'un, et il a tous les droits. " +
        "Surveillez votre comportement en sa présence", "Elève : Un élève de l'école", "Camarade : Un élève de votre classe",
        "Ami.e : Un élève que vous avez ajouter à votre liste d'amis", "Note : Votre score lors d'un test. Modifie votre moyenne." +
        "Augmente vos points de vie", "Vie : Indique votre force en tant de bon élève. Ne peux plus défier d'élève si tombe à zéro",
        "Test : Un quiz que vous pouvez faire. Pas de limite d'essai", "Quiz : Un simple qcm. Occroie une note");
    public final static List<String> Helps = Arrays.asList(game_name + " est un jeu basé sur le parcours scolaire.",
        "En tant qu'élève, vous pouvez défier les autres élèves de votre classe (proches de vous).",
        "Faites des contrôles pour augmenter votre santé et déverrouiller de nouvelles questions.",
        "Si vos points de vie tombent à zéro, vous ne pourrez plus défier d'autres élèves.",
        "Réussissez au mieux les contrôles pour avoir de meileures notes.",
        "Chaque note augmente votre santé de sa valeur et modifie votre moyenne.",
        "Bonne chance, l'école de " + game_name + " compte sur vous !");

    public final static int min_name_size = 3;
    public final static int max_name_size = 20;
    public final static List<String> ForbiddenNames = Arrays.asList("", " ", "caca", "pipi", "zeub", "teub", "verge",
        "penis", "vagin", "uretre", "anus", "sexe", "nazi", "hitler", "pedophile", "zoophile");
    public final static List<Character> ForbiddenLetters = Arrays.asList('à', 'â', 'ä', 'é', 'è', 'ê', 'ë', 'î', 'ï',
        'ô', 'ö', 'ù', 'û', 'ü');
    public final static List<Character> ForbiddenChar = Arrays.asList('/', '\\', '#', '"', '\'');

    public final static List<String> CommentsForbiddenNames = Arrays.asList("Vous êtes bien vulgaire...",
        "Veuillez tenir votre langue !", "Ceci n'est pas un nom correct !", "Arretez les enfants de faire de la merde.");
    public final static List<String> CommentsForbiddenLetters = Arrays.asList("Lettres interdites détectées !",
        "Certaines lettres ne sont pas dignes d'être des notres...", "Veuillez retirer les caractères impis !",
        "Il serait temps de trouver un emploi stable.");
    public final static List<String> CommentsForbiddenChar = Arrays.asList("Caractères interdits détectées !",
        "Il semblerait que vos mains aient glissées.", "Veuillez retirer les ponctuations impies !",
        "Hashtag : Pas ma ponctuation !");
    public final static List<String> CommentsGoodNotes = Arrays.asList("Dis donc, vous êtes fort !",
        "Vous êtes toujours aussi bon élève ?", "Vous êtes sûr que vous avez eu cette note ?",
        "Vos parents doivent être très fiers de vous.");
    public final static List<String> CommentsBadNotes = Arrays.asList("Dommage ça...",
        "Aïe... Ça doit plomber votre moyenne ça.", "Un coup de malchance, assurément.",
        "Vous ferez mieux la prochaine fois.");

    public final static List<String> Difficulties = Arrays.asList("facile", "moyenne", "intermédiaire", "difficile", "extrême");
    public final static List<String> Grades = Arrays.asList("novice", "apprenti.e", "bonne élève", "élève modèle",
        "délégué.e", "professeur.e", "directeur", "expert.e");

    public final static List<String> Domains = Arrays.asList("informatique", "mathématiques", "physique-chimie", "svt",
        "technologie", "anglais", "français", "histoire", "géographie", "culture générale");

    public final static List<String> Weekdays = Arrays.asList("lundi", "mardi", "mercredi", "jeudi", "vendredi",
        "samedi", "dimanche");

    public static String get_director_name() {
        return director_name;
    }

    public static String get_password() {
        return password;
    }
}
