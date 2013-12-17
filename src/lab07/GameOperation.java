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
 * ��������ʵ���趨��Ϸ���棬����ͼƬ��ť��������ť�������ȥ�����������Լ���ʾ�����趨�Ȳ���
 */
public class GameOperation extends JFrame{
	
	/**�û��趨��Ϸ�а�ť�ĸ���*/
	private final int SIZE = 8;
	/**�������SIZE * SIZE����ť*/
	private JButton[][] buttonJ = new JButton[SIZE][SIZE];
	/**��ʾ���ʱ��һ�����µİ�ť*/
	private JButton selectedButton;
	/**��ʾ���ʱ��һ�����µİ�ť���ڵ��к�*/
	private int selectedX;
	/**��ʾ���ʱ��һ�����µİ�ť���ڵ��к�*/
	private int selectedY;
	/**��¼������û��ѵ����ť�ĸ���*/
	private int kicks = 0;
	/**��¼�ѱ���ȥ��ť�ĸ���*/
	private int count = 0;
	/**������ű�������*/
	private AudioClip music;
	/**������¼�û�������Ϸ��ʱ��*/
	private int second = 0;
	/**��ʱ�����������ƽ�ʱ��*/
	private Timer timer;
	/**��ʾ�û�������ʱ��*/
	private JLabel timeLabel = new JLabel("Time:" + second + " S");
	/**��ʾʱ����*/
	private JProgressBar timerProgressBar;
	
