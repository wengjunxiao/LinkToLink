package lab07;

import java.applet.Applet;
import java.util.Timer;
import java.util.TimerTask;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * 该类用于实现设定游戏界面，创建图片按钮，两个按钮的配对消去，背景音乐以及提示音的设定等操作
 */
public class GameOperation extends JFrame{
	
	/**用户设定游戏中按钮的个数*/
	private final int SIZE = 8;
	/**用来存放SIZE * SIZE个按钮*/
	private JButton[][] buttonJ = new JButton[SIZE][SIZE];
	/**表示配对时第一个按下的按钮*/
	private JButton selectedButton;
	/**表示配对时第一个按下的按钮所在的行号*/
	private int selectedX;
	/**表示配对时第一个按下的按钮所在的列号*/
	private int selectedY;
	/**记录配对事用户已点击按钮的个数*/
	private int kicks = 0;
	/**记录已被消去按钮的个数*/
	private int count = 0;
	/**用来存放背景音乐*/
	private AudioClip music;
	/**用来记录用户进行游戏的时间*/
	private int second = 0;
	/**计时器用来按秒推进时间*/
	private Timer timer;
	/**显示用户所花的时间*/
	private JLabel timeLabel = new JLabel("Time:" + second + " S");
	/**表示时间条*/
	private JProgressBar timerProgressBar;
	
