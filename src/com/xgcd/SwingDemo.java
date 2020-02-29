package com.xgcd;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class SwingDemo {

    static final String ENCRYPT = "加密";
    static final String DECRYPT = "解密";
    static final String ENCRYPTTIPS = "请输入明文:";
    static final String DECRYPTTIPS = "请输入密文:";
    static final String DEFAULTTIPS = "输入框内容不得为空!";
    static final String SUCCESSTITLE = "点击确定拷贝内容到剪切板";

    public static void main(String[] args) {
        new SwingDemo();
    }

    /**
     * 实现加解密业务
     */
    public SwingDemo() {
        /*创建Frame窗体*/
        JFrame mainWin = new JFrame("AES加密&解密工具");
        /*卡片式布局的面板*/
        JPanel cards = new JPanel(new CardLayout());
        /*单选按钮面板*/
        JPanel radioPanel = new JPanel();
        /*文本框面板*/
        JPanel textPanel = new JPanel();
        /*按钮面板*/
        JPanel buttonPanel = new JPanel();
        /*组合面板*/
        JPanel combinationPanel = new JPanel();

        /*将单选按钮和按钮面板添加到组合面板*/
        combinationPanel.setLayout(new BorderLayout());
        combinationPanel.add(radioPanel, BorderLayout.NORTH);
        combinationPanel.add(textPanel, BorderLayout.CENTER);
        combinationPanel.add(buttonPanel, BorderLayout.SOUTH);

        /*将组合面板添加到窗体*/
        mainWin.add(combinationPanel, BorderLayout.SOUTH);

        /*向单选按钮面板中填充两个单选按钮*/
        radioPanel.setBorder(new TitledBorder(new EtchedBorder(), "请选择模式"));// 设置单选按钮的外框标题
        JRadioButton rb1 = new JRadioButton(ENCRYPT, true);//创建JRadioButton对象,默认选中
        JRadioButton rb2 = new JRadioButton(DECRYPT);//创建JRadioButton对象
        radioPanel.add(rb1);
        radioPanel.add(rb2);
        // 将单选按钮添加到组,实现单选效果
        ButtonGroup jRadioGroup = new ButtonGroup();
        jRadioGroup.add(rb1);
        jRadioGroup.add(rb2);

        /*向按钮面板中填充两个按钮*/
        JButton okButton = new JButton("确定");
        JButton clearButton = new JButton("重置");
        buttonPanel.add(okButton);
        buttonPanel.add(clearButton);

        /*向文本框面板中添加输入明文或密文的文本框*/
        JTextField contentTextField = new JTextField(28);
        JLabel contentLabel = new JLabel();
        contentLabel.setText(ENCRYPTTIPS);
        textPanel.add(contentLabel);
        textPanel.add(contentTextField);

        /*给单选按钮添加监听事件*/
        rb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentLabel.setText(ENCRYPTTIPS);// 动态修改标签文本
                clearJTextField(contentTextField);// 清空文本框内容
            }
        });
        rb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentLabel.setText(DECRYPTTIPS);
                clearJTextField(contentTextField);
            }
        });

        /*给按钮添加监听事件*/
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 调用加密或解密逻辑
                String text = contentTextField.getText().trim();// 去掉文本框中字符串前后空格
                if (StringUtils.isEmpty(text)) {
                    // JOptionPane.showMessageDialog(null, DEFAULTTIPS);
                    JOptionPane.showMessageDialog(null, DEFAULTTIPS, "警告", 2);
                    return;
                }

                // 根据单选按钮组的模式执行响应逻辑
                Enumeration<AbstractButton> jRadioGroupElements = jRadioGroup.getElements();
                while (jRadioGroupElements.hasMoreElements()) {
                    AbstractButton button = jRadioGroupElements.nextElement();
                    if (button.isSelected()) {
                        String buttonText = button.getText();// 按钮文本域内容
                        if (buttonText.equals(ENCRYPT)) {
                            try {
                                String cleartext = AESDecoder.aesEncrypt(text);
                                JOptionPane.showMessageDialog(null, cleartext, SUCCESSTITLE, JOptionPane.PLAIN_MESSAGE);
                                setSysClipboardText(cleartext);// 拷贝到剪切板
                            } catch (Exception ex) {
                                String message = ex.getMessage();
                                System.out.println(message);
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "错误 ", 0);
                            }
                        } else if (buttonText.equals(DECRYPT)) {
                            try {
                                String ciphertext = AESDecoder.aesDecrypt(text);
                                JOptionPane.showMessageDialog(null, ciphertext, SUCCESSTITLE, JOptionPane.PLAIN_MESSAGE);
                                setSysClipboardText(ciphertext);
                            } catch (Exception ex) {
                                String message = ex.getMessage();
                                System.out.println(message);
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "错误 ", 0);
                            }
                        }
                        break;
                    }
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearJTextField(contentTextField);
            }
        });

        cards.add(combinationPanel, "card1");//向卡片式布局面板中添加面板1
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "card1");
        mainWin.add(cards);

        /*设置窗体相关属性*/
        mainWin.setVisible(true);// 窗体可见
        mainWin.setResizable(false);// 禁用最大化
        mainWin.setSize(new Dimension(480, 270));// 窗体宽高
        mainWin.setLocationRelativeTo(mainWin.getOwner());// 窗体位置居中当前屏幕
        mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭窗口程序停止
    }

    /**
     * 清空文本框内容
     *
     * @param contentTextField
     */
    private void clearJTextField(JTextField contentTextField) {
        contentTextField.setText("");
    }

    /**
     * 实现将字符串复制到剪切板
     *
     * @param writeMe
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }
}
