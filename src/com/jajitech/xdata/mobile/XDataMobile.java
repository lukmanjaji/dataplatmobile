package com.jajitech.xdata.mobile;

import com.codename1.capture.Capture;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.BufferedInputStream;
import com.codename1.io.BufferedOutputStream;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.io.tar.TarEntry;
import com.codename1.io.tar.TarOutputStream;
import com.codename1.maps.Coord;
import com.codename1.maps.MapListener;
import com.codename1.messaging.Message;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Stroke;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.RoundRectBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.validation.Validator;
import com.codename1.util.StringUtil;
import com.jajitech.cn1.util.Helper;
import com.jajitech.cn1.util.ProjectReader;
import com.jajitech.cn1.util.components.Components;
import com.jajitech.cn1.util.components.logicImpl.Logic;
import com.jajitech.cn1.util.components.saver.Save;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.littlemonkey.connectivity.Connectivity;

/**
 * @author  Javalove
 */
public class XDataMobile {

    private Form current;
    String res = "-";
    String AMAZON_URL, URL;
    String response = "empty", componentName = "";
    private Resources theme;
    Label wlabel;
    private Form home, settings, projects, later;
    Container holder;
    private Object imageMask;
    private int maskWidth;
    private int maskHeight;
    private Object circleMask;
    private int circleMaskWidth;
    private int circleMaskHeight;
    String picture = "";
    private int[] colors;
    private Image[] colorBottoms;
    private int currentColor;
    boolean picAdded = false;
    String mres = "";
    boolean misc = false;
    Dialog ddd;
    Logic logic;
    String tres = "";

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);
        Toolbar.setOnTopSideMenu(true);
        // Pro only feature, uncomment if you have a pro subscription
        // Log.bindCrashProtection(true);
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        
       
        boolean x = this.checkIsUserSet();
        
        if(x == false)
        {
            //Toolbar.setGlobalToolbar(false);
            Form f = new Form("Setup");
            f.setLayout(new BorderLayout());
            f.addComponent("Center", this.buildProfileC(f));
            f.show();
            return;
        }
        
        
        buildSplash();
       
    }
    
    public void buildSplash()
    {
        /*
        Storage.getInstance().writeObject("LS", "https://dataplat.net");
        Storage.getInstance().writeObject("FL", "http://127.0.0.1");
        Form splash = new Form(new LayeredLayout());
        splash.setUIID("Splash");
        splash.getContentPane().setUIID("Container");
        splash.getToolbar().setUIID("Container");
        ScaleImageLabel iconBackground = new ScaleImageLabel(theme.getImage("4k.png"));
        iconBackground.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Container centerBackground = BorderLayout.center(iconBackground);
        splash.add(centerBackground);
        Label iconForeground = new Label(theme.getImage("icon.png"));
        Container centerIcon = BorderLayout.centerAbsolute(iconForeground);
        splash.add(centerIcon);
        
        splash.show();
        Display.getInstance().callSerially(() -> {
            ((BorderLayout)centerBackground.getLayout()).setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
            centerBackground.setShouldCalcPreferredSize(true);
            centerBackground.animateLayoutAndWait(350);
            
            iconForeground.remove();
            iconBackground.remove();
            centerIcon.remove();
            Container layers = LayeredLayout.encloseIn(
                    new Label(iconBackground.getIcon(), "CenterIcon"), 
                    new Label(iconForeground.getIcon(), "CenterIcon"));
            Container boxy = BoxLayout.encloseY(layers);
            Label placeholder = new Label();
            placeholder.setShowEvenIfBlank(true);
            Label kitchenSink = new Label("DataPlat", "SplashTitle");
            Component.setSameHeight(placeholder, kitchenSink);
            Component.setSameWidth(placeholder, kitchenSink, boxy);
            centerBackground.add(BorderLayout.CENTER, boxy);
            splash.revalidate();
            Display.getInstance().callSerially(() -> {
                placeholder.setText(" ");
                boxy.add(placeholder);
                boxy.setShouldCalcPreferredSize(true);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(placeholder, kitchenSink, CommonTransitions.createFade(500));
                
                Label newPlaceholder = new Label(" ");
                Label byCodenameOne = new Label("by LabAfrique", "SplashSubTitle");
                Component.setSameHeight(newPlaceholder, byCodenameOne);
                Component.setSameWidth(newPlaceholder, byCodenameOne);
                boxy.add(newPlaceholder);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(newPlaceholder, byCodenameOne, CommonTransitions.createFade(500));
                
                byCodenameOne.setY(splash.getHeight());                
                kitchenSink.setY(splash.getHeight());
                layers.setY(splash.getHeight());
                boxy.setHeight(splash.getHeight());
                
                boxy.animateUnlayoutAndWait(450, 20);
                splash.setTransitionOutAnimator(CommonTransitions.createEmpty());
                
                loadDefaults();
                boolean x = this.checkIsUserSet();
                
                if(x == false)
                {
                    Toolbar.setGlobalToolbar(false);
                    Form f = new Form("Setup");
                    f.setLayout(new BorderLayout());
                    f.addComponent("Center", this.buildProfileContainer(f));
                    f.show();
                    return;
                }
                else
                {
                    goHome();
                }
                
            });
        });
        */
        
        loadDefaults();
                boolean x = this.checkIsUserSet();
                
                if(x == false)
                {
                    Toolbar.setGlobalToolbar(false);
                    Form f = new Form("Setup");
                    f.setLayout(new BorderLayout());
                    f.addComponent("Center", this.buildProfileContainer(f));
                    f.show();
                    return;
                }
                else
                {
                    goHome();
                }
    }
    
    
    public boolean fetchJAJFile(String code)
    {
        boolean done = false;
         
            NetworkManager networkManager = NetworkManager.getInstance();
            networkManager.start();
            networkManager.addErrorListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent evt)
              {
                NetworkEvent n = (NetworkEvent) evt;
                n.getError().printStackTrace();
              }});
              //ConnectionRequest object   
              ConnectionRequest request = new ConnectionRequest()
              {
                int chr;
                StringBuffer sb = new StringBuffer();                 
                @Override
                protected void readResponse(InputStream input) throws IOException
                {
                  //reading the answer                      
                  while ((chr = input.read()) != -1)
                  {
                    sb.append((char) chr);
                  }
                  response = sb.toString();                                           
                  response = response.trim();
                  if(response.equals("not found"))
                  {
                      ToastBar.showErrorMessage("Project not found");
                      picAdded = false;
                  }
                  if(response.equals("found"))
                  {
                      picAdded = true;
                  }
                  if(response.startsWith("Error:"))
                  {
                      ToastBar.showErrorMessage(response);
                      picAdded = false;
                  }
                  
                  
                }
                @Override
                protected void handleException(Exception err)
                {
                  //An error occured - show a message:
                    err.printStackTrace();
                    System.out.println("handling error");
                    response = "error";
                  res = "error";
                   ToastBar.showErrorMessage("Error connecting to LS...");
                      picAdded = false;
                }
              };
              request.setUrl(URL+"prepare.jsp"); //servlet calling
              request.setPost(false);
              request.addArgument("code", code); //sending a the parameter Id to the servlet 
              ToastBar.showMessage("Establishing connection to LS...", FontImage.MATERIAL_DESKTOP_WINDOWS);
              networkManager.addToQueueAndWait(request);  
                  
        while(response.equals("empty"))
        {
        //waiting for the answer from the serlvet or jsp server
        }
        return picAdded;

    }
    
    
    
    public void loadDefaults()
    {
        try
        {
            
            URL  = Storage.getInstance().readObject("LS").toString();
            AMAZON_URL  = Storage.getInstance().readObject("FL").toString();
            String FLPort = Storage.getInstance().readObject("FLPort").toString();
            String LSPort = Storage.getInstance().readObject("LSPort").toString();
            boolean t = LSPort.length() > 1;
            System.out.println("LS port "+LSPort);
            try
            {
                if(FLPort.length() > 1)
                {
                    if(AMAZON_URL.endsWith("/"))
                    {
                        AMAZON_URL = AMAZON_URL+"#p";
                        AMAZON_URL = StringUtil.replaceAll(AMAZON_URL, "/#p", "");
                        AMAZON_URL = AMAZON_URL+":"+FLPort+"/";
                    }
                    else
                    {
                        AMAZON_URL = AMAZON_URL+":"+FLPort+"/";
                    }
                }
                int g = LSPort.length();
                System.out.println("length of ls port "+g);
                if(t == true)
                {
                    if(URL.endsWith("/"))
                    {
                        URL = URL+"#p";
                        URL = StringUtil.replaceAll(URL, "/#p", "");
                        URL = URL+":"+LSPort+"/";
                        System.out.println("1 LS server "+URL);
                    }
                    else
                    {
                        URL = URL+":"+LSPort+"/";
                    }
                }
                if(!URL.endsWith("/"))
                {
                    URL = URL+"/";
                }
                if(!AMAZON_URL.endsWith("/"))
                {
                    AMAZON_URL = AMAZON_URL+"/";
                }
                URL = URL+"xdataweb/";
                AMAZON_URL = AMAZON_URL+"dataplat/";
                System.out.println("2 LS server "+URL);
            }
            catch(Exception tt)
            {
                tt.printStackTrace();
            }
            if(URL.length() < 12 || AMAZON_URL.length() < 12)
            {
                ToastBar.showMessage("You are yet to set the LS and FL server. Please go to settings and enter the required arameters.", FontImage.MATERIAL_ERROR);
                Display.getInstance().vibrate(200);
                showLocalNotification("DataPlat Collector", "Please complete settings to use app..!", 2);
            }
        }
        catch(Exception ee)
        {
            
        }
            
    }
    
    public void showLocalNotification(String title, String body, int id)
    {
        LocalNotification n = new LocalNotification();
        n.setAlertBody(body);
        n.setAlertTitle(title);
        n.setId(""+id);
        n.setBadgeNumber(1);
        int repeatType = LocalNotification.REPEAT_NONE;
        Display.getInstance().scheduleLocalNotification(n, System.currentTimeMillis() + 10 * 1000, repeatType);
    }
    public void buildSideMenu()
    {
        Label l = new Label("DataPlat") {

            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(getX(), getY() + getHeight() - 1, getX() + getWidth(), getY() + getHeight() - 1);
            }
        };
        l.setUIID("Separator");

        Container cc = new Container();
        cc.setScrollableY(false);
        cc.setLayout(new BorderLayout());
        Label bb = new Label();
        
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
            final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"photo.jpg";
            Image profilePic = null;
            
            try
            {
                profilePic = Image.createImage(FileSystemStorage.getInstance().openInputStream(p));
                if(!FileSystemStorage.getInstance().exists(p))
                {
                    profilePic = theme.getImage("photo.png");
                }
                profilePic.getRGB();
            }
            catch(Exception ee){
            profilePic = theme.getImage("photo.png");
            }
            
            int width = Display.getInstance().getDisplayWidth();
            
            Image capturedImage = profilePic.scaledWidth(Math.round(width / 4)); //change value as necessary

        Image roundMask = Image.createImage(width/4, capturedImage.getHeight(), 0xff000000);
        Graphics gr = roundMask.getGraphics();
        gr.setColor(0xffffff);
        gr.fillArc(0, 0, width/4, width/4, 0, 360);
        Object mask = roundMask.createMask();
        capturedImage = capturedImage.applyMask(mask);
        Label profilePicLabel = new Label(capturedImage, "SideComponent");
        profilePicLabel.setIcon(capturedImage);
        
        Label t = new Label("DataPlat Collector");
        t.setUIID("FontSize3");
        
        Container c1 = BoxLayout.encloseY(t, FlowLayout.encloseCenter(profilePicLabel), FlowLayout.encloseCenter(new Label(this.userEmail, "SideEmail")));
        c1.setUIID("SideTop");
        c1.setScrollableY(false);
        cc.addComponent(BorderLayout.CENTER, c1);
        
        home.getToolbar().addComponentToSideMenu(cc);
        
        home.getToolbar().addSearchCommand(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String text = (String)e.getSource();
            if(text == null || text.length() == 0) {
                // clear search
                for(Component cmp : home.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
                home.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                Container p = holder;
                for(Component cmp : p) {

                    MultiButton mb = (MultiButton)cmp;
                    String line1 = mb.getTextLine1();
                    String line2 = mb.getTextLine2();
                    boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                            line2 != null && line2.toLowerCase().indexOf(text) > -1;
                    mb.setHidden(!show);
                    mb.setVisible(show);
                }
                home.getContentPane().animateLayout(150);
            }
            }
        });
        
        home.getToolbar().addMaterialCommandToSideMenu("New", FontImage.MATERIAL_CREATE, e -> {buildNewCollection();});
        home.getToolbar().addMaterialCommandToSideMenu("Profile", FontImage.MATERIAL_PERSON, e -> {buildProfile();});
        home.getToolbar().addMaterialCommandToSideMenu("Sent Entries", FontImage.MATERIAL_STAR, e -> {showAllEntries(true);});
        home.getToolbar().addMaterialCommandToSideMenu("Settings", FontImage.MATERIAL_SETTINGS, e -> {buildSettingsForm();});
        home.getToolbar().addMaterialCommandToSideMenu("Help", FontImage.MATERIAL_HELP, e -> {testrect();});
        
    }
    
    
    public void testrect()
    {
        Form hi = new Form("RoundRect", new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER));

Button ok = new Button("OK");
Button cancel = new Button("Cancel");

Label loginLabel = new Label("Login", "Container");
loginLabel.getAllStyles().setAlignment(Component.CENTER);

Label passwordLabel = new Label("Password", "Container");
passwordLabel.getAllStyles().setAlignment(Component.CENTER);

TextField login = new TextField("", "Login", 20, TextArea.ANY);
TextField password = new TextField("", "Password", 20, TextArea.PASSWORD);
Style loginStyle = login.getAllStyles();
Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);
loginStyle.setBorder(RoundRectBorder.create().
        strokeColor(0).
        strokeOpacity(120).
        stroke(borderStroke));
