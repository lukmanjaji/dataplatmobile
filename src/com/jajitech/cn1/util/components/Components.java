/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components;

import com.codename1.capture.Capture;

import com.codename1.components.Accordion;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MediaPlayer;
import com.codename1.components.OnOffSwitch;
import com.codename1.components.SpanLabel;
import com.codename1.components.WebBrowser;
import com.codename1.ext.codescan.CodeScanner;
import com.codename1.ext.codescan.ScanResult;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.Util;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.GenericSpinner;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.jajitech.cn1.util.Helper;
import com.jajitech.cn1.util.components.Matrix.Renderer;
import com.jajitech.cn1.util.components.Matrix.Validate;
import com.jajitech.cn1.util.components.logicImpl.Logic;
import com.jajitech.cn1.util.components.saver.Save;
import com.jajitech.xdata.mobile.XDataMobile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javafx.scene.control.Spinner;

/**
 *
 * @author Jaji
 */
public class Components {
    
    String type,caption,items,required,entry,code,section,isRider;
    boolean isNew = true;
    Helper helper = new Helper();
    Logic logic;
    String fullName = "";
    XDataMobile xdata;
    
    public Components(String type, String caption, String items, String required, String entry, String code, String section, boolean isNew, String isRider, XDataMobile xdata)
    {
        this.caption = caption;
        this.items = items;
        this.required = required;
        this.type = type;
        this.entry = entry;
        this.code = code;
        this.section = section;
        this.isNew = isNew;
        this.isRider = isRider;
        fullName = type+"##"+caption+"##"+items+"##"+required+"##"+isRider;
        logic = new Logic();
        this.xdata = xdata;
    }
    
    public Container getMatrixContainer()
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        top.setName("label"+type);
        Label r = new Label();
        r.setName("valtype");
        String y[] = helper.split(required, "@@");
        String vv = y[0].trim();
        Validate valtype = null;
        
        if(!required.startsWith("NONE"))
        {
            r.setUIID("ErrorLabel");
            r.setText(y[0]);
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText(y[0]);
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        
        Helper helper = new Helper();
        String h[] = helper.split(items, "@@@");
        String head[] = helper.split(h[0].trim(), "@@");
        String row[] = helper.split(h[1].trim(), "@@");
        String m[] = helper.split(required, "@@");
        
        Vector headers = new Vector();
        for(Object t: head)
        {
            headers.add(t);
        }
        Vector rows = new Vector();
        for(Object d: row)
        {
            rows.add(d);
        }
        final Matrix matrix = new Matrix(headers, rows);
        if(m[1].trim().startsWith("Radio"))
        {
            matrix.setRenderer(Renderer.RADIOBUTTON);
        }
        else
        {
            matrix.setRenderer(Renderer.TEXTFIELD);
        }
        if(vv.startsWith("AT_LEAST"))
        {
            valtype = Validate.AT_LEAST_ONE_ROW_FILLED;
            matrix.setValidateType(valtype);
        }
        
        if(vv.startsWith("ALL_ROWS"))
        {
            valtype = Validate.ALL_ROWS_FILLED;
            matrix.setValidateType(valtype);
        }
        
        if(vv.startsWith("NONE"))
        {
            valtype = Validate.NONE;
            matrix.setValidateType(valtype);
        }
        
        matrix.createMatrix();
        c.addComponent(matrix);
        
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        
        Label req = new Label("");
        req.setName("req");
        if(!required.equals("NONE"))
        {
          req.setText(" ");
        }
        c.addComponent(req);
        
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            //System.out.println(value);
            if(value.length() > 2 && value != null)
            {
                List<Map<String,String>> values = new ArrayList<Map<String,String>>();
                String a[] = helper.split(value.trim(), "##@##@");
                for(String f: a)
                {
                    //System.out.println("this is f "+f);
                    try
                    {
                    String j[] = helper.split(f, "##");
                    Map map = new HashMap();
                    map.put("question", j[0]);
                    map.put("header", j[1]);
                    map.put("value", j[2]);
                    values.add(map);
                    }catch(Exception er){}
                }
                matrix.setMatrixValues(values);
            }
        }
        
