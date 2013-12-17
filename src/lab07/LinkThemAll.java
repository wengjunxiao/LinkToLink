package lab07;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.*;

import java.awt.event.*;
import java.applet.AudioClip;   
import java.io.*;   
import java.applet.Applet;   
import java.awt.Frame;   
import java.net.MalformedURLException;   
import java.net.URL; 

/**
 * 该类用于存放主类，实现连连看游戏开始和关闭的操作
 */
public class LinkThemAll extends JFrame {
	
	/**用于判断游戏是否开始*/
	public static boolean isOpen = false;
	
	/**
	 * 构造函数用于实现游戏开始和关闭的操作
	 */
	public LinkThemAll() {
		Container contentPane = getContentPane();//新建个内容面板用来存放按钮
		setSize(250,150);//设定面板大小
		setLocation(500,200);//设定面板在屏幕上的位置
		contentPane.setLayout(new GridLayout(3, 1));//设定面板的排版

		final JButton buttonJ1 = new JButton("游戏开始");//开始游戏的按钮
		final JButton buttonJ2 = new JButton("关闭");//关闭的按钮
		JLabel myLabel = new JLabel("         版权所有：翁骏晓");
		
		//为游戏开始按钮添加点击事件
		buttonJ1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent paramActionEvent) {
	    		if (isOpen == false) {
	    			isOpen = true;
	    			GameOperation gameOperation = new GameOperation();
	    		}
	    			
	    	}
		});
		
		//为关闭按钮添加点击事件
		buttonJ2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				System.exit(0);
	    	}
		});
		
		//将buttonJ1和buttonJ2以及myLable添加到内容面板中
		contentPane.add(buttonJ1);
		contentPane.add(buttonJ2);
		contentPane.add(myLabel);
		
		setTitle("马里奥版连连看");
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new LinkThemAll();
	}

}
