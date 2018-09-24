package project.sample.objects;



import javax.xml.bind.annotation.*;

import java.util.List;



/**

 * Created by VZyatkin on 23.12.2017.

 */



//Вспомогательный класс для обёртывания списка данных.

//Используется для сохранения списка данных в XML



@XmlRootElement(name = "test")



public class XmlWrapper {



    private String check;



    private String item;



    @XmlElement

    public void setItem(String item){

        this.item = item;

    }



    @XmlAttribute (name = "check")

    public void setCheck(String check) {

        this.check = check;

    }



    public String getItem(){

        return item;

    }



    public String getCheck() {

        return check;

    }







    @Override

    public String toString(){

        return item + " " + check;

    }

}

