/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components;

import com.codename1.components.SpanButton;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Container;
import com.codename1.ui.RadioButton;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author Usman
 */
public class LRadioButton extends Container {
    
    ButtonGroup bg;
    String caption;
    SpanButton label;
    RadioButton radio;
    
    
    public void addListener(ActionListener al)
    {
        addListenerToContainer(al);
        radio.addActionListener(al);
        label.addActionListener(al);
    }
    
    public void addListenerToContainer(ActionListener al)
    {
        addPointerReleasedListener(al);
    }

    
    
    public LRadioButton(String caption, ButtonGroup bg)
    {
        this.caption = caption;
        this.bg = bg;
        setLayout(new BorderLayout());
        label = new SpanButton(caption);
        radio = new RadioButton();
        bg.add(radio);
        label.setUIID("RadioButton");
        setUIID("RadioButton");
        
        addComponent("West", label);
        addComponent("East", radio);
    }
    
    public String getText()
    {
        return label.getText();
    }
    public void setSelected(boolean value)
    {
        radio.setSelected(value);
    }
    
    public boolean getSelected()
    {
        return radio.isSelected();
    }
    
    public RadioButton getLRadioButton()
    {
        return radio;
    }
    
    public boolean isSelected()
    {
        return radio.isSelected();
    }
    
    public void setText(String text)
    {
        label.setText(caption);
    }
    
    
    
}
