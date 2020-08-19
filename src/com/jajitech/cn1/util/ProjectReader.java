/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jajitech.cn1.util;

import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Util;
import com.codename1.io.tar.TarEntry;
import com.codename1.io.tar.TarInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Jaji
 */
public class ProjectReader {
    
    public ProjectReader()
    {
        
    }
    
    
    
    public Map read(String project, char sep, String dir)
    {
        System.out.println("okay..."+dir + sep + project);
        Map result = new HashMap();
        try
                    {
                        TarInputStream tis = null;
                        try
                        {
                            tis = new TarInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + project));
                        }
                        catch(Exception er)
                        {
                            Map map = new HashMap();
                            map.put("error","error");
                            return map;
                        }
                        TarEntry e = null;
                        while ( (e = tis.getNextEntry())!= null)
                        {
                            if ( e.isDirectory() ){
                                continue;
                            }
                            String name = e.getName();
                            name = name.substring(name.lastIndexOf("/")+1);
                            if ( name.equals("project.jaj"))
                            {
                                String contents = Util.readToString(tis);
                                //System.out.println(contents);
                                InputStream input = new ByteArrayInputStream(contents.getBytes());
                                JSONParser parser = new JSONParser();
                                Hashtable response = null;
                                try
                                {
                                    response = parser.parse(new InputStreamReader(input));
                                    Enumeration en = response.elements();
                                     while (en.hasMoreElements())
                                     {
                                         Vector v = (Vector)en.nextElement();
                                         Enumeration ee = v.elements(); //
                                         while (ee.hasMoreElements()) 
                                         {
                                             final Hashtable ht = (Hashtable) ee.nextElement(); 
                                             result.put("name", ht.get("name").toString());
                                             result.put("sections", ht.get("sections").toString());
                                             result.put("code", ht.get("id").toString());
                                             result.put("access", ht.get("access").toString());
                                             result.put("user", ht.get("user").toString());
                                         }
                                    }
                                } catch (IOException ex){
                                    throw new RuntimeException(ex.getMessage());
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
        return result;
    }
    
    
    
    
    public List readSectionAccess(String project, char sep, String dir)
    {
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        try
                    {
                        TarInputStream tis = null;
                        try
                        {
                            tis = new TarInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + project));
                        }
                        catch(Exception er)
                        {
                            Map map = new HashMap();
                            map.put("error","error");
                            return result;
                        }
                        TarEntry e = null;
                        while ( (e = tis.getNextEntry())!= null)
                        {
                            if ( e.isDirectory() ){
                                continue;
                            }
                            String name = e.getName();
                            name = name.substring(name.lastIndexOf("/")+1);
                            if ( name.equals("accessList.jaj"))
                            {
                                String contents = Util.readToString(tis);
                                //System.out.println(contents);
                                InputStream input = new ByteArrayInputStream(contents.getBytes());
                                JSONParser parser = new JSONParser();
                                Hashtable response = null;
                                try
                                {
                                    response = parser.parse(new InputStreamReader(input));
                                    Enumeration en = response.elements();
                                     while (en.hasMoreElements())
                                     {
                                         Vector v = (Vector)en.nextElement();
                                         Enumeration ee = v.elements(); //
                                         while (ee.hasMoreElements()) 
                                         {
                                             final Hashtable ht = (Hashtable) ee.nextElement(); 
                                             Map map = new HashMap();
                                             map.put("code", ht.get("code").toString());
                                             map.put("user", ht.get("user").toString());
                                             String sec = ht.get("section").toString();
                                             map.put("section", sec);
                                             result.add(map);
                                         }
                                    }
                                } catch (IOException ex){
                                    throw new RuntimeException(ex.getMessage());
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
        return result;
    }
    
    
    
    
    
    public List readSection(String project, String section, char sep, String dir)
    {
        List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        try
                    {
                        TarInputStream tis = null;
                        try
                        {
                            tis = new TarInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + project));
                        }
                        catch(Exception er)
                        {
                            Map map = new HashMap();
                            map.put("error","error");
                            //return map;
                        }
                        TarEntry e = null;
                        while ( (e = tis.getNextEntry())!= null)
                        {
                            if ( e.isDirectory() ){
                                continue;
                            }
                            String name = e.getName();
                            name = name.substring(name.lastIndexOf("/")+1);
                            if (name.equals(section+".jaj"))
                            {
                                String contents = Util.readToString(tis);
                                //System.out.println(contents);
                                InputStream input = new ByteArrayInputStream(contents.getBytes());
                                JSONParser parser = new JSONParser();
                                Hashtable response = null;
                                try {
                                    response = parser.parse(new InputStreamReader(input));
                                    Enumeration en = response.elements();
                                     while (en.hasMoreElements())
                                     {
                                         Vector v = (Vector)en.nextElement();
                                         Enumeration ee = v.elements(); //
                                         while (ee.hasMoreElements()) 
                                         {
                                             final Hashtable ht = (Hashtable) ee.nextElement();
                                             Map map = new HashMap();
                                             map.put("items", ht.get("items").toString());
                                             map.put("caption", ht.get("caption").toString());
                                             map.put("required", ht.get("required").toString());
                                             map.put("type", ht.get("type").toString());
                                             map.put("isRider", ht.get("rider").toString());
                                             result.add(map);
                                         }
                                    }
                                } catch (IOException ex){
                                    throw new RuntimeException(ex.getMessage());
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
        return result;
    }
    
    
    public String readInstructions(String project, String section, char sep, String dir)
    {
        String in = "";
        try
                    {
                        TarInputStream tis = null;
                        try
                        {
                            tis = new TarInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + project));
                        }
                        catch(Exception er)
                        {
                            Map map = new HashMap();
                            map.put("error","error");
                            //return map;
                        }
                        TarEntry e = null;
                        while ( (e = tis.getNextEntry())!= null)
                        {
                            if ( e.isDirectory() ){
                                continue;
                            }
                            String name = e.getName();
                            name = name.substring(name.lastIndexOf("/")+1);
                            if (name.equals(section+".yas"))
                            {
                                String contents = Util.readToString(tis);
                                in = contents;
                            }
                        }
                    }
        catch(Exception er)
        {
            
        }
        if(in.length() < 1)
        {
            in = "<html><head></head><body><b>No instruction was set for this section</b></body></html>";
        }
        return in;
    }
    
    
    public String getEntryCount(String code, String section)
    {
        String count = "0";
        try
        {
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section;
        String c[] = FileSystemStorage.getInstance().listFiles(dir);
        if(c != null)
        {
            count = c.length + "";
        }
        }
        catch(Exception er)
        {
            count = "0";
            er.printStackTrace();
        }
        return count;
    }
    
    
    public boolean isSingleEntry(String project, String section)
    {
        boolean result = false;
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
        Helper helper = new Helper();
        try
                    {
                        TarInputStream tis = null;
                        try
                        {
                            tis = new TarInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + project));
                        }
                        catch(Exception er)
                        {
                            Map map = new HashMap();
                            map.put("error","error");
                            //return map;
                        }
                        TarEntry e = null;
                        while ( (e = tis.getNextEntry())!= null)
                        {
                            if ( e.isDirectory() ){
                                continue;
                            }
                            String name = e.getName();
                            name = name.substring(name.lastIndexOf("/")+1);
                            System.out.println(name);
                            if (name.equals("singleList.jaj"))
                            {
                                String contents = Util.readToString(tis);
                                String values[] = helper.split(contents, "@@");
                                ArrayList lx = new ArrayList();
                                char old = ' ';
                                char rep = '_';
                                for(String ee: values)
                                {
                                    ee =ee.replace(old, rep);
                                    lx.add(ee.toLowerCase());
                                    System.out.println(ee);
                                }
                                section = section.replace(old, rep);
                                System.out.println("new section "+section);
                                if(lx.contains(section.toLowerCase()))
                                {
                                    result = true;
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
        return result;
    }
}
