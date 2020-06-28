import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

interface minesweeper{
    /* 맵 만들기 랜덤 지뢰만들기 눌럿을때 그림바뀌기 등등 */
    public void setScreen();
}

class TitlePanel extends JPanel{
    JPanelChange pc;
    JButton[] btn = new JButton[3];
    public TitlePanel(JPanelChange pc){
        this.pc = pc;
        setLayout(null);
        JLabel lb = new JLabel("두근두근 하트찾기");
        lb.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
        lb.setBounds(180, 120, 300, 90);
        lb.setHorizontalAlignment(SwingConstants.CENTER);
        String path = TitlePanel.class.getResource("").getPath();
        ImageIcon im = new ImageIcon(path+"하트.png");
        JLabel imlb = new JLabel(im);
        imlb.setBounds(0, 0, 700, 500);
        String[] st = {"10 x 10", "15 x 15", "20 x 20"};
        int j=0;
        for(int i=100;i<600;i+=180){
            btn[j] = new JButton(st[j]);
            btn[j].setBounds(i, 550, 100, 30);
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
                pc.change("panel20");
        }
    }
}

class Panel10 extends JPanel implements minesweeper{   /* 각각 지뢰찾기 화면 구성 */
    JPanelChange pc;
    GridBagLayout grid;
    GridBagConstraints gbc = new GridBagConstraints();
    JButton[][] btn = new JButton[10][10];
    String[][] arr = new String[10][10];
    public Panel10(JPanelChange pc){
        grid = new GridBagLayout();
        setLayout(grid);
        this.pc = pc;
        setScreen();
    }
    @Override
    public void setScreen(){
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx=1.0;
        gbc.weighty=1.0;

        JTextField tx = new JTextField(20);
        tx.setText("10");
        tx.setHorizontalAlignment(JLabel.CENTER);
        tx.setEditable(false);
        createHeart();
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                btn[i][j] = new JButton(String.valueOf(arr[i][j]));
                btn[i][j].setBackground(Color.PINK);
                btn[i][j].setForeground(Color.PINK);
                btn[i][j].setPreferredSize(new Dimension(10, 10));
                btn[i][j].addActionListener(new bntActionListener());
                addGrid(btn[i][j], j, i+1, 1);
            }
        }
        addGrid(tx, 0, 0, 10);
    }
    public void createHeart() {
        Random rand = new Random();
        int mine = 10;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++)
                arr[i][j]="0";
        }
        while (mine-- > 0) {
            int row = rand.nextInt(10);
            int col = rand.nextInt(10);
            if (arr[row][col].equals("-1"))
                mine++;
            if (arr[row][col].equals("0"))
                arr[row][col] = "-1";
        }
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                int a = getMine(i, j);
                if(arr[i][j].equals("0")&&a!=0)
                    arr[i][j] = String.valueOf(a);
            }
        }
    }
    public boolean isExist(int row, int col){
        if(row<0||row>=10||col<0||col>=10)
            return false;
        return arr[row][col].equals("-1");
    }
    public int getMine(int row, int col){
        int cnt = 0;
        if(isExist(row-1, col-1)) cnt++;
        if(isExist(row-1, col)) cnt++;
        if(isExist(row-1, col+1)) cnt++;
        if(isExist(row, col-1)) cnt++;
        if(isExist(row, col+1)) cnt++;
        if(isExist(row+1, col-1)) cnt++;
        if(isExist(row+1, col)) cnt++;
        if(isExist(row+1, col+1)) cnt++;

        return cnt;
    }
    private void addGrid(Component c, int x, int y, int w){
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = 1;
        grid.setConstraints(c, gbc);
        add(c);
    }

    public void findAction(int row, int col){

        findAction(row-1, col-1);
        findAction(row-1, col);
        findAction(row-1, col+1);
        findAction(row, col-1);
        findAction(row, col+1);
        findAction(row+1, col-1);
        findAction(row+1, col);
        findAction(row+1, col+1);
    }

    static class bntActionListener implements ActionListener{
        private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
            Image img = icon.getImage();
            Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        }
        public void actionPerformed(ActionEvent e){
            JButton b = (JButton)e.getSource();
            b.setEnabled(false);
            String path = TitlePanel.class.getResource("").getPath();
            if(e.getActionCommand().equals("-1")){
                b.setText("");
                int offset = b.getInsets().left;
                ImageIcon icon = new ImageIcon(path+"지뢰.png");
                b.setIcon(resizeIcon(icon, b.getWidth() - offset, b.getHeight() - offset));
                b.setContentAreaFilled(false);
                b.setBorder(BorderFactory.createEmptyBorder());
                int result = JOptionPane.showConfirmDialog(null, "다시하시겠습니까?", "안내", JOptionPane.YES_NO_OPTION);
                if(result!=0) System.exit(0);
            }
            else if(e.getActionCommand().equals("0")){
                b.setText("");
                //findAction();
            }
            else{
                switch (e.getActionCommand()){
                    case "1": b.setText("");
                        int offset = b.getInsets().left;
                        ImageIcon icon = new ImageIcon(path+"1.png");
                        b.setIcon(resizeIcon(icon, b.getWidth() - offset, b.getHeight() - offset));
                        b.setContentAreaFilled(false);
                        b.setBorder(BorderFactory.createEmptyBorder());
                }
            }
        }
    }

}

