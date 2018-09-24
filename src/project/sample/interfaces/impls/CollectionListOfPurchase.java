package project.sample.interfaces.impls;



import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.scene.text.Text;

import org.jdom2.Attribute;

import org.jdom2.Document;

import org.jdom2.Element;

import org.jdom2.JDOMException;

import org.jdom2.input.SAXBuilder;

import org.jdom2.output.Format;

import org.jdom2.output.XMLOutputter;

import project.sample.controllers.MainController;

import project.sample.interfaces.ListOfPurchase;

import project.sample.objects.ListElement;



import java.io.File;

import java.io.FileWriter;

import java.io.IOException;



//import java.util.ArrayList;



/**

 * Created by VZyatkin on 29.11.2017.

 */

public class CollectionListOfPurchase implements ListOfPurchase {







    private ObservableList<ListElement> listElementList = FXCollections.observableArrayList();



    public static int rowNum;

    private static String s;



    public static String getS() {

        return s;

    }



    public ObservableList <ListElement> getListElementList() {

        return listElementList;

    }



    @Override

    public void add(ListElement listElement) {

        listElementList.add(listElement);

    }



    @Override

    public void update(ListElement listElement) {

    }



    @Override

    public void delete(ListElement listElement) {

        listElementList.remove(listElement);

    }



    //заполнение таблицы в программе

    public void fillTestData(){



        SAXBuilder builder = new SAXBuilder();

        File file = new File(MainController.getOpenFile());

        Text text = new Text();

        try {

            Document document = builder.build(file);

            Element rootElement = document.getRootElement();

            XMLOutputter outputter = new XMLOutputter();

            outputter.output(document, System.out);

            for (int i = 0; i<document.getRootElement().getChild("items").getChildren().size(); i++){

                listElementList.add(new ListElement(rootElement.getChild("items").getChildren().get(i).getText(),

                        rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue()));

                if (rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue().equals("yes")){

                    s = rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue();

                    rowNum = i;



                }

                /*if (rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue().equals("yes")){

                    text.setText(rootElement.getChild("items").getChildren().get(i).getText());

                    text.setStrikethrough(true);

                    listElementList.add(new ListElement(text.getText(),

                            rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue()));

                }else {

                listElementList.add(new ListElement(rootElement.getChild("items").getChildren().get(i).getText(),

                        rootElement.getChild("items").getChildren().get(i).getAttribute("check").getValue()));

                }*/



            }



        } catch (JDOMException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }



    }



    public void writeToXml(){



        SAXBuilder builder = new SAXBuilder();

        File file = new File("C:/temp/test.xml");

        try {

            org.jdom2.Document document = builder.build(file);

            org.jdom2.Element rootElement = document.getRootElement();

            rootElement.getChild("items").addContent(new Element("item").addContent("hfghm").setAttribute(new Attribute("check", "yes")));



            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            FileWriter writer = new FileWriter("C:/temp/test.xml");

            outputter.output(document, writer);

            writer.close();

        } catch (JDOMException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }



    }



    public void readTheList(){

        for (int i=0; i<listElementList.size(); i++){

            System.out.println("Элемент списка " + listElementList.get(i).getElement());

        }



    }

}