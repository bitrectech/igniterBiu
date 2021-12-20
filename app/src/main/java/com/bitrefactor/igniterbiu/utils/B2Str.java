package com.bitrefactor.igniterbiu.utils;

public class B2Str {
    public static String parseBytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] parseHexStringToBytes(String hex)
    {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for (int i = 0; i < len; i++)
        {
            int pos = i * 2;
            result[i] = (byte) ((byte)(achar[pos]) << 4 | (byte)(achar[pos + 1]));
        }
        return result;

    }
}