loginStyle.setBgColor(0xffffff);
loginStyle.setBgTransparency(255);
loginStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
loginStyle.setMargin(Component.BOTTOM, 3);
Style passwordStyle = password.getAllStyles();
passwordStyle.setBorder(RoundRectBorder.create().
        strokeColor(0).
        strokeOpacity(120).
        stroke(borderStroke));
passwordStyle.setBgColor(0xffffff);
passwordStyle.setBgTransparency(255);


Container box = BoxLayout.encloseY(
        loginLabel, 
        login, 
        passwordLabel, 
        password,
            GridLayout.encloseIn(2, cancel, ok));

Button closeButton = new Button();
Style closeStyle = closeButton.getAllStyles();
closeStyle.setFgColor(0xffffff);
closeStyle.setBgTransparency(0);
closeStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
closeStyle.setPadding(3, 3, 3, 3);
closeStyle.setBorder(RoundBorder.create().shadowOpacity(100));
FontImage.setMaterialIcon(closeButton, FontImage.MATERIAL_CLOSE);

Container layers = LayeredLayout.encloseIn(box, FlowLayout.encloseRight(closeButton));
Style boxStyle = box.getUnselectedStyle();
boxStyle.setBgTransparency(255);
boxStyle.setBgColor(0xeeeeee);
boxStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
boxStyle.setPaddingUnit(Style.UNIT_TYPE_DIPS);
boxStyle.setMargin(4, 3, 3, 3);
boxStyle.setPadding(2, 2, 2, 2);

hi.add(BorderLayout.CENTER, layers);

