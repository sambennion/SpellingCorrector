package spell;

import java.io.IOException;
import java.io.File;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;


public class SpellCorrector implements ISpellCorrector{
    LinkedList<StringBuilder> wordTries = new LinkedList<StringBuilder>();
    private INode bestWord;
    private String bestWordStr;
    boolean firstCheck;
    private final Trie trie = new Trie();
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            String str = scanner.next();
            trie.add(str);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        firstCheck = true; //used to make sure not to add to wordTries after first check
        inputWord = inputWord.toLowerCase();
        if(trie.find(inputWord) != null && trie.find(inputWord).getValue() > 0){
            return inputWord;
        }
        bestWordStr = "";
        bestWord = new Node();
        StringBuilder similarWord = new StringBuilder(inputWord);
        suggestSimilarWord_Helper(similarWord);
        if(bestWordStr == ""){
            firstCheck = false;
            for(StringBuilder word: wordTries){
                suggestSimilarWord_Helper(word);
            }
        }
        return bestWordStr != "" ? bestWordStr : null;
    }

    private void suggestSimilarWord_Helper(StringBuilder word){
        //Try changing every index with a different letter
        String originalWord = word.toString();
        for(char i = 'a'; i < 'z'+1; i++){ // I might need to swap these two for loops to be alphabetical
            for(int j = 0; j < word.length(); j++){
                word.setCharAt(j, i);
                tryWord(word);
                word = new StringBuilder(originalWord);
            }
        }
        //Try deletion
        for(int i = 0; i < word.length(); i++){
            word.deleteCharAt(i);
            tryWord(word);
            word = new StringBuilder(originalWord);
        }
        //Add chars at each index
        for(int i = 0; i <= word.length(); i++){
            for(char j = 'a'; j <= 'z'; j++){
                word.insert(i,j);
                tryWord(word);
                word = new StringBuilder(originalWord);
            }

        }
        //transpose word
        for(int i = 0; i < word.length() - 1; i++){
            char swapper = word.charAt(i);
            word.setCharAt(i, word.charAt(i+1));
            word.setCharAt(i+1, swapper);
            tryWord(word);
            word = new StringBuilder(originalWord);
        }

    }
    private void tryWord(StringBuilder word){
        if(trie.find(word.toString()) != null && trie.find(word.toString()).getValue() > 0){
            INode newWord = trie.find(word.toString());
            if(bestWord == null || bestWord.getValue() <= newWord.getValue()){
                bestWord = newWord;
                bestWordStr = word.toString();
            }
        }
        if(firstCheck){
            wordTries.add(word);
        }
    }
}
