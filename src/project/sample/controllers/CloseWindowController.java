package project.sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by VZyatkin on 13.03.2018.
 */
public class CloseWindowController {

    @FXML
    private
    Button btnOk;

    @FXML
    private
    Button btnNo;

    @FXML
    private
    Button btnCancel;

    public void eventButton(ActionEvent actionEvent){
        MainController controller = new MainController();
        Object source = actionEvent.getSource();
        Button clickedButton = (Button) source;
        switch (clickedButton.getId()){
            case "btnOk":
                System.out.println("ок");
                controller.save();
                break;
            case "btnNo":
                System.out.println("no");

                break;
            case "btnCancel":

                System.out.println("cancel");
                break;

        }
    }


/*    @FXML
    private void initialize(){


    }*/

}
