package com.xgs.hisystem.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XueGuiSheng
 * @date 2020/5/2
 * @description:
 */
@SuppressWarnings("unchecked")
public class IdCardValidUtil {

    private static String reg="^1[0-9]{10}$";


    public static final int CHINA_ID_MIN_LENGTH = 15;
    public static final int CHINA_ID_MAX_LENGTH = 18;
    public static final String[] CITY_CODE = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};
    public static final int[] power = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    public static final String[] verifyCode = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    public static final int MIN = 1930;
    public static Map<String, String> cityCodes = new HashMap<>();
    public static Map<String, Integer> twFirstCode = new HashMap<>();
    public static Map<String, Integer> hkFirstCode = new HashMap<>();


    public static String conver15CardTo18(String idCard) {
        String idCard18 = "";
        if (idCard.length() != 15) {
            return null;
        } else if (isNum(idCard)) {
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;

            try {
                birthDate = (new SimpleDateFormat("yyMMdd")).parse(birthday);
            } catch (ParseException var10) {
                var10.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }

            String sYear = String.valueOf(cal.get(1));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            char[] cArr = idCard18.toCharArray();
            int[] iCard = converCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            String sVal = getCheckCode18(iSum17);
            if (sVal.length() > 0) {
                idCard18 = idCard18 + sVal;
                return idCard18;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean validateIDCardByRegex(String idCard) {
        String curYear = "" + Calendar.getInstance().get(1);
        int y3 = Integer.valueOf(curYear.substring(2, 3));
        int y4 = Integer.valueOf(curYear.substring(3, 4));
        return idCard.matches("^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19\\d{2}|20([0-" + (y3 - 1) + "][0-9]|" + y3 + "[0-" + y4 + "]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])))\\d{3}([0-9]|x|X)$");
    }

    /**
     * ????????????
     * @param idCard
     * @return
     */
    public static boolean validateIdCard(String idCard) {
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        } else if (validateIdCard15(card)) {
            return true;
        } else {
            String[] cardval = validateIdCard10(card);
            return cardval != null && "true".equals(cardval[2]);
        }
    }

    public static boolean validateMainLandIdCard(String idCard) {
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        } else {
            return validateIdCard15(card);
        }
    }

    public static boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == 18) {
            String code17 = idCard.substring(0, 17);
            String code18 = idCard.substring(17, 18);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                int[] iCard = converCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                String val = getCheckCode18(iSum17);
                if (val.length() > 0 && val.equalsIgnoreCase(code18)) {
                    bTrue = true;
                }
            }
        }

        return bTrue && validateIDCardByRegex(idCard) && getProvinceByIdCard(idCard) != null;
    }

    public static boolean validateIdCard15(String idCard) {
        if (idCard.length() != 15) {
            return false;
        } else if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (cityCodes.get(proCode) == null) {
                return false;
            } else {
                String birthCode = idCard.substring(6, 12);
                Date birthDate = null;

                try {
                    birthDate = (new SimpleDateFormat("yy")).parse(birthCode.substring(0, 2));
                } catch (ParseException var5) {
                    var5.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                if (birthDate != null) {
                    cal.setTime(birthDate);
                }

                return valiDate(cal.get(1), Integer.valueOf(birthCode.substring(2, 4)), Integer.valueOf(birthCode.substring(4, 6)));
            }
        } else {
            return false;
        }
    }

    public static String[] validateIdCard10(String idCard) {
        String[] info = new String[3];
        String card = idCard.replaceAll("[\\(|\\)]", "");
        if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
            return null;
        } else {
            if (idCard.matches("^[a-zA-Z][0-9]{9}$")) {
                info[0] = "??????";
                System.out.println("11111");
                String char2 = idCard.substring(1, 2);
                if ("1".equals(char2)) {
                    info[1] = "M";
                    System.out.println("MMMMMMM");
                } else {
                    if (!"2".equals(char2)) {
                        info[1] = "N";
                        info[2] = "false";
                        System.out.println("NNNN");
                        return info;
                    }

                    info[1] = "F";
                    System.out.println("FFFFFFF");
                }

                info[2] = validateTWCard(idCard) ? "true" : "false";
            } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) {
                info[0] = "??????";
                info[1] = "N";
            } else {
                if (!idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) {
                    return null;
                }

                info[0] = "??????";
                info[1] = "N";
                info[2] = validateHKCard(idCard) ? "true" : "false";
            }

            return info;
        }
    }
    public static boolean validateHKCard(String idCard) {
        return true;
    }

    public static boolean validateTWCard(String idCard) {
        String start = idCard.substring(0, 1);
        String mid = idCard.substring(1, 9);
        String end = idCard.substring(9, 10);
        Integer iStart = (Integer)twFirstCode.get(start);
        Integer sum = iStart / 10 + iStart % 10 * 9;
        char[] chars = mid.toCharArray();
        Integer iflag = 8;
        char[] var8 = chars;
        int var9 = chars.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            char c = var8[var10];
            sum = sum + Integer.valueOf(c + "") * iflag;
            iflag = iflag - 1;
        }

        return (sum % 10 == 0 ? 0 : 10 - sum % 10) == Integer.valueOf(end);
    }

    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];

        try {
            for(int i = 0; i < len; ++i) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException var4) {
            var4.printStackTrace();
        }

        return iArr;
    }

    public static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (power.length == iArr.length) {
            for(int i = 0; i < iArr.length; ++i) {
                for(int j = 0; j < power.length; ++j) {
                    if (i == j) {
                        iSum += iArr[i] * power[j];
                    }
                }
            }
        }

        return iSum;
    }

    public static String getCheckCode18(int iSum) {
        String sCode = "";
        switch(iSum % 11) {
            case 0:
                sCode = "1";
                break;
            case 1:
                sCode = "0";
                break;
            case 2:
                sCode = "x";
                break;
            case 3:
                sCode = "9";
                break;
            case 4:
                sCode = "8";
                break;
            case 5:
                sCode = "7";
                break;
            case 6:
                sCode = "6";
                break;
            case 7:
                sCode = "5";
                break;
            case 8:
                sCode = "4";
                break;
            case 9:
                sCode = "3";
                break;
            case 10:
                sCode = "2";
        }

        return sCode;
    }
    // ??????????????????
    public static int getAgeByIdCard(String idCard) {
        try {
            int iAge = 0;
            if (idCard.length() == 15) {
                idCard = conver15CardTo18(idCard);
            }

            if (idCard == null) {
                return 0;
            } else {
                String year = idCard.substring(6, 10);
                Calendar cal = Calendar.getInstance();
                int iCurrYear = cal.get(1);
                iAge = iCurrYear - Integer.valueOf(year);
                return iAge;
            }
        } catch (Exception var5) {
            return 0;
        }
    }
    // ???????????? ????????????????????????????????????19921204
    public static String getBirthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < 15) {
            return null;
        } else {
            if (len == 15) {
                idCard = conver15CardTo18(idCard);
            }

            return idCard == null ? null : idCard.substring(6, 14);
        }
    }
    // ???????????? ?????????xxxx-xx-xx
    public static String getBirthByIdCard2(String idCard) {
        Integer len = idCard.length();
        if (len < 15) {
            return null;
        } else {
            if (len == 15) {
                idCard = conver15CardTo18(idCard);
            }

            return idCard == null ? null : idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        }
    }
    //????????????
    public static Short getYearByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < 15) {
            return null;
        } else {
            if (len == 15) {
                idCard = conver15CardTo18(idCard);
            }

            return idCard == null ? null : Short.valueOf(idCard.substring(6, 10));
        }
    }
    //????????????
    public static Short getMonthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < 15) {
            return null;
        } else {
            if (len == 15) {
                idCard = conver15CardTo18(idCard);
            }

            return idCard == null ? null : Short.valueOf(idCard.substring(10, 12));
        }
    }
    //?????? ???
    public static Short getDateByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < 15) {
            return null;
        } else {
            if (len == 15) {
                idCard = conver15CardTo18(idCard);
            }

            return idCard == null ? null : Short.valueOf(idCard.substring(12, 14));
        }
    }

    public static String getProvinceByIdCard(String idCard) {
        int len = idCard.length();
        String sProvince = null;
        String sProvinNum = "";
        if (len == 15 || len == 18) {
            sProvinNum = idCard.substring(0, 2);
        }

        sProvince = (String)cityCodes.get(sProvinNum);
        return sProvince;
    }

    public static boolean isNum(String val) {
        return val != null && !"".equals(val) && val.matches("^[0-9]*$");
    }

    public static boolean valiDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        if (iYear >= 1930 && iYear < year) {
            if (iMonth >= 1 && iMonth <= 12) {
                int datePerMonth;
                switch(iMonth) {
                    case 2:
                        boolean dm = (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) && iYear > 1930 && iYear < year;
                        datePerMonth = dm ? 29 : 28;
                        break;
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    default:
                        datePerMonth = 31;
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        datePerMonth = 30;
                }

                return iDate >= 1 && iDate <= datePerMonth;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    static {
        cityCodes.put("11", "??????");
        cityCodes.put("12", "??????");
        cityCodes.put("13", "??????");
        cityCodes.put("14", "??????");
        cityCodes.put("15", "?????????");
        cityCodes.put("21", "??????");
        cityCodes.put("22", "??????");
        cityCodes.put("23", "?????????");
        cityCodes.put("31", "??????");
        cityCodes.put("32", "??????");
        cityCodes.put("33", "??????");
        cityCodes.put("34", "??????");
        cityCodes.put("35", "??????");
        cityCodes.put("36", "??????");
        cityCodes.put("37", "??????");
        cityCodes.put("41", "??????");
        cityCodes.put("42", "??????");
        cityCodes.put("43", "??????");
        cityCodes.put("44", "??????");
        cityCodes.put("45", "??????");
        cityCodes.put("46", "??????");
        cityCodes.put("50", "??????");
        cityCodes.put("51", "??????");
        cityCodes.put("52", "??????");
        cityCodes.put("53", "??????");
        cityCodes.put("54", "??????");
        cityCodes.put("61", "??????");
        cityCodes.put("62", "??????");
        cityCodes.put("63", "??????");
        cityCodes.put("64", "??????");
        cityCodes.put("65", "??????");
        cityCodes.put("71", "??????");
        cityCodes.put("81", "??????");
        cityCodes.put("82", "??????");
        cityCodes.put("91", "??????");
        twFirstCode.put("A", 10);
        twFirstCode.put("B", 11);
        twFirstCode.put("C", 12);
        twFirstCode.put("D", 13);
        twFirstCode.put("E", 14);
        twFirstCode.put("F", 15);
        twFirstCode.put("G", 16);
        twFirstCode.put("H", 17);
        twFirstCode.put("J", 18);
        twFirstCode.put("K", 19);
        twFirstCode.put("L", 20);
        twFirstCode.put("M", 21);
        twFirstCode.put("N", 22);
        twFirstCode.put("P", 23);
        twFirstCode.put("Q", 24);
        twFirstCode.put("R", 25);
        twFirstCode.put("S", 26);
        twFirstCode.put("T", 27);
        twFirstCode.put("U", 28);
        twFirstCode.put("V", 29);
        twFirstCode.put("X", 30);
        twFirstCode.put("Y", 31);
        twFirstCode.put("W", 32);
        twFirstCode.put("Z", 33);
        twFirstCode.put("I", 34);
        twFirstCode.put("O", 35);
        hkFirstCode.put("A", 1);
        hkFirstCode.put("B", 2);
        hkFirstCode.put("C", 3);
        hkFirstCode.put("R", 18);
        hkFirstCode.put("U", 21);
        hkFirstCode.put("Z", 26);
        hkFirstCode.put("X", 24);
        hkFirstCode.put("W", 23);
        hkFirstCode.put("O", 15);
        hkFirstCode.put("N", 14);
    }
}
