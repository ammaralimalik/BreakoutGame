import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
/**
 * Keeps track of player score
 *
 * @author Ammar Malik, Amal Thomas, Daniel Graham, Bailey Cross, Aimen HARIZI
 * @version Spring 2021
 */
public class Score {
    //The list of scores
    private ArrayList<String> highScores;

    //The main scanner to use
    Scanner inputFile;

    /**
     * Construct objects of class Score
     *
     */
    public Score() throws FileNotFoundException {
        highScores = new ArrayList<String>();
        inputFile = new Scanner(new File("highscores.txt"));
        readFile();
    }

    /**
     * Adds the current score to the list
     *
     * @param time the current score
     */
    public void addToList(int time) throws IOException {
        FileWriter writer = new FileWriter("highscores.txt", true);

        writer.append(setName() + "," + time + "\n");

        writer.close();

        highScores.add(setName() + "," + time);
        readFile();
    }

    /**
     * Prints out the current list
     * 
     * @return the string version of the current list
     */
    public String printList(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < highScores.size(); i++){
            str.append(highScores.get(i) + "\n");
        }

        return str.toString();
    }

    /**
     * Prints out the current list
     * 
     * @return the current player name (number of runs)
     */
    public String setName(){
        int numPlayers = highScores.size();
        return "Player #" + numPlayers;
    }

    /**
     * Reads the data from the file
     * 
     */
    public void readFile(){
        while(inputFile.hasNext()){
            highScores.add(inputFile.nextLine());
        }
    }

    /**
     * Prints out the current top 3 players
     * 
     * @return the current top 3 players from the list
     */
    public String leaderBoard(){
        if(highScores.size() > 2) {
            StringBuilder str = new StringBuilder();
            int temp;
            int highest = 0;
            int highest2 = 0;
            int highest3 = 0;
            int currHigh = 0;

            for (int i = 0; i < highScores.size(); i++) {
                temp = Integer.parseInt(highScores.get(i).substring(highScores.get(i).indexOf(",") + 1, highScores.get(i).length()));

                if(temp > currHigh){
                    currHigh = temp;
                    highest = i;
                }
            }

            currHigh = 0;
            for(int i = 0; i < highScores.size(); i++){
                temp = Integer.parseInt(highScores.get(i).substring(highScores.get(i).indexOf(",") + 1, highScores.get(i).length()));

                if(temp > currHigh && temp < Integer.parseInt(highScores.get(highest ).substring(highScores.get(highest).indexOf(",") + 1, highScores.get(highest).length()))){
                    currHigh = temp;
                    highest2 = i;
                }
            }

            currHigh = 0;

            for(int i = 0; i < highScores.size(); i++){
                temp = Integer.parseInt(highScores.get(i).substring(highScores.get(i).indexOf(",") + 1, highScores.get(i).length()));

                if(temp > currHigh && temp < Integer.parseInt(highScores.get(highest2).substring(highScores.get(highest2).indexOf(",") + 1, highScores.get(highest2).length()))){
                    currHigh = temp;
                    highest3 = i;
                }
            }

            str.append(highScores.get(highest) + " ");
            str.append(highScores.get(highest2) + " ");
            str.append(highScores.get(highest3) + " ");

            return str.toString();
        } else {
            return "Not enough players to show leaderboard";
        }
    }
}