        return main;
    }
    
    public Container getTextFieldContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        top.setName("label"+type);
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        TextField t = null;
        
        if(items.equals("Numeric"))
        {
           t = new TextField("", "", 100, TextArea.NUMERIC);
        }
        if(items.equals("Phone Number"))
        {
            t = new TextField("", "", 100, TextArea.PHONENUMBER);
        }
        if(items.equals("Email"))
        {
            t = new TextField("", "", 100, TextArea.EMAILADDR);
        }
        if(items.equals("Website Address"))
        {
            t = new TextField("", "", 100, TextArea.URL);
        }
        if(items.equals("Normal"))
        {
            t = new TextField("", "", 100, TextArea.ANY);
        }
        c.addComponent(t);
        Label req = new Label("");
        req.setName("req");
        if(required.equals("true"))
        {
          req.setText(" ");
        }
        c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            t.setText(value);
        }
        if(isRider.length() > 5 && items.equals("Numeric"))
        {
            Vector v = getRiderMap();
            logic.addLogicForTextField(t,v, tabs, xdata);
        }
        return main;
    }
    
    
    public Vector getRiderMap()
    {
        Vector result = new Vector();
        String p[] = helper.split(isRider,"@#_#@");
        for(String y: p)
        {
            if(y.length() >3)
            {
                System.out.println("this is y "+y);
                String rider[] = helper.split(y, ";;;;");
                System.out.println("this is r size "+rider.length);
                Map m = new HashMap();
                System.out.println("this is r con "+rider[0]);
                System.out.println("this is r paeam  "+rider[1]);
                m.put("condition", rider[0]);
                m.put("param", rider[1]);
                m.put("action", rider[2]);
                String a[] = helper.split(rider[3],"#@");
                m.put("affected", rider[3]);
                m.put("items", rider[4]);
                result.add(m);
            }
        }
        return result;
    }
    
    public Container getLabelContainer()
    {
        Container main = new Container(new BorderLayout());
        main.setName("none");
        main.setScrollableY(true);
        main.setName(type);
        WebBrowser label = new WebBrowser();
        label.setPage(caption, "");
        main.addComponent(BorderLayout.CENTER, label);
        return main;
    }
    
    public Container getComboBoxContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        String it[] = helper.split(items, "@@");
        ComboBox t = new ComboBox();
        t.addItem("Select");
        for(String tt: it)
        {
            t.addItem(tt);
        }
        c.addComponent(t);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            t.setSelectedItem(value);
        }
        String[] options = helper.split(items, "@@");
        Vector ite = new Vector();
        for(String g : options)
        {
            ite.add(g);
        }
        if(isRider.length() > 5)
        {
            Vector v = getRiderMap();
            logic.addLogicForComboBox(t,v, tabs, xdata);
        }
        
        
        return main;
    }
    
    public Container getTextAreaContainer()
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        TextArea t = new TextArea();
        t.setUIID("ComponentTextArea");
        t.setRows(3);
        c.addComponent(t);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            t.setText(value);
        }
        return main;
    }
    
    public Container getCheckBoxContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        String[] options = helper.split(items, "@@");
        String value[] = null;
        List lx = new ArrayList();
        if(isNew == false)
        {
            value = helper.split(this.readFromDevice(type),"___");
            for(int g=0;g<value.length;g++)
            {
                char aa = '#';
                lx.add(value[g].replace(aa, ' ').trim());
                System.out.println(value[g]);
            }
        }
        Vector ite = new Vector();
        for(String citems: options)
        {
            LCheckBox bx = new LCheckBox(citems);
            ite.add(bx);
            c.addComponent(bx);
            if(isNew == false)
            {
                if(lx.contains(citems))
                {
                    System.out.println(citems);
                    bx.setSelected(true);
                }
            }
        }
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        if(isRider.length() > 5)
        {
            Vector v = getRiderMap();
            logic.addLogicForCheckBox(ite,v, tabs, xdata);
        }
        return main;
    }
    
    public Container getRadioButtonContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        String[] options = helper.split(items, "@@");
        ButtonGroup bg = new ButtonGroup();
        LRadioButton rb = null;
        Vector items = new Vector();
        for(String citems: options)
        {
            rb = new LRadioButton(citems, bg);
            if(citems.equals("") || citems.length() < 1)
            {
                
            }
            else
            {
                c.addComponent(rb);
                items.add(rb);
            }
            if(isNew == false)
            {
                String value = this.readFromDevice(type);
                if(value.trim().equals(citems))
                {
                    rb.setSelected(true);
                }
            }
        }
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        if(isRider.length() > 5)
        {
            Vector v = getRiderMap();
            logic.addLogicForRadioButton(items,v, tabs, xdata);
        }
        return main;
    }
    
    public Container getTimeContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        
        Picker p = new Picker();
        p.setType(Display.PICKER_TYPE_TIME);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            if(value.length() > 3)
            {
             try{System.out.println(value+" is it");
             String t[] = helper.split(value, ":");
             
             
             p.setTime(Integer.parseInt(t[0].trim()), Integer.parseInt(t[1].trim()));}catch(Exception ee){}
            }
        }
        c.addComponent(p);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
            
            if(isRider.length() > 5)
            {
                Vector v = getRiderMap();
                logic.addLogicForTime(p,v, tabs, xdata);
            }
            
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getDateContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        
        Picker p = new Picker();
        p.setType(Display.PICKER_TYPE_DATE);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            if(value.length() > 3)
            {
             try{System.out.println(value+" is it");
             
             Map sdate = helper.parse(value);
                Calendar cc2 = Calendar.getInstance();
                cc2.set(Calendar.YEAR, Integer.parseInt(sdate.get("year").toString()));
                cc2.set(Calendar.MONTH, Integer.parseInt(sdate.get("month").toString())-1);
                cc2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sdate.get("day").toString()));
                cc2.set(Calendar.HOUR_OF_DAY, 0);
                cc2.set(Calendar.MINUTE, 0);
                cc2.set(Calendar.SECOND, 0);
                cc2.set(Calendar.MILLISECOND, 0);
             p.setDate(cc2.getTime());
             
             
             }catch(Exception ee){}
            }
            
        }
        c.addComponent(p);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
            
            if(isRider.length() > 5)
            {
                Vector v = getRiderMap();
                logic.addLogicForDate(p,v, tabs, xdata);
            }
            
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getImageContainer()
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        
        Container pic = new Container(new FlowLayout(Component.CENTER));
        Button b1 = new Button("Gallery");
        //b1.setUIID("ComponentButton");
        pic.addComponent(b1);
        Button b2 = new Button("Camera");
        //b2.setUIID("ComponentButton");
        pic.addComponent(b2);
        c.addComponent(pic);
        final Button picture  = new Button("");
        picture.setName("p");
        if(isNew == false)
        {
            try
            {
                char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type+".jpg";
                System.out.println(p);
//String value = this.readFromDevice(p);
                if (FileSystemStorage.getInstance().exists(p))
                    {
                Image img = Image.createImage(p);
                Image small = img.scaled(250, 250);
                picture.setIcon(small);
                picture.setText(" ");
                    }
            }
            catch(Exception er){er.printStackTrace();}
        }
        b2.addActionListener((new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                String i = Capture.capturePhoto();
                System.out.println("i is "+i);
                if(i != null)
                {
                    try
                    {
                        Image img = Image.createImage(i);
                        Image small = img.scaled(300, 300);
                        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                        String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
                        if(!FileSystemStorage.getInstance().exists(dir))
                        {
                            FileSystemStorage.getInstance().mkdir(dir);
                        }

                        OutputStream save = FileSystemStorage.getInstance().openOutputStream(dir+sep+type+".jpg");
                        ImageIO.getImageIO().save(img, save,  ImageIO.FORMAT_JPEG, 0.85f);
                        picture.setIcon(small);
                        save.close();
                        
                    }
                    catch(Exception er)
                    {
                        er.printStackTrace();
                    }
                }
            }
        }));
                
        
        
        b1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                Display.getInstance().openImageGallery(new ActionListener() {
            InputStream is;
            public void actionPerformed(ActionEvent evt) {
                try {
                    String path=evt.getSource().toString();
                    System.out.println(path);
                    Image image=Image.createImage(path);
                    Image small = image.scaled(300, 300);

                    
                    char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                    String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
                    if(!FileSystemStorage.getInstance().exists(dir))
                    {
                        FileSystemStorage.getInstance().mkdir(dir);
                    }
                   
                    OutputStream save = FileSystemStorage.getInstance().openOutputStream(dir+sep+type+".jpg");
                    ImageIO.getImageIO().save(image, save,  ImageIO.FORMAT_JPEG, 0.85f);
                    picture.setIcon(small);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }               
            }
        });
            }
        });
        c.addComponent(picture);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getLocationContainer(final Tabs tabs, final Form form, final int selectedIndex, final Resources res)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        
        Container g = new Container(new FlowLayout(Component.CENTER));
        Button b1 = new Button("Get Location");

        b1.setUIID("Gray3A");
        g.addComponent(b1);
        c.addComponent(g);
        
        Container d = new Container(new GridLayout(2,2));
        d.setName("mloc");
        Label lat = new Label("Latitude");
        d.addComponent(lat);
        final TextField tlat = new TextField();
        tlat.setName("lat");
        tlat.setEditable(false);
        d.addComponent(tlat);
        
        Label lon = new Label("Longitude");
        d.addComponent(lon);
        final TextField tlon = new TextField();
        tlon.setName("lon");
        tlon.setEditable(false);
        d.addComponent(tlon);
        c.addComponent(d);
        
        if(isNew == false)
        {
            String lati = this.readFromDevice("lat"+type);
            String longi = this.readFromDevice("lon"+type);
            tlat.setText(lati);
            tlon.setText(longi);
        }
        
        Button aloc = new Button("View on Map");
        c.addComponent(aloc);
        
        b1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                
                if (Display.getInstance().getLocationManager().isGPSDetectionSupported()) {
                    if (Display.getInstance().getLocationManager().isGPSEnabled()) {
                        InfiniteProgress ip = new InfiniteProgress();
                        final Dialog ipDlg = ip.showInifiniteBlocking();
                        //Cancel after 20 seconds
                        Location loc = LocationManager.getLocationManager().getCurrentLocationSync(20000);
                        ipDlg.dispose();
                        if (loc != null) {
                            double lat = loc.getLatitude();
                            double lng = loc.getLongitude();
                            tlat.setText(""+lat);
                            tlon.setText(""+lng);
                        } else {
                            Dialog.show("GPS error", "Your location could not be found, please try going outside for a better GPS signal", "Ok", null);
                        }
                    } else {
                        Dialog.show("GPS disabled", "Data Plat needs access to GPS. Please enable GPS", "Ok", null);
                    }
                } else {
                    InfiniteProgress ip = new InfiniteProgress();
                    final Dialog ipDlg = ip.showInifiniteBlocking();
                    //Cancel after 20 seconds
                    Location loc = LocationManager.getLocationManager().getCurrentLocationSync(20000);
                    ipDlg.dispose();
                    if (loc != null) {
                        double lat = loc.getLatitude();
                        double lng = loc.getLongitude();
                        tlat.setText(""+lat);
                            tlon.setText(""+lng);
                        
                    } else {
                        Dialog.show("GPS error", "Your location could not be found, please try going outside for a better GPS signal", "Ok", null);
                    }
                }
            }
        });
        
        
        aloc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                if(tlat.getText().length() < 1)
                {
                    return;
                }
                final Form d = new Form("Viewing Location");
                Command b = new Command("")
                {
                    public void actionPerformed(ActionEvent v)
                    {
                       tabs.setSelectedIndex(selectedIndex);
                       form.show();
                    }
                };
                Image img = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand"));
                b.setIcon(img);
                d.getToolbar().setTitleCentered(false);
                d.getToolbar().addCommandToLeftBar(b);
                d.setBackCommand(b);
                d.setLayout(new BorderLayout());
                final MapContainer mc = new MapContainer("AIzaSyBFSARzsYO1ZkQ1OAw27q8gXqPeECz36uU");
                //mc.setMapType(MapContainer.MAP_TYPE_TERRAIN);
                Coord lastLocation = new Coord(Double.parseDouble(tlat.getText()), Double.parseDouble(tlon.getText()));
                mc.setCameraPosition(lastLocation);
                Image i = res.getImage("pin.png");
                
                //FontImage b1 = FontImage.createMaterial(FontImage.MATERIAL_LOCATION_ON, bb.getStyle());

                mc.addMarker(EncodedImage.createFromImage(i,false), lastLocation, "Hi marker", "Optional long description", new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            //Dialog.show("Marker Clicked!", "You clicked the marker", "OK", null);
                        }
                    });
                
                
                //mc.addLayer(pl);
                //mc.zoomToLayers();
                mc.zoom(lastLocation, 15);
                d.addComponent(BorderLayout.CENTER, mc);
                d.show();
                mc.setVisible(true);mc.repaint();
                
            }
        });
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    Image getMatrialImage(char charcode) {
        int iconWidth = Display.getInstance().convertToPixels(8, true);
        Style iconStyle = new Style();
        Font iconFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        if (Font.isNativeFontSchemeSupported()) {
            iconFont = Font.createTrueTypeFont("native:MainLight", null).derive((int)(iconWidth * 0.5), Font.STYLE_PLAIN);
        }
        iconStyle.setFont(iconFont);
        //iconStyle.setFgColor(0xffffff);
        //iconStyle.setBgTransparency(0);

        FontImage completeIcon = FontImage.createMaterial(charcode, iconStyle);
        return completeIcon;
    }
    
    public Container getSpinnerContainer(final Tabs tabs)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        Picker t = new Picker();
        String[] vi = new String[1000];
        for(int x=0; x<1000;x++)
        {
            vi[x] = x+"";
        }
        t.setType(Display.PICKER_TYPE_STRINGS);
        
        t.setStrings(vi);
        
        //t.setUIID("ComponentTextArea");
        //t.setRows(3);
        c.addComponent(t);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            try{t.setSelectedString(value);}catch(Exception er){}
        }
        
        if(isRider.length() > 5)
        {
            Vector v = getRiderMap();
            logic.addLogicForSpinner(t,v, tabs, xdata);
        }
        
        
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getOnOffContainer()
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        OnOffSwitch t = new OnOffSwitch();
        t.setOff("NO");
        t.setOn("YES");
        //t.setUIID("ComponentTextArea");
        //t.setRows(3);
        c.addComponent(t);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            String on = t.getOn();
            if(value.trim().equals(on))
            {
                t.setComponentState(true);
            }
            else
            {
                t.setComponentState(false);
            }
            
        }
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getAudioContainer(final Resources res)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        final Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        Button t = new Button("");
        t.setUIID("FontSize1");
        FontImage.setMaterialIcon(t, FontImage.MATERIAL_MIC);
        t.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                recordAudio(c);
            }
        });
        //t.setUIID("ComponentTextArea");
        //t.setRows(3);
        c.addComponent(t);
        final String p = this.readFromDevice(type);
        if(isNew == false && p != null & !p.equals(""))
        {
            
            if(p != null && !p.equals(""))
            {
                final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                final Container bt = new Container(new GridLayout(1,2));
            Button play = new Button();
            play.setUIID("Gray3A");
            FontImage.setMaterialIcon(play, FontImage.MATERIAL_PLAY_ARROW);
            play.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent v)
                {
                    try 
                    {

                                try {
                                    
                                    
        final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                            Media m = MediaManager.createMedia(p, false);
                            m.play();
                        } catch(IOException err) {
                             err.printStackTrace();
                        }
                              
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
            bt.addComponent(play);
            
            
            Button delete = new Button();
            delete.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent v)
                {
                    boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
                    if(!confirm)
                    {
                         return;
                    }
                    String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                    FileSystemStorage.getInstance().delete(p);
                    c.removeComponent(bt);
                    c.revalidate();
                }
            });
            delete.setUIID("Gray4A");
            FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
            bt.addComponent(delete);
            c.addComponent(bt);
        }
        }
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    
    
    public void recordVideo(final Container retVal, final Tabs tabs, final Form form, final int index)
    {
        String file = Capture.captureVideo();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
        FileSystemStorage fs = FileSystemStorage.getInstance();
            if(file != null) {
                try
                {
                    final String filePath = p;
                    Util.copy(fs.openInputStream(file), fs.openOutputStream(filePath));
                    final Container bt = new Container(new GridLayout(1,2));
                    Button play = new Button();
                    play.setUIID("Gray3A");
                    FontImage.setMaterialIcon(play, FontImage.MATERIAL_PLAY_ARROW);
                    bt.addComponent(play);
                    play.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                            String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                            VideoPlayer(p, tabs, index, form);
                        }
                    });
                    
                    Button delete = new Button();
                    delete.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
                            if(!confirm)
                            {
                                 return;
                            }
                            String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                            FileSystemStorage.getInstance().delete(p);
                            retVal.removeComponent(bt);
                            retVal.revalidate();
                        }
                    });
                    delete.setUIID("Gray4A");
                    FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
                    bt.addComponent(delete);
                    
                    retVal.add(bt);
                    retVal.revalidate();
                }catch(Exception er){}
            }
    }
    
    
    
    
    
    
    
    public void recordAudio(final Container retVal)
    {
        String file = Capture.captureAudio();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
        FileSystemStorage fs = FileSystemStorage.getInstance();
            if(file != null) {
                try
                {
                    final String filePath = p;
                    Util.copy(fs.openInputStream(file), fs.openOutputStream(filePath));
                    final Container bt = new Container(new GridLayout(1,2));
                    Button play = new Button();
                    play.setUIID("Gray3A");
                    FontImage.setMaterialIcon(play, FontImage.MATERIAL_PLAY_ARROW);
                    play.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                        try {
                            Media m = MediaManager.createMedia(filePath, false);
                            m.play();
                        } catch(IOException err) {
                             err.printStackTrace();
                        }}
                    });
                    bt.addComponent(play);
                    
                    Button delete = new Button();
            delete.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent v)
                {
                    boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
                    if(!confirm)
                    {
                         return;
                    }
                    String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                    FileSystemStorage.getInstance().delete(p);
                    retVal.removeComponent(bt);
                    retVal.revalidate();
                }
            });
            delete.setUIID("Gray4A");
            FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
            bt.addComponent(delete);
                    
                    retVal.addComponent(bt);
                    retVal.revalidate();
                }catch(Exception er){}
            }
    }
    
    
    
    
    private void startRecording(final Container retVal, final Resources res) {
        try {
            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
            final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
            final Media media = MediaManager.createMediaRecorder(p);
            media.play();

            final Dialog d = new Dialog("Recording");

            d.placeButtonCommands(new Command[]{new Command("Ok") {

                    public void actionPerformed(ActionEvent evt) {
                        media.pause();
                        media.cleanup();
                        d.dispose();
                        Save save = new Save();
                        save.writeToDevice(type, p);
                        Button play = new Button();
                        play.setIcon(res.getImage("741-execute.png"));
                        play.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent v)
                            {
                                try 
                                {
                                    final InputStream is = FileSystemStorage.getInstance().openInputStream(p);
                                    final Media m = MediaManager.createMedia(is, MediaManager.getMediaRecorderingMimeType());
                                    m.play();
                                    final Dialog d = new Dialog("Playing");
                                    d.placeButtonCommands(new Command[]{new Command("Ok") {

                                            public void actionPerformed(ActionEvent evt) {
                                                try {
                                                    is.close();
                                                } catch (IOException ex) {
                                                    ex.printStackTrace();
                                                }
                                                m.pause();
                                                m.cleanup();
                                                d.dispose();
                                            }
                                        }});
                                    d.show();

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        retVal.addComponent(play);
                        /*
                        retVal.addComponent(new Button(new Command("Play recording " + retVal.getComponentCount()) {

                            public void actionPerformed(ActionEvent evt) {
                                
                            }
                        }));
                                */
                    }
                }});
            d.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getTempFileName() {
        String[] roots = FileSystemStorage.getInstance().getRoots();
        // iOS doesn't have an SD card
        String root = roots[0];
        for (int i = 0; i < roots.length; i++) {
            if (FileSystemStorage.getInstance().getRootType(roots[i]) == FileSystemStorage.ROOT_TYPE_SDCARD) {
                root = roots[i];
                break;
            }
        }
        return root + FileSystemStorage.getInstance().getFileSystemSeparator() + "audioSample" + System.currentTimeMillis();
    }
    
    
    protected void setBackCommand(Form f, Form source) {
        Command back = new Command("") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                source.showBack();
            }

        };
        Image img = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand"));
        back.setIcon(img);
        f.getToolbar().addCommandToLeftBar(back);
        f.getToolbar().setTitleCentered(false);
        f.setBackCommand(back);
    }
    
    
    
    public void VideoPlayer(final String toPlay, final Tabs tabs, final int selectedIndex, final Form form)
    {
        final Form hi = new Form("MediaPlayer", new BorderLayout());
        setBackCommand(hi, form);
        hi.setLayout(new BorderLayout());

                try {
                    Media video = MediaManager.createMedia(toPlay, true);
                    hi.removeAll();
                    hi.add(BorderLayout.CENTER, new MediaPlayer(video));
                    hi.revalidate();
                } catch(IOException err) {
                    Log.e(err);
                } 
                hi.setTitle("Video Player");
                hi.show();

    }
    
    
    public Container getVideoContainer(final Resources res, final Tabs tabs, final Form form, final int selectedIndex)
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        final Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        Button t = new Button("");
        t.setUIID("FontSize1");
        FontImage.setMaterialIcon(t, FontImage.MATERIAL_VIDEOCAM);
        t.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                recordVideo(c, tabs, form, selectedIndex);
            }
        });
        c.addComponent(t);
        final String pp = this.readFromDevice(type);
        if(isNew == false && pp != null & !pp.equals(""))
        {
            final Container bt = new Container(new GridLayout(1,2));
                    Button play = new Button();
                    play.setUIID("Gray3A");
                    FontImage.setMaterialIcon(play, FontImage.MATERIAL_PLAY_ARROW);
                    bt.addComponent(play);
                    
                    play.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                            String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                            VideoPlayer(p, tabs, selectedIndex, form);
                        }
                    });
                    
                    Button delete = new Button();
                    delete.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
                            if(!confirm)
                            {
                                 return;
                            }
                            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                            String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+type;
                            FileSystemStorage.getInstance().delete(p);
                            c.removeComponent(bt);
                            c.revalidate();
                        }
                    });
                    delete.setUIID("Gray4A");
                    FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
                    bt.addComponent(delete);
                    
                    c.add(bt);
                    c.revalidate();
            
        }
        //t.setUIID("ComponentTextArea");
        //t.setRows(3);
        
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    public Container getBarcodeContainer()
    {
        Container main = new Container(new BorderLayout());
        Container top = new Container(new FlowLayout(Component.RIGHT));top.setName("none");
        Label r = new Label();
        if(required.equals("true"))
        {
            r.setUIID("ErrorLabel");
            r.setText("Required");
        }
        else
        {
            r.setUIID("SuccessLabel");
            r.setText("Not Required");
        }
        top.addComponent(r);
        main.addComponent(BorderLayout.NORTH, top);
        
        Container  c = new Container();
        c.setName(type);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));c.setScrollableY(true);
        SpanLabel title = new SpanLabel();title.setName("title");
        title.setText(caption);
        c.addComponent(title);
        Accordion ac = new Accordion();
        Button t = new Button("Scan Barcode");
        Button t1 = new Button("Scan QRCode");
        ac.addContent("Barcode", t);
        ac.addContent("QRCode", t1);
        c.addComponent(ac);
        final TextField f = new TextField();
        //f.setEditable(false);
        if(isNew == false)
        {
            String value = this.readFromDevice(type);
            f.setText(value);
        }
        c.addComponent(f);
        t.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                scanBarCode(f);
                
            }
            
        });
        
        t1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                scanQR(f);
            }
            
        });
        
        
        //t.setUIID("ComponentTextArea");
        //t.setRows(3);
        Label req = new Label("");
            req.setName("req");
            if(required.equals("true"))
            {
              req.setText(" ");
            }
            c.addComponent(req);
        main.addComponent(BorderLayout.CENTER, c);Container fname = new Container();fname.setName("#@&"+fullName);main.addComponent(BorderLayout.SOUTH, fname);
        return main;
    }
    
    
    public void scanBarCode(final TextField f)
    {
        CodeScanner.getInstance().scanBarCode(new ScanResult() {

        public void scanCompleted(String contents, String formatName, byte[] rawBytes) {
            f.setText(contents);
        }

        public void scanCanceled() {
            System.out.println("cancelled");
        }

        public void scanError(int errorCode, String message) {
            System.out.println("err " + message);
        }
    });  
    }
    
    
    public void scanQR(final TextField f)
    {
        CodeScanner.getInstance().scanQRCode(new ScanResult() {

        public void scanCompleted(String contents, String formatName, byte[] rawBytes) {
            f.setText(contents);
        }

        public void scanCanceled() {
            System.out.println("cancelled");
        }

        public void scanError(int errorCode, String message) {
            System.out.println("err " + message);
        }
    });
    }
    
    
    public String readFromDevice(String fileName)
    {
        String result = "";
        try{
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String file = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry+sep+fileName;
        InputStream in = FileSystemStorage.getInstance().openInputStream(file);
        if (in != null)
        {
            try
            {
                result = Util.readToString(in);
                in.close();
            }
            catch (IOException ex)
            {
                System.out.println(ex);
            }
        }}catch(Exception ee){}
        return result;
    }
    
    
    
    
}