class Panel15 extends JPanel implements minesweeper{
    JPanelChange pc;
    GridBagLayout grid;
    GridBagConstraints gbc = new GridBagConstraints();
    JButton[][] btn = new JButton[15][15];
    String[][] arr = new String[15][15];
    public Panel15(JPanelChange pc){
        grid = new GridBagLayout();
        setLayout(grid);
        this.pc = pc;
        setScreen();
    }
    @Override
    public void setScreen(){
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx=1.0;
        gbc.weighty=1.0;

        JTextField tx = new JTextField(20);
        tx.setText("10");
        tx.setHorizontalAlignment(JLabel.CENTER);
        tx.setEditable(false);
        createHeart();
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                btn[i][j] = new JButton(String.valueOf(arr[i][j]));
                btn[i][j].setBackground(Color.PINK);
                btn[i][j].setForeground(Color.PINK);
                btn[i][j].setPreferredSize(new Dimension(10, 10));
                btn[i][j].addActionListener(new Panel10.bntActionListener());
                addGrid(btn[i][j], j, i+1, 1);
            }
        }
        addGrid(tx, 0, 0, 15);
    }
    public void createHeart() {
        Random rand = new Random();
        int mine = 12;
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++)
                arr[i][j]="0";
        }
        while (mine-- > 0) {
            int row = rand.nextInt(15);
            int col = rand.nextInt(15);
            if (arr[row][col].equals("-1"))
                mine++;
            if (arr[row][col].equals("0"))
                arr[row][col] = "-1";
        }
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                int a = getMine(i, j);
                if(arr[i][j].equals("0")&&a!=0)
                    arr[i][j] = String.valueOf(a);
            }
        }
    }
    public boolean isExist(int row, int col){
        if(row<0||row>=10||col<0||col>=10)
            return false;
        return arr[row][col].equals("-1");
    }
    public int getMine(int row, int col){
        int cnt = 0;
        if(isExist(row-1, col-1)) cnt++;
        if(isExist(row-1, col)) cnt++;
        if(isExist(row-1, col+1)) cnt++;
        if(isExist(row, col-1)) cnt++;
        if(isExist(row, col+1)) cnt++;
        if(isExist(row+1, col-1)) cnt++;
        if(isExist(row+1, col)) cnt++;
        if(isExist(row+1, col+1)) cnt++;

        return cnt;
    }
    private void addGrid(Component c, int x, int y, int w){
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = 1;
        grid.setConstraints(c, gbc);
        add(c);
    }
}

