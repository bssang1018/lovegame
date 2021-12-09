package com.gudi.best.util;

import java.net.InetAddress;

public class IPUtil {

    public static String getServerIp() {

        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (local == null) {
            return "";
        } else {
            String ip = local.getHostAddress();
            return ip;
        }

    }
}
