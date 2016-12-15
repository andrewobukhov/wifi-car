import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

public class MainForm2 extends JFrame {
    public JPanel rootPanel;

    private Timer timer;
    private SocketClient socketClient;

    private String lastSysmbol;

    MainForm2() {
        super("Wifi Car");

        initUI();
        initSocket();
        initTimer();
    }

    private void initTimer() {
        timer = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lastSysmbol != null) {
                    socketClient.writeToAndReadFromSocket(lastSysmbol + "\n");
                }
            }
        });

        timer.start();
    }

    private WebcamPanel getWebcamPanel() {
        Webcam.setDriver(new IpCamDriver());

        try {
            IpCamDeviceRegistry.register("Lignano", "http://192.168.1.22:8080/?action=stream", IpCamMode.PUSH);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        WebcamPanel panel = new WebcamPanel(Webcam.getWebcams().get(0));
        panel.setFPSLimit(1);

        panel.setFocusable(true);

        return panel;
    }

    private void addKeyListener(Component component) {
        component.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {

                //System.out.println(e);
                lastSysmbol = send(e);
            }

            public void keyReleased(KeyEvent e) {
                lastSysmbol = null;
            }
        });
    }

    private void initUI() {
        setContentPane(rootPanel);


        WebcamPanel panel = getWebcamPanel();
        addKeyListener(panel);
        rootPanel.add(panel);

/*        JLabel label = new JLabel();

        label.setFocusable(true);
        addKeyListener(label);
        JButton btn = new JButton("Test");*/


        //rootPanel.add(label);
        //rootPanel.add(btn);


        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (socketClient != null) {
                    socketClient.close();
                }
                if (timer != null) {
                    timer.stop();
                }

                super.windowClosing(e);
            }
        });

        setVisible(true);
    }

    private String send(KeyEvent e) {
        boolean isShift = e.isShiftDown();
        String _lastAction = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                _lastAction = !isShift ? "r" : "h";
                break;
            case KeyEvent.VK_LEFT:
                _lastAction = !isShift ? "l" : "g";
                break;
            case KeyEvent.VK_UP:
                _lastAction = isShift ? "y" : "b";
                break;
            case KeyEvent.VK_DOWN:
                _lastAction = isShift ? "t" : "f";
                break;
            case KeyEvent.VK_W:
                _lastAction = "p";
                break;
            case KeyEvent.VK_S:
                _lastAction = "o";
                break;
            case KeyEvent.VK_D:
                _lastAction = "j";
                break;
            case KeyEvent.VK_A:
                _lastAction = "k";
                break;
            case KeyEvent.VK_E:
                _lastAction = "e";
                break;
            case KeyEvent.VK_SPACE:
                _lastAction = "h";
                break;
            case KeyEvent.VK_1:
                _lastAction = "play_1";
                break;
            case KeyEvent.VK_2:
                _lastAction = "play_2";
                break;
            case KeyEvent.VK_3:
                _lastAction = "play_3";
                break;
            case KeyEvent.VK_4:
                _lastAction = "play_4";
                break;
            case KeyEvent.VK_5:
                _lastAction = "play_5";
                //System.out.p
                break;
            case KeyEvent.VK_6:
                _lastAction = "play_6";
                break;
            case KeyEvent.VK_7:
                _lastAction = "play_7";
                break;
            case KeyEvent.VK_0:
                _lastAction = "stop";
                break;
        }

        return _lastAction;
    }

    private void initSocket() {
        try {
            socketClient = new SocketClient("192.168.1.22", 4000);
        } catch (IOException e) {
            System.out.println("I can't open socket");
        }
    }
}
