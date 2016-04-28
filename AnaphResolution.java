
package anaphkun;

/**
 *
 * @author Alexandra
 */
import java.io.*;
import java.util.ArrayList;
public class AnaphResolution {
public static ArrayList<String> readDict(String fileName) throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }

        try {
            BufferedReader fin = new BufferedReader(new FileReader(file));
            String line;

            while ((line = fin.readLine())!=null)
                list.add(line);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    /**
     * @param args the command line arguments
     */
   public static void main(String[] args){
        try {
            ArrayList<String> dict = readDict("C:\\Users\\Alexandra\\IdeaProjects\\AnaphR\\src\\AnaphoraRes\\pronouns.txt"); //словарик
            ArrayList<String> parenthesis = readDict("C:\\Users\\Alexandra\\IdeaProjects\\AnaphR\\src\\AnaphoraRes\\parenthesis.txt");
            ArrayList<String> subordUn = readDict("C:\\Users\\Alexandra\\IdeaProjects\\AnaphR\\src\\AnaphoraRes\\subordinateUnions.txt");


            ReadXMLFile r = new ReadXMLFile();

            Text text = new Text(r.readSentences(r.readAll()));
            text.setSkas();
            text.setFins();
            text.neutralizePlugins();
            text.deterParenthesis(parenthesis);
            text.addTypeOfSent(subordUn);

            //text.resolveAnaphor(dict);


            for (int i = 0; i < text.getSentenceList().size(); i++) {
                for (int j = 0; j < text.getSentenceList().get(i).getWordList().size(); j++) {

                    System.out.print(text.getSentenceList().get(i).getWordList().get(j).getForm()+"("+text.getSentenceList().get(i).getWordList().get(j).getRole()+") ");   //выводиим предложение и его индекс на экран
                    /*if (text.get(i).getWordList().get(j).getIsControlling()!=null)
                        System.out.print("["+ text.get(i).getWordList().get(j).getIsControlling().getNorm()+"] ");*/
                }
                System.out.print("["+text.getSentenceList().get(i).getType()+"] ");
            }

        } catch (Exception e)
        {e.printStackTrace();}
    }
    
}
