package com.chakak.util;


/**
 * 주소에서 시/구/동 단위 추출
 * ex: "인천 남동구 간석동 414-4" → "인천 남동구 간석동"
 */
public class AddressUtils {
	public static String extractRegion(String address) {
        if (address == null || address.isBlank()) return null;
        String[] parts = address.trim().split(" ");
        if (parts.length >= 3) {
            return parts[0] + " " + parts[1] + " " + parts[2];
        } else if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        } else {
            return address;
        }
    }
}
