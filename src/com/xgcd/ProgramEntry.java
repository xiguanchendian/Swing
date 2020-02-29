package com.xgcd;

import java.util.Scanner;

public class ProgramEntry {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("[加密请输入:e 解密请输入:d] [Encrypt, please enter:e Decrypt, please enter:d]");
            String inputStr = sc.next();
            if (inputStr.equals("e")) {
                System.out.println("[请输入明文] [Please enter clear text]:");
                String cleartext = sc.next();
                System.out.println("[加密后结果] [Encrypted result]:");
                System.out.println(AESDecoder.aesEncrypt(cleartext));
            } else if (inputStr.equals("d")) {
                System.out.println("[请输入密文] [Please enter ciphertext]:");
                String ciphertext = sc.next();
                System.out.println("[解密后结果] [Decrypted result]:");
                System.out.println(AESDecoder.aesDecrypt(ciphertext));
            }
            System.out.println();
        }
    }
}
