package project.sample.objects;



import javafx.scene.control.TableRow;

import javafx.scene.control.TableView;

import javafx.util.Callback;

import project.sample.controllers.MainController;

import project.sample.interfaces.impls.CollectionListOfPurchase;

import project.sample.start.Main;



/**

 * Created by VZyatkin on 02.03.2018.

 */

/*public class ColorRow<T> implements Callback<TableView<T>, TableRow<T>> {



 *//*    @Override

    public TableRow<T> call(TableView<T> tableView) {

        return new TableRow<T>() {

            @Override

            protected void updateItem( T paramT, boolean b ) {

                if ( getIndex() == CollectionListOfPurchase.rowNum ) {

//              if ( tableView.getItems().get( Main2.STYLING_ROW_INDEX ) == paramT ) {

                    setStyle( "-fx-background-color: lightgreen;" );

                } else {

                    setStyle( null );

                }

                super.updateItem(paramT, b);

            }

        };

    }*//*

}*/



public class ColorRow implements Callback<TableView, TableRow>{

    @Override

    public TableRow call(TableView param) {

        return null;

    }

}
