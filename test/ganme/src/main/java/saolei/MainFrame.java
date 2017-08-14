package saolei;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainFrame extends JFrame implements Runnable {

	private Diamond[] diamonds = new Diamond[82];// 编号1-81的81个小方块,舍弃第0个

	private boolean isWin = true;// 是否胜利,其实可以不用这个变量
	private boolean isStart;
	private boolean isStop;
	
	private long startTime;
	private long stopTime;
	
	private Label label;// 用来显示剩余的雷数
	private int bombLeft = 10;// 剩余的雷数
	
	// 初始化81个小方块
	private void initDiamond() {
		// 产生1-81编号的小方块
		for(int i=1; i<diamonds.length; i++) {
			diamonds[i] = new Diamond(diamonds);
			diamonds[i].setBackground(Color.gray);
			diamonds[i].setNo(i);
		}
		
		// 设置小方块周围的小方块的关联关系
		for(int i=1; i<diamonds.length; i++) {
			Diamond northWest = diamonds[i].getNearDimaond(1); // 1表示西北方向,2表示北方向,以此类推 
			Diamond north = diamonds[i].getNearDimaond(2);       
			Diamond northEast = diamonds[i].getNearDimaond(3);   
			Diamond east = diamonds[i].getNearDimaond(4);        
			Diamond southEast = diamonds[i].getNearDimaond(5);   
			Diamond south = diamonds[i].getNearDimaond(6);       
			Diamond southWest = diamonds[i].getNearDimaond(7);   
			Diamond west = diamonds[i].getNearDimaond(8);      
			
			diamonds[i].setNorthWest(northWest);
			diamonds[i].setNorth(north);
			diamonds[i].setNorthEast(northEast);
			diamonds[i].setEast(east);
			diamonds[i].setSouthEast(southEast);
			diamonds[i].setSouth(south);
			diamonds[i].setSouthWest(southWest);
			diamonds[i].setWest(west);
		}
		
		// 随机产生10个在1-81范围内的不重复的数字
		Random r = new Random();
		Set<Integer> bombNo = new HashSet<Integer>();
		for(int i=0; i<10; i++) {
			int temp = r.nextInt(81);
			while(temp == 0 || bombNo.contains(temp)) {// 不能为0并且不重复
				temp = r.nextInt(81);
			}
			bombNo.add(temp);
		}
		
		// 根据产生的编号来确定哪些小方块是雷
		for (Integer bombIndex : bombNo) {
			//System.out.println(bombIndex);
			diamonds[bombIndex].setBomb();
		}
	}
	
	// 加载主界面
	private void launchFrame() {
		initDiamond();
		int INIT_GAME_HEIGHT = 390;
		int INIT_GAME_WIDTH = 370;
		this.setSize(INIT_GAME_WIDTH, INIT_GAME_HEIGHT);
		this.setAlwaysOnTop(true);
		this.setLayout(new BorderLayout());
		
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new GridLayout(9, 9));
		Panel statePanel = new Panel();
		//statePanel.setSize(INIT_GAME_WIDTH, 20);
		
		label = new Label("10");// 刚开始有10个雷
		label.setSize(INIT_GAME_WIDTH, 20);
		statePanel.add(label);
		
		for(int i=1; i<diamonds.length; i++) {
			//diamonds[i].addActionListener(new MyActionListener(i));// 为每一个方块增加按键事件,未用,用下面的代替
			diamonds[i].addMouseListener(new MyMouseListener(i));// 为每一个方块增加按键事件
			/*if(diamonds[i].isBomb()) {
				diamonds[i].setBackground(new Color(255, 0, 0));
			}*/
			mainPanel.add(diamonds[i]);
		}
		
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(statePanel, BorderLayout.SOUTH);
		
		
		
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		
	}
	
	/*private boolean isOnePress;
	private boolean isTwoPress;*/
	
	// 没有用这个,放到了mouseClicked方法中
	/*public class MyActionListener implements ActionListener {
		
		private int index;

		public MyActionListener(int index) {
			this.index = index;
		}

		// 单击左键就会触发
		// 翻开小方块,如果是雷的话就输了,不是的话,显示周围的雷数
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(isOnePress && isTwoPress) {
				return;
			}
			//System.out.println("action");
			// 翻开的小方块如果周围没有雷的话,则产生连锁反应,即继续向周围翻
			if(diamonds[index].isBomb() == false && diamonds[index].getNearBombNo() == 0) {
				diamonds[index].change();
				Set<Diamond> set = new HashSet<Diamond>();
				diamonds[index].moveon(set);
				return;
			}
			
			if(diamonds[index].change()) {// 翻开小方块,如果返回true,表示触雷
				isWin = false;
				diamonds[index].setBackground(Color.green);
				for(int i=1; i<diamonds.length; i++) {
					if(diamonds[i].isBomb() && !diamonds[i].isChange()) {
						diamonds[i].setBackground(Color.blue);
					}
				}
				JOptionPane.showMessageDialog(MainFrame.this, "你触雷了", "危险", JOptionPane.ERROR_MESSAGE, null);
				MainFrame.this.dispose();
				new MainFrame().launchFrame();
				return;
			}
			
			
			for(int i=1; i<diamonds.length; i++) {
				
				if(!diamonds[i].isBomb() && diamonds[i].isChange() == false) {// 不是雷,并且没有被翻过,表示肯定还没有胜利
					isWin = false;
					return;
				}
			}
			isWin = true;
			
			if(isWin) {
				//JOptionPane.showMessageDialog(MainFrame.this, "你胜利了", "恭喜你", JOptionPane.PLAIN_MESSAGE, null);
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(MainFrame.this, "你胜利了", "恭喜你", JOptionPane.OK_CANCEL_OPTION)) {
					MainFrame.this.dispose();
					new MainFrame().launchFrame();
				} else {
					System.exit(0);
				}
			}
			
		}
	}*/
	
	private int changedNum = 0;// 用来记录某个小方块周围已经标记为雷的小方块的个数
	public class MyMouseListener implements MouseListener {
		
		private int index;
		private Color color = Color.gray;

		MyMouseListener(int index) {
			this.index = index;
			//this.color = diamonds[1].getBackground();
			//this.color = diamonds[1].getForeground();//获取的是系统的颜色,并不是小方块真正显示的颜色
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//System.out.println("click");
			// 双击左键后松开,则恢复高亮显示的,并且如果周围的雷到标记了,就翻开周围没有翻开的
			if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				
				if(diamonds[index].getNorthWest() != null) {
					diamonds[index].getNorthWest().setBackground(this.color);
					if(diamonds[index].getNorthWest().isBomb() && diamonds[index].getNorthWest().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getNorth() != null) {
					diamonds[index].getNorth().setBackground(color);
					if(diamonds[index].getNorth().isBomb() && diamonds[index].getNorth().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getNorthEast() != null) {
					diamonds[index].getNorthEast().setBackground(color);
					if(diamonds[index].getNorthEast().isBomb() && diamonds[index].getNorthEast().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getEast() != null) {
					diamonds[index].getEast().setBackground(color);
					if(diamonds[index].getEast().isBomb() && diamonds[index].getEast().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getSouthEast() != null) {
					diamonds[index].getSouthEast().setBackground(color);
					if(diamonds[index].getSouthEast().isBomb() && diamonds[index].getSouthEast().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getSouth() != null) {
					diamonds[index].getSouth().setBackground(color);
					if(diamonds[index].getSouth().isBomb() && diamonds[index].getSouth().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getSouthWest() != null) {
					diamonds[index].getSouthWest().setBackground(color);
					if(diamonds[index].getSouthWest().isBomb() && diamonds[index].getSouthWest().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				if(diamonds[index].getWest() != null) {
					diamonds[index].getWest().setBackground(color);
					if(diamonds[index].getWest().isBomb() && diamonds[index].getWest().getLabel().equals("雷")) {
						changedNum++;
					}
				}
				
				if(diamonds[index].getNearBombNo() == changedNum) {// 说明周围的雷已经全部找出来了
					if(diamonds[index].getNorthWest() != null && !diamonds[index].getNorthWest().isChange() && !diamonds[index].getNorthWest().isBomb()) {// 翻开没有翻开的小方块
						diamonds[index].getNorthWest().change();
						if(diamonds[index].getNorthWest().getNearBombNo() == 0) {// 为0的话就产生连锁反应
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getNorthWest().moveon(set);
						}
					}
					if(diamonds[index].getNorth() != null && !diamonds[index].getNorth().isChange() && !diamonds[index].getNorth().isBomb()) {
						diamonds[index].getNorth().change();
						if(diamonds[index].getNorth().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getNorth().moveon(set);
						}	
					}
					if(diamonds[index].getNorthEast() != null && !diamonds[index].getNorthEast().isChange() && !diamonds[index].getNorthEast().isBomb()) {
						diamonds[index].getNorthEast().change();
						if(diamonds[index].getNorthEast().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getNorthEast().moveon(set);
						}
					}
					if(diamonds[index].getEast() != null && !diamonds[index].getEast().isChange() && !diamonds[index].getEast().isBomb()) {
						diamonds[index].getEast().change();
						if(diamonds[index].getEast().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getEast().moveon(set);
						}
					}
					if(diamonds[index].getSouthEast() != null && !diamonds[index].getSouthEast().isChange() && !diamonds[index].getSouthEast().isBomb()) {
						diamonds[index].getSouthEast().change();
						if(diamonds[index].getSouthEast().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getSouthEast().moveon(set);
						}
					}
					if(diamonds[index].getSouth() != null && !diamonds[index].getSouth().isChange() && !diamonds[index].getSouth().isBomb()) {
						diamonds[index].getSouth().change();
						if(diamonds[index].getSouth().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getSouth().moveon(set);
						}
					}
					if(diamonds[index].getSouthWest() != null && !diamonds[index].getSouthWest().isChange() && !diamonds[index].getSouthWest().isBomb()) {
						diamonds[index].getSouthWest().change();
						if(diamonds[index].getSouthWest().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getSouthWest().moveon(set);
						}
					}
					if(diamonds[index].getWest() != null && !diamonds[index].getWest().isChange() && !diamonds[index].getWest().isBomb()) {
						diamonds[index].getWest().change();
						if(diamonds[index].getWest().getNearBombNo() == 0) {
							Set<Diamond> set = new HashSet<Diamond>();
							diamonds[index].getWest().moveon(set);
						}
					}
					
					
				}
				
				changedNum = 0;
				
				return;
			}
			
			if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				// 翻开的小方块如果周围没有雷的话,则产生连锁反应,即继续向周围翻
				if(!diamonds[index].isBomb() && diamonds[index].getNearBombNo() == 0) {
					diamonds[index].change();
					Set<Diamond> set = new HashSet<Diamond>();
					diamonds[index].moveon(set);
					return;
				}
				
				if(diamonds[index].change()) {// 翻开小方块,如果返回true,表示触雷
					isWin = false;
					isStop = true;// 计时停止
					stopTime = System.currentTimeMillis();
					diamonds[index].setBackground(Color.green);
					for(int i=1; i<diamonds.length; i++) {
						if(diamonds[i].isBomb() && !diamonds[i].isChange()) {
							diamonds[i].setBackground(Color.blue);
						}
					}
					JOptionPane.showMessageDialog(MainFrame.this, "你触雷了,用时:" + (stopTime - startTime)/1000 + "s", "危险", JOptionPane.ERROR_MESSAGE, null);
					MainFrame.this.dispose();
					new MainFrame().launchFrame();
				}
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		
		@Override
		public void mousePressed(MouseEvent e) {
			
			// 左键双击的话,则把周围的小方块高亮显示,模仿windows下的扫雷,只不过windows下的是同时单击左键和右键
			if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				if(diamonds[index].getNorthWest() != null) diamonds[index].getNorthWest().setBackground(Color.black);
				if(diamonds[index].getNorth() != null) diamonds[index].getNorth().setBackground(Color.black);
				if(diamonds[index].getNorthEast() != null) diamonds[index].getNorthEast().setBackground(Color.black);
				if(diamonds[index].getEast() != null) diamonds[index].getEast().setBackground(Color.black);
				if(diamonds[index].getSouthEast() != null) diamonds[index].getSouthEast().setBackground(Color.black);
				if(diamonds[index].getSouth() != null) diamonds[index].getSouth().setBackground(Color.black);
				if(diamonds[index].getSouthWest() != null) diamonds[index].getSouthWest().setBackground(Color.black);
				if(diamonds[index].getWest() != null) diamonds[index].getWest().setBackground(Color.black);
			}
			
			//System.out.println("press");
			/*if(e.getButton() == MouseEvent.BUTTON1) {
				isOnePress = true;
			}
			
			if(e.getButton() == MouseEvent.BUTTON3) {
				isTwoPress = true;
			}
			
			if(isOnePress && isTwoPress) {
				//System.out.println("left right");
				if(diamonds[index].getNorthWest() != null) diamonds[index].getNorthWest().setBackground(Color.black);
				if(diamonds[index].getNorth() != null) diamonds[index].getNorth().setBackground(Color.black);
				if(diamonds[index].getNorthEast() != null) diamonds[index].getNorthEast().setBackground(Color.black);
				if(diamonds[index].getEast() != null) diamonds[index].getEast().setBackground(Color.black);
				if(diamonds[index].getSouthEast() != null) diamonds[index].getSouthEast().setBackground(Color.black);
				if(diamonds[index].getSouth() != null) diamonds[index].getSouth().setBackground(Color.black);
				if(diamonds[index].getSouthWest() != null) diamonds[index].getSouthWest().setBackground(Color.black);
				if(diamonds[index].getWest() != null) diamonds[index].getWest().setBackground(Color.black);
			}*/
		}

		// 既然是扫雷么,那么得必须找到所有的雷才算赢
		@Override
		public void mouseReleased(MouseEvent e) {
			//System.out.println("release");
			/*if(isOnePress && isTwoPress) {
				if(diamonds[index].getNorthWest() != null) diamonds[index].getNorthWest().setBackground(this.color);
				if(diamonds[index].getNorth() != null) diamonds[index].getNorth().setBackground(color);
				if(diamonds[index].getNorthEast() != null) diamonds[index].getNorthEast().setBackground(color);
				if(diamonds[index].getEast() != null) diamonds[index].getEast().setBackground(color);
				if(diamonds[index].getSouthEast() != null) diamonds[index].getSouthEast().setBackground(color);
				if(diamonds[index].getSouth() != null) diamonds[index].getSouth().setBackground(color);
				if(diamonds[index].getSouthWest() != null) diamonds[index].getSouthWest().setBackground(color);
				if(diamonds[index].getWest() != null) diamonds[index].getWest().setBackground(color);
				isOnePress = false;
				isTwoPress = false;
				return;
			}*/
			
			if(!isStart) {
				isStart = true;// 表示按键了,计时开始
				startTime = System.currentTimeMillis();
				new Thread(MainFrame.this).start();
			}
			
			if(e.isPopupTrigger()) {// 右击的话依次显示雷,?,空
				if(!diamonds[index].isChange()) {// 必须是没有翻过的才能进行右击
					//System.out.println("popup");
					Diamond d = (Diamond) e.getSource();
					//System.out.println(d.getLabel() + "---");JButton
					/*if(!d.getText().equals("雷") && !d.getText().equals("?")) {
						d.setText("雷");
					} else if(d.getText().equals("雷")) {
						d.setText("?");
					} else {
						d.setText("");
					}*/
					
					d.requestFocus();
					if(!d.getLabel().equals("雷") && !d.getLabel().equals("?")) {
						d.setLabel("雷");
						bombLeft --;
					} else if(d.getLabel().equals("雷")) {
						d.setLabel("?");
						bombLeft ++;
					} else {
						d.setLabel("");
						//bombLeft ++;
					}
					label.setText(bombLeft + "");
				}
				
				// 判断是否胜利
				// 如果是雷,但是没有标记则不赢,或者不是雷竟然标记成了雷或者?则不赢
				for(int i=1; i<diamonds.length; i++) {
					/*if((diamonds[i].isBomb() && !diamonds[i].getText().equals("雷")) 
							|| (!diamonds[i].isBomb() && (diamonds[i].getText().equals("雷")) || diamonds[i].getText().equals("?"))) {
						isWin = false;
						return;
					}*/
					if((diamonds[i].isBomb() && !diamonds[i].getLabel().equals("雷")) 
							|| (!diamonds[i].isBomb() && (diamonds[i].getLabel().equals("雷")) || diamonds[i].getLabel().equals("?"))) {
						isWin = false;
						return;
					}
				}
				
				isWin = true;// 胜利的情况
				//JOptionPane.showMessageDialog(MainFrame.this, "你胜利了", "恭喜你", JOptionPane.PLAIN_MESSAGE, null);
				isStop = true;// 计时停止
				stopTime = System.currentTimeMillis();
				for(int i=1; i<diamonds.length; i++) {// 如果还有不是雷的方块并且还没有翻过来的,则翻过来
                    if(!diamonds[i].isBomb() && !diamonds[i].isChange()) {
                        diamonds[i].change();
                    }
                }
				// 点击确定的话,继续,否则直接退出
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(MainFrame.this, "你胜利了,用时:" + (stopTime - startTime)/1000 + "s", "恭喜你", JOptionPane.OK_CANCEL_OPTION)) {
                    MainFrame.this.dispose();
                    new MainFrame().launchFrame();
                } else {
                    System.exit(0);
                }
			}
			
			/*isOnePress = false;
			isTwoPress = false;*/
			
			/*if(e.getButton() == MouseEvent.BUTTON1) {
				//System.out.println("button1");//左键
				isOnePress = false;
			}
			
			
			if(e.getButton() == MouseEvent.BUTTON2) {
				System.out.println("button2");中键
			}
			
			if(e.getButton() == MouseEvent.BUTTON3) {
				//System.out.println("button3");//右键
				isTwoPress = false;
			}*/
			
		}
		
	}
	

	@Override
	public void run() {
		
		int time = 0;
		
		while(isStart && !isStop) {
			//System.out.println(time);
			this.setTitle(time + "");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time ++;
		}
	}
	
	// main方法
	public static void main(String[] args) {
		new MainFrame().launchFrame();
	}

}
