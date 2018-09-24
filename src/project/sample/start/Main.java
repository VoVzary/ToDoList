package project.sample.start;



import javafx.application.Application;

import javafx.application.Platform;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;

import javafx.stage.Stage;

import javafx.stage.WindowEvent;

import project.sample.controllers.MainController;





public class Main extends Application {



    @Override

    public void start(Stage primaryStage) throws Exception{

        //Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = fxmlLoader.load(getClass().getResource("../fxml/main.fxml").openStream());



        MainController controller = fxmlLoader.getController();

        controller.setPrimaryStage(primaryStage);



        primaryStage.setTitle("Список дел");

        primaryStage.setMinHeight(620);

        primaryStage.setMinWidth(450);

        Scene scene = new Scene(root, 430, 580);





        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override

            public void handle(WindowEvent event) {

                if (MainController.getF() == true){



                    Platform.setImplicitExit(false); //позволяет не закрывать приложение

                    event.consume();



                    controller.alertStage();



                }

            }

        });

        scene.getStylesheets().add(getClass().getResource("../css/myTheme.css").toExternalForm());

        primaryStage.setScene(scene);











        //Запрос на сохранение данных при закрытии



        /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override

            public void handle(WindowEvent event) {



                if (MainController.getF() == true){



                    //Убрать в отдельный класс и сделать обычное модальное окно



                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.initOwner(primaryStage);



                    alert.setOnCloseRequest(new EventHandler<DialogEvent>() {

                        @Override

                        public void handle(DialogEvent event) {



                            //System.out.println(event.getSource());

                        }

                    });



                    //alert.initStyle(StageStyle.UNDECORATED);



                    alert.setTitle("Сохранить изменения?");

                    alert.setHeaderText("Данные изменены! Желаете сохранить изменения?");

                    //alert.showAndWait();



                    ButtonType btnSave = new ButtonType("Сохранить");

                    ButtonType btnNo = new ButtonType("Нет");

                    ButtonType btnCancel = new ButtonType("Отмена");





                    //btnNo.getButtonData().isDefaultButton(); //делаем кнопку кнопкой по умолчанию и она закрывает программу

                    alert.getButtonTypes().setAll(btnSave, btnNo, btnCancel);



                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == btnSave) {

                        CollectionListOfPurchase collectionListOfPurchase = new CollectionListOfPurchase();

                        collectionListOfPurchase.writeToXml();



                    } else if (result.get() == btnNo) {

                        primaryStage.close();



                    } else if (result.get() == btnCancel) {

                        event.consume();

                    }

                }

            }

        });*/





        primaryStage.show();



    }





    public static void main(String[] args) {

        launch(args);

    }

}

