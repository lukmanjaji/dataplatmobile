/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components;

import com.codename1.components.SpanButton;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;


/**
 *
 * @author Usman
 */
public class LCheckBox extends Container {
    
    String caption;
    CheckBox checkbox;
    SpanButton label;
    Alignment type;
    
    public static enum Alignment {
        LEFT_ALIGNMENT,
        CENTER_ALIGNMENT,
        RIGHT_ALIGNMENT
    };
    
    public void addListener(ActionListener al)
    {
        addListenerToContainer(al);
        checkbox.addActionListener(al);
        label.addActionListener(al);
    }
    
    public void addListenerToContainer(ActionListener al)
    {
        addPointerReleasedListener(al);
    }
    

    
    public LCheckBox(String caption)
    {
        this.caption = caption;
        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        setUIID("CheckBox");
        checkbox = new CheckBox();
        label = new SpanButton(caption); 
        label.setUIID("CheckBox");

        addComponent(checkbox);
        addComponent(label);
    }
    
    public void setSelected(boolean value)
    {
        checkbox.setSelected(value);
    }
    
    public CheckBox getLCheckBox()
    {
        return checkbox;
    }
    
    public boolean getSelected()
    {
        return checkbox.isSelected();
    }
    
    public boolean isSelected()
    {
        return checkbox.isSelected();
    }
    
    public void setText(String text)
    {
        label.setText(caption);
    }
    
    public String getText()
    {
        return label.getText();
    }
    
    
}
