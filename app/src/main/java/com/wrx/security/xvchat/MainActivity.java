package com.wrx.security.xvchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private XMPPTCPConnection connection;
    public static final String SERVER_NAME = "localhost";//主机名
    public static final String SERVER_IP = "192.168.241.128";//ip
    public static final int PORT = 5222;//端口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getConnection();
                boolean connection = isConnected();
                if (connection){
                    Log.e("===","已连接");
                    registerAccount("Scott","123456",null);
                }else {
                    Log.e("===","连接失败");
                }
            }
        }).start();

    }
    /**
     * 获得与服务器的连接
     *
     */
    public XMPPTCPConnection getConnection() {
        try {
            if (connection == null) {
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setHost(SERVER_IP)//服务器IP地址
                        //服务器端口
                        .setPort(PORT)
                        //设置登录状态
                        .setSendPresence(false)
                        //服务器名称
                        .setServiceName(SERVER_NAME)
                        //是否开启安全模式
                        .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                        //是否开启压缩
                        .setCompressionEnabled(false)
                        //开启调试模式
                        .setDebuggerEnabled(true).build();
                connection = new XMPPTCPConnection(config);
                connection.connect();
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    /**
     * 是否连接成功
     *
     * @return
     */
    public boolean isConnected() {
        if (connection == null) {
            return false;
        }
        if (!connection.isConnected()) {
            try {
                connection.connect();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    /**
     * 创建一个新用户
     *
     * @param username 用户名
     * @param password 密码
     * @param attr     一些用户资料
     * @see AccountManager
     */
    public void registerAccount(String username, String password, Map<String, String> attr) {
        getConnection();
        isConnected();
        AccountManager manager = AccountManager.getInstance(connection);
        try {
            if (attr == null) {
                manager.createAccount(username, password);
            } else {
                manager.createAccount(username, password, attr);
            }
        } catch (Exception e) {
            Log.e("===","创建失败");
            e.printStackTrace();
        }
    }
}
