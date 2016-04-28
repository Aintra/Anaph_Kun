package AnResClasses;

import java.util.ArrayList;

/**
 * Created by Helena on 12.03.2015.
 */
public class Sentence {
    private int type=0; //0 - simple sentence
    private int index;
    private ArrayList<Word> wordList = new ArrayList<Word>();

    public Sentence(int index, ArrayList<Word> wordList){
        this.index = index;
        this.type = type;
        for (int i = 0; i < wordList.size(); i++) {
            this.wordList.add(wordList.get(i));
        }
    }

    public ArrayList<Word> getWordList(){
        return wordList;
    }

    public int getType(){
        return type;
    }

    public int getIndex(){
        return index;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int isSimple(ArrayList<String> subDict) {   // определить вид предложения

        ArrayList<Word> fins = new ArrayList<Word>();
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getRole()!=null){
                if (this.getWordList().get(i).getRole().equals("fin_es"))
                    fins.add(this.getWordList().get(i));
                if (this.getWordList().get(i).getRole().equals("chto_es"))
                    return 1;
            }
            else continue;
        }

        int count = 0;
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getRole()!=null)
            if (this.getWordList().get(i).getRole().equals("skas_es"))
                count++;
        }

        if (count>1)
            return 1;

        if (this.hasSubUnions(subDict))
            return 1;

        for (int i = 1; i < fins.size(); i++) {
            if (fins.get(0).getAttrList().size()==fins.get(i).getAttrList().size()){
                for (int j = 0; j < fins.get(i).getAttrList().size(); j++) {
                    if (!fins.get(0).getAttrList().get(j).equals(fins.get(i).getAttrList().get(j)))
                        return 1;
                }

            }
            else return 1;
        }
        return 0;
    }

    public boolean hasSubUnions(ArrayList<String> subDict) {  //выявить подчиненные союзы, если они есть,предлодение - сложное
        int count = 0;
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getForm().equals(","))
                count++;
        }

        if (count > 0) {

            ArrayList<String> list1 = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();

            ArrayList<Integer> listComma1 = new ArrayList<Integer>();
            String s = "", s1 = "";
            int l = 0;

            for (int i = 0; i < this.getWordList().size(); i++) {
                if (this.getWordList().get(i).getForm().equals(",")) {
                    listComma1.add(i);
                }
            }

            for (int i = 0; i < 5; i++) {
                list2.add(this.getWordList().get(i).getForm());
            }

            for (int j = 0; j < listComma1.size(); j++) {
                if (l!=5) {
                    list1.add(this.getWordList().get(listComma1.get(j) + 1).getForm());
                    l++;
                }
                if (l==5){
                    int m = 0, n=0;
                    for (int i = 0; i < subDict.size(); i++) {
                        String[] sub_mas = subDict.get(i).split(" ");
                        for (j = 0; j < sub_mas.length; j++) {
                            if (sub_mas[j].equals(list1.get(j)))
                                m++;
                            if (sub_mas[j].equals(list2.get(j)))
                                n++;
                            if (m == sub_mas.length && n == 0)
                                return true;
                            if (n == sub_mas.length && m == 0)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setSkas() {   //дополнить проплешины дварфа, установить роль сазуемого, где оно есть
        int skas=0;
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getForm().equals("-"))
                if (i+1<this.getWordList().size()) {
                    if (this.getWordList().get(i + 1).getPOS().equals("4") || this.getWordList().get(i + 1).getPOS().equals("3"))
                        this.getWordList().get(i + 1).setRole("skas_es");
                }

            if (this.getWordList().get(i).getRole()!=null)
            if (this.getWordList().get(i).getRole().equals("skas_es"))
                skas++;

            if (skas>0) {
                if (this.getWordList().get(i).getForm().equals("-"))
                    if (i-2>=0) {
                        if (this.getWordList().get(i - 1).getRole().equals("inf_es") || this.getWordList().get(i - 1).getRole().equals("noun_es") || this.getWordList().get(i - 1).getRole().equals("subj_es") || this.getWordList().get(i - 2).getRole().equals("inf_es") || this.getWordList().get(i - 2).getRole().equals("noun_es") || this.getWordList().get(i - 2).getRole().equals("subj_es"))
                        this.getWordList().get(i).setRole("skas_es");
                }
            }
        }
    }

    public void setFins() {    // заполнить проплешины дварфа, установить финитность глаголов, где она есть
        int fin=0;
        for (int i = 0; i < this.getWordList().size(); i++) {
            for (int j = 0; j < this.getWordList().get(i).getAttrList().size(); j++) {
                if (this.getWordList().get(i).getAttrList().get(j).equals("31"))
                    this.getWordList().get(i).setRole("fin_es");
            }

            if (this.getWordList().get(i).getRole()!=null)
                if (this.getWordList().get(i).getRole().equals("fin_es"))
                fin++;

            if (fin>0) {
                if (this.getWordList().get(i).getForm().equals("-"))
                    if (i-2>=0) {
                    if (this.getWordList().get(i - 1).getRole().equals("inf_es") || this.getWordList().get(i - 1).getRole().equals("noun_es") || this.getWordList().get(i - 1).getRole().equals("subj_es") || this.getWordList().get(i - 2).getRole().equals("inf_es") || this.getWordList().get(i - 2).getRole().equals("noun_es") || this.getWordList().get(i - 2).getRole().equals("subj_es"))
                        this.getWordList().get(i).setRole("fin_es");
                    }
            }
        }
    }

    public void neutralizePlugins() {     //нейтрализация вводных конструкций( не могут участвовать в межфраз. связи)
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getForm().equals("("))
                for (int j = i; j < this.getWordList().size(); j++) {
                    if (!this.getWordList().get(j).getForm().equals(")"))
                        this.getWordList().get(j).setRole(null);
                    else {
                        this.getWordList().get(j).setRole(null);
                        break;
                    }
                }
        }
    }

    public void deterParenthesis(ArrayList<String> parenDict) {   //выявление вводных слов
        ArrayList<Integer> listComma = new ArrayList<Integer>();
        for (int i = 0; i < this.getWordList().size(); i++) {
            if (this.getWordList().get(i).getForm().equals(","))
                listComma.add(i);
        }

        if (!listComma.isEmpty()) {
            int count = 0;
            if (listComma.get(0) < 6) {    // в начале предложения
                for (int i = 0; i < parenDict.size(); i++) {
                    String[] parMas = parenDict.get(i).split(" ");
                    if (parMas.length == listComma.get(0))
                        for (int m = 0; m < listComma.get(0); m++) {
                            if (this.getWordList().get(m).getForm().equals(parMas[m]))
                                count++;
                        }

                    if (count == parMas.length) {
                        for (int k = 0; k < listComma.get(0); k++) {
                            this.getWordList().get(k).setRole("misc_es");
                        }
                        break;
                    }
                }
            }
            if (listComma.size()>1)
                for (int i = 0; i <parenDict.size(); i++) {
                    String[] parMas = parenDict.get(i).split(" ");
                    for (int k = 0; k < listComma.size(); k++) {
                        if (k + 1 < listComma.size())
                            if ((listComma.get(k + 1) - listComma.get(k) < 6) && (listComma.get(k + 1) - listComma.get(k) - 1) == parMas.length) {
                                count = 0;
                                for (int l = listComma.get(k) + 1; l < listComma.get(k + 1); l++) {
                                    if (count <= parMas.length)
                                        if (this.getWordList().get(l).getForm().equals(parMas[count]))
                                            count++;
                                }
                                if (count == parMas.length)
                                    for (int l = listComma.get(k) + 1; l < listComma.get(k + 1); l++) {
                                        this.getWordList().get(l).setRole("misc_es");
                                    }
                            }
                    }
                }
        }
    }

    /*public Anaphor findAnaphor() {


    когда находим анафору, создаем объект анафоры и аозвращаем его в метод resolveAnaphor
    }

    public Referent findReferent(Anaphor anaphor) {
    this.findNP()   // ищем именную группу, если референт не ограничивается словом,
                    // а тут шаманим, определяя, является ли ИГ(NP-noun phrase) референтом.

    когда нафодим референт, возвращаем объект референта в метод resolveAnaphor
    }*/

    public ArrayList<String> findNP() {   // находим именную группу  - этим методом я займусь сегодня
        ArrayList<String> list = new ArrayList<String>();



        return  list;
    }
}



