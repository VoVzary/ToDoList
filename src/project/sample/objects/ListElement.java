package project.sample.objects;



/**

 * Created by VZyatkin on 19.11.2017.

 */

public class ListElement {



    private String element;

    private String check;





    public ListElement(String element, String check) {

        this.element = element;

        this.check = check;



    }





    public String getCheck() {

        return check;

    }



    public void setCheck(String check) {

        this.check = check;

    }



    public String getElement() {

        return element;

    }



    public void setElement(String element) {

        this.element = element;

    }





    @Override

    public String toString() {

        return "ListElement{" +

                "element='" + element + '\'' +

                '}';

    }



}





