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
 * �������ڴ�����࣬ʵ����������Ϸ��ʼ�͹رյĲ���
 */
public class LinkThemAll extends JFrame {
	
	/**�����ж���Ϸ�Ƿ�ʼ*/
	public static boolean isOpen = false;
	
	/**
	 * ���캯������ʵ����Ϸ��ʼ�͹رյĲ���
	 */
	public LinkThemAll() {
		Container contentPane = getContentPane();//�½����������������Ű�ť
		setSize(250,150);//�趨����С
		setLocation(500,200);//�趨�������Ļ�ϵ�λ��
		contentPane.setLayout(new GridLayout(3, 1));//�趨�����Ű�

		final JButton buttonJ1 = new JButton("��Ϸ��ʼ");//��ʼ��Ϸ�İ�ť
		final JButton buttonJ2 = new JButton("�ر�");//�رյİ�ť
		JLabel myLabel = new JLabel("         ��Ȩ���У��̿���");
		
		//Ϊ��Ϸ��ʼ��ť��ӵ���¼�
		buttonJ1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent paramActionEvent) {
	    		if (isOpen == false) {
	    			isOpen = true;
	    			GameOperation gameOperation = new GameOperation();
	    		}
	    			
	    	}
		});
		
		//Ϊ�رհ�ť��ӵ���¼�
		buttonJ2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				System.exit(0);
	    	}
		});
		
		//��buttonJ1��buttonJ2�Լ�myLable��ӵ����������
		contentPane.add(buttonJ1);
		contentPane.add(buttonJ2);
		contentPane.add(myLabel);
		
		setTitle("����°�������");
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new LinkThemAll();
	}

}
