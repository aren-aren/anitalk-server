package com.anitalk.app.utils;

public class IpFormatter {
    public static String format(String ip){
        if(ip == null) return ip;

        String[] ips = ip.split("\\.");
        String result = ip;
        if(ips.length > 1){
            result = ips[0] + "." + ips[1];
        }
        return result;
    }
}
