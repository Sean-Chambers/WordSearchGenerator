//WordSearchGenerator is a project by Sean Chambers
//WordSearchGenerator prints out the resulting word search from the input of several words
//The word search is scaled up and down based on the number of words that are accepted. 


//imports
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class WordSearchGenerator {
    //Fields
    private static char[][] solution;
    private static char[][] wordSearch;
    private static int size;

    //Scanner is initialized to be used by multiple methods
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        //constant that scales the size of the word search
        final double scale = 1.5;
        
        //variable fields
        Scanner scan = new Scanner(System.in);
        String input;
        ArrayList<String> list;
        boolean loop = true;
        boolean generated = false;

        //do while loop, controls the main menu functions
        do{
            printIntro();
            input = scan.nextLine();
            switch(input.toLowerCase()){
                //generates the word search after accepting list of words
                case "g":
                    list = acceptWords();
                    //list is organized largest to smallest, size based on the length of the longest and a constant scaler
                    size = (int) (list.get(0).length() * scale);
                    solution = new char[size][size];
                    wordSearch = new char[size][size];
                    generate(list);
                    System.out.print("Word Search has been generated!");
                    clearOnAcknowledge();
                    generated = true;
                    break;
                //calls the print method to print the word search 
                case "p":
                    if(generated){
                        print();
                        clearOnAcknowledge();
                    } else{
                        System.out.println("Please generate a word search first!");
                        clearOnAcknowledge();
                    }
                    break;
                //calls the showSolution method to print the word search with the solution visable
                case "s":
                    if(generated){
                        showSolution();
                        clearOnAcknowledge();
                    } else{
                        System.out.println("Please generate a word search first!");
                        clearOnAcknowledge();
                    }
                    break;
                case "q":
                    //this will close the loop as the while condition is evaluated false
                    loop = false;
                    continue;
                default:
                    System.out.print("Sorry input is not recognized...");
                    clearOnAcknowledge();
            }
        }while(loop);
        scan.close();
    }

    //prints a quick intro for the user
    private static void printIntro(){
        System.out.println("Welcome to my word search generator!");
        System.out.println("This program will let you generate your own word search puzzle.");
        System.out.println("Please select an option:");
        System.out.println("Generate a new word search (g)");
        System.out.println("Print you word search (p)");
        System.out.println("Show the solution to your word search (s)");
        System.out.print("Quit the program (q)  ");
    }

    //generates the word search, first fills the solution array
    //then copies solution to word search and fills in blanks
    private static void generate(ArrayList<String> wordList){
        //Random object to get random numbers
        Random ran = new Random();
        //adds each word to solution array
        for(String word : wordList){
            addWord(word);
        }
        //i is the iterator for each row
        for(int i = 0; i < size; i++){
            //j is the iterator for each collumn
            for(int j = 0; j < size; j++){
                //adds random character if no character is present 
                if(wordSearch[j][i] > 'Z' || wordSearch[j][i] < 'A'){
                    wordSearch[j][i] = (char)(ran.nextInt(26) + 'A');
                }
            }
        }
    }

    //prompts user for number of words in word search
    //then accepts words until word count reached
    private static ArrayList<String> acceptWords(){
        System.out.print("How many words would you like to add? ");
        int max = scan.nextInt();
        ArrayList<String> words = new ArrayList<String>(max);
        //clears out the scanner
        String input = scan.nextLine();
        for(int i = 0; i < max; i++){
            System.out.print("Please input a word: ");
            input = scan.nextLine();
            words = addSorted(words,input);
        }
        return words;
    }

    //attempts 100 times to add a word to solution array
    private static void addWord(String word){
        //Random object to get random numbers
        Random ran = new Random();
        //location variables 
        int dir;
        int row;
        int col;
        for(int i = 0 ; i < 100; i++){
            //selects a direction
            dir = ran.nextInt(3);
            switch(dir){
                //this case is when the word is downward
                case 0: 
                    //prevents word from starting where it will run out of space, random is constrained
                    row = ran.nextInt(size - word.length());
                    col = ran.nextInt(size);
                    if(checkSpace(dir,row,col,word)){
                        //loops through ever char in the word
                        for(char c : word.toCharArray()){
                            //adds char to spot on array and iterates for next loop
                            wordSearch[col][row] = Character.toUpperCase(c);
                            solution[col][row++] = Character.toUpperCase(c);
                        }
                        //stops trying once word has been added
                        return;
                    }
                    break;
                //this case the word is left to right
                case 1: 
                    row = ran.nextInt(size);
                    col = ran.nextInt(size - word.length());
                    if(checkSpace(dir,row,col,word)){
                        for(char c : word.toCharArray()){
                            wordSearch[col][row] = Character.toUpperCase(c);
                            solution[col++][row] = Character.toUpperCase(c);
                        }
                        return;
                    }
                    break;
                //this case the word is diagonal
                case 2:  
                    //this constrains the row so it can be be no lower than needed if the word is on the longest diagonal
                    row = ran.nextInt(size - word.length() + 1);
                    col = ran.nextInt(size - word.length() + 1);
                    if(checkSpace(dir,row,col,word)){
                        for(char c : word.toCharArray()){
                            wordSearch[col][row] = Character.toUpperCase(c);
                            solution[col++][row++] = Character.toUpperCase(c);
                        }
                        return;
                    }
                    break;
                //if dir fails to become a valid direction continue for loop and retry
                default:
                    continue;
            }
        }
    }

    //checks to see if placement is valid by comparing attempted placement to current values.
    private static boolean checkSpace(int direction,int row, int col, String word){
        switch(direction){
            //this direction is downward
            case 0:
                //loops through ever char in the - 1 
                for(char c : word.toCharArray()){
                    char currentChar = solution[col][row++];
                    //checks if current value in array is the same as what letter we want to put there
                    if(currentChar == Character.toUpperCase(c)){
                        continue;
                    //checks if current value in array is empty space
                    } else if(currentChar < 'A' || currentChar > 'Z'){
                        continue;
                    //returns false if current character is a placed value that is not the same as what we want
                    } else {
                        return false;
                    }
                }
                return true;
            //this direction is left to right
            case 1:
                for(char c : word.toCharArray()){
                    char currentChar = solution[col++][row];
                    if(currentChar == Character.toUpperCase(c)){
                        continue;
                    } else if(currentChar < 'A' || currentChar > 'Z'){
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            //this direction is diagonal 
            case 2:
                for(char c : word.toCharArray()){
                    char currentChar = solution[col++][row++];
                    if(currentChar == Character.toUpperCase(c)){
                        continue;
                    } else if(currentChar < 'A' || currentChar > 'Z'){
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    //Prints the word search to the console
    private static void print(){
        //i is the iterator for each row
        for(int i = 0; i < size; i++){
            //j is the iterator for each collumn
            for(int j = 0; j < size; j++){
                //i keeps the row the same as each item in the row is printed for each inner for loop
                System.out.print("[" + wordSearch[j][i] + "]");
            }
            System.out.println("");
        }
    }

    //prints the solution to the console
    private static void showSolution(){
        //i is the iterator for each row
        for(int i = 0; i < size; i++){
            //j is the iterator for each collumn
            for(int j = 0; j < size; j++){
                //prints character if it is part of solution else prints "[x]"
                if(solution[j][i] <= 'Z' && solution[j][i] >= 'A'){
                    //prints character in red tex so it is easy to identify 
                    System.out.print("[" + "\u001B[31m" + solution[j][i] + "\u001B[37m" + "]");
                } else {
                    System.out.print("[X]");
                }
            }
            System.out.println("");
        }
    }

    //sorts a word into an already sorted array, words are sorted longest to shortest
    //as it is easier to place short words in a full array than long words
    private static ArrayList<String> addSorted(ArrayList<String> list, String word){
        //on first word being added for loop wont trigger
        if(list.isEmpty()){
            list.add(word);
        } else {
            //initalize variable so the size doesnt increase when adding words, causing infinite loop
            int listSize = list.size();
            for(int i = 0; i < listSize; i++){
                //assumes every word before current element is longer than current element
                if(list.get(i).length() < word.length()){
                    //adds element inplace, shifts smaller words down one index
                    list.add(i, word);
                    break;
                } else if(i < list.size()){
                    list.add(word);
                    break;
                }
            }
        }
        return list;
    }

    //clears the screen once enter has been pressed.
    private static void clearOnAcknowledge(){
        scan.nextLine();
        for(int i = 0; i < 15; i++){
            System.out.println("");
        }
    }
}
