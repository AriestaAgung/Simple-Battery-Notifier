package tech.devatacreative.batterynotifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class MainForm  {

    private JLabel Title;
    private JTextField chargePresentationField;
    private JButton applyButton;
    private JRadioButton rbEnabled;
    private JRadioButton rbDisable;
    private JPanel panel;
    private JLabel batteryStatusLabel;
    JCheckBox bgCheckbox;
    String percentation;
    String percentNotif;
    Boolean bgCheckStatus;
    static Preferences prefs = Preferences.userRoot().node("tech/devatacreative/batterynotifier/prefs");


    Kernel32.SYSTEM_POWER_STATUS batteryStatus;


    public MainForm() {
//        percentation = chargePresentationField.getText();
//        Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
//        Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
        checkBatteryStatusEverySec();
        System.out.println(batteryStatus.getBatteryLifePercent());
//        percentNotif = chargePresentationField.getText();
        batteryStatusLabel.setText(convertToMultiline(batteryStatus.toString()));
        Integer percents = Integer.valueOf(batteryStatus.getBatteryLifePercent().replace("%", ""));
        System.out.println(batteryStatus.getBatteryLifePercent());




        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rbEnabled.isSelected()){

//                    try {
//                        if (percents < 20){
//                            notif50();
//                            System.out.println("under "+percents+" ");
//                        } else if (batteryStatus.getBatteryLifePercent() == "100%"){
//                            notifCabut();
//                        } else if (batteryStatus.getBatteryLifePercent() == "20%"){
//                            notifCritical();
//                        }
//                    } catch (AWTException e1) {
//                        e1.printStackTrace();
//                    }
                    checkBatteryEverySec();
                    JOptionPane.showMessageDialog(null, "Enable selected !");
                } else if (rbDisable.isSelected()){
                    JOptionPane.showMessageDialog(null, "Disable selected !");
                }
            }
        });
        rbDisable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbEnabled.setSelected(false);
            }
        });
        rbEnabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbDisable.setSelected(false);
            }
        });
//        bgCheckbox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
    }




//    public static Boolean checkBGStatus(){
//        if (bgCheckbox.isSelected()){
////            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//            return true;
//        } else {
////            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            return false;
//        }
//    }

    public static String convertToMultiline(String orig)
    {
        return "<html>" + orig.replaceAll("\n", "<br>");
    }

    public String checkBatteryStatusEverySec(){
        batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
        Runnable checking = new Runnable() {
            @Override
            public void run() {

                Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
//                System.out.println(batteryStatus.toString());
                batteryStatusLabel.setText(convertToMultiline(batteryStatus.toString()));

            }
        };
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleWithFixedDelay(checking, 1, 2, TimeUnit.SECONDS);
        return batteryStatus.toString();
    }

    public void checkBatteryEverySec(){
        Runnable checking = new Runnable() {
            @Override
            public void run() {
                Integer percents = Integer.valueOf(batteryStatus.getBatteryLifePercent().replace("%", ""));
                System.out.println(percents);
                Boolean status;
                try {
                    switch (percents){
                        case 50:
                            if (batteryStatus.getACLineStatusString().equals("Not Charging")){
                            notif50();
                            }
                            break;
                        case 20: case 15: case 35:
                            if (batteryStatus.getACLineStatusString().equals("Not Charging")){
                                notifCritical();
                            }
                            break;
                        case 100:
                            if (batteryStatus.getACLineStatusString().equals("Charging")){
                                notifCabut();

                            }
                            break;
                            default:
                                break;
                    }
                } catch (AWTException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleWithFixedDelay(checking, 1, 30, TimeUnit.SECONDS);
    }

//    @Override
//    protected List<String> getFieldOrder() {
//        ArrayList<String> fields = new ArrayList<String>();
//        fields.add("BatteryPercent");
//
//        return fields;
//    }
//

    public void notif50() throws AWTException{
        SystemTray tray = SystemTray.getSystemTray();
        percentation = batteryStatus.getBatteryLifePercent();
        Image image = Toolkit.getDefaultToolkit().createImage("");
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Demo");
        tray.add(trayIcon);
        trayIcon.displayMessage("Battery Notifier", "Your battery percentation is "+percentation + ", remember to charge your laptop", TrayIcon.MessageType.WARNING);
        try {
            Thread.sleep(5000);
            tray.remove(trayIcon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifCritical() throws AWTException{
        SystemTray tray = SystemTray.getSystemTray();
        percentation = batteryStatus.getBatteryLifePercent();
        Image image = Toolkit.getDefaultToolkit().createImage("");
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Demo");
        tray.add(trayIcon);
        trayIcon.displayMessage("Battery Notifier", "Your battery percentation is on save mode at "+percentation + ", Charge your laptop soon !", TrayIcon.MessageType.ERROR);
        try {
            Thread.sleep(5000);
            tray.remove(trayIcon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifCabut() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        percentation = batteryStatus.getBatteryLifePercent();
        Image image = Toolkit.getDefaultToolkit().createImage("src/tech/devatacreative/batterynotifier/res/favicon.png");
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Battery Notifier");
        tray.add(trayIcon);
        trayIcon.displayMessage("Battery Notifier", "Your battery percentation is "+percentation + ", unplug your charger", TrayIcon.MessageType.INFO);
        try {
            Thread.sleep(5000);
            tray.remove(trayIcon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    public static void main(String[] args) {
//            PlatformLogger logger = PlatformLogger.getLogger("java.util.prefs");
//            logger.setLevel(PlatformLogger.Level.SEVERE);

        JFrame frame = new JFrame("Battery Status Notifier");
        frame.setContentPane(new MainForm().panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "Reminder ! if you click close button (X) the application will be on tray icon. To close it please right click on the tray icon");
//                super.windowClosing(e);
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        Image mainImg = Toolkit.getDefaultToolkit().createImage("src/tech/devatacreative/batterynotifier/res/favicon.png");
        ImageIcon mainIcon = new ImageIcon(mainImg);
        frame.setIconImage(mainIcon.getImage());


        Image image = Toolkit.getDefaultToolkit().getImage("src/tech/devatacreative/batterynotifier/res/trayIcon.png");
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image, "Battery Notifier", popup);
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        MenuItem showItem = new MenuItem("Restore Battery Notifier");
        showItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
            }
        });
        popup.add(showItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

    }
}

