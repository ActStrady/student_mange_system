package com.eos.sms.ui;

import com.eos.sms.entity.Admin;
import com.eos.sms.entity.Student;
import com.eos.sms.entity.Teacher;
import com.eos.sms.service.LoginLogoutService;
import com.eos.sms.service.UICommonUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JRadioButton studentRadioButton;
    private JRadioButton teacherRadioButton;
    private JRadioButton adminRadioButton;
    private JButton LoginButton;
    private JPanel LoginPanel;
    private JButton ExitButton;
    private static JFrame frame;

    public Login() {


        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
//
                if (usernameTextField.getText() == null || usernameTextField.getText().length() <= 0) {
                    JOptionPane.showMessageDialog(frame, "请输入用户名", "提示",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (passwordTextField.getText() == null || passwordTextField.getText().length() <= 0) {
                    JOptionPane.showMessageDialog(frame, "请输入密码", "提示",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }


                LoginLogoutService loginLogoutService = new LoginLogoutService();
                String username = usernameTextField.getText().trim();
                String password = passwordTextField.getText().trim();
                Object userinfo = null;

                if (studentRadioButton.isSelected()) {
                    userinfo = new Student(username, password);
                } else if (adminRadioButton.isSelected()) {
                    userinfo = new Admin(username, password);
                } else {
                    userinfo = new Teacher(username, password);
                }
//

                String result = loginLogoutService.login(userinfo);

                if (!result.equals("登陆成功")) {
                    JOptionPane.showMessageDialog(frame, result, "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {

                    if (studentRadioButton.isSelected()) {
                        new HomeStudent();
                    } else if (adminRadioButton.isSelected()) {
                        new HomeAdmin();
                    } else {
                        new HomeTeacher();
                    }

                    frame.dispose();

//

                }
            }
        });
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        frame = new JFrame("学生信息管理系统登陆页面");
        frame.setContentPane(LoginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UICommonUtils.makeFrameToCenter(frame);
        frame.setSize(600, 600);

        frame.setVisible(true);


        usernameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    passwordTextField.requestFocus();

                }
                super.keyPressed(e);
            }
        });
        passwordTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    LoginButton.doClick();

                }
                super.keyPressed(e);
            }
        });
    }
}
