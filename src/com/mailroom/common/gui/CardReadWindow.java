package com.mailroom.common.gui;

import com.mailroom.common.objects.Person;
import com.mailroom.mainclient.MainFrame;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by james on 9/3/15.
 */
public class CardReadWindow
{
    static Stage window;
    public static Person displayWindow()
    {
        ArrayList<Person> results = new ArrayList<Person>();
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scan DbUser Card");
        window.setWidth(200);
        window.setHeight(150);

        Label promptLable = new Label();
        promptLable.setText("Please Swipe DbUser ID");

        TextField cardText = new TextField();
        cardText.setOpacity(0);
        cardText.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if ( event.getCode() == KeyCode.ENTER)
                {
                    //find person
                    window.hide();
                }
            }
        });
        if(results.size() > 1)
        {
            //prompt selector
            return null;
        }
        else
        {
            if(results.size() == 1)
            {
                return results.get(0);
            }
            else
            {
                if(results.size() == 0)
                {
                    return null;
                }
            }
        }
        return null;
    }
}
