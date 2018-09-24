package project.sample.controllers;



import javafx.application.Platform;

import javafx.beans.value.ChangeListener;

import javafx.beans.value.ObservableValue;

import javafx.collections.ListChangeListener;

import javafx.event.EventHandler;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.input.*;

import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;

import javafx.stage.Modality;

import javafx.stage.Stage;

import javafx.util.Callback;

import org.jdom2.Attribute;

import org.jdom2.Document;

import org.jdom2.Element;

import org.jdom2.JDOMException;

import org.jdom2.input.SAXBuilder;

import org.jdom2.output.Format;

import org.jdom2.output.XMLOutputter;

import org.xml.sax.SAXException;

import project.sample.interfaces.impls.CollectionListOfPurchase;

import project.sample.objects.ListElement;

import javafx.event.ActionEvent;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;

import java.io.*;

import java.util.Optional;





public class MainController {

    private CollectionListOfPurchase listOfPurchase = new CollectionListOfPurchase();

    private Stage primaryStage; //ОШИБКА СО СЛУШАТЕЛЕМ КЛАВИАТУРЫ, ИЗ-ЗА ТОГО, ЧТО ОН НУЛЕВОЙ!!!

    @FXML
    private Button btnNew;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSaveAs;

    @FXML
    private Button btnEditElement;

    @FXML
    private Button btnNewElement;

    @FXML
    private Button btnDelElement;

    @FXML
    private AnchorPane lowerPanel;

    @FXML
    private HBox lowerBox;

    @FXML
    private TableView tblElements;

    @FXML
    private TableColumn <ListElement, String> check;

    @FXML
    private TableColumn <ListElement, String> columnElement;

    @FXML
    private VBox mainBox;

    private static boolean f = false;

    private boolean checkEdit = false; // флаг, что включено редактирование

    private boolean openCheck = false; // флаг, что был открыт файл

    private boolean saveAsFlag = false; //флаг, что в окне "Сохранить, как" нажато Ок

    private static String openFile;

    private boolean newFileCheck;

    private EventHandler handlerScene = new EventHandler <KeyEvent>() {

        KeyCombination keyCombinationNewFile = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);

