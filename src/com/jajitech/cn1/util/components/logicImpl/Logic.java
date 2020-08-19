/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util.components.logicImpl;

import com.codename1.components.SpanLabel;
import com.codename1.io.Storage;
import com.codename1.l10n.DateFormat;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.spinner.Picker;
import com.jajitech.cn1.util.Helper;
import com.jajitech.cn1.util.components.LCheckBox;
import com.jajitech.cn1.util.components.LRadioButton;
import com.jajitech.xdata.mobile.XDataMobile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author lukman
 */
public class Logic {

    Helper helper;
    XDataMobile main;
    boolean st;

    public Logic() {
        helper = new Helper();
    }

    public Logic(Map tabElements) {
        this.tabElements = tabElements;
    }
    
    
    public  boolean equalLists(List one, List two){     
    if (one == null && two == null){
        System.out.println("they were null");
        return true;
    }

    if((one == null && two != null) 
      || one != null && two == null
      || one.size() != two.size()){
        System.out.println("they were are not equal oo");
        return false;
    }

    //to avoid messing the order of the lists we will use a copy
    //as noted in comments by A. R. S.
    
    
    for(Object t: one)
    {
        System.out.println("1: "+t);
    }
    
    for(Object t: two)
    {
        System.out.println("2: "+t);
    }
    
    one = new ArrayList(one);
    two = new ArrayList(two);   

    Collections.sort(one);
    Collections.sort(two);
    return one.equals(two);
}
    

