/*
 * Copyright 2008-9 Compulsion
 * <pes_compulsion@yahoo.co.uk>
 * <http://www.purplehaze.eclipse.co.uk/>
 * <http://uk.geocities.com/pes_compulsion/>
 *
 * This file is part of PES Editor.
 *
 * PES Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PES Editor is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PES Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package editor;

import java.util.*;

class CSVAttributes {

    private final int bytesFactor = 16;
    private final int singlePhysicalOptsSettingMinValue = 0;
    private final int singlePhysicalOptsSettingMaxValue = 14;

    private final Map<String, Integer> footFavSideOptsByLabelFootVal = new HashMap<String, Integer>() {
        {
            put("R/R", 0);
            put("R/L", 0);
            put("R/B", 0);
            put("L/L", 1);
            put("L/R", 1);
            put("L/B", 1);
        }
    };

    private final Map<String, Integer> footFavSideOptsByLabelFavSideVal = new HashMap<String, Integer>() {
        {
            put("R/R", 0);
            put("R/L", 1);
            put("R/B", 2);
            put("L/L", 0);
            put("L/R", 1);
            put("L/B", 2);
        }
    };

    private final Map<String, Integer> physicalOptsByLabel = new HashMap<String, Integer>() {
        {
            put("-7", 0);
            put("-6", 1);
            put("-5", 2);
            put("-4", 3);
            put("-3", 4);
            put("-2", 5);
            put("-1", 6);
            put("0", 7);
            put("1", 8);
            put("2", 9);
            put("3", 10);
            put("4", 11);
            put("5", 12);
            put("6", 13);
            put("7", 14);
        }
    };

    private final Map<Integer, String> physicalOptsByValue = new HashMap<Integer, String>() {
        {
            put(0, "-7");
            put(1, "-6");
            put(2, "-5");
            put(3, "-4");
            put(4, "-3");
            put(5, "-2");
            put(6, "-1");
            put(7, "0");
            put(8, "1");
            put(9, "2");
            put(10, "3");
            put(11, "4");
            put(12, "5");
            put(13, "6");
            put(14, "7");
        }
    };

    private final Map<String, Integer> physicalLinkedOptsByLabel = new HashMap<String, Integer>() {
        {
            put("-7/-7", 0);
            put("-6/-7", 1);
            put("-5/-7", 2);
            put("-4/-7", 3);
            put("-3/-7", 4);
            put("-2/-7", 5);
            put("-1/-7", 6);
            put("0/-7", 7);
            put("1/-7", 8);
            put("2/-7", 9);
            put("3/-7", 10);
            put("4/-7", 11);
            put("5/-7", 12);
            put("6/-7", 13);
            put("7/-7", 14);

            put("-7/-6", 16);
            put("-6/-6", 17);
            put("-5/-6", 18);
            put("-4/-6", 19);
            put("-3/-6", 20);
            put("-2/-6", 21);
            put("-1/-6", 22);
            put("0/-6", 23);
            put("1/-6", 24);
            put("2/-6", 25);
            put("3/-6", 26);
            put("4/-6", 27);
            put("5/-6", 28);
            put("6/-6", 29);
            put("7/-6", 30);

            put("-7/-5", 32);
            put("-6/-5", 33);
            put("-5/-5", 34);
            put("-4/-5", 35);
            put("-3/-5", 36);
            put("-2/-5", 37);
            put("-1/-5", 38);
            put("0/-5", 39);
            put("1/-5", 40);
            put("2/-5", 41);
            put("3/-5", 42);
            put("4/-5", 43);
            put("5/-5", 44);
            put("6/-5", 45);
            put("7/-5", 46);

            put("-7/-4", 48);
            put("-6/-4", 49);
            put("-5/-4", 50);
            put("-4/-4", 51);
            put("-3/-4", 52);
            put("-2/-4", 53);
            put("-1/-4", 54);
            put("0/-4", 55);
            put("1/-4", 56);
            put("2/-4", 57);
            put("3/-4", 58);
            put("4/-4", 59);
            put("5/-4", 60);
            put("6/-4", 61);
            put("7/-4", 62);

            put("-7/-3", 64);
            put("-6/-3", 65);
            put("-5/-3", 66);
            put("-4/-3", 67);
            put("-3/-3", 68);
            put("-2/-3", 69);
            put("-1/-3", 70);
            put("0/-3", 71);
            put("1/-3", 72);
            put("2/-3", 73);
            put("3/-3", 74);
            put("4/-3", 75);
            put("5/-3", 76);
            put("6/-3", 77);
            put("7/-3", 78);

            put("-7/-2", 80);
            put("-6/-2", 81);
            put("-5/-2", 82);
            put("-4/-2", 83);
            put("-3/-2", 84);
            put("-2/-2", 85);
            put("-1/-2", 86);
            put("0/-2", 87);
            put("1/-2", 88);
            put("2/-2", 89);
            put("3/-2", 90);
            put("4/-2", 91);
            put("5/-2", 92);
            put("6/-2", 93);
            put("7/-2", 94);

            put("-7/-1", 96);
            put("-6/-1", 97);
            put("-5/-1", 98);
            put("-4/-1", 99);
            put("-3/-1", 100);
            put("-2/-1", 101);
            put("-1/-1", 102);
            put("0/-1", 103);
            put("1/-1", 104);
            put("2/-1", 105);
            put("3/-1", 106);
            put("4/-1", 107);
            put("5/-1", 108);
            put("6/-1", 109);
            put("7/-1", 110);

            put("-7/0", 112);
            put("-6/0", 113);
            put("-5/0", 114);
            put("-4/0", 115);
            put("-3/0", 116);
            put("-2/0", 117);
            put("-1/0", 118);
            put("0/0", 119);
            put("1/0", 120);
            put("2/0", 121);
            put("3/0", 122);
            put("4/0", 123);
            put("5/0", 124);
            put("6/0", 125);
            put("7/0", 126);

            put("-7/1", -128);
            put("-6/1", -127);
            put("-5/1", -126);
            put("-4/1", -125);
            put("-3/1", -124);
            put("-2/1", -123);
            put("-1/1", -122);
            put("0/1", -121);
            put("1/1", -120);
            put("2/1", -119);
            put("3/1", -118);
            put("4/1", -117);
            put("5/1", -116);
            put("6/1", -115);
            put("7/1", -114);

            put("-7/2", -112);
            put("-6/2", -111);
            put("-5/2", -110);
            put("-4/2", -109);
            put("-3/2", -108);
            put("-2/2", -107);
            put("-1/2", -106);
            put("0/2", -105);
            put("1/2", -104);
            put("2/2", -103);
            put("3/2", -102);
            put("4/2", -101);
            put("5/2", -100);
            put("6/2", -99);
            put("7/2", -98);

            put("-7/3", -96);
            put("-6/3", -95);
            put("-5/3", -94);
            put("-4/3", -93);
            put("-3/3", -92);
            put("-2/3", -91);
            put("-1/3", -90);
            put("0/3", -89);
            put("1/3", -88);
            put("2/3", -87);
            put("3/3", -86);
            put("4/3", -85);
            put("5/3", -84);
            put("6/3", -83);
            put("7/3", -82);

            put("-7/4", -80);
            put("-6/4", -79);
            put("-5/4", -78);
            put("-4/4", -77);
            put("-3/4", -76);
            put("-2/4", -75);
            put("-1/4", -74);
            put("0/4", -73);
            put("1/4", -72);
            put("2/4", -71);
            put("3/4", -70);
            put("4/4", -69);
            put("5/4", -68);
            put("6/4", -67);
            put("7/4", -66);

            put("-7/5", -64);
            put("-6/5", -63);
            put("-5/5", -62);
            put("-4/5", -61);
            put("-3/5", -60);
            put("-2/5", -59);
            put("-1/5", -58);
            put("0/5", -57);
            put("1/5", -56);
            put("2/5", -55);
            put("3/5", -54);
            put("4/5", -53);
            put("5/5", -52);
            put("6/5", -51);
            put("7/5", -50);

            put("-7/6", -48);
            put("-6/6", -47);
            put("-5/6", -46);
            put("-4/6", -45);
            put("-3/6", -44);
            put("-2/6", -43);
            put("-1/6", -42);
            put("0/6", -41);
            put("1/6", -40);
            put("2/6", -39);
            put("3/6", -38);
            put("4/6", -37);
            put("5/6", -36);
            put("6/6", -35);
            put("7/6", -34);

            put("-7/7", -32);
            put("-6/7", -31);
            put("-5/7", -30);
            put("-4/7", -29);
            put("-3/7", -28);
            put("-2/7", -27);
            put("-1/7", -26);
            put("0/7", -25);
            put("1/7", -24);
            put("2/7", -23);
            put("3/7", -22);
            put("4/7", -21);
            put("5/7", -20);
            put("6/7", -19);
            put("7/7", -18);
        }
    };

    private final Map<Integer, String> physicalLinkedOptsByValue = new HashMap<Integer, String>() {
        {
            put(0, "-7/-7");
            put(1, "-6/-7");
            put(2, "-5/-7");
            put(3, "-4/-7");
            put(4, "-3/-7");
            put(5, "-2/-7");
            put(6, "-1/-7");
            put(7, "0/-7");
            put(8, "1/-7");
            put(9, "2/-7");
            put(10, "3/-7");
            put(11, "4/-7");
            put(12, "5/-7");
            put(13, "6/-7");
            put(14, "7/-7");

            put(16, "-7/-6");
            put(17, "-6/-6");
            put(18, "-5/-6");
            put(19, "-4/-6");
            put(20, "-3/-6");
            put(21, "-2/-6");
            put(22, "-1/-6");
            put(23, "0/-6");
            put(24, "1/-6");
            put(25, "2/-6");
            put(26, "3/-6");
            put(27, "4/-6");
            put(28, "5/-6");
            put(29, "6/-6");
            put(30, "7/-6");

            put(32, "-7/-5");
            put(33, "-6/-5");
            put(34, "-5/-5");
            put(35, "-4/-5");
            put(36, "-3/-5");
            put(37, "-2/-5");
            put(38, "-1/-5");
            put(39, "0/-5");
            put(40, "1/-5");
            put(41, "2/-5");
            put(42, "3/-5");
            put(43, "4/-5");
            put(44, "5/-5");
            put(45, "6/-5");
            put(46, "7/-5");

            put(48, "-7/-4");
            put(49, "-6/-4");
            put(50, "-5/-4");
            put(51, "-4/-4");
            put(52, "-3/-4");
            put(53, "-2/-4");
            put(54, "-1/-4");
            put(55, "0/-4");
            put(56, "1/-4");
            put(57, "2/-4");
            put(58, "3/-4");
            put(59, "4/-4");
            put(60, "5/-4");
            put(61, "6/-4");
            put(62, "7/-4");

            put(64, "-7/-3");
            put(65, "-6/-3");
            put(66, "-5/-3");
            put(67, "-4/-3");
            put(68, "-3/-3");
            put(69, "-2/-3");
            put(70, "-1/-3");
            put(71, "0/-3");
            put(72, "1/-3");
            put(73, "2/-3");
            put(74, "3/-3");
            put(75, "4/-3");
            put(76, "5/-3");
            put(77, "6/-3");
            put(78, "7/-3");

            put(80, "-7/-2");
            put(81, "-6/-2");
            put(82, "-5/-2");
            put(83, "-4/-2");
            put(84, "-3/-2");
            put(85, "-2/-2");
            put(86, "-1/-2");
            put(87, "0/-2");
            put(88, "1/-2");
            put(89, "2/-2");
            put(90, "3/-2");
            put(91, "4/-2");
            put(92, "5/-2");
            put(93, "6/-2");
            put(94, "7/-2");

            put(96, "-7/-1");
            put(97, "-6/-1");
            put(98, "-5/-1");
            put(99, "-4/-1");
            put(100, "-3/-1");
            put(101, "-2/-1");
            put(102, "-1/-1");
            put(103, "0/-1");
            put(104, "1/-1");
            put(105, "2/-1");
            put(106, "3/-1");
            put(107, "4/-1");
            put(108, "5/-1");
            put(109, "6/-1");
            put(110, "7/-1");

            put(112, "-7/0");
            put(113, "-6/0");
            put(114, "-5/0");
            put(115, "-4/0");
            put(116, "-3/0");
            put(117, "-2/0");
            put(118, "-1/0");
            put(119, "0/0");
            put(120, "1/0");
            put(121, "2/0");
            put(122, "3/0");
            put(123, "4/0");
            put(124, "5/0");
            put(125, "6/0");
            put(126, "7/0");

            put(-128, "-7/1");
            put(-127, "-6/1");
            put(-126, "-5/1");
            put(-125, "-4/1");
            put(-124, "-3/1");
            put(-123, "-2/1");
            put(-122, "-1/1");
            put(-121, "0/1");
            put(-120, "1/1");
            put(-119, "2/1");
            put(-118, "3/1");
            put(-117, "4/1");
            put(-116, "5/1");
            put(-115, "6/1");
            put(-114, "7/1");

            put(-112, "-7/2");
            put(-111, "-6/2");
            put(-110, "-5/2");
            put(-109, "-4/2");
            put(-108, "-3/2");
            put(-107, "-2/2");
            put(-106, "-1/2");
            put(-105, "0/2");
            put(-104, "1/2");
            put(-103, "2/2");
            put(-102, "3/2");
            put(-101, "4/2");
            put(-100, "5/2");
            put(-99, "6/2");
            put(-98, "7/2");

            put(-96, "-7/3");
            put(-95, "-6/3");
            put(-94, "-5/3");
            put(-93, "-4/3");
            put(-92, "-3/3");
            put(-91, "-2/3");
            put(-90, "-1/3");
            put(-89, "0/3");
            put(-88, "1/3");
            put(-87, "2/3");
            put(-86, "3/3");
            put(-85, "4/3");
            put(-84, "5/3");
            put(-83, "6/3");
            put(-82, "7/3");

            put(-80, "-7/4");
            put(-79, "-6/4");
            put(-78, "-5/4");
            put(-77, "-4/4");
            put(-76, "-3/4");
            put(-75, "-2/4");
            put(-74, "-1/4");
            put(-73, "0/4");
            put(-72, "1/4");
            put(-71, "2/4");
            put(-70, "3/4");
            put(-69, "4/4");
            put(-68, "5/4");
            put(-67, "6/4");
            put(-66, "7/4");

            put(-64, "-7/5");
            put(-63, "-6/5");
            put(-62, "-5/5");
            put(-61, "-4/5");
            put(-60, "-3/5");
            put(-59, "-2/5");
            put(-58, "-1/5");
            put(-57, "0/5");
            put(-56, "1/5");
            put(-55, "2/5");
            put(-54, "3/5");
            put(-53, "4/5");
            put(-52, "5/5");
            put(-51, "6/5");
            put(-50, "7/5");

            put(-48, "-7/6");
            put(-47, "-6/6");
            put(-46, "-5/6");
            put(-45, "-4/6");
            put(-44, "-3/6");
            put(-43, "-2/6");
            put(-42, "-1/6");
            put(-41, "0/6");
            put(-40, "1/6");
            put(-39, "2/6");
            put(-38, "3/6");
            put(-37, "4/6");
            put(-36, "5/6");
            put(-35, "6/6");
            put(-34, "7/6");

            put(-32, "-7/7");
            put(-31, "-6/7");
            put(-30, "-5/7");
            put(-29, "-4/7");
            put(-28, "-3/7");
            put(-27, "-2/7");
            put(-26, "-1/7");
            put(-25, "0/7");
            put(-24, "1/7");
            put(-23, "2/7");
            put(-22, "3/7");
            put(-21, "4/7");
            put(-20, "5/7");
            put(-19, "6/7");
            put(-18, "7/7");
        }
    };

    private final int[] wristbandVals = { 0, 8, 16, 24, 40, 48, 56, 72, 80, 88, 104, 112, 120, -120, -112, -104, -88,
            -80, -72, -56, -48, -40, -24, -16, -8 };

    private final String[] wristbandLabels = { "N-None", "L-White", "R-White", "B-White", "L-Black", "R-Black",
            "B-Black", "L-Red", "R-Red", "B-Red", "L-Blue", "R-Blue", "B-Blue", "L-Yellow", "R-Yellow", "B-Yellow",
            "L-Green", "R-Green", "B-Green", "L-Purple", "R-Purple", "B-Purple", "L-Cyan", "R-Cyan", "B-Cyan" };

    private final Map<String, Integer> wristbandOptsByLabel = new HashMap<String, Integer>() {
        {
            put("N-None", 0);
            put("L-White", 8);
            put("R-White", 16);
            put("B-White", 24);
            put("L-Black", 40);
            put("R-Black", 48);
            put("B-Black", 56);
            put("L-Red", 72);
            put("R-Red", 80);
            put("B-Red", 88);
            put("L-Blue", 104);
            put("R-Blue", 112);
            put("B-Blue", 120);
            put("L-Yellow", -120);
            put("R-Yellow", -112);
            put("B-Yellow", -104);
            put("L-Green", -88);
            put("R-Green", -80);
            put("B-Green", -72);
            put("L-Purple", -56);
            put("R-Purple", -48);
            put("B-Purple", -40);
            put("L-Cyan", -24);
            put("R-Cyan", -16);
            put("B-Cyan", -8);
        }
    };

    private final Map<Integer, String> wristbandOptsByValue = new HashMap<Integer, String>() {
        {
            put(0, "N-None");
            put(8, "L-White");
            put(16, "R-White");
            put(24, "B-White");
            put(40, "L-Black");
            put(48, "R-Black");
            put(56, "B-Black");
            put(72, "L-Red");
            put(80, "R-Red");
            put(88, "B-Red");
            put(104, "L-Blue");
            put(112, "R-Blue");
            put(120, "B-Blue");
            put(-120, "L-Yellow");
            put(-112, "R-Yellow");
            put(-104, "B-Yellow");
            put(-88, "L-Green");
            put(-80, "R-Green");
            put(-72, "B-Green");
            put(-56, "L-Purple");
            put(-48, "R-Purple");
            put(-40, "B-Purple");
            put(-24, "L-Cyan");
            put(-16, "R-Cyan");
            put(-8, "B-Cyan");
        }
    };

    private final Map<String, Integer> faceTypesByLabel = new HashMap<String, Integer>() {
        {
            put("Build", 0);
            put("Preset1", 1);
            put("Preset2", 2);
        }
    };

    private final Map<Integer, String> faceTypesByValue = new HashMap<Integer, String>() {
        {
            put(0, "Build");
            put(1, "Preset1");
            put(2, "Preset2");
        }
    };

    /**
     * @return the wristbandVals
     */
    public int[] getWristbandVals() {
        return wristbandVals;
    }

    /**
     * @return the footFavSideOptsByLabelFavSideVal
     */
    public Map<String, Integer> getFootFavSideOptsByLabelFavSideVal() {
        return footFavSideOptsByLabelFavSideVal;
    }

    /**
     * @return the footFavSideOptsByLabelFootVal
     */
    public Map<String, Integer> getFootFavSideOptsByLabelFootVal() {
        return footFavSideOptsByLabelFootVal;
    }

    /**
     * @return the bytesFactor
     */
    public int getBytesFactor() {
        return bytesFactor;
    }

    /**
     * @return the singlePhysicalOptsSettingMaxValue
     */
    public int getSinglePhysicalOptsSettingMaxValue() {
        return singlePhysicalOptsSettingMaxValue;
    }

    /**
     * @return the singlePhysicalOptsSettingMinValue
     */
    public int getSinglePhysicalOptsSettingMinValue() {
        return singlePhysicalOptsSettingMinValue;
    }

    /**
     * @return the physicalLinkedOptsByValue
     */
    public Map<Integer, String> getPhysicalLinkedOptsByValue() {
        return physicalLinkedOptsByValue;
    }

    /**
     * @return the physicalLinkedOptsByLabel
     */
    public Map<String, Integer> getPhysicalLinkedOptsByLabel() {
        return physicalLinkedOptsByLabel;
    }

    /**
     * @return the physicalOptsByValue
     */
    public Map<Integer, String> getPhysicalOptsByValue() {
        return physicalOptsByValue;
    }

    /**
     * @return the physicalOptsByLabel
     */
    public Map<String, Integer> getPhysicalOptsByLabel() {
        return physicalOptsByLabel;
    }

    /**
     * @return the wristbandLabels
     */
    public String[] getWristbandLabels() {
        return wristbandLabels;
    }

    /**
     * @return the wristbandOptsByLabel
     */
    public Map<String, Integer> getWristbandOptsByLabel() {
        return wristbandOptsByLabel;
    }

    /**
     * @return the wristbandOptsByValue
     */
    public Map<Integer, String> getWristbandOptsByValue() {
        return wristbandOptsByValue;
    }

        /**
     * @return the faceTypesByValue
     */
    public Map<Integer, String> getFaceTypesByValue() {
        return faceTypesByValue;
    }

    /**
     * @return the faceTypesByLabel
     */
    public Map<String, Integer> getFaceTypesByLabel() {
        return faceTypesByLabel;
    }
    
}