        KeyCombination keyCombinationOpenFile = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);

        KeyCombination keyCombinationSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

        @Override

        public void handle(KeyEvent event) {

            if (keyCombinationNewFile.match(event)){

                if (newFileCheck == false && f == false && openCheck == false) {



                    newEmptyFile();

                    btnEditElement.setDisable(false);

                    startEdit();

                    emptyElement();

                    openCheck = false;

                    setF(false);

                    firstElementChoice();

                }

                else{

                    alert();

                    tblElements.edit(tblElements.getSelectionModel().getFocusedIndex(), columnElement);

                    firstElementChoice();



                }

            }

            if (keyCombinationOpenFile.match(event)){

                open();

                event.consume();

            }

            if (keyCombinationSave.match(event)){

                save();

                event.consume();

            }



        }



    };

    private EventHandler handlerEdit = new EventHandler<KeyEvent>() {

        KeyCombination keyCombinationNew = new KeyCodeCombination(KeyCode.PAGE_UP, KeyCombination.CONTROL_DOWN);

        KeyCombination keyCombinationDel = new KeyCodeCombination(KeyCode.PAGE_DOWN, KeyCombination.CONTROL_DOWN);

        @Override

        public void handle(KeyEvent event) {

            if (keyCombinationNew.match(event)){

                emptyElement();

                event.consume();

            }

            if (keyCombinationDel.match(event)){

                try {

                    listOfPurchase.delete(listOfPurchase.getListElementList().get(tblElements.getSelectionModel().getFocusedIndex()));

                } catch (ArrayIndexOutOfBoundsException e){}

                event.consume();

            }

        }

    };

    @FXML
    private void initialize() {



        columnElement.setCellValueFactory(new PropertyValueFactory<ListElement, String>("element"));

        check.setCellValueFactory(new PropertyValueFactory<ListElement, String>("check"));



        columnElement.getStyleClass().add("text-color1");



        Callback<TableView<ListElement>,TableRow<ListElement>> tableRowCallback = value -> {

            TableRow<ListElement> row = new TableRow<ListElement>() {

                @Override

                public void updateItem( ListElement item, boolean empty ) {

                    setStyle("");

                    super.updateItem( item, empty );

                    if (item == null || empty){

                        setStyle("");

                    } else if ( item.getCheck().equals("yes")){



                        getStyleClass().add("table-column1");

                    }

                }

            };

            return row;

        };



        tblElements.setRowFactory( tableRowCallback );



        tblElements.setPlaceholder(new Label("Нажмите кнопку 'Создать список' или 'Открыть список'"));// узнать, как выставить ковычки



        tblElements.setOnMouseClicked(event -> {



            if (!event.getTarget().toString().substring(111, 115).equals("null")){ //проверка, что непустая строка ПЕРЕДЕЛАТЬ

                if (event.getClickCount() == 2 ){



                    int row = tblElements.getSelectionModel().getFocusedIndex();



                    if ((listOfPurchase.getListElementList().get(row).getCheck()).equals("no")){

                        listOfPurchase.getListElementList().get(row).setCheck("yes");

                        setF(true);

                        tblElements.refresh(); //Работает, но шрифт не меняется!!!!!!!!!!!!!!!!!!!!!



                    } else{

                        listOfPurchase.getListElementList().get(row).setCheck("no");

                        tblElements.refresh();

                        setF(true);

                    }

                }

            }



            /*if (event.getClickCount() == 1){

                return;

            }*/

        });



        tooltip();



        //переопределение метода, чтобы не по интеру работало, а при потери фокуса !!!НЕ РАБОТАЕТ!!!

        tblElements.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override

            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (! newValue){

                    tblElements.setItems(listOfPurchase.getListElementList());

                }

            }

        });



        //индикатор, что в таблице изменения

        listOfPurchase.getListElementList().addListener(new ListChangeListener<ListElement>() {

            @Override

            public void onChanged(Change<? extends ListElement> c) {



                f = true;



                System.out.println("Изменения - " + f);



            }

        });





        tblElements.setItems(listOfPurchase.getListElementList());

        iHearKeyboardMainBoxOn(); // подключает слушатель к Stage

        btnEditElement.setDisable(true);

        forEditingXml();



    }

    public static boolean getF() {

        return f;

    }

    public static void setF(boolean b) {

        f = b;

    }

    public static String getOpenFile() {

        return openFile;

    }

    //Делаем ячейки редактируемыми
    public void setRowEditable(){

        //columnElement.setCellValueFactory(new PropertyValueFactory<>("Элемент списка")); // Зачем это?

        columnElement.setCellFactory(TextFieldTableCell.<ListElement> forTableColumn());



        //tblElements.setEditable(false); //редактирование отключается!!!! Ура!



        //Зачем это?

        /*columnElement.setOnEditCommit((TableColumn.CellEditEvent<ListElement, String> event) ->{

            TablePosition<ListElement, String> pos = event.getTablePosition();

            String newColumnElement = event.getNewValue();



            int row = pos.getRow();

            ListElement listElement = event.getTableView().getItems().get(row);

            listElement.setElement(newColumnElement);

        });*/

    }

    public void setPrimaryStage(Stage primaryStage){

        this.primaryStage = primaryStage;

    }

    @FXML
    public void hndSaveAsFile() {

        procSaveAs();

    }

    @FXML
    private void hndOpenFile(){



        if (f==false){

            onOffLowerPanel(false);

            open();

        } else{

            alert();

            newEmptyFile();

            onOffLowerPanel(false);

            endEdit();

            open();

        }



    }

    @FXML
    private void hndNewFile(ActionEvent actionEvent) {



        if (newFileCheck == false && f == false && openCheck == false) {



            newEmptyFile();

            btnEditElement.setDisable(false);

            startEdit();

            emptyElement();

            openCheck = false;

            setF(false);

            firstElementChoice();

        }

        else{

            alert();

            tblElements.edit(tblElements.getSelectionModel().getFocusedIndex(), columnElement);

            firstElementChoice();

        }



    }

    private void tooltip(){



        btnNew.setTooltip(new Tooltip("Новый список"));

        btnOpen.setTooltip(new Tooltip("Открыть список"));

        btnSaveAs.setTooltip(new Tooltip("Сохранить как..."));

        btnSave.setTooltip(new Tooltip("Сохранить"));

        btnEditElement.setTooltip(new Tooltip("Редактировать"));

        btnNewElement.setTooltip(new Tooltip("Новый элемент"));

        btnDelElement.setTooltip(new Tooltip("Удалить элемент"));



    }

    private void open(){



        try {

            FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения

            fileChooser.setTitle("Открытие файла");

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML", "*.xml");//расширения

            fileChooser.getExtensionFilters().add(extensionFilter);

            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) { //это чтобы не выпадал эксепшн, если не выбрали файл

                openFile = file.getAbsolutePath();

                listOfPurchase.getListElementList().clear();

                listOfPurchase.fillTestData();

                openCheck = true;

                newFileCheck = false;

                btnEditElement.setDisable(false);

                setF(false);

            }

        } catch (NullPointerException e) {

            e.printStackTrace();

        } catch (RuntimeException e) {

            e.printStackTrace();



        }

    }

    public void delElement(ActionEvent actionEvent){



        Object source = actionEvent.getSource();



        if (source instanceof Button){

            try {

                listOfPurchase.delete(listOfPurchase.getListElementList().get(tblElements.getSelectionModel().getFocusedIndex()));

            } catch (ArrayIndexOutOfBoundsException e){}



        }





    }

    public void editElement(){



        if (checkEdit == false) {





            startEdit();



        } else {



            endEdit();



        }



    }

    public void save(){



        if (newFileCheck == false){

            procSave();

            newFileCheck = false;

            setF(false);

            openCheck = false;



        } else {

            newFileCheck = false;

            setF(false);

            openCheck = false;

            procSaveAs();



        }



    }

    public void onOffLowerPanel(boolean b){

        //включаем/отключаем нижнюю панель

        lowerPanel.setVisible(b); //делаем видимой нижнюю панель (потом переделать и расположить вверху)

        lowerBox.setVisible(b); //делаем видимой нижний бокс (потом переделать и расположить вверху)

    }

    public void btnDisabled(boolean b){

        btnNew.setDisable(b); // сделать отдельный метод

        btnOpen.setDisable(b);

        btnSave.setDisable(b);

        btnSaveAs.setDisable(b);

    }

    //создаём возможность задавать стилизацию отдельной ячейке в колонке Elements
    public void columnElementCellFactory(){

        columnElement.setCellFactory(new Callback<TableColumn<ListElement, String>, TableCell<ListElement, String>>() {

            @Override

            public TableCell<ListElement, String> call(TableColumn<ListElement, String> param) {

                return new TableCell<ListElement, String>(){

                    @Override

                    protected void updateItem(String item, boolean empty){

                        super.updateItem(item,empty);



                        if (!empty){

                            setText(item);

                            System.out.println(getStyleClass().get(5) + " класс стилей");

                            /*if (getStyleClass().toString().equals("table-column1")){

                                getStyleClass().add("text-color2");

                            }*/

                            if (item.equals("Йогурт")){

                                getStyleClass().add("text-color1");



                            }



                            if (item.equals("Хлеб")){

                                getStyleClass().add("text-color1");

                            }

                        }



                    }

                };

            }

        });



    }

    //создаём возможность задавать стилизацию отдельной ячейке в колонке check
    public void checkCellFactory(){

        check.setCellFactory(new Callback<TableColumn<ListElement, String>, TableCell<ListElement, String>>() {

            @Override

            public TableCell<ListElement, String> call(TableColumn<ListElement, String> param) {

                return new TableCell<ListElement, String>(){

                    @Override

                    protected void updateItem(String item, boolean empty){

                        super.updateItem(item,empty);



                        if (!empty){

                            setText(item);

                            if (item.equals("yes")){

                                //System.out.println(columnElement.getText());

                                setStyle("-fx-background-color: lightgreen;");



                                //getStyleClass().add("-fx-background-color: lightgreen;");

                            }



                            if (item.equals("no")){

                                setStyle("-fx-background-color: skyblue;");



                                //getStyleClass().add("-fx-background-color: blue;");

                            }

                        }



                    }

                };

            }

        });

    }

    public void emptyElement(){

        //Добавление пустого элемента

        listOfPurchase.add(new ListElement(null, "no"));

        tblElements.edit(listOfPurchase.getListElementList().size()-1, columnElement); //выделяем последний элемент и переводим его в режим редактирования

    }

    //слушатели клавиатуры
    public void iHearKeyboardTblOn(){

        // primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, handlerEdit);

        mainBox.addEventHandler(KeyEvent.KEY_RELEASED, handlerEdit);

    }

    public void iHearKeyboardTblOff(){

        // primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, handlerEdit);

        mainBox.removeEventHandler(KeyEvent.KEY_RELEASED, handlerEdit);

    }

    public void iHearKeyboardMainBoxOn(){

        //primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, handlerScene);

        mainBox.addEventHandler(KeyEvent.KEY_RELEASED, handlerScene);



    }

    public void iHearKeyboardMainBoxOff(){

        //primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, handlerScene);

        mainBox.removeEventHandler(KeyEvent.KEY_RELEASED, handlerScene);

    }

    public void startEdit(){

// старт редактирования

        //forEditingXml();

        tblElements.refresh();

        checkEdit = true;

        lowerPanel.setVisible(true);

        lowerBox.setVisible(true);

        btnDisabled(true);

        setRowEditable();

        tblElements.setEditable(true);

        tblElements.edit(tblElements.getSelectionModel().getFocusedIndex(), columnElement);

        iHearKeyboardTblOn();

        iHearKeyboardMainBoxOff();



    }

    public void endEdit(){

        //окончание редактирования

        iHearKeyboardTblOff();

        iHearKeyboardMainBoxOn();

        tblElements.refresh();

        checkEdit = false;

        btnDisabled(false);

        lowerPanel.setVisible(false);

        lowerPanel.setVisible(false);

        tblElements.setEditable(false);

        setF(true);

    }

    public void procSave(){

        //сохранение

        SAXBuilder builder = new SAXBuilder();

        File file = new File(openFile);

        try {

            Document document = builder.build(file);

            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            FileWriter writer = new FileWriter(openFile);

            Element rootElement = document.getRootElement(); // в релизе добавить создание файла с готовой структурой

            // (рут и чайлд) иначе тут будет эксепшен, если файла нет

            // возможно, это решаемо xml-схемой

            rootElement.getChild("items").removeContent(); // удаление старого контента

            for (int i=0; i<listOfPurchase.getListElementList().size(); i++){

                rootElement.getChild("items").addContent(new Element("item").addContent(listOfPurchase.getListElementList().get(i).getElement())

                        .setAttribute(new Attribute("check", listOfPurchase.getListElementList().get(i).getCheck())));

            }

            outputter.output(document,writer);

            writer.close();

            setF(false);

            Platform.setImplicitExit(true);

        } catch (JDOMException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void procSaveAs(){

        //сохранить как

        onOffLowerPanel(false);

        try {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Сохранить как...");

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML", "*.xml");

            fileChooser.getExtensionFilters().add(extensionFilter);

            SAXBuilder builder = new SAXBuilder();

            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null){

                openFile = file.getAbsolutePath();

                saveAsFlag = true;

                file = new File(openFile); //!!!!!!!!!!!!!!!!ЧТО ЭТО??????!!!!!!!!!!!!!!!!

            } else {return;}

            Document document = builder.build("./src/project/sample/xml/defaultNewFile.xml");

            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            FileWriter writer = new FileWriter(openFile);

            Element rootElement = new Element("test");

            rootElement.setContent(new Element("items"));

            document.setRootElement(rootElement);



            for (int i=0; i<listOfPurchase.getListElementList().size(); i++){

                rootElement.getChild("items").addContent(new Element("item").addContent(listOfPurchase.getListElementList().get(i).getElement())

                        .setAttribute(new Attribute("check", listOfPurchase.getListElementList().get(i).getCheck())));

            }

            outputter.output(document,writer);

            writer.close();



        }catch (NullPointerException e){

            e.printStackTrace();

        } catch (RuntimeException e) {

            e.printStackTrace();

        } catch (JDOMException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void forEditingXml(){

        //без этой конструкции не сохраняет элемент в xml

        columnElement.setOnEditCommit((TableColumn.CellEditEvent<ListElement, String> event) ->{

            TablePosition<ListElement, String> pos = event.getTablePosition();

            String newColumnElement = event.getNewValue();

            int row = pos.getRow();

            ListElement listElement = event.getTableView().getItems().get(row);

            listElement.setElement(newColumnElement);

        });

    }

    public void newEmptyFile(){



        listOfPurchase.getListElementList().clear();

        File file = new File("./src/project/sample/xml/defaultNewFile.xml");

        openFile = file.getAbsolutePath();





    }

    public void alert(){



        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initOwner(primaryStage);

        alert.setTitle("Сохранить изменения?");

        alert.setHeaderText("Данные изменены! Желаете сохранить изменения?");



        ButtonType buttonTypeOne = new ButtonType("Да");

        ButtonType buttonTypeTwo = new ButtonType("Нет");

        ButtonType buttonTypeThree = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo, buttonTypeThree);



        Optional<ButtonType> result = alert.showAndWait();



        if (result.get() == buttonTypeOne){

            save();

            alert.close();

        } else if (result.get() == buttonTypeTwo){



            newEmptyFile();

            btnEditElement.setDisable(false);

            startEdit();

            emptyElement();



            openCheck = false;

            //newFileCheck = false;

            setF(false);



            firstElementChoice();



        }

    }

    public void alertStage(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initOwner(primaryStage);

        alert.setTitle("Сохранить изменения?");

        alert.setHeaderText("Данные изменены! Желаете сохранить изменения?");

        ButtonType buttonTypeOne = new ButtonType("Да");

        ButtonType buttonTypeTwo = new ButtonType("Нет");

        ButtonType buttonTypeThree = new ButtonType("Сохранить как...");

        ButtonType buttonTypeFour = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo, buttonTypeThree, buttonTypeFour);



        Optional<ButtonType> result = alert.showAndWait();



        Platform.setImplicitExit(true);



        if (result.get() == buttonTypeOne){

            save();

            primaryStage.close();

        } else if (result.get() == buttonTypeTwo){

            primaryStage.close();

        } else if (result.get() == buttonTypeThree){

            procSaveAs();

            if (saveAsFlag){

                primaryStage.close();

            } else {alert.close();}

        }

    }

    public void firstElementChoice(){

        //Выделяем элемент в списке, в данном случае первый - selectFirst()

        tblElements.requestFocus();

        tblElements.getFocusModel().focus(0);

        tblElements.getSelectionModel().selectFirst();

        newFileCheck = true;

    }

    public void readTheList(){

        for (int i=0; i<listOfPurchase.getListElementList().size(); i++){

            System.out.println("Элемент списка " + listOfPurchase.getListElementList().get(i).getElement());

        }



    }

    //запись в xml
    public void writeToXml() throws ParserConfigurationException, IOException, SAXException, TransformerException {



        SAXBuilder builder = new SAXBuilder();



        File file = new File("C:/temp/test.xml");

        try {

            org.jdom2.Document document = builder.build(file);

            org.jdom2.Element rootElement = document.getRootElement();

            rootElement.getChild("items").addContent(new Element("item").addContent("Тест1").setAttribute(new Attribute("check", "yes")));



            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            FileWriter writer = new FileWriter("C:/temp/test.xml");

            outputter.output(document, writer);

            writer.close();

        } catch (JDOMException e) {

            e.printStackTrace();

        }



    }

    /*public void loadFromXml() {



        SAXBuilder builder = new SAXBuilder();

        File file = new File("C:/temp/test.xml");

        try {

            org.jdom2.Document document = builder.build(file);

            org.jdom2.Element rootElement = document.getRootElement();

            XMLOutputter outputter = new XMLOutputter();

            outputter.output(document, System.out);

            System.out.println(rootElement.getChild("items").getChildren().get(2).getText());

            System.out.println(rootElement.getChild("items").getChild("item").getText());



        } catch (JDOMException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }





    }*/



}