class Panel30 extends JPanel implements minesweeper{
    JPanelChange pc;
    GridBagLayout grid;
    GridBagConstraints gbc = new GridBagConstraints();
    JButton[][] btn = new JButton[20][20];
    String[][] arr = new String[20][20];
    public Panel30(JPanelChange pc){
        grid = new GridBagLayout();
        setLayout(grid);
        this.pc = pc;
        setScreen();
    }
    @Override
    public void setScreen(){
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx=1.0;
        gbc.weighty=1.0;

        JTextField tx = new JTextField(20);
        tx.setText("10");
        tx.setHorizontalAlignment(JLabel.CENTER);
        tx.setEditable(false);
        createHeart();
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                btn[i][j] = new JButton(String.valueOf(arr[i][j]));
                btn[i][j].setBackground(Color.PINK);
                btn[i][j].setForeground(Color.PINK);
                btn[i][j].setPreferredSize(new Dimension(10, 10));
                btn[i][j].addActionListener(new Panel10.bntActionListener());
                addGrid(btn[i][j], j, i+1, 1);
            }
        }
        addGrid(tx, 0, 0, 20);
    }
    public void createHeart() {
        Random rand = new Random();
        int mine = 15;
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++)
                arr[i][j]="0";
        }
        while (mine-- > 0) {
            int row = rand.nextInt(20);
            int col = rand.nextInt(20);
            if (arr[row][col].equals("-1"))
                mine++;
            if (arr[row][col].equals("0"))
                arr[row][col] = "-1";
        }
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                int a = getMine(i, j);
                if(arr[i][j].equals("0")&&a!=0)
                    arr[i][j] = String.valueOf(a);
            }
        }
    }
    public boolean isExist(int row, int col){
        if(row<0||row>=10||col<0||col>=10)
            return false;
        return arr[row][col].equals("-1");
    }
    public int getMine(int row, int col){
        int cnt = 0;
        if(isExist(row-1, col-1)) cnt++;
        if(isExist(row-1, col)) cnt++;
        if(isExist(row-1, col+1)) cnt++;
        if(isExist(row, col-1)) cnt++;
        if(isExist(row, col+1)) cnt++;
        if(isExist(row+1, col-1)) cnt++;
        if(isExist(row+1, col)) cnt++;
        if(isExist(row+1, col+1)) cnt++;

        return cnt;
    }
    private void addGrid(Component c, int x, int y, int w){
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = 1;
        grid.setConstraints(c, gbc);
        add(c);
    }
}

class JPanelChange extends JFrame{
    TitlePanel titlePanel = null;
    Panel10 panel10 = null;
    Panel15 panel15 = null;
    Panel30 panel20 = null;

    public void change(String panelName){
        getContentPane().removeAll();
        switch (panelName) {
            case "titlePanel":
                getContentPane().add(titlePanel);break;
            case "panel10":
                getContentPane().add(panel10);break;
            case "panel15":
                getContentPane().add(panel15);break;
            default: getContentPane().add(panel20);break;
        }
        revalidate();
        repaint();
    }
}

public class heartsweeper extends JFrame {
    JPanelChange panelSet = new JPanelChange();
    public heartsweeper(){  // 생성자 (화면 구성)
        panelSet.setTitle("♥ 두근두근 하트찾기 ♥");

        panelSet.titlePanel = new TitlePanel(panelSet);
        panelSet.panel10 = new Panel10(panelSet);
        panelSet.panel15 = new Panel15(panelSet);
        panelSet.panel20 = new Panel30(panelSet);

        createMenu();
        panelSet.add(panelSet.titlePanel);
        panelSet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelSet.setSize(700, 700);
        panelSet.setVisible(true);
    }

    public void createMenu(){   // 메뉴 바 만드는 함수
        JMenuBar mb = new JMenuBar();
        String[] barName = {"Game", "Edit", "Help"};
        String[][] subItem = {{"new game", "10 x 10", "15 x 15", "30 x 30"}, {"실행 취소"}, {"도움말"}};
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
            panelSet.panel10 = new Panel10(panelSet);
            panelSet.panel15 = new Panel15(panelSet);
            panelSet.panel20 = new Panel30(panelSet);
            if(cmd.equals("new game"))
                panelSet.change("titlePanel");
            /* 눌렀을때 다이얼로그로 새로 하겠냐 yes no 띄우는거 */
            else if(cmd.equals("10 x 10"))
                panelSet.change("panel10");
            else if(cmd.equals("15 x 15"))
                panelSet.change("pane15");
            else if(cmd.equals("20 x 20"))
                panelSet.change("pane20");
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