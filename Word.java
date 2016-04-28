package AnResClasses;

import java.util.ArrayList;

/**
 * Created by Helena on 12.03.2015.
 */
public class Word {
    private String form;
    private String norm;
    private String POS;
    ArrayList<String> attrList = new ArrayList<String>(); // list of attributes
    private String position;
    private String role; //синтаксическая роль в предложении
    Word isControlling = null;

    public Word(String form, String norm, String POS, ArrayList<String> attrList){
        this.form = form;
        this.norm = norm;
        this.POS = POS;
        for (int i = 0; i < attrList.size(); i++) {
            this.attrList.add(attrList.get(i));
        }
    }

    public String getForm(){
        return form;
    }

    public String getNorm(){
        return norm;
    }

    public String getPOS(){
        return POS;
    }

    public ArrayList<String> getAttrList(){
        return attrList;
    }

    public String getPosition(){
        return position;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public Word getIsControlling(){
        return isControlling;
    }

    public void setChanges(String role, String position){
        this.role = role;
        this.position = position;
    }

    public void setControllTo(Word isControlling){
        this.isControlling = isControlling;
    }

    public boolean compare(String form, String POS){
        if (this.form.equals(form)) {
            if (this.POS.equals(POS))
                return true;
        }
        return false;
    }


}
