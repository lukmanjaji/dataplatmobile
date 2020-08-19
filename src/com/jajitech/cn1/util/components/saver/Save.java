/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components.saver;

import com.codename1.components.OnOffSwitch;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.spinner.NumericSpinner;
import com.codename1.ui.spinner.Picker;
import com.jajitech.cn1.util.components.LCheckBox;
import com.jajitech.cn1.util.components.LRadioButton;
import com.jajitech.cn1.util.components.Matrix;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jaji
 */
public class Save {
    
    String code;
    String section;
    String entry;
    
    public Save()
    {
        
    }
    
    public void saveToDevice(Container comp, String comp_name, String code, String section, String entry)
    {
        this.code = code;
        this.section = section;
        this.entry = entry;
        
        if(comp_name.startsWith("textfield"))
        {
          saveTextField(comp, comp_name);
        }
        if(comp_name.startsWith("drop"))
        {
          saveComboBox(comp, comp_name);
        }
        if(comp_name.startsWith("textarea"))
        {
          saveTextArea(comp, comp_name);
        }
        if(comp_name.startsWith("checkbox"))
        {
          saveCheckBox(comp, comp_name);
        }
        if(comp_name.startsWith("radio"))
        {
          saveRadioButton(comp, comp_name);
        }
        if(comp_name.startsWith("time") || comp_name.startsWith("date") || comp_name.startsWith("spinner"))
        {
          savePicker(comp, comp_name);
        }
        if(comp_name.startsWith("location"))
        {
          saveLocation(comp, comp_name);
        }
       
        if(comp_name.startsWith("onoff"))
        {
          saveSwitch(comp, comp_name);
        }
        if(comp_name.startsWith("barcode"))
        {
          saveBarcode(comp, comp_name);
        }
        if(comp_name.startsWith("matrix"))
        {
          saveMatrix(comp, comp_name);
        }
    }
    
    public void saveTextField(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof TextField)
            {
                TextField t = (TextField)comp;
                result = t.getText();
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveMatrix(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        System.out.println("I want to save matrix");
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Matrix)
            {
                System.out.println("its a matrix to save");
                Matrix t = (Matrix) comp;
                List<Map<String,String>> values = t.getMatrixValues();
                for(Map m: values)
                {
                    result = result + m.get("question")+"##"+m.get("header")+"##"+m.get("value")+"##@##@";
                }
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveComboBox(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof ComboBox)
            {
                ComboBox t = (ComboBox)comp;
                result = t.getSelectedItem().toString();
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveTextArea(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof TextArea)
            {
                TextArea t = (TextArea)comp;
                result = t.getText().toString();
            }
        }
        writeToDevice(comp_name, result);
    }
    
    
    public void saveCheckBox(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof LCheckBox)
            {
                LCheckBox t = (LCheckBox)comp;
                if(t.isSelected())
                {
                    result = result+t.getText()+"___";
                }
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveRadioButton(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof LRadioButton)
            {
                LRadioButton t = (LRadioButton)comp;
                if(t.isSelected())
                {
                    result = t.getText();
                }
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void savePicker(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Picker)
            {
                Picker t = (Picker)comp;
                if(t.getType() == Display.PICKER_TYPE_DATE)
                {
                    result = t.getDate().toString();
                }
                if(t.getType() == Display.PICKER_TYPE_TIME)
                {
                    result = t.getText();
                }
                if(t.getType() == Display.PICKER_TYPE_STRINGS)
                {
                    result = t.getSelectedString();
                }
                
                
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveLocation(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comps = c.getComponentAt(count);
            if(comps instanceof Container)
            {
                Container cc = (Container) comps;
                    for(int x=0; x<cc.getComponentCount();x++)
                    {
                        Component a = cc.getComponentAt(x);
                        if(a instanceof TextField)
                        {
                            TextField t = (TextField) a;
                            if(t.getName().equals("lat"))
                            {
                                writeToDevice("lat"+comp_name, t.getText());
                            }
                            else
                            {
                                writeToDevice("lon"+comp_name, t.getText());
                            }
                        }
                    }
                
            }
        }
    }
    
    public void saveSpinner(Container component, String comp_name)
    {
        double result = 0.0;
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof NumericSpinner)
            {
                NumericSpinner t = (NumericSpinner)comp;
                result =  t.getValue();
            }
        }
        writeToDevice(comp_name, ""+result);
    }
    
    public void saveSwitch(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof OnOffSwitch)
            {
                OnOffSwitch t = (OnOffSwitch)comp;
                result =  t.getComponentState().toString();
                if(result.equals("true"))
                {
                    result = t.getOn();
                }
                else
                {
                    result = t.getOff();
                }
            }
        }
        writeToDevice(comp_name, result);
    }
    
    public void saveBarcode(Container component, String comp_name)
    {
        String result = "";
        Container c =  component;
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof TextField)
            {
                TextField t = (TextField) comp;
                result =  t.getText();
            }
        }
        writeToDevice(comp_name, result);
    }
    
    
    
    
    
    public void writeToDevice(String fileName, String contents)
    {
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String file = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+fileName;
        byte[]b=  contents.getBytes();
        try
        {
            FileSystemStorage.getInstance().openOutputStream(file).write(b);
        }
        catch(Exception ex)
        {
           ex.printStackTrace();
        }   
    }
    
    
    

}
    
    
    