hi.show();
    }
    
    
    public void testMask()
    {
        Toolbar.setGlobalToolbar(true);
Form hi = new Form("Rounder", new BorderLayout());
Label picture = new Label("", "Container");
hi.add(BorderLayout.CENTER, picture);
hi.getUnselectedStyle().setBgColor(0xff0000);
hi.getUnselectedStyle().setBgTransparency(255);
Style s = UIManager.getInstance().getComponentStyle("TitleCommand");
Image camera = FontImage.createMaterial(FontImage.MATERIAL_CAMERA, s);
hi.getToolbar().addCommandToRightBar("", camera, (ev) -> {
    try {
        int width = Display.getInstance().getDisplayWidth();
        Image capturedImage = Image.createImage(Capture.capturePhoto(width/4, -1));
        Image roundMask = Image.createImage(width/4, capturedImage.getHeight(), 0xff000000);
        Graphics gr = roundMask.getGraphics();
        gr.setColor(0xffffff);
        gr.fillArc(0, 0, width/4, width/4, 0, 360);
        Object mask = roundMask.createMask();
        capturedImage = capturedImage.applyMask(mask);
        picture.setIcon(capturedImage);
        hi.revalidate();
    } catch(IOException err) {
        Log.e(err);
    }
});
hi.show();
    }
    
    
    
    public void testMaps()
    {
        Form mapDemo = new Form("Maps", new LayeredLayout());
mapDemo.getToolbar().addMaterialCommandToSideMenu("Hi", FontImage.MATERIAL_3D_ROTATION, e -> {});
if(BrowserComponent.isNativeBrowserSupported()) {
    MapContainer mc = new MapContainer("AIzaSyBFSARzsYO1ZkQ1OAw27q8gXqPeECz36uU");
    mapDemo.add(mc);
    Container markers = new Container();
    markers.setLayout(new MapLayout(mc, markers));
    mapDemo.add(markers);

    Coord moscone = new Coord(37.7831, -122.401558);
    Button mosconeButton = new Button("");
    FontImage.setMaterialIcon(mosconeButton, FontImage.MATERIAL_PLACE);
    markers.add(moscone, mosconeButton);

    mc.zoom(moscone, 5);
} else {
    // iOS Screenshot process...
    mapDemo.add(new Label("Loading, please wait...."));
}
mapDemo.show();
    }
    
    public void testTabs()
    {
        Form f = new Form("Test", new BorderLayout());
        f.getToolbar().setBackCommand("", e -> home.showBack());
        Tabs t = new Tabs(Component.BOTTOM);
        ComboBox p = new ComboBox();
        for(int x=1; x<11; x++)
        {
            Container c = new Container();
            Label l = new Label("Tab "+x);
            c.addComponent(l);
            c.setName(x+"");
            t.addTab(x+"", c);
        }
        f.getContentPane().add("Center", t);
        
        Vector vc = new Vector();
        
        Container b = new Container(new FlowLayout(Component.CENTER));
        TextField n = new TextField();
        Button b1 = new Button("Remove");
        Button b2 = new Button("Restore");
        Button b3 = new Button("View");
        b.add(n);
        b.add(b1) ;
        b.add(b2);
        b.add(b3);
        
        b1.addActionListener(e -> { 
            for(int y=0; y<t.getTabCount(); y++)
            {
                String g = t.getTabTitle(y);
                if(g.equals(n.getText()+""))
                {
                    Component v = t.getTabComponentAt(y);
                    Object o = (Object) v;
                    Map m = new HashMap();
                    m.put("container", v);
                    m.put("title", g);
                    m.put("tabIndex", y);
                    m.put("tabCount", t.getTabCount());
                    vc.add(m);
                    n.setText("");
                    t.removeTabAt(y);
                    t.revalidate();
                    //System.out.println(t.getTabCount());
                }
            }
        });
        
        b2.addActionListener(e -> {
            Dialog d = new Dialog("Restore");
            d.setAutoDispose(true);
            d.setLayout(BoxLayout.y());
            for(Object y: vc)
            {
                Map m = (Map) y;
                String title = m.get("title").toString();
                Container x = (Container) m.get("container");
                
                Button h = new Button(title);
                h.addActionListener(q -> {
                    int originalIndex = Integer.parseInt(title);
                    boolean restored = false;
                    int newIndex=-1;
                     
                    for(int c=0; c<t.getTabCount(); c++)
                    {
                        int tabInt = Integer.parseInt(t.getTabTitle(c));
                        if(originalIndex > tabInt)
                        {
                            System.out.println("i m greater than you "+c);
                            newIndex = c+1;
                        }
                        else
                        {
                            if(restored == false)
                            {
                                System.out.println("Ooopd....you greater oooo "+c);
                                restored = true;
                                newIndex = c;
                            }
                        }
                    }
                    t.insertTab(title, null, x, newIndex);                    
                    vc.remove(y);
                t.revalidate();
                });
                d.addComponent(h);
            }
            d.setDisposeWhenPointerOutOfBounds(true);
            d.show();
        });
        
        
        b3.addActionListener(k -> {
          Form a = new Form("Tabs");
          a.setScrollableY(true);
          a.getToolbar().setBackCommand("", e -> f.showBack());
          a.setLayout(BoxLayout.y());
          for(Object r: vc)
          {
              Map i = (Map) r;
              Container x = (Container) i.get("container");
              a.addComponent(x);
              a.show();
          }
          
        });
        
        
        
        f.getContentPane().add("North", b);
        
        f.show();
    }
    
    
    public void hideable()
    {
        //TwilioSMS smsAPI = TwilioSMS.create("AC39b699b1f09b417147d708f12b29649f", "f306ef03e952b90e1fb68ac795a82f5b", "9845381180");
        //ActivationForm.create("Signup").
        //show(s -> Log.p(s), smsAPI);

    }
    
    
    public void showAllEntries(boolean type)
    {
        Form f = new Form("All Sent Entries");
        if(type == false)
        {
            f.setTitle("All Draft Entries");
        }
        setBackCommand(f, home);
        f.setLayout(new BorderLayout());
        Container c = new Container();
        try
        {
            getEntries(type, c, f);
        }
        catch(Exception er){}
        f.getToolbar().addSearchCommand(new ActionListener()
{
    public void actionPerformed(ActionEvent e)
    {
        String text = (String)e.getSource();
    if(text == null || text.length() == 0) {
        // clear search
        for(Component cmp : c) {
            cmp.setHidden(false);
            cmp.setVisible(true);
        }
        f.getContentPane().animateLayout(150);
    } else {
        text = text.toLowerCase();
        Container p = c;
        for(Component cmp : p) {
            MultiButton mb = (MultiButton)cmp;
            String line1 = mb.getTextLine1();
            String line2 = mb.getTextLine2();
            boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                    line2 != null && line2.toLowerCase().indexOf(text) > -1;
            mb.setHidden(!show);
            mb.setVisible(show);
        }
        f.getContentPane().animateLayout(150);
    }
    }
});
       
        
        
        f.addComponent("Center", c);
        f.show();
    }
    
    
    
    
    public void createEntry(final String code, final String name, final String access, Form f)
    {
        System.out.println("i arrived");
        final Form c = new Form("New Entry");
        setBackCommand(c,f);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        c.addComponent(new Label("Enter Entry Name"));
        TextField t = new TextField();
        c.addComponent(t);
        Button ok = new Button("Create Entry");
        c.addComponent(ok);
        ok.addActionListener((ActionListener) (ActionEvent v) -> {
        String entry = t.getText();
        if(entry.length() < 1)
        {
            return;
        }
        setupEntry(entry, code, name);
        NewDataEntry(name,code, entry, true, access, c);
        });
        c.show();
    }
    
    
    public boolean checkAccessCode(final String code)
    {
        String access_code = "always";
        try
        {
             access_code = Storage.getInstance().readObject("access_code").toString();
        }catch(Exception er)
        {
            access_code = "always";
        }
        if(access_code.equals("once"))
        {
            try
            {
                String read = Storage.getInstance().readObject("r"+code).toString();
                return true;
            }
            catch(Exception e)
            {
                
            }
        }
        misc = false;
        ddd = new Dialog("Access Code Required");
                ddd.setDisposeWhenPointerOutOfBounds(true);
                ddd.setLayout(new BorderLayout());
                ddd.addComponent(BorderLayout.NORTH, new Label("Access code"));
                final TextField t = new TextField();
                t.setConstraint(TextField.NUMERIC);
                ddd.addComponent(BorderLayout.CENTER, t);
                Container cc = new Container(new GridLayout(1,2));
                Button ok = new Button("");
                Button ca = new Button("");
                FontImage.setMaterialIcon(ok, FontImage.MATERIAL_CHECK);
                FontImage.setMaterialIcon(ca, FontImage.MATERIAL_CLOSE);
                ddd.addComponent(BorderLayout.SOUTH, cc);
                ca.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                    {
                        ddd.dispose();
                    }
                });
                
                ok.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                    {
                        String entry = t.getText();
                        if(entry.equals(code))
                        {
                            misc = true;
                            Storage.getInstance().writeObject("r"+code, "r"+code);
                        }
                        ddd.dispose();
                    }
                });
                
                cc.addComponent(ok);
                cc.addComponent(ca);
                ddd.showPacked(BorderLayout.CENTER, true);
                return misc;
    }
    
    
    
    protected void setBackCommand2(Form f, String code, String access) {
        Command back = new Command("") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                buildProjectForm(code, access);
            }

        };
        Image img = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand"));
        back.setIcon(img);
        f.getToolbar().addCommandToLeftBar(back);
        f.getToolbar().setTitleCentered(false);
        f.setBackCommand(back);
    }
    
    
    
    
    public void openAllEntriesForSection(final String code, final String section, final String access, Form source)
    {
        final Form f = new Form();
        f.setLayout(new BorderLayout());
        f.setTitle("Section Entries");
        setBackCommand2(f, code, access);
        Button b44 = new Button();b44.setUIID("FontSize2");
        FontImage img = FontImage.createMaterial(FontImage.MATERIAL_ADD, UIManager.getInstance().getComponentStyle("TitleCommand"));
        Command nqp1 = new Command("", img)
        {
            public void actionPerformed(ActionEvent v)
            {
               final ProjectReader fromDevice = new ProjectReader();
                String c = fromDevice.getEntryCount(code, section);
                boolean isSingleAllowed = fromDevice.isSingleEntry(code, section);
                if( c != null)
                {
                    if(Integer.parseInt(c) > 0 && isSingleAllowed == true)
                    {
                        Dialog.show("Error", "The settings for this section allows only for single entry.", "Ok", null);
                        return;
                    }
                }

                createEntry(code, section, access, f);
            }
        };
        f.getToolbar().addCommandToRightBar(nqp1);
        
        final Container c = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        c.setScrollableY(true);
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section; 
        System.out.println(dir);
        String projects[] = null;
        try{
            projects = FileSystemStorage.getInstance().listFiles(dir);
        }catch(Exception er)
        {
            
        }
        if(projects == null)
        {
            
        }else{
        for(String entry: projects)
        {
            final String s = entry;
            System.out.println("entry s is "+s);
            MultiButton e = new MultiButton()
            {
              @Override
                public void longPointerPress(int x, int y)
                {
                    Display.getInstance().vibrate(200);
                    final Dialog d = new Dialog();
                    d.setLayout(new BorderLayout());
                    d.setTitle("Options");
                    d.setDisposeWhenPointerOutOfBounds(true);

                    Container top = new Container();
                    Button delete = new Button();
                    delete.setUIID("FontSize1");
                    FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
                    Button open = new Button(); 
                    open.setUIID("FontSize1");
                    FontImage.setMaterialIcon(open, FontImage.MATERIAL_DONE);
                    top.addComponent(open);
                    top.addComponent(delete);
                    d.addComponent("Center", top);
                    open.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            //Status status = ToastBar.getInstance().createStatus();
                            //status.setMessage("Hello world");
                            //status.setShowProgressIndicator(true);
                            //status.show();
                            Thread t= new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    NewDataEntry(section,code, s, false, access, f);
                                }
                            });
                            t.start();
                            //status.clear();

                        }
                    });

                    delete.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent v)
                        {
                            d.dispose();
                            String toDelete = dir+sep+s;
                            System.out.println(toDelete +" is toDelete");
                            deleteSectionEntry(toDelete, code, section, access, sep, f);
                        }
                    });

                    d.showModeless();
                }  
            };
            //e.setCheckBox(true);
            entry = StringUtil.replaceAll(entry, "#", ":");
            String toc = entry;
            entry = StringUtil.replaceAll(entry, "_", " ");
            e.setTextLine1(entry);
            Image star = FontImage.createMaterial(FontImage.MATERIAL_STAR, UIManager.getInstance().getComponentStyle("MultiButton"));
            Image half = FontImage.createMaterial(FontImage.MATERIAL_STAR_HALF, UIManager.getInstance().getComponentStyle("MultiButton"));
            try
            {
                
                String t= Storage.getInstance().readObject(code+section+toc).toString();//ToastBar.showErrorMessage("this is t "+t);
                System.out.println(code+section+toc+"   "+t);
                String stat = "";
                try
                {   
                    stat = Storage.getInstance().readObject("sent_status").toString();
                }
                catch(Exception ee)
                {
                    stat = "hide";
                }
                if(stat.equals("show"))
                {
                    try
                    {
                        int tt = Integer.parseInt(t);
                        e.setEmblem(star);
                        e.setUIID("Gray3");
                        e.setTextLine3("Sent");
                        e.setUIIDLine3("SuccessText");
                    }
                    catch(Exception er)
                    {
                        e.setEmblem(half);
                        e.setUIID("Gray4");
                        e.setTextLine3("Not Sent");
                        e.setUIIDLine3("AccessCodeInfo");
                    }
                }
            }
            catch(Exception er)
            {
                System.out.println("not found sent");
                
                er.printStackTrace();
                e.setEmblem(half);
                e.setTextLine3("Not Sent");
                e.setUIID("Gray4");
                e.setUIIDLine3("AccessCodeInfo");
            }
            e.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent v)
                {
                    NewDataEntry(section,code, s, false, access, f);
                }
            });
            c.addComponent(e);
        }
        }
        f.addComponent(BorderLayout.CENTER, c);
        
        f.show();
    }
    
    
    
    
    public void deleteSectionEntry(String toDelete, String code, String section, String access, char sep, Form form)
    {
        boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
       if(!confirm)
        {
             return;
        }

        System.out.println("to delete "+toDelete);
        try
        {
            String entries[] = FileSystemStorage.getInstance().listFiles(toDelete);
            for(String r: entries)
            {
                FileSystemStorage.getInstance().delete(toDelete+sep+r);
            }
            FileSystemStorage.getInstance().delete(toDelete+sep+"sent"+sep+"sent.jaji");
            FileSystemStorage.getInstance().delete(toDelete+sep+"sent");
        }catch(Exception ee){ee.printStackTrace();}
        FileSystemStorage.getInstance().delete(toDelete);
        openAllEntriesForSection(code,section, access, form);
    }
    
    
    public void setupEntry(String entry, String code, String section)
    {
        System.out.println("entry is "+entry);
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
        mkdirs("entries",code+";;;"+section+";;;"+entry);
        
    }
    
    
    public void mkdirs(String parent, String dirs)
    {
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String root = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+parent+sep;
        String y[] = new Helper().split(dirs, ";;;");
        for(String g: y)
        {
            root = root + g + sep;
            if(!FileSystemStorage.getInstance().exists(root))
            {
                FileSystemStorage.getInstance().mkdir(root);
            }
        }
    }

    public boolean saveToDevice(Container comp, String comp_name, String code, String section, String entry)
    {
        Save save = new Save();
        save.saveToDevice(comp, comp_name, code, section, entry);
        return true;
    }
    
    
    
    public void save(final Tabs t, final int x, String code, String section, String entry)
    {
        boolean done = true;
        com.jajitech.cn1.util.components.validator.Validator validate = new com.jajitech.cn1.util.components.validator.Validator();
        List lx = new ArrayList();
        //System.out.println(t.getContentPane().getComponentCount()+" is d total");
        for(int y=0; y<x; y++)
        {
            Container r = (Container) t.getContentPane().getComponentAt(y);
            for (int count = 0; count<r.getComponentCount(); count++) {
                Component c1 = r.getComponentAt(count);
                if (c1 instanceof Container) {
                    Container cont = (Container) c1;
                    TextField tt = new TextField();
                    tt.setText(cont.getName());
                    saveToDevice(cont, tt.getText(), code, section, entry); 
                }
            }
        }
    }
    
    
    
    
    public Container getQuestionContainer(String type, String caption, String items, String required, String entry, String code, String section, final boolean isNew, Tabs tab, Form form, int index, Resources resFile, String isRider, List<Map<String,String>> questions)
    {
        Container main = null;
        Components c = new Components(type,caption,items,required, entry, code, section, isNew, isRider, this);
        if(type.startsWith("textfield") && !isRider.equals("isRider"))
        {
            main = c.getTextFieldContainer(tab);
        }
        if(type.startsWith("rtflabel") && !isRider.equals("isRider"))
        {
            main = c.getLabelContainer();
        }
        if(type.startsWith("drop") && !isRider.equals("isRider"))
        {
            main = c.getComboBoxContainer(tab);
        }
        if(type.startsWith("textarea") && !isRider.equals("isRider"))
        {
            main = c.getTextAreaContainer();
        }
        if(type.startsWith("checkbox") && !isRider.equals("isRider"))
        {
            main = c.getCheckBoxContainer(tab);
        }
        if(type.startsWith("radio") && !isRider.equals("isRider"))
        {
            main = c.getRadioButtonContainer(tab);
        }
        if(type.startsWith("time") && !isRider.equals("isRider"))
        {
            main = c.getTimeContainer(tab);
        }
        if(type.startsWith("date") && !isRider.equals("isRider"))
        {
            main = c.getDateContainer(tab);
        }
        if(type.startsWith("image") && !isRider.equals("isRider"))
        {
            main = c.getImageContainer();
        }
        if(type.startsWith("location") && !isRider.equals("isRider"))
        {
            try{
                main = c.getLocationContainer(tab, form, index, resFile);
            }catch(Exception e){}
        }
        if(type.startsWith("spinner") && !isRider.equals("isRider"))
        {
            main = c.getSpinnerContainer(tab);
        }
        if(type.startsWith("onoff") && !isRider.equals("isRider"))
        {
            main = c.getOnOffContainer();
        }
        if(type.startsWith("audio") && !isRider.equals("isRider"))
        {
            try{
                main = c.getAudioContainer(Resources.open("/theme.res"));
            }catch(Exception e){}
            
        }
        if(type.startsWith("video") && !isRider.equals("isRider"))
        {
           try{
                main = c.getVideoContainer(Resources.open("/theme.res"), tab, form, index);
            }catch(Exception e){}
        }
        if(type.startsWith("barcode") && !isRider.equals("isRider"))
        {
            main = c.getBarcodeContainer();
        }
        
        if(type.startsWith("matrix") && !isRider.equals("isRider"))
        {
            main = c.getMatrixContainer();
        }
        /*
        if(isRider.length() > 9)
        {
           try{
            int y = main.getComponentCount();
            System.out.println("y "+y);
            
            Container mainContainer = null;
            for(int x=0; x<y; x++)
            {
                System.out.println("x "+x);
                Component cc = main.getComponentAt(x);
                try{ if(cc != null && cc.getName() != null && cc instanceof Container)
                {
                    mainContainer = (Container) cc;
                    System.out.println(mainContainer.getName()+" is the name");
                } }catch(Exception ee){ee.printStackTrace();}
            }
            System.out.println("this is q size "+questions.size());
            for(Map m: questions)
            {
                System.out.println("this is "+isRider);
                if(isRider.startsWith(m.get("type").toString()))
                {   
                    System.out.println("i made it there");
                    String caption1 = m.get("caption").toString();
                    String items1 = m.get("items").toString();
                    String type1 = m.get("type").toString();
                    System.out.println("this is type1 "+type1);
                    String required1 = m.get("required").toString();
                    c = new Components(type1,caption1,items1,required1, entry, code, section, isNew, isRider);
                    Container rider = null;
                    Container sepa = new Container();
                    sepa.setUIID("Button");
                    sepa.add(new Label(" "));
                    //mainContainer.addComponent(sepa);
                    if(type1.startsWith("textfield") && !isRider.equals("isRider"))
                    {
                        rider = c.getTextFieldContainer(tab);
                    }
                    if(type1.startsWith("label") && !isRider.equals("isRider"))
                    {
                        rider = c.getLabelContainer();
                    }
                    if(type1.startsWith("drop") && !isRider.equals("isRider"))
                    {
                        rider = c.getComboBoxContainer();
                    }
                    if(type1.startsWith("textarea") && !isRider.equals("isRider"))
                    {
                        rider = c.getTextAreaContainer();
                    }
                    if(type1.startsWith("checkbox") && !isRider.equals("isRider"))
                    {
                        rider = c.getCheckBoxContainer();
                    }
                    if(type1.startsWith("radio") && !isRider.equals("isRider"))
                    {
                        rider = c.getRadioButtonContainer();
                    }
                    if(type1.startsWith("time") && !isRider.equals("isRider"))
                    {
                        rider = c.getTimeContainer();
                    }
                    if(type1.startsWith("date") && !isRider.equals("isRider"))
                    {
                        rider = c.getDateContainer();
                    }
                    if(type1.startsWith("image") && !isRider.equals("isRider"))
                    {
                        rider = c.getImageContainer();
                    }
                    if(type1.startsWith("location") && !isRider.equals("isRider"))
                    {
                        try{
                            rider = c.getLocationContainer(tab, form, index, resFile);
                        }catch(Exception e){}
                    }
                    if(type1.startsWith("spinner") && !isRider.equals("isRider"))
                    {
                        rider = c.getSpinnerContainer();
                    }
                    if(type1.startsWith("onoff") && !isRider.equals("isRider"))
                    {
                        rider = c.getOnOffContainer();
                    }
                    if(type1.startsWith("audio") && !isRider.equals("isRider"))
                    {
                        try{
                            rider = c.getAudioContainer(Resources.open("/theme.res"));
                        }catch(Exception e){}

                    }
                    if(type1.startsWith("video") && !isRider.equals("isRider"))
                    {
                       try{
                            rider = c.getVideoContainer(Resources.open("/theme.res"), tab, form, index);
                        }catch(Exception e){}
                    }
                    if(type1.startsWith("barcode") && !isRider.equals("isRider"))
                    {
                        rider = c.getBarcodeContainer();
                    }

                    if(type1.startsWith("matrix") && !isRider.equals("isRider"))
                    {
                        rider = c.getMatrixContainer();
                    }
                    Component t = rider.getComponentAt(0);
                    rider.removeComponent(t);
                    //rider.setUIID("LineBorder");
                    rider.setName(type1);
                    mainContainer.addComponent(rider);
                }
            }
        }catch(Exception er){er.printStackTrace();}
        }
        */
       
        return main;
    }
    
    
    
    public void removeSentEntry(String entry)
    {
        Storage.getInstance().deleteStorageFile(entry);
    }
    
    
    
    public String deleteSectionEntryFromCloud(final String code, final int id, final String code2)
    {
        System.out.println(code);
        try
        {
            final ToastBar.Status status = ToastBar.getInstance().createStatus();
            status.setMessage("Initializing...");
            status.setIcon(createIcon(FontImage.MATERIAL_WORK));
            status.setShowProgressIndicator(true);
            status.show();
            
    //the NetworkManager object
            NetworkManager networkManager = NetworkManager.getInstance();
            networkManager.start();
            networkManager.addErrorListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent evt)
              {
                NetworkEvent n = (NetworkEvent) evt;
              }});
              //ConnectionRequest object
              status.setMessage("Processing...");
            status.setIcon(createIcon(FontImage.MATERIAL_WORK));
            status.setShowProgressIndicator(true);

              ConnectionRequest request = new ConnectionRequest()
              {
                int chr;
                StringBuffer sb = new StringBuffer();
                @Override
                protected void readResponse(InputStream input) throws IOException
                {
                  //reading the answer                      
                  while ((chr = input.read()) != -1)
                  {
                    sb.append((char) chr);
                  }
                  response = sb.toString();                                           
                  response = response.trim();
                  if(response.equals("error"))
                  {
                        status.setMessage("Error deleting entry...");
                        status.setIcon(createIcon(FontImage.MATERIAL_ERROR));
                        status.setShowProgressIndicator(false);
                        status.setExpires(3000);
                        status.show();
                  }
                  else
                  {
                      Storage.getInstance().deleteStorageFile("later_"+code2);
                       removeSentEntry(code2);
                        status.setMessage("Entry deleted from cloud...");
                        status.setIcon(createIcon(FontImage.MATERIAL_DONE));
                        status.setShowProgressIndicator(false);
                        status.setExpires(3000);
                        status.show();
                  }
                }
                @Override
                protected void handleException(Exception err)
                {
                    System.out.println("handling error");
                        status.setMessage("Network error encountered. Ensure you have internet access.");
                        status.setIcon(createIcon(FontImage.MATERIAL_ERROR));
                        status.setShowProgressIndicator(false);
                        status.setExpires(3000);
                        status.show();
                }
              };
              request.setUrl(URL+"deleteEntry.jsp"); //servlet calling
              request.setPost(false);
              request.addArgument("code", code);
              request.addArgument("id", id+"");
              networkManager.addToQueueAndWait(request);           
        }
        catch (Exception e)
        {
          System.out.println(e.getMessage());
          return "error";
        }                
        while(response.equals("empty"))
        {
        //waiting for the answer from the serlvet or jsp server
        }
       
        return res;

    }
    
    public Map getTabElements()
    {
        return tabElements;
    }
    
    
    public static Map tabElements = new HashMap();
    Vector activeSection = new Vector();
    public void NewDataEntry(final String section, final String code, final String entry, final boolean isNew, final String access, Form source)
    {
        final Form main = new Form(section);
        setBackCommand(main, source);
        
        main.setLayout(new BorderLayout());
        Container north = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container label = new Container(new FlowLayout(Component.RIGHT));
        SpanLabel title = new SpanLabel(section);
        title.setUIID("CalendarTitleArea");
        label.addComponent(title);
        //north.addComponent(label);
        
        Container jump = new Container(new FlowLayout(Component.RIGHT));
        Label l = new Label("Jump To");
        //jump.addComponent(l);
        final ComboBox c = new ComboBox();
        //jump.addComponent(c);
        north.addComponent(jump);
        
        main.addComponent("North",north);
        
        final Tabs t = new Tabs(Component.BOTTOM);
        t.hideTabs();
        final TextField tt = new TextField();
        t.setUIID("Container");
        t.addSelectionListener((int oldSelected, int newSelected) -> {
            Container toSave = (Container) t.getContentPane().getComponentAt(oldSelected);
            for (int count = 0; count<toSave.getComponentCount(); count++) {
                c.setSelectedIndex(newSelected);
                Component c1 = toSave.getComponentAt(count);
                if (c1 instanceof Container) {
                    Container cont = (Container) c1;
                    tt.setText(cont.getName());
                    boolean x = saveToDevice(cont, tt.getText(), code, section, entry); 
                    for(int ttt=0; ttt<cont.getComponentCount(); ttt++)
                    {
                        System.out.println("this is t rider "+ttt);
                        Component cc = cont.getComponentAt(ttt);
                        if(cc instanceof Container)
                        {
                            System.out.println("i found  A CONTAINER  name "+cc.getName());
                            Container rt = (Container) cc;
                            tt.setText(rt.getName()); 
                            boolean xx = saveToDevice(rt, tt.getText(), code, section, entry); 
                        }
                    }
                }
            }
        });
        //t.setLayout(new BorderLayout());
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
        List<Map<String,String>> questions = fromDevice.readSection(code, section, sep, dir);
        int x=0;
        for(Map comp: questions)
        {
            x++;
            Map question = comp;
            String caption = question.get("caption").toString();
            String items = question.get("items").toString();
            String type = question.get("type").toString();
            System.out.println("type name is "+type);
            String required = question.get("required").toString();
            String isRider = question.get("isRider").toString();
            c.addItem(x+"-"+caption);
            Container questionContainer = getQuestionContainer(type,caption,items,required, entry, code, section, isNew, t, main, x-1, theme, isRider, questions);
            try{questionContainer.setName(type);}catch(Exception er){}
            tabElements.put(type+"_"+x, questionContainer);
            if(Storage.getInstance().exists("hide_"+type+"_"+x)== false)
            {    
                t.addTab(type+"_"+x, questionContainer);
            }
        }
        System.out.println("tabs size from xdata "+tabElements.size());
        try{
        Logic logic = new Logic(tabElements);
        logic.prepareLogic(t, this);}catch(Exception er){er.printStackTrace();}
        
        final int y = x;
        c.addItem((x+1)+"-End");
        Container end = new Container();
        end.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Container n = new Container(new FlowLayout(Component.CENTER));
        n.addComponent(new SpanLabel("You have reached the end. Tap the button below to send now."));
        end.addComponent(n);
        Button send = new Button("");
        send.addActionListener((ActionListener) (ActionEvent v) -> {
            List f = validateEntry(t,y);
            if (f.size() < 1) {
                String response1 = prepareToSendToCloud(section, code, entry, false);
                System.out.println("here goes response " + response1);
                //handleEntrySentResponse(response, code, section, entry, main);
            } else {
                showMissingFields(f, t.getSelectedIndex(), main, t, c);
            }
        });
        send.setUIID("FontSize1");
        FontImage.setMaterialIcon(send, FontImage.MATERIAL_CLOUD_UPLOAD);
        end.addComponent(send);
        end.addComponent(new SpanLabel("Alternatively, you could mark this entry for sending automatically once internet is available by checking the box below."));
        Button bc = new Button();
        bc.addActionListener((ActionListener) (ActionEvent v) -> {
            List f = validateEntry(t,y);
            if(f.size() > 1) 
            {
                Dialog.show("Alert", "Some required fields for this entry are not filled yet...", "Ok", null);
                return;
            }
            Storage.getInstance().deleteStorageFile("later_"+code+"@@#"+section+"@@#"+entry);
            Storage.getInstance().writeObject("later_"+code+"@@#"+section+"@@#"+entry, section+"@@#"+code+"@@#"+entry);
            Dialog.show("Alert", "Entry will be sent later once internet access is available...", "Ok", null);
            openAllEntriesForSection(code, section, access, main);
        });
        bc.setUIID("FontSize1");
        FontImage.setMaterialIcon(bc, FontImage.MATERIAL_FILE_UPLOAD);
        end.addComponent(bc);
        t.addTab("End", end); 
        
        final Toolbar tool = main.getToolbar();
        Style s = UIManager.getInstance().getComponentStyle("Label");
        FontImage b5 = FontImage.createMaterial(FontImage.MATERIAL_DELETE, s);
        Command b1 = new Command("Delete from Device", FontImage.createMaterial(FontImage.MATERIAL_DELETE, s))
        {
            public void actionPerformed(ActionEvent v)
            {
                String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
                System.out.println("tt t is "+dir);
                deleteSectionEntry( dir, code,  section,  access,  sep, main);
            }
        };
        
        Command b1a = new Command("Delete from Cloud", b5)
        {
            public void actionPerformed(ActionEvent v)
            {
                int x = isEntrySent(code+section+entry);
                if(x > 0)
                {
                    boolean confirm = Dialog.show("Confirm", "Delete entry from cloud? Once deleted, entry must be resent. Do you wish to continue?", "YES", "NO");
                    if(!confirm)
                    {
                        return;
                    }
                    deleteSectionEntryFromCloud(code+section, x, code+section+entry);
                }
            }
        };
        
        Command b1b = new Command("Delete from All", b5)
        {
            public void actionPerformed(ActionEvent v)
            {
                String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
                System.out.println("tt t is "+dir);
                deleteSectionEntry( dir, code,  section,  access,  sep, main);
                int x = isEntrySent(code+section+entry);
                if(x > 0)
                {
                    boolean confirm = Dialog.show("Confirm", "Delete entry from cloud? Once deleted, entry must be resent. Do you wish to continue?", "YES", "NO");
                    if(!confirm)
                    {
                        return;
                    }
                    deleteSectionEntryFromCloud(code+section, x, code+section+entry);
                }
                
            }
        };
        
                Style ss = UIManager.getInstance().getComponentStyle("TitleCommand");
        FontImage b4 = FontImage.createMaterial(FontImage.MATERIAL_SEND, ss);
        FontImage bs = FontImage.createMaterial(FontImage.MATERIAL_SAVE, ss);
        
      
        tool.addCommandToOverflowMenu(b1);
        tool.addCommandToOverflowMenu(b1a);
        tool.addCommandToOverflowMenu(b1b);

        //main.addCommand(b1);
        
      
        c.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                String[] index = new Helper().split(c.getSelectedItem().toString(),"-");
                t.setSelectedIndex(Integer.parseInt(index[0])-1);
            }
        });
        //t.hideTabs();
 
        main.addComponent(BorderLayout.CENTER,t);
        
        
        Container cc = new Container(new GridLayout(1,4));
        Button ba = new Button();
        ba.setUIID("FontSize1");
        FontImage.setMaterialIcon(ba, FontImage.MATERIAL_NAVIGATE_BEFORE);
        ba.addActionListener(e -> {
            String[] index = new Helper().split(c.getSelectedItem().toString(),"-");
            try{t.setSelectedIndex(Integer.parseInt(index[0])-1-1);}catch(Exception ee){}
        });
        cc.addComponent(ba);
        
        Button bb = new Button();
        bb.setUIID("FontSize1");
        FontImage.setMaterialIcon(bb, FontImage.MATERIAL_NAVIGATE_NEXT);
        bb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                String[] index = new Helper().split(c.getSelectedItem().toString(),"-");
                try{t.setSelectedIndex(Integer.parseInt(index[0])-1+1);}catch(Exception ee){}
            }
        });
        cc.addComponent(bb);
        final int u = y;
        Button bd = new Button();
        bd.setUIID("FontSize1");
        FontImage.setMaterialIcon(bd, FontImage.MATERIAL_LIST);
        bd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                /*
                final InteractionDialog d = new InteractionDialog("Section Trail");
                                        d.setLayout(new BorderLayout());
                                        Container cc = TrailContainer(t, u, c);
                                        d.addComponent(BorderLayout.CENTER, cc);
                                        Button close = new Button("Close");
                                        close.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent v)
                                            {
                                                d.dispose();
                                            }
                                        });
                                        //close.addActionListener((ee) -> d.dispose());
                                        d.addComponent(BorderLayout.SOUTH, close);
                                        Dimension pre = d.getContentPane().getPreferredSize();
                                        d.show(0, 0, Display.getInstance().getDisplayWidth() - (pre.getWidth() +
                                        pre.getWidth() / 6), 0); */
                
                String[] index = new Helper().split(c.getSelectedItem().toString(),"-");
                showTrailForm(index[0], t, c, main, y);
            }
        });
        cc.addComponent(bd);
        
        Button be = new Button();
        be.setUIID("FontSize1");
        be.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                final Dialog d = new Dialog("Send");
                d.setDisposeWhenPointerOutOfBounds(true);
                d.setLayout(new BorderLayout());
                Container cc = new Container();
                cc.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                Button b1 = new Button("Send Now");
                b1.setTextPosition(2);
                FontImage.setMaterialIcon(b1, FontImage.MATERIAL_CLOUD_UPLOAD);                
                cc.addComponent(b1);
                b1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                    {
                        d.dispose();
                        List f = validateEntry(t,y);
                        if(f.size() < 1)
                        {
                            String response = prepareToSendToCloud(section, code, entry, false);
                            //handleEntrySentResponse(response, code, section, entry, main);
                        }
                        else
                        {
                            showMissingFields(f, t.getSelectedIndex(), main, t, c);
                        }
                    }
                });
                
                
                Button b2 = new Button("Send Later");
                b2.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                    {
                        d.dispose();
                        List f = validateEntry(t,y);
                        if(f.size() > 1)
                        {
                            Dialog.show("Alert", "Some required fields for this entry are not filled yet...", "Ok", null);
                            return;
                        }
                        Storage.getInstance().deleteStorageFile("later_"+code+"@@#"+section+"@@#"+entry);
                        Storage.getInstance().writeObject("later_"+section+"@@#"+code+"@@#"+entry, section+"@@#"+code+"@@#"+entry);
                        Dialog.show("Alert", "Entry will be sent later once internet access is available...", "Ok", null);
                        openAllEntriesForSection(code, section, access, main);
                    }
                });
                b2.setTextPosition(2);
                FontImage.setMaterialIcon(b2, FontImage.MATERIAL_FILE_UPLOAD);
                cc.addComponent(b2);
                d.addComponent("Center", cc);
                d.show();
            }
        });
        FontImage.setMaterialIcon(be, FontImage.MATERIAL_SEND);
        cc.addComponent(be);
        
        main.addComponent(BorderLayout.SOUTH,cc);
        checkNetworkStatus(tool, 1);
        main.setTitle(section);
        main.show();
        Timer tw = new Timer();
        TimerTask ta = new TimerTask()
        {
            public void run() 
            {
                 //checkNetworkStatus(tool, 0);
                 //tool.revalidate();
                 //System.out.println("just checked network");
            }
        };
        
        tw.schedule(ta,15000);
    }
    
    
    
    
    public int isEntrySent(String entry)
    {
        System.out.println("this is ee "+entry);
        int sent_code = 0;
        try
        {
            sent_code = Integer.parseInt(Storage.getInstance().readObject(entry).toString());
        }catch(Exception er){sent_code = 0;}

        System.out.println("this is sent code "+sent_code);
        return sent_code;
    }
    
    
    
    
    public String prepareToSendToCloud(final String section, final String code, final String entry, final boolean isSilent)
    {
        
        final ToastBar.Status status = ToastBar.getInstance().createStatus();
        if(isSilent == false)
        {
            status.setMessage("Initializing...");
            status.setShowProgressIndicator(true);
            status.setIcon(createIcon(FontImage.MATERIAL_WORK));
            status.show();
        }
        final int alreadySent = isEntrySent(code+section+entry);
        if(alreadySent > 0 && isSilent == false)
        {
            Dialog d = new Dialog();
            boolean confirm = d.show("Confirm", "This entry is already sent. Would you like to overwrite?", "YES", "NO");
            if(!confirm)
            {
                status.clear();
                return "";
            }
        }
        Display.getInstance().invokeAndBlock(new Runnable()
        {
            public void run()
            {
        System.err.println("entry is "+entry);
        tres = "1572";
        final ProjectReader fromDevice = new ProjectReader();
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
        final List<Map<String,String>> comp = fromDevice.readSectionAccess(code, sep, dir);
        final Map m = (Map)Storage.getInstance().readObject("userData");
        String c = "";
        try
        {
            c = getSectionCode(comp, section, new Helper());
        }catch(Exception er){}
        final String rcode = c;
        Display.getInstance().callSerially(new Runnable()
        {
            public void run()
            {
                if(isSilent == false)
                {
                    status.setMessage("Preparing to send...");
                    status.setShowProgressIndicator(true);
                    status.setIcon(createIcon(FontImage.MATERIAL_WORK));
                    status.show();
                    tres = "1594";
                }
                char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep+code+sep+section+sep+entry;
                try
                {
                    mkdirs("entries", code+";;;"+section+";;;"+entry+";;;"+"sent");
                    tres = "1595";
                    OutputStream dest = FileSystemStorage.getInstance().openOutputStream(dir + sep + "sent" + sep + "sent.jaji");
                    TarOutputStream out = new TarOutputStream( new BufferedOutputStream( dest ) );
                    String entries[]  = FileSystemStorage.getInstance().listFiles(dir);
                    for(String e: entries)
                    {
                        System.out.println(e);
                        if(!FileSystemStorage.getInstance().isDirectory(dir + sep + e))
                        {
                            out.putNextEntry(new TarEntry(dir + sep + e, e));
                            BufferedInputStream origin = new BufferedInputStream(FileSystemStorage.getInstance().openInputStream(dir + sep + e ));
                            int count;
                            byte data[] = new byte[12048];
                            while((count = origin.read(data)) != -1)
                            {
                                out.write(data, 0, count);
                            }
                            out.flush();
                            origin.close();
                        }
                    }
                    try
                    {
                        String h = Storage.getInstance().readObject("profile_sent").toString();
                        tres = "1624";
                    }
                    catch(Exception er)
                    {
                        String pic = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"photo.jpg";
                        tres = "1630";
                        if(FileSystemStorage.getInstance().exists(pic))
                        {
                            tres = "1631";
                            out.putNextEntry(new TarEntry(pic, "photo.jpg"));
                            BufferedInputStream origin = new BufferedInputStream(FileSystemStorage.getInstance().openInputStream(pic));
                            int count;
                            byte data[] = new byte[12048];
                            while((count = origin.read(data)) != -1)
                            {
                                out.write(data, 0, count);
                            }
                            out.flush();
                            origin.close();
                        }
                    }
                    tres = "1644";
                    /*
                    final String dir2 = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"user_signature.png";
                    if (FileSystemStorage.getInstance().exists(dir2))
                    {
                        out.putNextEntry(new TarEntry(dir2, "user_signature.png"));
                        BufferedInputStream origin = new BufferedInputStream(FileSystemStorage.getInstance().openInputStream(dir2 ));
                        int count;
                        byte data[] = new byte[12048];
                        while((count = origin.read(data)) != -1)
                        {
                            out.write(data, 0, count);
                        }
                        out.flush();
                        origin.close();
                    }
                    */
                    out.close();
                    
                    if(isSilent == false)
                    {
                        status.setMessage("Sending entry...");
                        status.setShowProgressIndicator(true);
                        status.setIcon(createIcon(FontImage.MATERIAL_WORK));
                        status.show();
                        tres = "1669";
                    }
                    else
                    {
                        System.out.println("sending quietly...");
                    }
                    //status.setExpires(3000);
                    MultipartRequest request = new MultipartRequest()
                    {
                        
                      int chr;
                      StringBuffer sb = new StringBuffer(); 
                      @Override
                      protected void readResponse(InputStream input) throws IOException
                      {
                        //reading the answer                      
                        while ((chr = input.read()) != -1)
                        {
                          sb.append((char) chr);
                        }

                        response = sb.toString();
                        response = response.trim();
                        System.out.println("this is response "+response);
                        String r[] = new Helper().split(response, "#");
                        res = response;
                        if(res.startsWith("done"))
                        {
                            saveSentEntries(code+section+entry, r[1]);
                            if(isSilent == false)
                            {
                                status.setIcon(createIcon(FontImage.MATERIAL_DONE)); 
                                status.setExpires(3000);
                                status.setShowProgressIndicator(false);
                                status.setMessage("Entry sent successfully...");
                                status.show();
                                Storage.getInstance().writeObject("profile_sent", "profile_sent");
                            }
                            else
                            {
                                System.out.println("entry was sent silently...");
                                Storage.getInstance().deleteStorageFile("later_"+section+"@@#"+code+"@@#"+entry);
                                showLocalNotification("xData Mobile", "One entry has been sent to cloud...", 1);
                            }
                        }
                        else
                        {
                            System.out.println("its an error response");
                            if(isSilent == false)
                            {
                                status.setShowProgressIndicator(false);
                                status.setMessage(res);
                                 status.setIcon(createIcon(FontImage.MATERIAL_ERROR));
                                status.setExpires(3000);
                                status.show();
                            }
                            else
                            {
                                System.out.println("an error occured when sending silently...");
                                
                            }
                        }
                        
                      }  
                      @Override
                      protected void handleException(Exception err)
                      {
                        //An error occured - show a message:
                          System.out.println("handling error");
                          err.printStackTrace();
                          response = "error";
                        res = "network";
                        tres = "1741";
                        if(isSilent == false)
                        {
                            status.setShowProgressIndicator(false);
                            status.setMessage("Network error ecountered. Ensure you have internet access.");
                            status.setIcon(createIcon(FontImage.MATERIAL_ERROR));
                            status.show();
                            // show this error message for 3 seconds, then hide automatically
                            status.setExpires(3000);
                        }
                      }
                    };
                    Form ff = new Form("");
                    String all = "URL is "+URL+"/nentry_name \n"+entry+"\naccess_code "+rcode+"\np_code "+code+"\nsection "+section+"\nFL "+AMAZON_URL;
                    request.setUrl(URL+"saveEntry.jsp");
                    request.setPost(false);
                    request.addArgument("entry_name", entry);
                    tres = "1758";
                    request.addArgument("access_code", rcode);
                    tres = "1760";
                    request.addArgument("p_code", code);
                    tres = "1762";
                    request.addArgument("p_section", section);
                    tres = "1764";
                    request.addArgument("fl", AMAZON_URL);
                    tres = "1766";
                    request.addArgument("email", m.get("email").toString());
                    tres = "1768";
                    request.addArgument("alreadySent", alreadySent+"");
                    tres = "1770";
                    request.addData("entry", dir + sep + "sent" + sep + "sent.jaji", "jaji");
                    tres = "1772";
                    request.setFilename("content", "sent.jaji");
                    tres = "1774";
                    NetworkManager.getInstance().addToQueue(request);
                    tres = "1776";
                    System.out.println("added to quue");
                    
                }
                catch(Exception er)
                {
                    res = "error";
                    er.printStackTrace();
                    status.setShowProgressIndicator(false);
                    status.setMessage("Error sending entry."+tres);
                    status.setIcon(createIcon(FontImage.MATERIAL_ERROR));
                    status.show();
                    // show this error message for 3 seconds, then hide automatically
                    
                    String p[] = {"smilewithjaji@gmail.com"};
                    Display.getInstance().sendMessage(p, "error", new Message(er.getMessage()));
                    status.setExpires(3000);  
                }
        
            }});
            }});
        
        return res;
        }
    
    
    
    public void saveSentEntries(String entry, String id)
    {
        Storage.getInstance().writeObject(entry, id);
    }
    
    
    public void showMissingFields(final List items, final int selectedIndex, final Form main, final Tabs t, final ComboBox list)
    {
        final Form f = new Form("Missing Fields");
        Command b = new Command("Back")
        {
            public void actionPerformed(ActionEvent v)
            {
               main.show();
               t.setSelectedIndex(selectedIndex);
            }
        };
        f.setBackCommand(b);
        f.setLayout(new BorderLayout());
        Container c = new Container();
        c.setScrollableY(true);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        Container n = new Container(new FlowLayout(Component.LEFT));
        SpanLabel s = new SpanLabel("The following fields are required and have not been filled. Tap on any question to navigate to it.");
        n.addComponent(s);
        f.addComponent(BorderLayout.NORTH, n);
        
        for(Object i: items)
        {
            final String m = i.toString();
            SpanButton m1 = new SpanButton();
            m1.getUnselectedStyle().setAlignment(Component.LEFT);
            m1.getSelectedStyle().setAlignment(Component.LEFT);
            //m1.setIcon(fetchResourceFile().getImage("221-point_red.png"));
            list.setSelectedIndex(Integer.parseInt(m));
            m1.setText(list.getSelectedItem().toString());
            m1.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent v)
                {
                    main.show();
                    t.setSelectedIndex(Integer.parseInt(m));
                }
            });
            c.addComponent(m1);
        }
        f.addComponent(BorderLayout.CENTER, c);
        f.show();
        
    }
    
    
    
    
    public List validateEntry(final Tabs t, final int x)
    {
        boolean done = true;
        com.jajitech.cn1.util.components.validator.Validator validate = new com.jajitech.cn1.util.components.validator.Validator();
        List lx = new ArrayList();
        System.out.println(t.getContentPane().getComponentCount()+" is d total  and y is "+x);
        
        for(int y=0; y<=x; y++)
        {
            Container r = (Container) t.getContentPane().getComponentAt(y);
            System.out.println("in 1");
            for(int count=0; count<r.getComponentCount();count++)
            {
                Component c = r.getComponentAt(count);
                System.out.println("this is class "+c.getClass());
                if(c instanceof Container)
                {
                    Container cont = (Container)c;
                    System.out.println("this is name in validator "+cont.getName());
                    //tt.setText(cont.getName());
                    Map d = validate.validateComponent(cont);
                    String a =  d.get("req").toString();
                    if(a.equals("true"))
                    {
                        System.out.println("its required....");
                        boolean isFilled = false;
                        try
                        {
                            isFilled = (Boolean) d.get("filled");
                        }catch(Exception er){isFilled = false;}
                        if(isFilled == false)
                        {
                            System.err.println(y + " is not filled..."+cont.getName());
                            lx.add(y);
                        }
                    }
                } 
            }
        }
        return lx;
    }
    
    
    
    public void showTrailForm(final String selectedIndex, final Tabs t, final ComboBox c, final Form main, int y)
    {
        Form f = new Form("Section Trail");
        setBackCommand(f, main);
        
        f.setScrollableY(true);
        f.setLayout(new BorderLayout());
        Container tc = TrailContainer(t, y, c, main);
        f.addComponent(BorderLayout.CENTER, tc);
        f.show();
    }
    
    
    
    public Container TrailContainer(final Tabs t, final int x, final ComboBox list, final Form main)
    {
        Container cc = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        cc.setScrollableY(true);
        com.jajitech.cn1.util.components.validator.Validator validate = new com.jajitech.cn1.util.components.validator.Validator();
        for(int y=0; y<x; y++)
        {
                list.setSelectedIndex(y);
                MultiButton m = new MultiButton();
                FontImage p = FontImage.createMaterial(FontImage.MATERIAL_NAVIGATE_NEXT, new Label().getAllStyles());
                //m.setEmblemUIID("CalendarTitleArea");
                m.setEmblem(p);
                m.setTextLine1("");
                final int yy = y;
                m.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                    {
                        main.show();
                        t.setSelectedIndex(yy);
                    }
                });
                m.setTextLine2(list.getSelectedItem().toString());
                //m.setUIIDLine2("SpanLabel");
                Container r = (Container) t.getContentPane().getComponentAt(y);
                for(int count=0; count<r.getComponentCount();count++)
                {
                    Component c = r.getComponentAt(count);
                    if(c instanceof Container)
                    {
                        Container cont = (Container)c;
                        //tt.setText(cont.getName());
                        Map d = validate.validateComponent(cont);
                        String a =  d.get("req").toString();
                        if(a.equals("true"))
                        {
                            System.out.println("its required....");
                            boolean isFilled = false;
                            try
                            {
                                isFilled = (Boolean) d.get("filled");
                                list.setSelectedIndex(y);
                                m.setUIIDLine3("SuccessText");
                                m.setTextLine3("Done");
                            }catch(Exception er){isFilled = false;}
                            if(isFilled == false)
                            {
                                System.err.println(y + " is not filled...");
                                list.setSelectedIndex(y);
                                m.setTextLine3("Not Done");
                                m.setUIIDLine3("ErrorText");
                            }
                        }
                    } 
                }
                cc.addComponent(m);
        }
        return cc;
    }
    
    
    public void checkNetworkStatus(Toolbar tool, int x)
    {
        if(x>0)
        {
               ToastBar.Status s = ToastBar.getInstance().createStatus();
               s.setExpires(2000);
               s.setMessage("Getting internet status...");
               s.setIcon(createIcon(FontImage.MATERIAL_DEVICES));
               s.show();
               s.clear();
        }
        
        
        FontImage b4 = null;
        String message = "";
        Connectivity.ConnectionState status = Connectivity.getConnectionState();
        switch (status) {
            case DISCONNECTED:
                b4 = FontImage.createMaterial(FontImage.MATERIAL_SIGNAL_WIFI_OFF, new Button().getStyle());
                message = "You are not connected...";
                break;
            case WIFI:
                b4 = FontImage.createMaterial(FontImage.MATERIAL_NETWORK_WIFI, new Button().getStyle());
                message = "You are connected (WIFI)";
                break;
            case MOBILE:
                b4 = FontImage.createMaterial(FontImage.MATERIAL_NETWORK_CELL, new Button().getStyle());
                message = "You are connected (Mobile Data)";
                break;
        }
        final String m = message;
        final FontImage f= b4;
        Command nqp = new Command("", b4)
        {
            public void actionPerformed(ActionEvent v)
            {
               ToastBar.Status status = ToastBar.getInstance().createStatus();
               status.setExpires(3000);
               status.setMessage(m);
               status.setIcon(createIcon(FontImage.MATERIAL_DEVICES));
               status.show();
            }
        };
        tool.addCommandToRightBar(nqp);
        
    }
    
    
    private Image createIcon(char charcode) {
        int iconWidth = Display.getInstance().convertToPixels(8, true);
        Style iconStyle = new Style();
        Font iconFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        if (Font.isNativeFontSchemeSupported()) {
            iconFont = Font.createTrueTypeFont("native:MainBold", null).derive((int)(iconWidth * 0.5), Font.STYLE_BOLD);
        }
        iconStyle.setFont(iconFont);
        //iconStyle.setFgColor(0xffffff);
        //iconStyle.setBgTransparency(0);

        FontImage completeIcon = FontImage.createMaterial(charcode, iconStyle);
        return completeIcon;
    }
    
    
    
    
    
    
    
    
    
    public Container getEntries(final boolean sent, Container c, Form f)
    {
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        c.setScrollableY(true);
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries"+sep;
        String projects[] = null;
        try{ projects = FileSystemStorage.getInstance().listFiles(dir); }catch(Exception er){}

        for(final String p_entry: projects)
        {
            final String code = p_entry;
            final String dirs = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
            System.out.println(dirs);
            final Map map = fromDevice.read(code, sep, dirs);
            System.out.println("its size..."+map.size());
            final String s = p_entry;
            String edir = dir + p_entry;
            String pp[] = null;
            try{ pp = FileSystemStorage.getInstance().listFiles(edir); }catch(Exception er){}
            for(String g: pp)
            {
                String section = g;
                String dd = edir + sep + g;
                String ee[] = null;
                try{ ee = FileSystemStorage.getInstance().listFiles(dd); }catch(Exception er){}
                for(String et: ee)
                {
                    MultiButton e = new MultiButton();
                    e.setUIID("FabLater");
                    et = StringUtil.replaceAll(et, "#", ":");
                    et = StringUtil.replaceAll(et, "_", " ");
                    e.setTextLine1(et);
                    e.setTextLine3(map.get("name").toString());
                    e.setTextLine2(section);
                    Image star = FontImage.createMaterial(FontImage.MATERIAL_STAR, UIManager.getInstance().getComponentStyle("Button"));
                    Image half = FontImage.createMaterial(FontImage.MATERIAL_STAR_HALF, UIManager.getInstance().getComponentStyle("Button"));
                    try
                    {
                        String t= Storage.getInstance().readObject(code+section+et).toString();
                        try
                        {
                            int tt = Integer.parseInt(t);
                            e.setEmblem(star);
                            //e.setTextLine3("Sent");
                            e.setUIIDLine3("SuccessText");
                            if(sent == true)
                            {
                                c.addComponent(e);
                            }
                        }
                        catch(Exception er)
                        {
                            e.setEmblem(half);
                            //e.setTextLine3("Not Sent");
                            e.setUIIDLine3("AccessCodeInfo");
                            if(sent == false)
                            {
                                c.addComponent(e);
                            }
                        }
                    }
                    catch(Exception er)
                    {
                        System.out.println("not found sent");

                        er.printStackTrace();
                        e.setEmblem(half);
                        //e.setTextLine3("Not Sent");
                        e.setUIIDLine3("AccessCodeInfo");
                        if(sent == false)
                            {
                                c.addComponent(e);
                            }
                    }
                    final String ss = section;
                    final String gg = et;
                    e.addActionListener(new ActionListener()
                    {
                    public void actionPerformed(ActionEvent v)
                    {
                        NewDataEntry(ss,code, gg, false, map.get("access").toString(), f);
                    }
                    });

                }
                
            }
        }
        
        return c;
    }
    
    
    
    public void goHome()
    {
        
       
        
        home = new Form("My Collections");
        home.setLayout(new BorderLayout());
        home.setScrollableY(false);
        
        Image im = FontImage.createMaterial(FontImage.MATERIAL_STAR, UIManager.getInstance().getComponentStyle("Command"));
        Command edit = new Command("Sent Entries", im) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                showAllEntries(true);
            }
        };
        //home.getToolbar().addCommandToOverflowMenu(edit);

        im = FontImage.createMaterial(FontImage.MATERIAL_STAR_HALF, UIManager.getInstance().getComponentStyle("Command"));
        Command add = new Command("Draft Entries", im) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                 showAllEntries(false);
            }
        };
        home.getToolbar().addCommandToOverflowMenu(edit);
        home.getToolbar().addCommandToOverflowMenu(add);

        buildSideMenu();
        home.show();
        loadCollections();
        
    }
    
    public void showProjectDetails(final String project, final Map details, final String access1, Form s)
    {
        String text = "Project Name : \n"+details.get("name").toString()+"\n\nAccess: \n"+details.get("access").toString()+"\n\nOwner: \n"+details.get("user").toString();
        Dialog.show("Project", text, "OK", null);
        
    }
    
    
    public void buildProjectForm(final String code, final String access)
    {
        Form f = new Form("Project Sections");
        setBackCommand(f, home);
        f.setLayout(new BorderLayout());
        FontImage img = FontImage.createMaterial(FontImage.MATERIAL_DESCRIPTION, UIManager.getInstance().getComponentStyle("TitleCommand"));
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
        final Map map = fromDevice.read(code, sep, dir);
        Command nqp1 = new Command("", img)
        {
            public void actionPerformed(ActionEvent v)
            {
               showProjectDetails(code, map, access, f);
            }
        };
        f.getToolbar().addCommandToRightBar(nqp1);
        
        Container c = new Container(new FlowLayout(Component.CENTER));
        c.setUIID("AContainer");
        Label name = new Label(map.get("name").toString());
        c.addComponent(name);
        final Container p = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        final Helper helper = new Helper();
        String sections[] = helper.split(map.get("sections").toString().trim(), "##");
        for(int x=0; x<sections.length-1; x++)
        {
            final MultiButton b = new MultiButton();
            b.setUIID("Gray1");
            final String section = sections[x];
            String count = fromDevice.getEntryCount(code, section);
            b.setTextLine3(count+" entries");
            b.setTextLine1(section);
            final List<Map<String,String>> comp = fromDevice.readSectionAccess(code, sep, dir);
            for(Map m: comp)
            {
                String s = m.get("section").toString();
                String u = m.get("user").toString().toLowerCase();
                final String acode = m.get("code").toString();                                                                                                            
                String aa[] = helper.split(s, ";");
                ArrayList lx = new ArrayList();
                for(String t: aa)
                {
                    char old = ' ';
                    char rep = '_';
                    t = t.replace(old, rep);
                    lx.add(t.toLowerCase());
                }
                if(lx.contains(section) && userName.toLowerCase().equals(u.toLowerCase()) && access.equals("Private"))
                {
                    try
                    {
                        String rcode = getSectionCode(comp, section, helper);
                        String fff = Storage.getInstance().readObject("r"+rcode).toString();
                        if(fff.equals("r"+rcode))
                        {
                            FontImage.setMaterialIcon(b, FontImage.MATERIAL_LOCK_OPEN);
                        }
                        else
                        {
                            FontImage.setMaterialIcon(b, FontImage.MATERIAL_LOCK); 
                        }
                    }
                    catch(Exception er)
                    {
                        er.printStackTrace();
                        FontImage.setMaterialIcon(b, FontImage.MATERIAL_LOCK); 
                    }
                } 
            }
            Image im = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_RIGHT, UIManager.getInstance().getComponentStyle("Label"));
            b.setEmblemUIID("AccessCodeInfo");
            b.setEmblem(im);
            p.addComponent(b);
            b.addActionListener(e ->
            {
                openAllEntriesForSection(code, section, access, f);
            });
        }
        
        Container main = BoxLayout.encloseY(c, p);
        f.addComponent("Center", main);
        
        
        f.show();
    }
    
    
    public String getSectionCode(List<Map<String,String>> lx, final String section, Helper helper)
    {
        String required = "";
        String code = "";
        for(Map m: lx)
            {
                String u = m.get("user").toString().toLowerCase();
                String s = m.get("section").toString();
                String aa[] = helper.split(s, ";");
                for(String t: aa)
                {
                    char old = ' ';
                    char rep = '_';
                    t = t.replace(old, rep);
                    if(t.toLowerCase().equals(section) && userName.toLowerCase().equals(u.toLowerCase()))
                    {
                        required = m.get("code").toString();
                    }
                }
            }

        return required;
    }
    
    
    public void loadCollections()
    {
        final Dialog waiter = showWaitDialog();
        holder = new Container();
        holder.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects"; 
        final Helper helper = new Helper();
        Display.getInstance().callSerially(new Runnable()
        {
            public void run()
            {
                Display.getInstance().callSerially(new Runnable()
                {
                    public void run()
                    {
                        waiter.showPacked(BorderLayout.CENTER, false);
                    }
                });

                
                   String projects[] = null;
                   try
                   {
                       projects = FileSystemStorage.getInstance().listFiles(dir);
                   }
                   catch(Exception er)
                   {
                       er.printStackTrace();
                   }
                   int x= 0;
                   final int y = projects.length;
                for (final String project : projects)
                {
                    x++;
                    final int sec = x;
                    final Map map = fromDevice.read(project, sep, dir);
                    final String access = map.get("access").toString();
                    Thread tt = new Thread(new Runnable()
                    {
                        public void run()
                        {   
                            final MultiButton m = new MultiButton();
                            m.setUIID("Gray2");
                            Button delete = new Button();
                            delete.setUIID("FontSize1");
                            FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
                            Button open = new Button();
                            open.setUIID("FontSize1");
                            FontImage.setMaterialIcon(open, FontImage.MATERIAL_DONE);
                            open.addActionListener(new ActionListener()
                            {
                                public void actionPerformed(ActionEvent v)
                                {
                                    //showProjectForm(project, access);
                                }
                            });

                            delete.addActionListener(new ActionListener()
                            {
                                public void actionPerformed(ActionEvent v)
                                {
                                    boolean confirm = Dialog.show("Confirm", "Confirm delete..?", "YES", "NO");
                                    if(!confirm)
                                    {
                                         return;
                                    }
                                    String toDelete = dir + sep + project;
                                    FileSystemStorage.getInstance().delete(toDelete);
                                    holder.removeAll();
                                    loadCollections();
                                    holder.revalidate();
                                }
                            });
                            
                            m.setName(""+map.get("code"));
                            m.setTextLine1(""+map.get("name"));
                            System.out.println("Section is "+map.get("sections"));
                            String sections[] = null;
                            try
                            {
                                sections = helper.split(map.get("sections").toString(),"##");
                            }
                            catch(Exception er){}
                            m.setTextLine2(sections.length-1 + " sections");
                            Image img = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_ARROW_RIGHT, UIManager.getInstance().getComponentStyle("Label"));
                            m.setEmblem(img);
                            
                            if(access.equals("Private"))
                            {
                                m.setTextLine3("Sections require access codes.");
                                m.setUIIDLine3("AccessCodeInfo");
                            }
                            
                            m.addActionListener(new ActionListener()
                            {
                                public void actionPerformed(ActionEvent v)
                                {
                                    boolean isUserSet = checkIsUserSet();
                                    if(isUserSet == false)
                                    {
                                        Dialog.show("Error", "Please setup your details before proceeding.", "Ok", null);
                                        return;
                                    }
                                    buildProjectForm(project, access);

                                }
                            });

                            Display.getInstance().callSerially(new Runnable()
                            {
                                public void run()
                                {
                                    
                            holder.add(m);
                            holder.repaint();
                                }
                            });
                            
                            //holder.revalidate();
                                                 }
                                             });tt.start();
                    
                }
                Display.getInstance().callSerially(new Runnable()
                {
                    public void run()
                    {
                        waiter.dispose();
                    }
                });
                        
            }
        });
        
        
        final FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_BUILD);
        FloatingActionButton fp = fab.createSubFAB(FontImage.MATERIAL_CREATE, "");
        fp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                buildNewCollection();
            }
        });
        
        FloatingActionButton fp1 = fab.createSubFAB(FontImage.MATERIAL_ASSIGNMENT, "");
        fp1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                showToSendLaterEntries();
            }
        });
        
        FloatingActionButton fp2 = fab.createSubFAB(FontImage.MATERIAL_PERSON, "");
        fp2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                buildProfile();
            }
        });
        
        FloatingActionButton fp3 = fab.createSubFAB(FontImage.MATERIAL_SETTINGS, "");
        fp3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                buildSettingsForm();
            }
        });
        
        
        fab.bindFabToContainer(home.getContentPane());
        
        holder.addPullToRefresh(new Runnable()
        {
            public void run()
            {
                fab.remove();
                //home.getToolbar().removeSearchCommand();
                loadCollections();
            }
        });
        
        
        
        
        holder.setScrollableY(true);
        holder.setScrollVisible(false);
        home.addComponent("Center", holder);
       
        
    }
    
    public void buildNewCollection()
    {
        Form f = new Form("Download Project");
        setBackCommand(f, home);
        f.setLayout(new BorderLayout());
        Label l = new Label("Project Code");
        TextField t = new TextField("", "Project Code", 20, TextField.ANY);
        FloatingActionButton d = FloatingActionButton.createFAB(FontImage.MATERIAL_ARROW_DOWNWARD);
        Container c = BoxLayout.encloseY(FlowLayout.encloseCenter(l), t, FlowLayout.encloseCenter(d));
        f.addComponent("Center", c);
        d.addActionListener(e ->{
            if(t.getText().length() < 8)
            {
                ToastBar.showErrorMessage("Invalid project code");
                return;
            }
            final String code = t.getText();
            try
            {
            Display.getInstance().callSerially(new Runnable()
            {
            public void run()
            {
                
                String m = "error";
                
                    ToastBar.showMessage("Initializing...", FontImage.MATERIAL_DESKTOP_WINDOWS);
                    char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                    String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
                    final String filename = dir + sep + code+".collector";
                    System.out.println("Downloading JAJ to " + filename);
                    String  res1 = "empty";
                    if (FileSystemStorage.getInstance().exists(filename))
                    {
                       Dialog d = new Dialog();
                       boolean confirm = d.show("Confirm", "Project already exists...Overwrite..?", "YES", "NO");
                       if(!confirm)
                       {
                            return;
                       }
                    }
                    
                    boolean g = fetchJAJFile(code);
                    System.out.println("here is g "+g);
                    if(g == false)
                    {
                        return;
                    }
                    ToastBar.showMessage("Downloading...", FontImage.MATERIAL_IMPORT_EXPORT);
                    res1 = saveJAJToStorage(code, filename);
                    while(res1.equals("empty"))
                    {
                        
                    }
                    
                    if(res1.equals("done"))
                    {
                        ToastBar.getInstance().setVisible(false);
                        Dialog.show("Success", "Project downloaded successfully...", "Ok", null);
                        goHome();
                        res = "-";
                        System.out.println(response + " - "+res+" - "+res1);
                        return;
                    }
                    if(res1.equals("error"))
                    {
                        ToastBar.showErrorMessage("Error downloading project...", FontImage.MATERIAL_ERROR);
                        return;
                    }
                    if(res1.equals("not found"))
                    {
                        ToastBar.showErrorMessage("Project not found...", FontImage.MATERIAL_ERROR);
                        return;
                    } 
            }
        });
        }
        catch(Exception er)
                {
                    ToastBar.showErrorMessage("Error making remote connection...", FontImage.MATERIAL_ERROR);
                }
        });
        f.show();
        c.animateLayout(150);
        
        
    }
    
    
    public String saveJAJToStorage(String code, String filename)
    {
        String res = "error";
        boolean v = false;
        if(!AMAZON_URL.endsWith("/"))
        {
            AMAZON_URL = AMAZON_URL+"/";
        }
        String d_url = AMAZON_URL+"projects/"+code+"/project.collector";
        System.out.println("this is the url "+d_url);
        try
        {   
            v = com.codename1.io.Util.downloadUrlToFile(d_url, filename, true);
            if(v==true)
            {
                res = "done";
            }
            else
            {
                res = "not found";
            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
            res = "error";
        }
        return res;
    }
    
    
    public void showToSendLaterEntries()
    {
        Form f = new Form("Entries To Send");
        setBackCommand(f, home);
        f.setLayout(new BorderLayout());
        Container main = new Container(new BorderLayout());
        SpanLabel sp = new SpanLabel("The entries below have been marked as 'Send Later' entries. You will be notified every time you start the DataPlat.");
        //main.addComponent(BorderLayout.NORTH, sp);
        Container n = new Container();
        n.setLayout(new FlowLayout(Component.RIGHT));
       
        
        f.addComponent("North", n);
        
        String h[] = Storage.getInstance().listEntries();
        List lx = new ArrayList();
        for(String toSend: h)
        {
            if(toSend.startsWith("later"))
            {
                lx.add(toSend);
            }
        }
        final ProjectReader fromDevice = new ProjectReader();
        final char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects"; 
        final Container c = new Container();
        c.setScrollableY(true);
        c.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        if(lx.size() > 0)
        {
            for(Object entry: lx)
            {
                System.out.println(entry);
                String ss = Storage.getInstance().readObject(entry.toString()).toString();
                String s[] = new Helper().split(ss, "@@#");
                String section = s[0].toString().trim();
                String code = s[1].toString().trim();
                String toSend = s[2].toString().trim();
                final Map map = fromDevice.read(code, sep, dir);
                String project = map.get("name").toString();
                MultiButton m = new MultiButton();
                m.setUIID("FabLater");
                //m.setUIID("TitleArea");
                m.setName(ss);
                m.setTextLine1(project);
                m.setTextLine2(section);
                m.setTextLine3(toSend);
                m.setCheckBox(true);
                //m.setEmblemUIID("Button");
                //m.setIcon(createIcon(FontImage.MATERIAL_ASSIGNMENT));
                c.addComponent(m);
                
            }
        }
        
        Image im = FontImage.createMaterial(FontImage.MATERIAL_SELECT_ALL, UIManager.getInstance().getComponentStyle("Command"));
        Command edit = new Command("Select All", im) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                for(int count=0; count<c.getComponentCount();count++)
                {
                    Component comp = c.getComponentAt(count);
                    if(comp instanceof MultiButton)
                    {
                        MultiButton t = (MultiButton)comp;
                        t.setSelected(true);
                    }
                }
            }
        };
        f.getToolbar().addCommandToOverflowMenu(edit);

        im = FontImage.createMaterial(FontImage.MATERIAL_TAB_UNSELECTED, UIManager.getInstance().getComponentStyle("Command"));
        Command add = new Command("Unselect All", im) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                  for(int count=0; count<c.getComponentCount();count++)
                {
                    Component comp = c.getComponentAt(count);
                    if(comp instanceof MultiButton)
                    {
                        MultiButton t = (MultiButton)comp;
                        t.setSelected(false);
                    }
                }
            }
        };
        f.getToolbar().addCommandToOverflowMenu(add);
        
        im = FontImage.createMaterial(FontImage.MATERIAL_DELETE, UIManager.getInstance().getComponentStyle("Command"));
        Command de = new Command("Delete", im) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                  for(int count=0; count<c.getComponentCount();count++)
                {
                    Component comp = c.getComponentAt(count);
                    if(comp instanceof MultiButton)
                    {
                        MultiButton t = (MultiButton)comp;
                        t.setSelected(false);
                    }
                }
            }
        };
        f.getToolbar().addCommandToOverflowMenu(de);
        
        im = FontImage.createMaterial(FontImage.MATERIAL_SEND, UIManager.getInstance().getComponentStyle("Command"));
        Command des = new Command("Send", im) {

            @Override
            public void actionPerformed(ActionEvent evt) {
                  for(int count=0; count<c.getComponentCount();count++)
                {
                    Component comp = c.getComponentAt(count);
                    if(comp instanceof MultiButton)
                    {
                        MultiButton t = (MultiButton)comp;
                        t.setSelected(false);
                    }
                }
            }
        };
        f.getToolbar().addCommandToOverflowMenu(des);

        
       
        main.addComponent("Center", c);
        f.addComponent("Center" ,main);
        f.show();
    }
    
    
    public void buildSettingsForm()
    {
        final Form f = new Form("Settings");
        setBackCommand(f, home);
        f.setLayout(new BorderLayout());
        Container main  = new Container();
        main.setLayout(new BorderLayout());
        main.setScrollableY(true);
        Container s = new Container();
        s.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        final MultiButton v = new MultiButton();
        v.setTextLine1("LS Server");
        FontImage.setMaterialIcon(v, FontImage.MATERIAL_DESKTOP_MAC);
        s.add(v);
        v.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                Form fv = new Form("LS Settings");
                
                TextField tx = new TextField();
                CheckBox cb = new CheckBox("Port Number");
                TextField port = new TextField();
                Command cm = new Command("")
                {
                   public void actionPerformed(ActionEvent v)
                   {
                       String ls = tx.getText();
                       Storage.getInstance().readObject("LS");
                       Storage.getInstance().readObject("LSPort");
                       if(ls.length() > 5 )
                       {
                           Storage.getInstance().writeObject("LS", ls);
                       }
                       if(port.getText().length() > 0 )
                       {
                           Storage.getInstance().writeObject("LSPort", port.getText());
                       }
                       else
                       {
                           Storage.getInstance().writeObject("LSPort", "0"); 
                       }
                       loadDefaults();
                       f.showBack();
                   }
               };
                Image img = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand"));
                cm.setIcon(img);
                fv.getToolbar().addCommandToLeftBar(cm);
                fv.getToolbar().setTitleCentered(true);
                fv.setBackCommand(cm);
               
               fv.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
               
               SpanLabel l = new SpanLabel("Please enter the Listening Server where the DataPlat web is running below.");
               fv.addComponent(l);
               
               tx.setConstraint(TextField.URL);
               fv.addComponent(tx);
               fv.add(cb);
               fv.add(port);
               String ss = "";
               try
                {
                    ss = Storage.getInstance().readObject("LS").toString();
                    if(ss.length() < 12)
                    {
                        ss = "<Not Set>";
                    }
                }
                catch(Exception ee)
                {
                    ss = "<Not Set>";
                }
               cb.addActionListener(new ActionListener()
               {
                   public void actionPerformed(ActionEvent v)
                   {
                       cb.setSelected(cb.isSelected());
                       if(cb.isSelected())
                       {
                           port.setEditable(true);
                       }
                       else
                       {
                           port.setText("");
                           port.setEditable(false);
                       }
                   }
               });
               port.setEditable(false);
               String p = "";
               try{ p = Storage.getInstance().readObject("LSPort").toString();p.toString();}catch(Exception ee){p="0";}
               tx.setText(ss);
               if(p != "0")
               {
                   cb.setSelected(true);
                   port.setEditable(true);
                   port.setText(p);
               }
               fv.show();
               
            }
        });
        
        final MultiButton l = new MultiButton();
        l.setTextLine1("FL Server");
        FontImage.setMaterialIcon(l, FontImage.MATERIAL_DESKTOP_WINDOWS);
        s.add(l);
        l.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent v)
            {
                final Form fv = new Form("FL Settings");
                TextField tx = new TextField();
                CheckBox cb = new CheckBox("Port Number");
                TextField port = new TextField();
                Command cm = new Command("")
                {
                   public void actionPerformed(ActionEvent v)
                   {
                       String ls = tx.getText();
                       Storage.getInstance().readObject("FL");
                       Storage.getInstance().readObject("FLPort");
                       if(ls.length() > 5 )
                       {
                           Storage.getInstance().writeObject("FL", ls);
                       }
                       if(port.getText().length() > 0 )
                       {
                           Storage.getInstance().writeObject("FLPort", port.getText());
                       }
                       else
                       {
                           Storage.getInstance().writeObject("FLPort", "0");
                       }
                       loadDefaults();
                       f.showBack();
                   }
               };
               
                Image img = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand"));
                cm.setIcon(img);
                fv.getToolbar().addCommandToLeftBar(cm);
                fv.getToolbar().setTitleCentered(true);
                fv.setBackCommand(cm);
                
                
               fv.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
               
               SpanLabel l = new SpanLabel("Please enter the Files Server where media files will be stored. It may be the same as the LS server.");
               fv.addComponent(l);
               tx.setConstraint(TextField.URL);
               fv.addComponent(tx);
               String ss = "";
               try
                {
                    ss = Storage.getInstance().readObject("FL").toString();;
                    if(ss.length() < 12)
                    {
                        ss = "<Not Set>";
                    }
                }
                catch(Exception ee)
                {
                    ss = "<Not Set>";
                }
               port.setEditable(false);
               cb.addActionListener(new ActionListener()
               {
                   public void actionPerformed(ActionEvent v)
                   {
                       cb.setSelected(cb.isSelected());
                       if(cb.isSelected())
                       {
                           port.setEditable(true);
                       }
                       else
                       {
                           port.setText("");
                           port.setEditable(false);
                       }
                   }
               });
               String p = "";
               try{ p = Storage.getInstance().readObject("FLPort").toString();port.setText(p);}catch(Exception ee){p="0";port.setText("");}
               tx.setText(ss);
               if(p.length() > 1)
               {
                   cb.setSelected(true);
                   port.setEditable(true);
               }
               fv.add(cb);
               fv.add(port);
               fv.show();
               
            }
        });
        
        String ss = "";
        String fl = "";
        try
        {
            ss = Storage.getInstance().readObject("LS").toString();
            if(ss.length() < 12)
            {
                ss = "<Not Set>";
            }
        }
        catch(Exception ee)
        {
            ss = "<Not Set>";
        }
        try
        {
            fl = Storage.getInstance().readObject("FL").toString();
            if(fl.length() < 12)
            {
                fl = "<Not Set>";
            }
        }
        catch(Exception ea)
        {
            fl = "<Not Set>";
        }

        v.setTextLine3(ss);
        l.setTextLine3(fl);
        v.setUIIDLine3("MultiLine2");
        l.setUIIDLine3("MultiLine2");
        
        final MultiButton r = new MultiButton();
        r.setTextLine1("Access Code");
        FontImage.setMaterialIcon(r, FontImage.MATERIAL_ENHANCED_ENCRYPTION);
        r.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    final Dialog d = new Dialog("== Select an option ==");
                    d.setDisposeWhenPointerOutOfBounds(true);
                    Container c = new Container();
                    d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                    RadioButton b1 = new RadioButton("Ask Once");
                    RadioButton b2 = new RadioButton("Ask Always");
                    ButtonGroup bg = new ButtonGroup();bg.add(b1);bg.add(b2);
                    d.addComponent(b1);
                    d.addComponent(b2);
                    try
                {
                    String g = Storage.getInstance().readObject("access_code").toString();
                    if(g.equals("always"))
                {
                    b2.setSelected(true);
                }else{b1.setSelected(true);}
                }catch(Exception e){b2.setSelected(true);}
                    b1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    Storage.getInstance().writeObject("access_code", "once");
                    r.setTextLine3("Ask Once");
                    d.dispose();
                }
                });
                    
                    b2.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    Storage.getInstance().writeObject("access_code", "always");
                    r.setTextLine3("Ask Always");
                    d.dispose();
                }
                });
                    d.show();
                    
                }
                });
        try{ String i = Storage.getInstance().readObject("access_code").toString();
        if(i != null)
        {
            if(i.equals("always"))
            {
                r.setTextLine3("Ask Always");
            }
            else
            {
                r.setTextLine3("Ask Once");
            }
        }
        else
        {
            r.setTextLine3("Ask Always");
        }}catch(Exception er){r.setTextLine3("Ask Always");}
        r.setUIIDLine3("MultiLine2");
        s.addComponent(r);
        
        final MultiButton b = new MultiButton();
        b.setTextLine1("Entry Sent Status");
        FontImage.setMaterialIcon(b, FontImage.MATERIAL_VISIBILITY);
        b.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    final Dialog d = new Dialog("== Select an option ==");
                    d.setDisposeWhenPointerOutOfBounds(true);
                    Container c = new Container();
                    d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
                    RadioButton b1 = new RadioButton("Show");
                    RadioButton b2 = new RadioButton("Hide");
                    ButtonGroup bg = new ButtonGroup();bg.add(b1);bg.add(b2);
                    d.addComponent(b1);
                    d.addComponent(b2);
                    try
                {
                    String g = Storage.getInstance().readObject("sent_status").toString();
                    if(g.equals("show"))
                {
                    b1.setSelected(true);
                }else{b2.setSelected(true);}
                }catch(Exception e){b1.setSelected(true);}
                    b1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    Storage.getInstance().writeObject("sent_status", "show");
                    b.setTextLine3("Show");
                    d.dispose();
                }
                });
                    
                    b2.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent v)
                {
                    Storage.getInstance().writeObject("sent_status", "hide");
                    b.setTextLine3("Hide");
                    d.dispose();
                }
                });
                    d.show();
                    
                }
                });
        try{ String i = Storage.getInstance().readObject("sent_status").toString();
        if(i != null)
        {
            if(i.equals("show"))
            {
                b.setTextLine3("Show");
            }
            else
            {
                b.setTextLine3("Hide");
            }
        }
        else
        {
            b.setTextLine3("Show");
        }}catch(Exception er){b.setTextLine3("Show");}
        b.setUIIDLine3("MultiLine2");
        s.addComponent(b);
        
        
        main.addComponent(BorderLayout.CENTER, s);
        f.addComponent("Center", main);
        f.show();
    }
    
    public void buildProfile()
    {
        Form profile = new Form("Profile");
        profile.setLayout(new BorderLayout());
        setBackCommand(profile, home);
        Container c = buildProfileContainer(profile);
        profile.addComponent("Center", c);
        profile.show();
    }
    
    public Container buildProfileC(Form f)
    {
        Container c = new Container(BoxLayout.y());

        final TextField name = new TextField("", "Name", 20, TextField.ANY);
        FontImage.setMaterialIcon(name.getHintLabel(), FontImage.MATERIAL_PERSON);
        final TextField email = new TextField("", "E-mail", 20, TextField.EMAILADDR);
        c.addComponent(name);
        c.addComponent(email);
        Button l = new Button("Done");
        c.addComponent(l);
        l.addActionListener(e -> {
            System.out.println(picAdded);
            String n = name.getText();
            String m = email.getText();
            /*
            if(picAdded == false)
            {
                ToastBar.showErrorMessage("Tap the icon to setup photo");
                return;
            }
*/
            Map p = new HashMap();
            p.put("name",n);
            p.put("email",m);
            Storage.getInstance().writeObject("userData", p);
            userEmail = m;
            userName = m;
            goHome();
           
        });
        return c;
    }
    
    
    public Container buildProfileContainer(Form f)
    {
        Container profile = new Container();
        profile.setLayout(new BorderLayout());
        final TextField name = new TextField("", "Name", 20, TextField.ANY);
        FontImage.setMaterialIcon(name.getHintLabel(), FontImage.MATERIAL_PERSON);
        final TextField email = new TextField("", "E-mail", 20, TextField.EMAILADDR);
        FontImage.setMaterialIcon(email.getHintLabel(), FontImage.MATERIAL_EMAIL);
        
        Validator val = new Validator();
        Button loginButton = new Button("Done");
        val.addSubmitButtons(loginButton);
        loginButton.setUIID("LoginButton");
        loginButton.addActionListener(e -> {
            System.out.println(picAdded);
            String n = name.getText();
            String m = email.getText();
            /*
            if(picAdded == false)
            {
                ToastBar.showErrorMessage("Tap the icon to setup photo");
                return;
            }
*/
            Map p = new HashMap();
            p.put("name",n);
            p.put("email",m);
            Storage.getInstance().writeObject("userData", p);
            userEmail = m;
            userName = m;
            goHome();
           
        });
        
        
        Container comps = new Container();
        addComps(f, comps, new Label("Name", "InputContainerLabel"), name, new Label("E-Mail", "InputContainerLabel"), email,  new Label (""), loginButton);
        
        comps.setScrollableY(true);
        comps.setUIID("PaddedContainer");
        
        Container content = BorderLayout.center(comps);
        
        Button avatar = new Button("");
        avatar.setUIID("InputAvatar");
        Image defaultAvatar = FontImage.createMaterial(FontImage.MATERIAL_PERSON, "InputAvatarImage", 8);
        Image circleMaskImage = theme.getImage("circle.png");

        defaultAvatar = defaultAvatar.scaled(circleMaskImage.getWidth(), circleMaskImage.getHeight());
        defaultAvatar = ((FontImage)defaultAvatar).toEncodedImage();
        Object circleMask = circleMaskImage.createMask();
        defaultAvatar = defaultAvatar.applyMask(circleMask);
        avatar.setIcon(defaultAvatar);
        
        avatar.addActionListener(e -> {
            if(Dialog.show("Camera or Gallery", "Would you like to use the camera or the gallery for the picture?", "Camera", "Gallery")) {
                String pic = Capture.capturePhoto();
                System.out.println("this is pic "+pic);
                if(pic != null) {
                    try {
                        Image img = Image.createImage(pic);
                        Storage.getInstance().writeObject("userPhoto", "photo");
                        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                        final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep;
                         
                        OutputStream dest = FileSystemStorage.getInstance().openOutputStream(dir + "photo.jpg");
                        ImageIO.getImageIO().save(img, dest, ImageIO.FORMAT_JPEG, 0.85f);
                        img = img.fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                        avatar.setIcon(img.applyMask(circleMask));
                        picAdded = true;
                    } catch(Exception err) {
                        picAdded = false;
                        ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                        err.printStackTrace();
                    }
                }
                
            } else {
                Display.getInstance().openGallery(ee -> { 
                    try{
                    if(ee.getSource() != null) {
                        try {
                            Image img = Image.createImage((String)ee.getSource());
                            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
                            final String dir = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep;
                        
                        OutputStream dest = FileSystemStorage.getInstance().openOutputStream(dir + "photo.jpg");
                        ImageIO.getImageIO().save(img, dest, ImageIO.FORMAT_JPEG, 0.85f);
                            Storage.getInstance().writeObject("userPhoto", "photo");
                            img = img.fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                            avatar.setIcon(img.applyMask(circleMask));
                            picAdded = true;
                        } catch(Exception err) {
                            picAdded = false;
                            ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                            Log.e(err);
                        }
                    }
                    }catch(Exception eee){}
                   
                }, Display.GALLERY_IMAGE);
            }
        });
        
        try
        {
            final Map m = (Map)Storage.getInstance().readObject("userData");
            name.setText(m.get("name").toString());
            email.setText(m.get("email").toString());
            char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
            final String p = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"photo.jpg";
            Image img = Image.createImage(FileSystemStorage.getInstance().openInputStream(p));
            img = img.fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
            avatar.setIcon(img.applyMask(circleMask));
            picAdded = true;
        }
        catch(Exception er)
        {
            er.printStackTrace();
        }
        
        Container actualContent = LayeredLayout.encloseIn(content, 
                        FlowLayout.encloseCenter(avatar));
        
        //profile.addComponent("North", avatar);
        
        Container input;
        if(!Display.getInstance().isTablet()) {
            Label placeholder = new Label(" ");

            Component.setSameHeight(actualContent, placeholder);
            Component.setSameWidth(actualContent, placeholder);

            input = BorderLayout.center(placeholder);

            f.addShowListener(e -> {
                if(placeholder.getParent() != null) {
                    input.replace(placeholder, actualContent, CommonTransitions.createFade(1500));
                }
            });
        } else {
            input = BorderLayout.center(actualContent);
        }
        input.setUIID("InputContainerBackground");
        
        
        
        
        profile.addComponent("Center", input);
        return profile;
    }
    
    
    private void addComps(Form parent, Container cnt, Component... cmps) {
        if(Display.getInstance().isTablet() || !Display.getInstance().isPortrait()) {
            TableLayout tl = new TableLayout(cmps.length / 2, 2);
            cnt.setLayout(tl);
            tl.setGrowHorizontally(true);
            for(Component c : cmps) {
                if(c instanceof Container) {
                    cnt.add(tl.createConstraint().horizontalSpan(2), c);
                } else {
                    cnt.add(c);
                }
            }
        } else {
            cnt.setLayout(BoxLayout.y());
            for(Component c : cmps) {
                cnt.add(c);
            }
        }
        if(cnt.getClientProperty("bound") == null) {
            cnt.putClientProperty("bound", "true");
            if(!Display.getInstance().isTablet()) {
                parent.addOrientationListener((e) -> {
                    Display.getInstance().callSerially(() -> {
                        cnt.removeAll();
                        addComps(parent, cnt, cmps);
                        cnt.animateLayout(800);
                    });
                });
            }
        }
    }
    
    
    
    
    public Dialog showWaitDialog()
    {
        Dialog d =new Dialog();
        d.setTitle("Please wait...");
        d.setLayout(new BorderLayout());
        d.addComponent("Center", new InfiniteProgress());
        //bok = new Button("OK");
        return d;
    }
    
    public void showWaiter(final String text)
    {
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                Dialog waiter = new Dialog();
                waiter.setTitle("Please wait...");
                wlabel = new Label(text);
                waiter.setLayout(new BorderLayout());
                waiter.addComponent("North", wlabel);
                //waiter.addComponent("Center", new InfiniteProgress());
                //bok = new Button("OK");
                waiter.showPacked(BorderLayout.CENTER, true);
            }
        });
        t.start();
        //waiter.show();
    }
    String userName = "not_set", userEmail = "not_set";
    
    public void makeDirs()
    {
        char sep = FileSystemStorage.getInstance().getFileSystemSeparator();
        String root = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep;
        if(!FileSystemStorage.getInstance().exists(root))
        {
            FileSystemStorage.getInstance().mkdir(root);
        }
        
        root = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"projects";
        if(!FileSystemStorage.getInstance().exists(root))
        {
            FileSystemStorage.getInstance().mkdir(root);
        }
        
        root = FileSystemStorage.getInstance().getAppHomePath() + sep + "dataplat"+sep+"entries";
        if(!FileSystemStorage.getInstance().exists(root))
        {
            FileSystemStorage.getInstance().mkdir(root);
        }
    }
    
    public boolean checkIsUserSet()
    {
        makeDirs();
        boolean isSet = false;
        try
        {
            Map m = (Map)Storage.getInstance().readObject("userData");
            userName = m.get("name").toString();
            userEmail = m.get("email").toString();
            isSet = true;
        }
        catch(Exception e)
        {
            userName = "not_set";
            userEmail = "not_set";
        }
        return isSet;
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

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }
    
    
    public void HelpForm()
    {
        Form f = new Form();
        f.setLayout(new LayeredLayout());
        f.getTitleArea().removeAll();
        f.getTitleArea().setUIID("Container");
        
        f.setTransitionOutAnimator(CommonTransitions.createUncover(CommonTransitions.SLIDE_HORIZONTAL, true, 400));
        
        Tabs walkthruTabs = new Tabs();
        walkthruTabs.setUIID("Container");
        walkthruTabs.getContentPane().setUIID("Container");
        walkthruTabs.getTabsContainer().setUIID("Container");
        walkthruTabs.hideTabs();
        
        Image notes = theme.getImage("097-connect_server.png");
        Image duke = theme.getImage("402-down.png");
        Image add = theme.getImage("034-doc_add.png");
        Image tr = theme.getImage("785-execute_form_comp.png");
        
        
        
        Label notesPlaceholder = new Label("","PF");
        Label notesLabel = new Label(notes, "PF");
        Label notesPlaceholder1 = new Label("","PF");
        Label notesLabel1 = new Label(duke, "PF");
        Label notesPlaceholder2 = new Label("","PF");
        Label notesPlaceholder2a = new Label("","PF");
        Label notesLabel2 = new Label(add, "PF");
        Label notesLabel2a = new Label(add, "PF");
        Component.setSameHeight(notesLabel, notesPlaceholder);
        Component.setSameWidth(notesLabel1, notesPlaceholder1);
        Component.setSameWidth(notesLabel2, notesPlaceholder2);
        Component.setSameWidth(notesLabel2a, notesPlaceholder2a);
        
        Label bottomSpace = new Label();
        
        Container tab1 = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                notesPlaceholder,
                new Label("Server Setup", "WalkthruWhite"),
                new SpanLabel("Setup LS and FL server under settings  to get started. This is required for data transmission",  "WalkthruBody"),
                bottomSpace
        ));
        tab1.setUIID("WalkthruTab1");
        
        walkthruTabs.addTab("", tab1);
        
        Label bottomSpaceTab2 = new Label();
        Container tab2 = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                 notesPlaceholder1,
                new Label(duke, "PF"),
                new Label("Fetch Project", "WalkthruWhite"),
                new SpanLabel("Using an obtained project code, download data collection instrument  " +
                                            "and start collecting data ",  "WalkthruBody"),
                bottomSpaceTab2
        ));
        
        tab2.setUIID("WalkthruTab2");

        walkthruTabs.addTab("", tab2);
        
        Label bottomSpaceTab3 = new Label();
        Container tab3 = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                 notesPlaceholder2,
                new Label(add, "PF"),
                new Label("Create Entries", "WalkthruWhite"),
                new SpanLabel("Open a project and create collection entries.\n\n\n",  "WalkthruBody"),
                bottomSpaceTab3
        ));
        
        tab3.setUIID("WalkthruTab3");

        walkthruTabs.addTab("", tab3);
        
        Label bottomSpaceTab2a = new Label();
        Container tab2a = BorderLayout.centerAbsolute(BoxLayout.encloseY(
                 notesPlaceholder2a,
                new Label(tr, "PF"),
                new Label("Collect and Transmit", "WalkthruWhite"),
                new SpanLabel("Collect on or offline and transmit data once internet  " +
                                            "access becomes available ",  "WalkthruBody"),
                bottomSpaceTab2a
        ));
        
        tab2a.setUIID("WalkthruTab4");
        walkthruTabs.addTab("", tab2a);
        
        
        
        f.add(walkthruTabs);
        
        ButtonGroup bg = new ButtonGroup();
        Image unselectedWalkthru = theme.getImage("unselected-walkthru.png");
        Image selectedWalkthru = theme.getImage("selected-walkthru.png");
        RadioButton[] rbs = new RadioButton[walkthruTabs.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(CENTER);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        walkthruTabs.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
        
        Button skip = new Button("SKIP ");
        skip.setUIID("SkipButton");
        skip.addActionListener(e -> saveWalk());
        
        Container southLayout = BoxLayout.encloseY(
                        radioContainer,
                        skip
                );
        f.add(BorderLayout.south(
                southLayout
        ));
        
        Component.setSameWidth(bottomSpace, bottomSpaceTab2, southLayout);
        Component.setSameHeight(bottomSpace, bottomSpaceTab2, southLayout);
        
        // visual effects in the first show
        f.addShowListener(e -> {
            notesPlaceholder.getParent().replace(notesPlaceholder, notesLabel, CommonTransitions.createFade(1500));
        });
        f.show();
    }
    
    
    public void saveWalk()
    {
        Storage.getInstance().writeObject("walk", "walk");
        goHome();
    }
    
    
    public class MapLayout extends Layout implements MapListener {
    private static final String COORD_KEY = "$coord";
    private MapContainer map;
    private Container actual;
    public MapLayout(MapContainer map, Container actual) {
        this.map = map;
        this.actual = actual;
        map.addMapListener(this);
    }

    @Override
    public void addLayoutComponent(Object value, Component comp, Container c) {
        comp.putClientProperty(COORD_KEY, (Coord)value);
    }

    @Override
    public boolean isConstraintTracking() {
        return true;
    }

    @Override
    public Object getComponentConstraint(Component comp) {
        return comp.getClientProperty(COORD_KEY);
    }

    @Override
    public boolean isOverlapSupported() {
        return true;
    }

    @Override
    public void layoutContainer(Container parent) {
        for(Component current : parent) {
            Coord crd = (Coord)current.getClientProperty(COORD_KEY);
            Point p = map.getScreenCoordinate(crd);
            current.setSize(current.getPreferredSize());
            current.setX(p.getX() - current.getWidth() / 2);
            current.setY(p.getY() - current.getHeight());
        }
    }

    @Override
    public Dimension getPreferredSize(Container parent) {
        return new Dimension(100, 100);
    }

    @Override
    public void mapPositionUpdated(Component source, int zoom, Coord center) {
        actual.setShouldCalcPreferredSize(true);
        actual.revalidate();
    }
}
    

}