	/**
	 * ���캯������������Ϸ�����ͳ�ʼ��ÿ����ť
	 */
	public GameOperation() {
		JPanel panel1 = new JPanel();//����һ��������ڴ����Ϸ�
		final JPanel panel2 = new JPanel();//����һ��������ڴ�������
		Container contentPane = getContentPane();//����һ������������ڴ��panel1��panel2
		int number[] = shuffle();//���(SIZE - 2)*(SIZE - 2)�������
		int sum = 0;//���ڱ�ʾ�ڼ�����ť���ڱ���ʼ��
		int i, j;
		
		//����ʱ������С������
		timerProgressBar = new JProgressBar(0,180);
		timerProgressBar.setValue(180);
		timerProgressBar.setForeground(Color.pink);
		Dimension d = timerProgressBar.getPreferredSize();
		d.height = 20;
		d.width = 350;
		timerProgressBar.setPreferredSize(d);
		timerProgressBar.setValue(180);
		
		//���panel1,panel2��������壬��������
		panel1.add(timeLabel);
		panel1.add(timerProgressBar);
		panel2.setLayout(new GridLayout(SIZE - 2, SIZE - 2));//�趨�����Ű�
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(panel2,BorderLayout.CENTER);
		setLocation(450,300);//���������������Ļ�е�λ��
		backgroundMusic();
		
		//���ü�ʱ��������1000MS����
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
		
		//��ʼ��buttonJ��ť��Ϊÿ����ť����ͼƬ����ӵ���¼���������ť��ӵ����
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
					//ΪbuttonJ[i][j]��ӵ��ʱ��
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
		
		//�رմ��ڴ������¼�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.cancel();
				music.stop();
				setVisible(false);
				LinkThemAll.isOpen = false;
			}
		});
		
		setTitle("����°�������");
		pack();
		setVisible(true);
	}
	
	/**
	 * ��ʾ�û���Ϸʧ�ܻ��߳ɹ��Ľ��
	 * @param flag �ж��û�ʧ�ܻ��߳ɹ�
	 */
	public void getResult(boolean flag){
		if(flag == false) {
			timer.cancel();
			JOptionPane.showMessageDialog(this, "���ź�����ʧ����", "ʧ��", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			timer.cancel();
			JOptionPane.showMessageDialog(this, "��ϲ�������سɹ�������ʱ" + (second - 1) + "�룡", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
		}	
		setVisible(false);
		music.stop();
		LinkThemAll.isOpen = false;
	}
	
	/**
	 * �԰�ť����ͼƬ
	 * @param button Ҫ����ͼƬ�İ�ť
	 */
	public void insertPicture(JButton button) {
		Icon ic;//���Ҫ�����ͼƬ
		int i;
		
		//���ݰ�ť��textѡ��ť��Ӧ10��ͼƬ�е�һ�Ų���
		for(i = 1; i <= 10; i++) {
			if(button.getText().equals("" + i)) {
				ic=new ImageIcon("C:\\ѧϰ\\�ҵĳ���\\linktolink\\src\\lab07\\photo\\" + i + ".gif");
				button.setIcon(ic);
				//button.setBorder(null);
				button.setText("");
			}
		}
	}
	
	/**
	 * ����(SIZE - 2) * (SIZE - 2)��1��10�������
	 * @return number ����1��10�������������
	 */
	public int[] shuffle() {
		int n = SIZE - 2;//Ҫ���������Ϊn * n ��
		int number[] = new int[n * n];//��������
		int i, j, k;
		
		//���������������ŵ�number������
		for(i = 0; i < n * n / 2; i++) {
			number[i] = (int)(Math.random() * 10) % 10 + 1;
			number[n * n / 2 + i] = number[i];
		}
		
		//��number�е������������
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
	 * ���ñ�������
	 */
	public void backgroundMusic() {   
		try {   
			URL cb;   
			File f = new File("C:\\ѧϰ\\�ҵĳ���\\linktolink\\src\\lab07\\music\\mario.wav");//�ڹ̶���λ�÷ź����������ļ���ע������ΪWAV��ʽ��   
			cb = f.toURL();   
			music = Applet.newAudioClip(cb);   
			music.loop();//ѭ������  music.play() ���� music.stop()ֹͣ����     
		} catch (MalformedURLException e) {      
		}
	}
	
	/**
	 * ���õ�������ťƥ��ʱ������
	 */
	public void matchMusic() {
		 try {   
			URL cb;
			AudioClip vox;
			File f = new File("C:\\ѧϰ\\�ҵĳ���\\linktolink\\src\\lab07\\music\\tishi.wav");//�ڹ̶���λ�÷ź����������ļ���ע������ΪWAV��ʽ��   
			cb = f.toURL();   
			vox = Applet.newAudioClip(cb);   
			vox.play();
		} catch (MalformedURLException e) {      
		}
	}
	
	/**
	 * ��������ť����ƥ�������ƥ��ɹ�����ȥ
	 * @param button1 ��һ��ѡ�еİ�ť
	 * @param button2 �ڶ���ѡ�еİ�ť
	 * @param x1 ��һ����ť���ڵ��к�
	 * @param y1 ��һ����ť���ڵ��к�
	 * @param x2 �ڶ�����ť���ڵ��к�
	 * @param y2 �ڶ�����ť���ڵ��к�
	 */
	public void removeButton(JButton button1, JButton button2, int x1, int y1, int x2, int y2) {
		int x[][] = new int[3][3];//������¼button1,button2���Ϻ������ܵ�������һ��textֵΪ"" + 0�İ�ť
		int y[][] = new int[3][3];//������¼button1,button2����������ܵ�������һ��textֵΪ"" + 0�İ�ť
		int i, j;
		
		//��ʼ��x,y������Ԫ��
		x[1][1] = x1;
		x[1][2] = x1;
		x[2][1] = x2;
		x[2][2] = x2;
		y[1][1] = y1;
		y[1][2] = y1;
		y[2][1] = y2;
		y[2][2] = y2;
		
		//��button1,button2���Ϻ����½���Ѱ���ҵ����һ��textΪ"" + 0�İ�ť
		if(button1.getIcon().toString().equals(button2.getIcon().toString())&&button1 != button2) {
			//��button1����Ѱ��
			for(i = x1 - 1; i >= 0; i--) {
				if(buttonJ[i][y1].getText().equals("" + 0))
					x[1][1] = i;
				else 
					break;
			}
			//��button1����Ѱ��
			for(i = x1 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[i][y1].getText().equals("" + 0))
					x[1][2] = i;
				else 
					break;
			}
			//��button2����Ѱ��
			for(i = x2 - 1; i >= 0; i--) {	
				if(buttonJ[i][y2].getText().equals("" + 0))
					x[2][1] = i;
				else 
					break;
			}
			//��button2����Ѱ��
			for(i = x2 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[i][y2].getText().equals("" + 0))
					x[2][2] = i;
				else 
					break;
			}
			
			//�õ�button1��button2˵��������������Ľ���
			int a=Math.max(x[1][1], x[2][1]);
			int b=Math.min(x[1][2], x[2][2]);
			int c=Math.min(y1, y2);
			int d=Math.max(y1, y2);
			
			//�ڽ���������Ѱ��һ��textȫΪ"" + 0��ͨ·������ҵ���ƥ��ɹ�
			if(a <= b) {
				for(i = a; i < b + 1; i++) {
					for(j = c; j < d + 1; j++) {
						if(j != d && j != c && !buttonJ[i][j].getText().equals("" + 0))
							break;
						else if(j == d && (buttonJ[i][j].getText().equals("" + 0) || (x1 == i && y1 == j) || (x2 == i && y2 == j))) {
							//ƥ��ɹ��ı�button1��button2������
							button1.setText("" + 0);
							button2.setText("" + 0);
							button1.setVisible(false);
							button2.setVisible(false);
							matchMusic();
							kicks = 0;
							count += 2;
							//ȫ����ť��ȥ�󵯳���ʾ���رմ���
							if(count == (SIZE - 2) * (SIZE - 2)) {
								getResult(true);
							}
						    return;
						}
					}
				}
			}
			
			//��button1,button2��������ҽ���Ѱ���ҵ����һ��textΪ"" + 0�İ�ť
			for(i = y1 - 1; i >= 0; i--) {
				//��button1���Ѱ��
				if(buttonJ[x1][i].getText().equals("" + 0))
					y[1][1] = i;
				else 
					break;
			}
			//��button1�ұ�Ѱ��
			for(i = y1 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[x1][i].getText().equals("" + 0))
					y[1][2] = i;
				else 
					break;
			}
			//��button2���Ѱ��
			for(i = y2 - 1; i >= 0; i--) {
				if(buttonJ[x2][i].getText().equals("" + 0))
					y[2][1] = i;
				else 
					break;
			}
			//��button2�ұ�Ѱ��
			for(i = y2 + 1; i <= SIZE - 1; i++) {
				if(buttonJ[x2][i].getText().equals("" + 0))
					y[2][2] = i;
				else 
					break;
			}	
			
			//�õ�button1��button2˵��������������Ľ���
			a=Math.max(y[1][1], y[2][1]);
			b=Math.min(y[1][2], y[2][2]);
			c=Math.min(x1, x2);
			d=Math.max(x1, x2);
			
			//�ڽ���������Ѱ��һ��textȫΪ"" + 0��ͨ·������ҵ���ƥ��ɹ�
			if(a <= b) {
				for(j = a; j < b + 1; j++) {
					for(i = c; i < d + 1; i++) {
						if(i != d && i != c && !buttonJ[i][j].getText().equals("" + 0))
							break;
						else if(i == d && (buttonJ[i][j].getText().equals("" + 0) || (x1 == i && y1 == j) || (x2 == i && y2 == j))) {
							//ƥ��ɹ��ı�button1��button2������
							button1.setText("" + 0);
							button2.setText("" + 0);
							button1.setVisible(false);
							button2.setVisible(false);
							matchMusic();
							kicks = 0;
							count += 2;
							//ȫ����ť��ȥ�󵯳���ʾ���رմ���
							if(count == (SIZE - 2) * (SIZE - 2)) {
								getResult(true);
							}
							return;
						}
					}
				}
			}
			
			//���ƥ��ʧ����ƥ���¼���һ������İ�ť��Ϊ��ǰ�ڶ�������İ�ť
			timerProgressBar.setValue(timerProgressBar.getValue() - 4);
			selectedButton.setBackground(null);
			selectedButton = button2;
			selectedX = x2;
			selectedY = y2;
		    kicks = 1;
		}
		//���ƥ��ʧ����ƥ���¼���һ������İ�ť��Ϊ��ǰ�ڶ�������İ�ť
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
