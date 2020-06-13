import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

interface minesweeper{
    /* 맵 만들기 랜덤 지뢰만들기 눌럿을때 그림바뀌기 등등 */
}

class TitlePanel extends JPanel{
    JPanelChange pc;
    JButton[] btn = new JButton[3];
    public TitlePanel(JPanelChange pc){
        this.pc = pc;
        setLayout(null);
        JLabel lb = new JLabel("두근두근 하트찾기");
        lb.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
        lb.setBounds(90, 20, 300, 90);
        lb.setHorizontalAlignment(SwingConstants.CENTER);
        String path = TitlePanel.class.getResource("").getPath();
        ImageIcon im = new ImageIcon(path+"하트.png");
        JLabel imlb = new JLabel(im);
        imlb.setBounds(0, 0, 500, 300);
        String[] st = {"10 x 10", "15 x 15", "30 x 30"};
        int j=0;
        for(int i=40;i<400;i+=150){
            btn[j] = new JButton(st[j]);
            btn[j].setBounds(i, 330, 100, 30);
            btn[j].addActionListener(new MyActionListener());
            add(btn[j++]);
        }
        add(lb);
        add(imlb);
    }
    class MyActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JButton b = (JButton)e.getSource();
            if(b.getText().equals("10 x 10"))
                pc.change("panel10");
            else if(b.getText().equals("15 x 15"))
                pc.change("panel15");
            else
                pc.change("panel30");
        }
    }
}

class Panel10 extends JPanel{   // 각각 지뢰찾기 화면 구성
    JPanelChange pc;
    public Panel10(JPanelChange pc){
        setLayout(null);
        this.pc = pc;
    }

}

class Panel15 extends JPanel{
    JPanelChange pc;
    public Panel15(JPanelChange pc){
        setLayout(null);
        this.pc = pc;
    }
}

class Panel30 extends JPanel{
    JPanelChange pc;
    public Panel30(JPanelChange pc){
        setLayout(null);
        this.pc = pc;
    }
}

class JPanelChange extends JFrame{
    TitlePanel titlePanel = null;
    Panel10 panel10 = null;
    Panel15 panel15 = null;
    Panel30 panel30 = null;

    public void change(String panelName){
        getContentPane().removeAll();
        switch (panelName) {
            case "titlePanel": getContentPane().add(titlePanel);break;
            case "panel10": getContentPane().add(panel10);break;
            case "panel15": getContentPane().add(panel15);break;
            default: getContentPane().add(panel30);break;
        }
        revalidate();
        repaint();
    }
}

public class heartsweeper extends JFrame {
    JPanelChange panelSet = new JPanelChange();
//    UndoManager undoMan = new UndoManager();
//    JTextPane textcomp = new JTextPane();
//    Document doc = textcomp.getDocument();
    /* 실행취소 기능 */
    public heartsweeper(){  // 생성자 (화면 구성)
        panelSet.setTitle("♥ 두근두근 하트찾기 ♥");

        panelSet.titlePanel = new TitlePanel(panelSet);
        panelSet.panel10 = new Panel10(panelSet);
        panelSet.panel15 = new Panel15(panelSet);
        panelSet.panel30 = new Panel30(panelSet);

        createMenu();
        panelSet.add(panelSet.titlePanel);
        panelSet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelSet.setSize(500, 500);
        panelSet.setVisible(true);
    }

    public void createMenu(){   // 메뉴 바 만드는 함수
        JMenuBar mb = new JMenuBar();
        String[] barName = {"Game", "Edit", "Help"};
        String[][] subItem = {{"new game", "10 x 10", "25 x 25", "50 x 50"}, {"실행 취소"}, {"도움말"}};
        JMenu[] menu = new JMenu[3];
        JMenuItem[][] menuItems = new JMenuItem[3][];
        for(int i=0;i<barName.length;i++){
            menu[i] = new JMenu(barName[i]);
            menu[i].setFont(new Font("맑은 고딕", Font.ITALIC, 13));
            menuItems[i] = new JMenuItem[subItem[i].length];
            for (int j=0;j<subItem[i].length;j++) {
                menuItems[i][j] = new JMenuItem(subItem[i][j]);
                menuItems[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                menuItems[i][j].addActionListener(new MenuActionListener());
                menuItems[i][j].setFont(new Font("맑은 고딕", Font.ITALIC, 13));
                menu[i].add(menuItems[i][j]);
                if(i==0 && j==0) menu[i].addSeparator();
            }
            mb.add(menu[i]);
        }
        panelSet.setJMenuBar(mb);
    }

    class MenuActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String cmd = e.getActionCommand();
            if(cmd.equals("new game"))
                panelSet.change("titlePanel");
            /* 눌렀을때 다이얼로그로 새로 하겠냐 yes no 띄우는거 */
            else if(cmd.equals("10 x 10"))
                panelSet.change("panel10");
            else if(cmd.equals("15 x 15"))
                panelSet.change("pane15");
            else if(cmd.equals("30 x 30"))
                panelSet.change("pane30");
            else if(cmd.equals("실행 취소")){

            }
            else{

            }
        }
    }

    public static void main(String[] args) {
        new heartsweeper();
    }
}