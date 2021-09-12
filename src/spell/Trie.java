package spell;

public class Trie implements ITrie{

    private Node root = new Node();
    private int wordCount;
    private int nodeCount = 1;

    @Override
    public void add(String word) {
        word = word.toLowerCase();
        Node currentNode = root;
        char currentLetter;
        int index;
        for(int i = 0; i < word.length(); i++){
            currentLetter = word.charAt(i);
            index = currentLetter - 'a';
            if(currentNode.getChildren()[index] == null){
                //create node at index
                currentNode.getChildren()[index] = new Node();
                nodeCount++;
            }
            currentNode = (Node) currentNode.getChildren()[index];
            if(i == word.length() - 1){
                if(currentNode.getValue() == 0){
                    wordCount++;
                }
                currentNode.incrementValue();
            }
        }



    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        Node currentNode = root;
        char currentLetter;
        int index;
        for(int i = 0; i < word.length(); i++){
            currentLetter = word.charAt(i);
            index = currentLetter - 'a';
            if(currentNode.getChildren()[index] == null){
                return null;
            }
            currentNode = (Node) currentNode.getChildren()[index];
            if(i == word.length() - 1){
                if(currentNode.getValue() == 0){
                    return null;
                }
                else{
                    return currentNode;
                }
            }
        }
        return currentNode;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toString_Helper(root, curWord, output);
        return output.toString();
    }
    private void toString_Helper(INode n, StringBuilder curWord, StringBuilder output){
        if(n.getValue() > 0){
            //append node's word to output
            output.append(curWord.toString());
            output.append("\n");
        }
        for(int i = 0; i < n.getChildren().length; i++){
            INode child = n.getChildren()[i];
            if(child != null){
                char childLetter = (char) ('a' + i);
                curWord.append(childLetter);
                toString_Helper(child, curWord, output);
                //delete last char you added after recursion
                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if (o == this){
            return true;
        }
        if(o.getClass() == this.getClass()){
            Trie t = (Trie) o;
            if(t.getWordCount() != this.getWordCount() || t.getNodeCount() != this.getNodeCount()){
                return false;
            }
            return equals_Helper(this.root, t.root);
        }

        return false;
    }
    private boolean equals_Helper(INode n1, INode n2){
        if(n1.getValue() != n2.getValue()){
            return false;
        }
        for(int i = 0; i < n1.getChildren().length; i++){
            if(n1.getChildren()[i] != null && n2.getChildren()[i] != null){
                if(n1.getChildren()[i].getValue() == n2.getChildren()[i].getValue()){
                    //recursive check
                    boolean recurseEquals = equals_Helper(n1.getChildren()[i], n2.getChildren()[i]);
                    if(recurseEquals == false){
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else if(n1.getChildren()[i] == null && n2.getChildren()[i] == null){

            }
            else{
                return false;
            }
        }
        return true;

    }
    @Override
    public int hashCode(){
        int hCode = wordCount * nodeCount;
        for(int i = 0; i < root.getChildren().length; i++){
            if (root.getChildren()[i] != null){
                hCode *= i;
            }
        }
        return hCode;
    }
}

