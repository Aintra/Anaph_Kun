package AnResClasses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class ReadXMLFile {

    public NodeList readAll() throws  Exception {
        File fXmlFile = new File("C:\\Users\\Helena\\IdeaProjects\\AnResolverR\\src\\AnResClasses\\OM.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Sentence");

        return nList;
    }

    public void readAttributes(NodeList attrs, ArrayList<String> attributes){
        for (int k = 0; k < attrs.getLength(); k++) {
            if (attrs.item(k).getNodeType() == Node.ELEMENT_NODE){
                Node nNode = attrs.item(k);
                Element eElement = (Element)nNode;

                attributes.add(eElement.getTextContent());
            }
        }
    }

    public void readWords(NodeList wordsN, ArrayList<Word> words){
        for (int j = 0; j < wordsN.getLength(); j++) {
            ArrayList<String> attributes = new ArrayList<String>();

            if (wordsN.item(j).getNodeType() == Node.ELEMENT_NODE) {
                Node node = wordsN.item(j);
                Element el = (Element) node;
                NodeList attrs = el.getElementsByTagName("Attribute");

                readAttributes(attrs, attributes);

                words.add(new Word(el.getAttribute("Form"), el.getAttribute("Norm"), el.getAttribute("POS"), attributes));
            }
        }
    }

    public ArrayList<Sentence> readSentences(NodeList senList) {
        ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
        for (int i = 0; i < senList.getLength(); i++) {
            ArrayList<Word> words = new ArrayList<Word>();
            if (senList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node node_wl = senList.item(i);
                Element el_wl = (Element) node_wl;
                NodeList wordsN = el_wl.getElementsByTagName("Word");

                readWords(wordsN, words);
                sentenceList.add(new Sentence(i, words));
            }
        }
        addInfo(senList, sentenceList);
        return sentenceList;
    }

    static public void addInfo(NodeList senList, ArrayList<Sentence> text) {
        for (int i = 0; i < senList.getLength(); i++) {
            if (senList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node node_s = senList.item(i);
                Element el_s = (Element) node_s;
                NodeList linkList = el_s.getElementsByTagName("Link");
                for (int j = 0; j < linkList.getLength(); j++) {
                    if (linkList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Node node_l = linkList.item(j);
                        Element el_l = (Element) node_l;

                        if (el_l.getAttribute("Sign").equals(">") || el_l.getAttribute("Sign").equals("<")) {
                            NodeList leftWord = el_l.getElementsByTagName("LeftWord");
                            for (int k = 0; k < leftWord.getLength(); k++) {
                                if (leftWord.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                    Node node_lw = leftWord.item(k);
                                    Element el_lw = (Element) node_lw;
                                    String lw = el_lw.getAttribute("Form");
                                    String lw_pos = el_lw.getAttribute("POS");

                                    NodeList rightWord = el_l.getElementsByTagName("RightWord");
                                    for (int m = 0; m < rightWord.getLength(); m++) {
                                        if (rightWord.item(m).getNodeType() == Node.ELEMENT_NODE) {
                                            Node node_rw = rightWord.item(m);
                                            Element el_rw = (Element) node_rw;
                                            String rw = el_rw.getAttribute("Form");
                                            String rw_pos = el_rw.getAttribute("POS");

                                            int left_ind = 0;
                                            int right_ind = 0;

                                            for (int l = text.get(i).getWordList().size() - 1; l >= 0; l--) {
                                                if (text.get(i).getWordList().get(l).compare(lw, lw_pos)) {
                                                    if (text.get(i).getWordList().get(l).getRole() != null) {
                                                        if (!text.get(i).getWordList().get(l).getRole().equals("fin_es")) {
                                                            text.get(i).getWordList().get(l).setChanges(el_l.getAttribute("LeftTag"), "LeftTag");
                                                            left_ind = l;
                                                        }
                                                    } else {
                                                        text.get(i).getWordList().get(l).setChanges(el_l.getAttribute("LeftTag"), "LeftTag");
                                                        left_ind = l;
                                                    }
                                                } else {

                                                    if (text.get(i).getWordList().get(l).compare(rw, rw_pos)) {
                                                        if (text.get(i).getWordList().get(l).getRole() != null) {
                                                            if (!text.get(i).getWordList().get(l).getRole().equals("fin_es")) {
                                                                text.get(i).getWordList().get(l).setChanges(el_l.getAttribute("RightTag"), "RightTag");
                                                                right_ind = l;
                                                            }
                                                        } else {
                                                            text.get(i).getWordList().get(l).setChanges(el_l.getAttribute("RightTag"), "RightTag");
                                                            right_ind = l;
                                                        }
                                                    }
                                                }

                                            }

                                            if (el_l.getAttribute("Sign").equals(">"))
                                                text.get(i).getWordList().get(right_ind).setControllTo(text.get(i).getWordList().get(left_ind));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
