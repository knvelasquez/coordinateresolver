package application.service;

public class MessageService {
    public String toString(String[] message) {
        StringBuilder result = new StringBuilder();

        for (String word : message) {
            if (!word.isEmpty()) {
                result.append(word).append(" ");
            }
        }
        //Remove extra trailing white space, if any
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
