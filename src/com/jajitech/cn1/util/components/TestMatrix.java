/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.jajitech.cn1.util.components.Matrix.Renderer;
import java.util.List;
import java.util.Map;
import java.util.Vector;



/**
 *
 * @author Usman
 */
public class TestMatrix {
    
    public TestMatrix()
    {
        Form f = new Form("Test");
        f.setLayout(new BorderLayout());
        Vector cols = new Vector();
        cols.add("Yes");
        cols.add("No");
        cols.add("Maybe");
        cols.add("I don't know");
        cols.add("Why asking");
        Vector q = new Vector();
        q.add("Are you mad");
        q.add("Where do you live");
        q.add("Where do you work");
        q.add("Do you have a wife");
        final Matrix m = new Matrix(cols, q);
        m.setRenderer(Renderer.RADIOBUTTON);
        m.createMatrix();
        f.add("Center", m);
        Button b = new Button("Get Values");
        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                m.setValidateType(Matrix.Validate.ALL_ROWS_FILLED);
                boolean done = m.validateMatrix();
                if(done == false)
                {
                    ToastBar.showErrorMessage("Please fill all rows");
                    return;
                }
                List<Map<String,String>> values = m.getMatrixValues();
                for(Map m: values)
                {
                    System.out.println(m.get("question"));
                    System.out.println(m.get("header"));
                    System.out.println(m.get("value"));
                    System.out.println("----");
                }
            }
        });
        f.add("South", b);
        f.show();
    }
    
}