    public void addLogicForCheckBox(Vector radio, Vector details, Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String p[] = helper.split(param, ";;");
            List pitems = new ArrayList();
            for (String y : p) {
                System.out.println("pitems " + y);
                pitems.add(y.trim());
            }
            pitems.remove("");
            pitems.remove(" ");
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            String result = "";
            List selected = new ArrayList();
            String y[] = helper.split(affected, "#@");
            boolean hidden = this.checkIsElementHidden(y[1]);
            if(hidden == true)
            {
                selected.addAll(pitems);
            }

            for (Object rb : radio) {
                LCheckBox l = (LCheckBox) rb;
                l.addListener((ActionEvent e) -> {
                    {
                        CheckBox rbutton = l.getLCheckBox();
                        if (rbutton.isSelected()) {
                            rbutton.setSelected(false);
                            selected.remove(l.getText());
                        } else {
                            rbutton.setSelected(true);
                            selected.add(l.getText());
                        }

                        boolean finalCondition = false;
                        boolean stop = false;
                        //System.out.println("you are a computer you are supposed to be smarter than me");
                        if (c.equals("contains any")) {
                            for (Object t : pitems) {
                                if (stop == false) {
                                    if (selected.contains(t)) {
                                        stop = true;
                                        finalCondition = true;
                                    }
                                }
                            }
                        }

                        if (c.equals("contains all")) {
                            
                            finalCondition = this.equalLists(selected, pitems);
                        }
                        
                        
                        System.out.println("final condition " + finalCondition + "and selected size is " + selected.size() + " and ritems size is " + pitems.size() );
                        if (finalCondition == true) {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, false, "");
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, false, xdata);
                            }
                        } else {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, true, af[0]);
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, true, xdata);
                            }

                        }
                    }
                });

            }
        }
        st = false;
    }

    public void addLogicForRadioButton(Vector radio, Vector details, Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            String result = "";
            for (Object rb : radio) {
                LRadioButton l = (LRadioButton) rb;
                l.addListener(e -> {
                    {
                        RadioButton rbutton = l.getLRadioButton();
                        if (rbutton.isSelected()) {

                        } else {
                            rbutton.requestFocus();
                            rbutton.setSelected(true);
                        }

                        if (l.getText().trim().equals(param.trim())) {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, false, "");
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, false, xdata);
                            }
                        } else {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, true, af[0]);
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, true, xdata);
                            }

                        }
                    }
                });

            }
        }
    }
    
    
    public void addLogicForComboBox(ComboBox l, Vector details, Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            String result = "";
                l.addActionListener(e -> {
                    {
                        if (l.getSelectedItem().equals(param.trim())) {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, false, "");
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, false, xdata);
                            }
                        } else {
                            if (action.equals("Caption")) {
                                changeElementCaption(affected, items, tab, true, af[0]);
                            }

                            if (action.equals("Hide")) {
                                hideElement(affected, tab, true, xdata);
                            }
                        }
                    }
                });
        }
    }
    
    
    
    
    

    public void addLogicForTextField(TextField text, Vector details, final Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            text.addDataChangedListener((int type, int index) -> {
                boolean finalCondition = false;
                if (c.equals("="))
                {
                    if(text.getText().trim().equals(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">"))
                {
                    if(Integer.parseInt(text.getText().trim()) > Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">="))
                {
                    if(Integer.parseInt(text.getText().trim()) >= Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                if (c.equals("<"))
                {
                    if(Integer.parseInt(text.getText().trim()) < Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals("<="))
                {
                    if(Integer.parseInt(text.getText().trim()) <= Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                    System.out.println(text.getText() + "     " + param);
                    if (finalCondition == true) {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, false, "");
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, false, xdata);
                        }
                    } else {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, true, af[0]);
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, true, xdata);
                        }

                    
                }
            });

        }
    }
    
    
    
    
    public void addLogicForSpinner(Picker text, Vector details, final Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            
            text.addActionListener(e -> {
                boolean finalCondition = false;
                if (c.equals("="))
                {
                    if(text.getText().trim().equals(param.trim()))
                    {
                        finalCondition = true; 
                    }
                }
                
                if (c.equals(">"))
                {
                    if(Integer.parseInt(text.getText().trim()) > Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">="))
                {
                    if(Integer.parseInt(text.getText().trim()) >= Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                if (c.equals("<"))
                {
                    if(Integer.parseInt(text.getText().trim()) < Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals("<="))
                {
                    if(Integer.parseInt(text.getText().trim()) <= Integer.parseInt(param.trim()))
                    {
                        finalCondition = true;
                    }
                }
                
                    System.out.println(text.getText() + "     " + param);
                    if (finalCondition == true) {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, false, "");
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, false, xdata);
                        }
                    } else {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, true, af[0]);
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, true, xdata);
                        }
                }
            });
            

        }
    }
    
    
    
    
    public void addLogicForTime(Picker text, Vector details, final Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
            Picker util = new Picker();
            util.setType(Display.PICKER_TYPE_TIME);
            util.setText(param);
            int hrs = 0;
            int min = 0;
            System.out.println("this is c "+c);
            if(!c.equals("between"))
            {
                String h[] = helper.split(param,":");
                hrs = Integer.parseInt(h[0].trim());
                min = Integer.parseInt(h[1].trim());
                util.setTime(hrs, min);
            }
            else
            {
                System.out.println("this is param "+param);
            }
            
            text.addActionListener(e -> {
                boolean finalCondition = false;
                int val = text.getTime();
                int paramValue = util.getTime();
                System.out.println("this is value "+val+" and this is time "+text.getText());
                System.out.println("this is paramValue "+paramValue+" and param is "+param);
                if (c.equals("="))
                {
                    if(val == paramValue)
                    {
                        finalCondition = true; 
                    }
                }
                
                if (c.equals(">"))
                {
                    if(val > paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">="))
                {
                    if(val >= paramValue)
                    {
                        finalCondition = true;
                    }
                }
                if (c.equals("<"))
                {
                    if(val < paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals("<="))
                {
                    if(val <= paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if(c.equals("between"))
                {
                    String g[] = helper.split(param, "and");
                    String p[] = helper.split(g[0].trim(), ":");
                    int minHr = Integer.parseInt(p[0].trim());
                    int minMn = Integer.parseInt(p[1].trim());
                    util.setTime(minHr, minMn);
                    int fMin = util.getTime();
                    String pp[] = helper.split(g[1].trim(), ":");
                    int maxHr = Integer.parseInt(pp[0].trim());
                    int maxMn = Integer.parseInt(pp[1].trim());
                    util.setTime(maxHr, maxMn);
                    int fMax = util.getTime();
                    if(val > fMin && val < fMax)
                    {
                        finalCondition = true;
                    }
                    
                }
                
                    System.out.println(text.getText() + "     " + util.getTime());
                    if (finalCondition == true) {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, false, "");
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, false, xdata);
                        }
                    } else {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, true, af[0]);
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, true, xdata);
                        }
                }
            });
            

        }
    }
    
    
    
    
    
    
    
    public void addLogicForDate(Picker text, Vector details, final Tabs tab, XDataMobile xdata) {
        for (Object o : details) {
            Map m = (Map) o;
            String condition = m.get("condition").toString();
            System.out.println(condition);
            final String c = condition;
            String param = m.get("param").toString();
            String affected = m.get("affected").toString();
            String items = m.get("items").toString();
            String action = m.get("action").toString();
            String af[] = helper.split(affected, "#@");
           Picker util = new Picker();
            util.setType(Display.PICKER_TYPE_DATE);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Map map = helper.parse(param);
            Calendar cc = Calendar.getInstance();
            cc.set(Calendar.YEAR, Integer.parseInt(map.get("year").toString()));
            cc.set(Calendar.MONTH, Integer.parseInt(map.get("month").toString())-1);
            cc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(map.get("day").toString()));
            cc.set(Calendar.HOUR_OF_DAY, 0);
            cc.set(Calendar.MINUTE, 0);
            cc.set(Calendar.SECOND, 0);
            cc.set(Calendar.MILLISECOND, 0);
            util.setDate(cc.getTime());
            System.out.println("this is the date "+util.getDate());
            text.addActionListener(e -> {
                boolean finalCondition = false;
                Date dt = text.getDate();
                Calendar cc1 = Calendar.getInstance();
                cc1.setTime(dt);
                cc1.set(Calendar.HOUR_OF_DAY, 0);
                cc1.set(Calendar.MINUTE, 0);
                cc1.set(Calendar.SECOND, 0);
                cc1.set(Calendar.MILLISECOND, 0);
                long val = cc1.getTime().getTime();
                long paramValue = util.getDate().getTime();
                System.out.println("this is value "+val+" = "+cc1.getTime());
                System.out.println("this is paramValue "+paramValue+" = "+util.getDate());
                if (c.equals("="))
                {
                    if(val == paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">"))
                {
                    if(val > paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals(">="))
                {
                    if(val >= paramValue)
                    {
                        finalCondition = true;
                    }
                }
                if (c.equals("<"))
                {
                    if(val < paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if (c.equals("<="))
                {
                    if(val <= paramValue)
                    {
                        finalCondition = true;
                    }
                }
                
                if(c.equals("between"))
                {
                    String g[] = helper.split(param, "and");
                    Map sdate = helper.parse(g[0].trim());
                    Map edate = helper.parse(g[1].trim());
                    long fMin = 0;
                    long fMax = 0;
                    Calendar cc2 = Calendar.getInstance();
                    cc2.set(Calendar.YEAR, Integer.parseInt(sdate.get("year").toString()));
                    cc2.set(Calendar.MONTH, Integer.parseInt(sdate.get("month").toString())-1);
                    cc2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sdate.get("day").toString()));
                    cc2.set(Calendar.HOUR_OF_DAY, 0);
                    cc2.set(Calendar.MINUTE, 0);
                    cc2.set(Calendar.SECOND, 0);
                    cc2.set(Calendar.MILLISECOND, 0);
                    System.out.println("this is date 1 "+cc2.getTime() + " its value is "+cc2.getTime().getTime());
                    fMin = cc2.getTime().getTime();
                    
                    cc2.set(Calendar.YEAR, Integer.parseInt(edate.get("year").toString()));
                    cc2.set(Calendar.MONTH, Integer.parseInt(edate.get("month").toString())-1);
                    cc2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(edate.get("day").toString()));
                    cc2.set(Calendar.HOUR_OF_DAY, 0);
                    cc2.set(Calendar.MINUTE, 0);
                    cc2.set(Calendar.SECOND, 0);
                    cc2.set(Calendar.MILLISECOND, 0);
                    System.out.println("this is date 2 "+cc2.getTime() + " its value is "+cc2.getTime().getTime());
                    fMax = cc2.getTime().getTime();
                    
                    if(val > fMin && val < fMax)
                    {
                        finalCondition = true;
                    }

                    
                } 
                
                    
                    if (finalCondition == true) {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, false, "");
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, false, xdata);
                        }
                    } else {
                        if (action.equals("Caption")) {
                            changeElementCaption(affected, items, tab, true, af[0]);
                        }

                        if (action.equals("Hide")) {
                            hideElement(affected, tab, true, xdata);
                        }
                }
            });

        }
    }
    
    
    
    
    

    int elementIndex = -1;

    public boolean checkIsElementHidden(String elementName) {
        String e[] = Storage.getInstance().listEntries();
        for (String m : e) {
            if (m.startsWith("hide_")) {
                String d[] = helper.split(m, "_");
                if (elementName.equals(d[1].trim())) {
                    elementIndex = Integer.parseInt(d[2]);
                    return true;
                }
            }
        }
        return false;
    }

    public void hideElement(String affected, Tabs tab, boolean isReverse, XDataMobile xdata) {
        System.out.println("this is affected " + affected);
        String y[] = helper.split(affected, "#@");
        String affectedTitle = y[0];
        String affectedElement = y[1];
        int index = tab.getSelectedIndex();
        if (isReverse == false) {
            for (int x = 0; x < tab.getTabCount(); x++) {
                String t = tab.getTabTitle(x);
                if (t.startsWith(affectedElement)) {
                    Component v = tab.getTabComponentAt(x);
                    Object o = (Object) v;
                    Map m = new HashMap();
                    m.put("title", t);
                    Object b = (Object) m;
                    Storage.getInstance().writeObject("hide_" + t, b);
                    tab.removeTabAt(x);
                    tab.setSelectedIndex(index);
                    tab.revalidate();
                }
            }
        } else {
            boolean hidden = checkIsElementHidden(y[1]);
            if (hidden == true) {
                Map map = xdata.getTabElements();
                Container container = (Container) map.get(y[1] + "_" + elementIndex);
                String h = y[1];
                container.setName(y[1]);
                int originalIndex = elementIndex;
                boolean restored = false;
                int newIndex = -1;
                for (int c = 0; c < tab.getTabCount(); c++) {
                    System.out.println("this is tab title " + tab.getTabTitle(c));
                    if (!tab.getTabTitle(c).equals("End")) {
                        String ti[] = helper.split(tab.getTabTitle(c), "_");
                        int tabInt = Integer.parseInt(ti[1]);
                        if (originalIndex > tabInt) {
                            System.out.println("i m greater than you " + c);
                            newIndex = c + 1;
                        } else {
                            if (restored == false) {
                                System.out.println("Ooopd....you greater oooo " + c);
                                restored = true;
                                newIndex = c;
                            }
                        }
                    }
                }
                tab.insertTab(h + "_" + elementIndex, null, container, newIndex);
                tab.getComponentForm().revalidate();
                Storage.getInstance().deleteStorageFile("hide_" + y[1] + "_" + elementIndex);
            }
        }

    }

    public void changeElementCaption(String elementID, String caption, Tabs tab, boolean isReverse, String af) {
        for (int x = 0; x < tab.getTabCount(); x++) {
            Container c = (Container) tab.getContentPane().getComponentAt(x);
            for (int count = 0; count < c.getComponentCount(); count++) {
                Component c1 = c.getComponentAt(count);
                if (c1 instanceof Container) {
                    Container cont = (Container) c1;
                    try {
                        String u[] = helper.split(elementID, "#@");
                        if (cont.getName().startsWith(u[1])) {
                            for (int a = 0; a < cont.getComponentCount(); a++) {
                                Component cap = cont.getComponentAt(a);
                                if (cap instanceof SpanLabel && cap.getName().equals("title")) {
                                    SpanLabel label = (SpanLabel) cap;
                                    if (isReverse == false) {
                                        label.setText(caption);
                                    } else {
                                        label.setText(af);
                                    }
                                }
                            }
                        }
                    } catch (Exception er) {
                    }
                }
            }
        }
    }

    public int getSize() {
        return tabElements.size();
    }

    Map tabElements;

    public void prepareLogic(Tabs tab, XDataMobile main) {
        System.out.println("redefined tabs size from logic " + this.getSize());
        this.main = main;
        this.tabElements = main.tabElements;
        for (int x = 0; x < tab.getTabCount(); x++) {
            Container c = (Container) tab.getContentPane().getComponentAt(x);
            for (int count = 0; count < c.getComponentCount(); count++) {
                Component c1 = c.getComponentAt(count);
                if (c1 instanceof Container) {
                    try {
                        Container cont = (Container) c1;
                        if (cont.getName().startsWith("#@&")) {
                            //System.out.println("this is full "+cont.getName());
                            String name[] = helper.split(cont.getName(), "##");
                            String type = name[0];
                            String ya[] = helper.split(type, "#@&");
                            type = ya[1];
                            String caption = name[1];
                            String items = name[2];
                            String required = name[3];
                            String rider = name[4];
                            System.out.println("this is rider " + rider);
                            if (rider.length() > 5) {
                                System.out.println("this is type " + type + " and items " + items);
                                if (type.startsWith("textfield") && items.equals("Numeric")) {
                                    for (int y = 0; y < cont.getComponentCount(); y++) {
                                        Component tx = cont.getComponentAt(y);
                                        if (tx instanceof TextField) {
                                            TextField t = (TextField) tx;
                                            System.out.println("i am in there");
                                            //Vector v = this.getLogicMap(rider);
                                            //this.addLogicForTextField(t,v, tab);
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception er) {
                    }

                }
            }
        }
    }

    public Vector getLogicMap(String isRider) {
        Vector result = new Vector();
        String p[] = helper.split(isRider, "@#_#@");
        for (String y : p) {
            if (y.length() > 3) {
                System.out.println("this is y " + y);
                String rider[] = helper.split(y, ";;;;");
                System.out.println("this is r size " + rider.length);
                Map m = new HashMap();
                System.out.println("this is r con " + rider[0]);
                System.out.println("this is r paeam  " + rider[1]);
                m.put("condition", rider[0]);
                m.put("param", rider[1]);
                m.put("action", rider[2]);
                String a[] = helper.split(rider[3], "#@");
                m.put("affected", a[1]);
                m.put("items", rider[4]);
                result.add(m);
            }
        }
        return result;
    }

}
