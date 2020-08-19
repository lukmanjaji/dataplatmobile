/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components;

import com.codename1.components.SpanLabel;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.table.TableLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author Usman
 */
public class Matrix extends Container {
    
    Vector headers;
    Vector questions;
    Renderer renderer = null;
    Validate type = null;
    int columnCount = 0;
    int rowCount = 0;
    Container main = new Container();
    TableLayout layout;
    public static enum Renderer {
        TEXTFIELD,
        RADIOBUTTON
    };
    public static enum Validate {
        AT_LEAST_ONE_ROW_FILLED,
        ALL_ROWS_FILLED,
        NONE
    };
    
    public Matrix(Vector headers, Vector questions)
    {
        super();
        setLayout(new BorderLayout());
        setScrollableX(true);
        setScrollableY(true);
        this.headers = headers;
        this.questions = questions;
        layout = new TableLayout(questions.size()+1, headers.size()+1);
        main.setLayout(layout);
        main.setScrollableX(true);
        main.setScrollableY(true);
        addComponent("Center", main);
        createMatrixHeader();
    }
    
    
    
    
    public void setRenderer(Renderer renderer)
    {
        this.renderer = renderer;
    }
    
    public Renderer getRenderer()
    {
        return renderer;
    }
    
    public void createMatrixHeader()
    {
        main.addComponent(new SpanLabel(" "));
        for(Object h: headers)
        {
            main.addComponent(new SpanLabel(h.toString()));
        }
    }
    
    public void createMatrix()
    {
        Component comp = null;
        for(Object r: questions)
        {
            SpanLabel question = new SpanLabel(r.toString());
            question.setName(r.toString());
            main.addComponent(question);
            ButtonGroup bg = new ButtonGroup();
            for(int x=0; x<headers.size(); x++)
            {
                if(renderer == Renderer.RADIOBUTTON)
                {
                    comp = new RadioButton("");
                    comp.setName(headers.get(x).toString());
                    bg.add((RadioButton)comp);
                }
                else
                {
                    comp = new TextField();
                    comp.setName(headers.get(x).toString());
                }
                main.addComponent(comp);
            }
    }
    }
    
    
    
    
    public Validate getValidation()
    {
        return type;
    }
    
    public void setValidateType(Validate type)
    {
        this.type = type;
    }
    
    public boolean validateMatrix()
    {
        if(type == Validate.NONE)
        {
            return true;
        }
        boolean isFilled = false;
        int rowCount = layout.getRows();
        int columnCount = layout.getColumns();
        List filled = new ArrayList();
        List notFilled = new ArrayList();
        List forQuestion = new ArrayList();
        List forOption = new ArrayList();
        for(int x=1; x<rowCount; x++)
        {
            SpanLabel question = (SpanLabel) layout.getComponentAt(x, 0);
            for(int y=1; y<columnCount; y++)
            {
                Map map = new HashMap();
                Component c = layout.getComponentAt(x, y);
                if(renderer == Renderer.RADIOBUTTON)
                {
                    RadioButton value = (RadioButton) c;
                    if(value.isSelected())
                    {
                        filled.add(value.getName());
                        forQuestion.add(question.getText());  
                    }
                    else
                    {
                        notFilled.add(question.getText());
                    }
                }
                else
                {
                    TextField value = (TextField) c;
                    if(value != null && value.getText().length() > 0)
                    {
                        filled.add(question.getText()+"##"+value.getName());
                    }
                    else
                    {
                        notFilled.add(question.getText()+"##"+value.getName());
                    }
                }
            }
        }
        if(renderer == Renderer.RADIOBUTTON)
        {
            Iterator<Object> ite = notFilled.iterator();
            while(ite.hasNext()) {
                   String v = ite.next().toString();
                   if(forQuestion.contains(v))
                        ite.remove();
              }
        }
        
        
        if(type == Validate.ALL_ROWS_FILLED)
        {
            if(notFilled.size() > 0)
            {
                isFilled = false;
            }
            else
            {
                isFilled = true;
            }
        }
        else
        {
            if(filled.size() > 0)
            {
                isFilled = true;
            }
            else
            {
                isFilled = false;
            }
        }
        return isFilled;
    }
        
    public List getMatrixValues()
    {
        List<Map<String,String>> values = new ArrayList<Map<String,String>>();
        int rowCount = layout.getRows();
        int columnCount = layout.getColumns();
        for(int x=1; x<rowCount; x++)
        {
            SpanLabel question = (SpanLabel) layout.getComponentAt(x, 0);
            for(int y=1; y<columnCount; y++)
            {
                Map map = new HashMap();
                map.put("question", question.getText());
                String header = headers.get(y-1).toString();
                map.put("header", header);
                Component c = layout.getComponentAt(x, y);
                if(renderer == Renderer.RADIOBUTTON)
                {
                    RadioButton value = (RadioButton) c;
                    if(value.isSelected())
                    {
                        map.put("value", value.getName());
                        values.add(map);
                    }
                }
                else
                {
                    TextField value = (TextField) c;
                    map.put("value", value.getText());
                    values.add(map);
                }
                
            }
            
        }
        return values;
    }
    
    
    public void setMatrixValues(List values)
    {
        int rowCount = layout.getRows();
        int columnCount = layout.getColumns();
        for(Object m: values)
        {
            Map map = (Map) m;
            for(int x=1; x<rowCount; x++)
            {
                String question = map.get("question").toString();
                String header = map.get("header").toString();
                String value = map.get("value").toString();
                //System.out.println("this is in matrix main\n "+map.get("question")+" "+map.get("header")+" "+map.get("value"));
                SpanLabel q = (SpanLabel) layout.getComponentAt(x, 0);
                if(q.getText().trim().equals(question))
                {
                    for(int y=1; y<columnCount; y++)
                    {
                        String h = headers.get(y-1).toString();
                        //System.out.println(h + "  "+header);
                        if(header.equals(h))
                        {
                            System.out.println("they are equal "+header + " "+h);
                            Component c = layout.getComponentAt(x, y);
                            if(renderer == Renderer.RADIOBUTTON)
                            {
                                //System.out.println("its a radio");
                                RadioButton v = (RadioButton) c;
                                String vl = v.getName();
                                if(vl.equals(value))
                                {
                                    v.setSelected(true);
                                }
                            }
                            else
                            {
                                TextField v = (TextField) c;
                                String vl = v.getName();
                                v.setText(value);
                                
                            }
                        }

                    }
                }
            }
        }
        
    
    }
    
}
