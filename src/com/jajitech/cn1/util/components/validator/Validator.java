/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components.validator;

import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.spinner.Picker;
import com.jajitech.cn1.util.components.LCheckBox;
import com.jajitech.cn1.util.components.LRadioButton;
import com.jajitech.cn1.util.components.Matrix;
import com.jajitech.cn1.util.components.Matrix.Validate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usman
 */
public class Validator {
    
    public Map validateComponent(Container c)
    {
        Map isFilled = null;
        String comp_name = null;
        try
        {
            comp_name = c.getName();
        }catch(Exception ee){}
 
        if(comp_name != null && comp_name.startsWith("textfield") && !comp_name.startsWith("label"))
        {
          isFilled = validateTextField(c, comp_name); 
        }
        if(comp_name != null && comp_name.startsWith("barcode") && !comp_name.startsWith("label"))
        {
          isFilled = validateTextField(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("textarea") && !comp_name.startsWith("label"))
        {
          isFilled = validateTextArea(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("drop") && !comp_name.startsWith("label"))
        {
          isFilled = validateComboBox(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("checkbox") && !comp_name.startsWith("label"))
        {
          isFilled = validateCheckBox(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("radio") && !comp_name.startsWith("label"))
        {
          isFilled = validateRadioButton(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("time")  && !comp_name.startsWith("label"))
        {
          isFilled = validateTimeDate(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("date")  && !comp_name.startsWith("label"))
        {
          isFilled = validateTimeDate(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("image")  && !comp_name.startsWith("label"))
        {
          isFilled = validatePicture(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("location")  && !comp_name.startsWith("label"))
        {
          isFilled = validateLocation(c, comp_name);
        }
        if(comp_name != null && comp_name.startsWith("matrix")  && !comp_name.startsWith("label"))
        {
          isFilled = validateMatrix(c, comp_name);
        }
        
        
        
        if(isFilled == null){isFilled = new HashMap(); isFilled.put("req", false);}
         
       
        return isFilled;
    }
     
     public Map validateMatrix(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                    if(t.getText().equals("") && t.getName().equals("req"))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" ") && t.getName().equals("req"))
                    {
                        m.put("req", true);
                    }
            }
        }
        
        Validate val = null;
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label l = (Label) comp;
                if(l.getName().equals("valtype") && required == true)
                {
                    if(l.getText().startsWith("AT_LEAST"))
                    {
                        val = Validate.AT_LEAST_ONE_ROW_FILLED;
                    }
                    else
                    {
                        val = Validate.ALL_ROWS_FILLED;
                    }
                }
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Matrix )
            {
                if(required == true)
                {
                    Matrix t = (Matrix) comp;
                    t.setValidateType(val);
                    boolean done = t.validateMatrix();
                    if(done == true)
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        return m;
    }
     
    
    public Map validateTextField(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                    if(t.getText().equals("") && t.getName().equals("req"))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" ") && t.getName().equals("req"))
                    {
                        m.put("req", true);
                    }
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof TextField )
            {
                if(required == true)
                {
                    TextField t = (TextField)comp;
                    if(t.getText().length() > 0)
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        return m;
    }
    
    public Map validateTextArea(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                if(t.getName().equals("req"))
                {
                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
                }
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof TextArea )
            {
                if(required == true)
                {
                    TextArea t = (TextArea)comp;
                    if(t.getText().length() > 0)
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        return m;
    }
    
    public Map validateComboBox(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                if(t.getName().equals("req"))
                {
                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
                }
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof ComboBox )
            {
                if(required == true)
                {
                    ComboBox t = (ComboBox)comp;
                    if(t.getSelectedIndex() > 0)
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        return m;
    }
    
    public Map validateCheckBox(Container component, String comp_name)
    {
        boolean result = false;
        List lx = new ArrayList();
        Container c =  component;
        int f = c.getComponentCount();
        boolean required = true;
        Map m = new HashMap();
        System.out.println(c.getComponentCount());
        for(int count=0; count<f;count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                
                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
            }
        }
        
        for(int count=0; count<f; count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof LCheckBox )
            {
                if(required == true)
                {
                    LCheckBox t = (LCheckBox)comp;
                    if(t.isSelected())
                    {
                        lx.add(count);
                    }
                    else
                    {
                        
                    }
                }
            }
        }
        if(lx.size() > 0)
        {
            m.put("filled", true);
                        System.out.println("filled is true");
        }
        else
        {
            m.put("filled", false);
                        System.out.println("filled is false");
        }
        return m;
    }
    
    
    public Map validateRadioButton(Container component, String comp_name)
    {
        boolean result = false;
        List lx = new ArrayList();
        Container c =  component;
        int f = c.getComponentCount();
        boolean required = true;
        Map m = new HashMap();
        System.out.println(c.getComponentCount());
        for(int count=0; count<f;count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
            }
        }
        
        for(int count=0; count<f; count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof LRadioButton )
            {
                if(required == true)
                {
                    LRadioButton t = (LRadioButton)comp;
                    if(t.isSelected() == true)
                    {
                        lx.add(count);
                    }
                }
            }
        }
        if(lx.size() > 0)
        {
            m.put("filled", true);
                        System.out.println("filled is true");
        }
        else
        {
            m.put("filled", false);
                        System.out.println("filled is false");
        }
        return m;
    }
    
    public Map validateTimeDate(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;

                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Picker )
            {
                if(required == true)
                {
                    Picker t = (Picker)comp;
                    if(t.getText().length() > 0)
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        return m;
    }
    
    
    public Map validatePicture(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;
                
                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
                
            }
        }
        
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Button )
            {
                if(required == true)
                {
                    Button t = (Button)comp;
                    if(t.getText().equals(" ") && t.getName().equals("p"))
                    {
                        System.out.println("filled is true");
                        m.put("filled", true);
                    }
                    else
                    {
                        m.put("filled", false);
                        System.out.println("filled is false");
                    }
                }
            }
        }
        
        
        return m;
        }
    
    
    public Map validateLocation(Container component, String comp_name)
    {
        boolean result = false;
        Container c =  component;
        boolean required = true;
        Map m = new HashMap();
        for(int count=0; count<c.getComponentCount();count++)
        {
            Component comp = c.getComponentAt(count);
            if(comp instanceof Label )
            {
                Label t = (Label)comp;

                    if(t.getText().equals(""))
                    {
                        required = false;
                        m.put("req", false);
                    }
                    if(t.getText().equals(" "))
                    {
                        m.put("req", true);
                    }
            }
        }
        
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
                            if(required == true)
                            {
                            TextField t = (TextField) a;
                            if(t.getText().length() > 0)
                            {
                                System.out.println("filled is true");
                        m.put("filled", true);
                            }
                            else
                            {
                                System.out.println("filled is false");
                        m.put("filled", false);
                            }
                            }
                        }
                    }
                
            }
        }
        return m;
    }
    
}
