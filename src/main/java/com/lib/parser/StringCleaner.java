package com.lib.parser;

import java.util.Arrays;
import java.util.List;

public class StringCleaner {

    private final static List<String> dontCapitialiseWords = Arrays.asList(
            "ETF", "S&P", "FTSE", "UCITS", "UK" //we know to exclude
            //country code
            ,"AF","AX","AL","DZ","AS","AD","AO","AI","AQ","AG","AR","AM","AW","AU","AT","AZ","BS","BH","BD","BB","BY"
            ,"BE","BZ","BJ","BM","BT","BO","BQ","BA","BW","BV","BR","IO","BN","BG","BF","BI","CV","KH","CM",
            "CA","KY","CF","TD","CL","CN","CX","CC","CO","KM","CD","CG","CK","CR","CI","HR","CU","CW","CY",
            "CZ","DK","DJ","DM","DO","EC","EG","SV","GQ","ER","EE","SZ","ET","FK","FO","FJ","FI","FR","GF",
            "PF","TF","GA","GM","GE","DE","GH","GI","GR","GL","GD","GP","GU","GT","GG","GN","GW","GY","HK","HT",
            "HM","VA","HN","HK","HU","IS"/*,"IN"*/,"ID","IR","IQ","IE","IM","IL","IT","JM","JP","JE","JO","KZ",
            "KE","KI","KP","KR","KW","KG","LA","LV","LB","LS","LR","LY","LI","LT","LU","MO","MK","MG","MW",
            "MY","MV","ML","MT","MH","MQ","MR","MU","YT","MX","FM","MD","MC","MN","ME","MS","MA","MZ","MM",
            "NA","NR","NP","NL","NC","NZ","NI","NE","NG","NU","NF","MP","NO","OM","PK","PW","PS","PA","PG",
            "PY","PE","PH","PN","PL","PT","PR","QA","RE","RO","RU","RW","BL","SH","KN","LC","MF","PM","VC",
            "WS","SM","ST","SA","SN","RS","SC","SL","SG","SX","SK","SI","SB","SO","ZA","GS","SS","ES","LK",
            "SD","SR","SJ","SE","CH","SY","TW","TJ","TZ","TH","TL","TG","TK","TO","TT","TN","TR","TM","TC",
            "TV","UG","UA","AE","GB","UM","US","UY","UZ","VU","VE","VN","VG","VI","WF","EH","YE","ZM","ZW"
            // company specific words
            , "AB", "NFJ", "EF", "ASI", "VP", "AQR", "CLA", "FU", "FD", "BMO", "NT", "MSCI", "BBH", "MSCI", "HSBC",
            "UBS", "MSCI"
    );

    // Capitalise name with pre-conditions
    public static String capitaliseName(String givenString){
        String[] arr = givenString.split("\\s+");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(capitaliseWord(arr[i])).append(" ");
        }
        return sb.toString().trim();
    }
    private static String capitaliseWord(String word){
        if(dontCapitialiseWords.contains(word.toUpperCase()))
            return word;
        else if(word.length() >= 2)
            return capitalise(word);
        else // 1 letter word don't need capitalising
            return word;
    }

    private static List<Character> needCap = Arrays.asList(' ', ';', ':', '!', '?', ',', '.', '_', '-', '/', '&', '\'' , '(');
    public static String capitalise(String word){
        StringBuilder sb = new StringBuilder();

        Character previousChar = needCap.get(0);
        for (int i=0; i < word.length(); i++){
            if(needCap.contains(previousChar))
                sb.append(Character.toUpperCase(word.charAt(i)));
            else
                sb.append(Character.toLowerCase(word.charAt(i)));
            previousChar = word.charAt(i);
        }
        return sb.toString();
    }
}