	/**
	 * 构造函数用于设置游戏的面板和初始化每个按钮
	 */
	public GameOperation() {
		JPanel panel1 = new JPanel();//创建一个面板置于窗口上方
		final JPanel panel2 = new JPanel();//创建一个面板置于窗口中央
		Container contentPane = getContentPane();//创建一个内容面板用于存放panel1和panel2
		int number[] = shuffle();//存放(SIZE - 2)*(SIZE - 2)个随机数
		int sum = 0;//用于表示第几个按钮正在被初始化
		int i, j;
		
		//设置时间条大小和容量
		timerProgressBar = new JProgressBar(0,180);
		timerProgressBar.setValue(180);
		timerProgressBar.setForeground(Color.pink);
		Dimension d = timerProgressBar.getPreferredSize();
		d.height = 20;
		d.width = 350;
		timerProgressBar.setPreferredSize(d);
		timerProgressBar.setValue(180);
		
		//添加panel1,panel2到内容面板，开启音乐
		panel1.add(timeLabel);
		panel1.add(timerProgressBar);
		panel2.setLayout(new GridLayout(SIZE - 2, SIZE - 2));//设定面板的排版
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(panel2,BorderLayout.CENTER);
		setLocation(450,300);//设置内容面板在屏幕中的位置
		backgroundMusic();
		
		//设置计时器，根据1000MS递增
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			public void run() {
				timeLabel.setText("Time:" + (second++) + "S");
				timerProgressBar.setValue(timerProgressBar.getValue() - 5);
				if(timerProgressBar.getValue() <= 0) {		
					getResult(false);
					return;
				}
			}
		}, 0, 1000);
		
		//初始化buttonJ按钮，为每个按钮附上图片和添加点击事件，并将按钮添加到面板
		for(i = 0; i < SIZE; i++){
			for(j = 0; j < SIZE; j++){
				if(i == 0 || i == SIZE - 1 || j == 0 || j == SIZE - 1) {
					buttonJ[i][j] = new JButton("" + 0);
					buttonJ[i][j].setVisible(false);
				}
				else {
					buttonJ[i][j] = new JButton("" + number[sum]);
					insertPicture(buttonJ[i][j]);
					final JButton TEMP = buttonJ[i][j];
					final int X = i;
					final int Y = j;
					//为buttonJ[i][j]添加点击时间
					buttonJ[i][j].addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent paramActionEvent) {
				    		kicks++;
				    		if(kicks == 1) {
				    			selectedButton = TEMP;
				    			selectedButton.setBackground(Color.gray);
				    			selectedX = X;
				    			selectedY = Y;
				    		}
				    		if(kicks == 2) {
				    			kicks = 0;
				    			TEMP.setBackground(Color.gray);
				    			removeButton(selectedButton, TEMP, selectedX, selectedY, X, Y);
				    		}
				    	}
				   });
					sum++;
				}
			}
		}
		for(i = 0; i < SIZE * SIZE; i++){
			if(i / SIZE != 0 && i % SIZE != 0 && i / SIZE != SIZE - 1 && i % SIZE != SIZE - 1)
			panel2.add(buttonJ[i / SIZE][i % SIZE]);
		}
		
		//关闭窗口触发的事件
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.cancel();
				music.stop();
				setVisible(false);
				LinkThemAll.isOpen = false;
			}
		});
		
		setTitle("马里奥版连连看");
		pack();
		setVisible(true);
	}
	
	/**
	 * 显示用户游戏失败或者成功的结果
	 * @param flag 判断用户失败或者成功
	 */
	public void getResult(boolean flag){
		if(flag == false) {
			timer.cancel();
			JOptionPane.showMessageDialog(this, "很遗憾，您失败了", "失败", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			timer.cancel();
			JOptionPane.showMessageDialog(this, "恭喜您，闯关成功，您用时" + (second - 1) + "秒！", "成功", JOptionPane.INFORMATION_MESSAGE);
		}	
		setVisible(false);
		music.stop();
		LinkThemAll.isOpen = false;
	}
	
	/**
	 * 对按钮插入图片
	 * @param button 要插入图片的按钮
	 */
	public void insertPicture(JButton button) {
		Icon ic;//存放要插入的图片
		int i;
		
		//根据按钮的text选择按钮对应10张图片中的一张插入
		for(i = 1; i <= 10; i++) {
			if(button.getText().equals("" + i)) {
				ic=new ImageIcon("C:\\学习\\我的程序\\linktolink\\src\\lab07\\photo\\" + i + ".gif");
				button.setIcon(ic);
				//button.setBorder(null);
				button.setText("");
			}
		}
	}
	
	/**
	 * 产生(SIZE - 2) * (SIZE - 2)个1到10的随机数
	 * @return number 储存1到10内随机数的数组
	 */
	public int[] shuffle() {
		int n = SIZE - 2;//要产生随机数为n * n 个
		int number[] = new int[n * n];//存放随机数
		int i, j, k;
		
		//将产生的随机数存放到number数组中
		for(i = 0; i < n * n / 2; i++) {
			number[i] = (int)(Math.random() * 10) % 10 + 1;
			number[n * n / 2 + i] = number[i];
		}
		
		//将number中的数字随机排序
		Random rand = new Random();
		int temp;
		for(k = 0; k <= 1000; k ++) {
			i = rand.nextInt(n * n - 1);
			j = rand.nextInt(n * n - 1);
			temp = number[i];
			number[i] = number[j];
			number[j] = temp;
		}
		return number;
	}
	
	/**
	 * 设置背景音乐
	 */
	public void backgroundMusic() {   
		try {   
			URL cb;   
			File f = new File("C:\\学习\\我的程序\\linktolink\\src\\lab07\\music\\mario.wav");//在固定的位置放好您的音乐文件，注：必须为WAV格式的   
			cb = f.toURL();   
			music = Applet.newAudioClip(cb);   
			music.loop();//循环播放  music.play() 单曲 music.stop()停止播放     
		} catch (MalformedURLException e) {      
		}
	}
	
	/**
	 * 设置当两个按钮匹配时的音乐
	 */
	public void matchMusic() {
		 try {   
			URL cb;
			AudioClip vox;
			File f = new File("C:\\学习\\我的程序\\linktolink\\src\\lab07\\music\\tishi.wav");//在固定的位置放好您的音乐文件，注：必须为WAV格式的   
			cb = f.toURL();   
			vox = Applet.newAudioClip(cb);   
			vox.play();
		} catch (MalformedURLException e) {      
		}
	}
	
	/**
	 * 对两个按钮进行匹配操作，匹配成功则消去
	 * @param button1 第一个选中的按钮
	 * @param button2 第二个选中的按钮
	 * @param x1 第一个按钮所在的行号
	 * @param y1 第一个按钮所在的列号
	 * @param x2 第二个按钮所在的行号
	 * @param y2 第二个按钮所在的列号
	 */
	public void removeButton(JButton button1, JButton button2, int x1, int y1, int x2, int y2) {
		int x[][] = new int[3][3];//用来记录button1,button2向上和向下能到达的最后一个text值为"" + 0的按钮
		int y[][] = new int[3][3];//用来记录button1,button2向左和向右能到达的最后一个text值为"" + 0的按钮
		int i, j;
		
		//初始化x,y的数组元素
		x[1][1] = x1;
		x[1][2] = x1;
		x[2][1] = x2;
		x[2][2] = x2;
		y[1][1] = y1;
		y[1][2] = y1;
		y[2][1] = y2;
		y[2][2] = y2;
		
		//对button1,button2向上和向下进行寻找找到最后一个text为"" + 0的按钮
		if(button1.getIcon().toString().equals(button2.getIcon().toString())&&button1 != button2) {
			//向button1上面寻找
			for(i = x1 - 1; i >= 0; i--) {
				if(buttonJ[i][y1].getText().equals("" + 0))
					x[1][1] = i;
				else 
					break;
			}
			//向button1下面寻找
			for(i = x1 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[i][y1].getText().equals("" + 0))
					x[1][2] = i;
				else 
					break;
			}
			//向button2上面寻找
			for(i = x2 - 1; i >= 0; i--) {	
				if(buttonJ[i][y2].getText().equals("" + 0))
					x[2][1] = i;
				else 
					break;
			}
			//向button2下面寻找
			for(i = x2 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[i][y2].getText().equals("" + 0))
					x[2][2] = i;
				else 
					break;
			}
			
			//得到button1和button2说到到达上下区间的交集
			int a=Math.max(x[1][1], x[2][1]);
			int b=Math.min(x[1][2], x[2][2]);
			int c=Math.min(y1, y2);
			int d=Math.max(y1, y2);
			
			//在交集区间里寻找一条text全为"" + 0的通路，如果找到则匹配成功
			if(a <= b) {
				for(i = a; i < b + 1; i++) {
					for(j = c; j < d + 1; j++) {
						if(j != d && j != c && !buttonJ[i][j].getText().equals("" + 0))
							break;
						else if(j == d && (buttonJ[i][j].getText().equals("" + 0) || (x1 == i && y1 == j) || (x2 == i && y2 == j))) {
							//匹配成功改变button1和button2的属性
							button1.setText("" + 0);
							button2.setText("" + 0);
							button1.setVisible(false);
							button2.setVisible(false);
							matchMusic();
							kicks = 0;
							count += 2;
							//全部按钮消去后弹出提示并关闭窗口
							if(count == (SIZE - 2) * (SIZE - 2)) {
								getResult(true);
							}
						    return;
						}
					}
				}
			}
			
			//对button1,button2向左和向右进行寻找找到最后一个text为"" + 0的按钮
			for(i = y1 - 1; i >= 0; i--) {
				//向button1左边寻找
				if(buttonJ[x1][i].getText().equals("" + 0))
					y[1][1] = i;
				else 
					break;
			}
			//向button1右边寻找
			for(i = y1 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[x1][i].getText().equals("" + 0))
					y[1][2] = i;
				else 
					break;
			}
			//向button2左边寻找
			for(i = y2 - 1; i >= 0; i--) {
				if(buttonJ[x2][i].getText().equals("" + 0))
					y[2][1] = i;
				else 
					break;
			}
			//向button2右边寻找
			for(i = y2 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[x2][i].getText().equals("" + 0))
					y[2][2] = i;
				else 
					break;
			}	
			
			//得到button1和button2说到到达左右区间的交集
			a=Math.max(y[1][1], y[2][1]);
			b=Math.min(y[1][2], y[2][2]);
			c=Math.min(x1, x2);
			d=Math.max(x1, x2);
			
			//在交集区间里寻找一条text全为"" + 0的通路，如果找到则匹配成功
			if(a <= b) {
				for(j = a; j < b + 1; j++) {
					for(i = c; i < d + 1; i++) {
						if(i != d && i != c && !buttonJ[i][j].getText().equals("" + 0))
							break;
						else if(i == d && (buttonJ[i][j].getText().equals("" + 0) || (x1 == i && y1 == j) || (x2 == i && y2 == j))) {
							//匹配成功改变button1和button2的属性
							button1.setText("" + 0);
							button2.setText("" + 0);
							button1.setVisible(false);
							button2.setVisible(false);
							matchMusic();
							kicks = 0;
							count += 2;
							//全部按钮消去后弹出提示并关闭窗口
							if(count == (SIZE - 2) * (SIZE - 2)) {
								getResult(true);
							}
							return;
						}
					}
				}
			}
			
			//如果匹配失败则将匹配事件第一个点击的按钮设为当前第二个点击的按钮
			timerProgressBar.setValue(timerProgressBar.getValue() - 4);
			selectedButton.setBackground(null);
			selectedButton = button2;
			selectedX = x2;
			selectedY = y2;
		    kicks = 1;
		}
		//如果匹配失败则将匹配事件第一个点击的按钮设为当前第二个点击的按钮
		else {
			timerProgressBar.setValue(timerProgressBar.getValue() - 4);
			selectedButton.setBackground(null);
			selectedButton = button2;
			selectedX = x2;
			selectedY = y2;
		    kicks = 1;
		}
	}
	
}
