package com.ypyproductions.utils;

public class Base64
{

    static boolean $assertionsDisabled = false;
    private static final byte ALPHABET[] = {
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 43, 47
    };
    private static final byte DECODABET[];
    public static final boolean DECODE = false;
    public static final boolean ENCODE = true;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte NEW_LINE = 10;
    private static final byte WEBSAFE_ALPHABET[] = {
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
        75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
        85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
        121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
        56, 57, 45, 95
    };
    private static final byte WEBSAFE_DECODABET[];
    private static final byte WHITE_SPACE_ENC = -5;

    private Base64()
    {
    }

    public static byte[] decode(String s)
        throws Base64DecoderException
    {
        byte abyte0[] = s.getBytes();
        return decode(abyte0, 0, abyte0.length);
    }

    public static byte[] decode(byte abyte0[])
        throws Base64DecoderException
    {
        return decode(abyte0, 0, abyte0.length);
    }

    public static byte[] decode(byte abyte0[], int i, int j)
        throws Base64DecoderException
    {
        return decode(abyte0, i, j, DECODABET);
    }

    public static byte[] decode(byte abyte0[], int i, int j, byte abyte1[])
        throws Base64DecoderException
    {
        byte abyte2[];
        int k;
        byte abyte3[];
        int l;
        int j1;
        int i1;
        byte byte0;
        byte byte1;
        abyte2 = new byte[2 + (j * 3) / 4];
        k = 0;
        abyte3 = new byte[4];
        l = 0;
        i1 = 0;
        while(l < j){
        	byte0 = (byte)(0x7f & abyte0[l + i]);
            byte1 = abyte1[byte0];
            if(byte1 < -5)
            	throw new Base64DecoderException((new StringBuilder("Bad Base64 input character at ")).append(l).append(": ").append(abyte0[l + i]).append("(decimal)").toString());
            if(byte1 < -1){
            	j1 = i1;
            	l++;
                i1 = j1;
                continue;
            }
            if(byte0 != 61)
            {
                j1 = i1 + 1;
                abyte3[i1] = byte0;
                if(j1 == 4)
                {
                    k += decode4to3(abyte3, 0, abyte2, k, abyte1);
                    j1 = 0;
                }
                l++;
                i1 = j1;
                continue;  
            }
            int k1 = j - l;
            byte byte2 = (byte)(0x7f & abyte0[i + (j - 1)]);
            if(i1 == 0 || i1 == 1)
            {
                throw new Base64DecoderException((new StringBuilder("invalid padding byte '=' at byte offset ")).append(l).toString());
            }
            if(i1 == 3 && k1 > 2 || i1 == 4 && k1 > 1)
            {
                throw new Base64DecoderException((new StringBuilder("padding byte '=' falsely signals end of encoded value at offset ")).append(l).toString());
            }
            if(byte2 != 61 && byte2 != 10)
            {
                throw new Base64DecoderException("encoded value has invalid trailing byte");
            }
            break;
        }
        if(i1 != 0){
        	if(i1 == 1)
            {
                throw new Base64DecoderException((new StringBuilder("single trailing character at offset ")).append(j - 1).toString());
            }
            //i1 + 1;
            abyte3[i1] = 61;
            k += decode4to3(abyte3, 0, abyte2, k, abyte1);
        }
        byte abyte4[] = new byte[k];
        System.arraycopy(abyte2, 0, abyte4, 0, k);
        return abyte4;
    }

    private static int decode4to3(byte abyte0[], int i, byte abyte1[], int j, byte abyte2[])
    {
        if(abyte0[i + 2] == 61)
        {
            abyte1[j] = (byte)(((abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12) >>> 16);
            return 1;
        }
        if(abyte0[i + 3] == 61)
        {
            int l = (abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12 | (abyte2[abyte0[i + 2]] << 24) >>> 18;
            abyte1[j] = (byte)(l >>> 16);
            abyte1[j + 1] = (byte)(l >>> 8);
            return 2;
        } else
        {
            int k = (abyte2[abyte0[i]] << 24) >>> 6 | (abyte2[abyte0[i + 1]] << 24) >>> 12 | (abyte2[abyte0[i + 2]] << 24) >>> 18 | (abyte2[abyte0[i + 3]] << 24) >>> 24;
            abyte1[j] = (byte)(k >> 16);
            abyte1[j + 1] = (byte)(k >> 8);
            abyte1[j + 2] = (byte)k;
            return 3;
        }
    }

    public static byte[] decodeWebSafe(String s)
        throws Base64DecoderException
    {
        byte abyte0[] = s.getBytes();
        return decodeWebSafe(abyte0, 0, abyte0.length);
    }

    public static byte[] decodeWebSafe(byte abyte0[])
        throws Base64DecoderException
    {
        return decodeWebSafe(abyte0, 0, abyte0.length);
    }

    public static byte[] decodeWebSafe(byte abyte0[], int i, int j)
        throws Base64DecoderException
    {
        return decode(abyte0, i, j, WEBSAFE_DECODABET);
    }

    public static String encode(byte abyte0[])
    {
        return encode(abyte0, 0, abyte0.length, ALPHABET, true);
    }

    public static String encode(byte abyte0[], int i, int j, byte abyte1[], boolean flag)
    {
        byte abyte2[] = encode(abyte0, i, j, abyte1, 0x7fffffff);
        do
        {
            int k;
            for(k = abyte2.length; flag || k <= 0 || abyte2[k - 1] != 61;)
            {
                return new String(abyte2, 0, k);
            }

            k--;
        } while(true);
    }

    public static byte[] encode(byte abyte0[], int i, int j, byte abyte1[], int k)
    {
        int l = 4 * ((j + 2) / 3);
        byte abyte2[] = new byte[l + l / k];
        int i1 = 0;
        int j1 = 0;
        int k1 = j - 2;
        int l1 = 0;
        int i2;
        do
        {
            if(i1 >= k1)
            {
                if(i1 < j)
                {
                    encode3to4(abyte0, i1 + i, j - i1, abyte2, j1, abyte1);
                    if(l1 + 4 == k)
                    {
                        abyte2[j1 + 4] = 10;
                        j1++;
                    }
                    j1 += 4;
                }
                if(!$assertionsDisabled && j1 != abyte2.length)
                {
                    throw new AssertionError();
                } else
                {
                    return abyte2;
                }
            }
            i2 = (abyte0[i1 + i] << 24) >>> 8 | (abyte0[i + (i1 + 1)] << 24) >>> 16 | (abyte0[i + (i1 + 2)] << 24) >>> 24;
            abyte2[j1] = abyte1[i2 >>> 18];
            abyte2[j1 + 1] = abyte1[0x3f & i2 >>> 12];
            abyte2[j1 + 2] = abyte1[0x3f & i2 >>> 6];
            abyte2[j1 + 3] = abyte1[i2 & 0x3f];
            if((l1 += 4) == k)
            {
                abyte2[j1 + 4] = 10;
                j1++;
                l1 = 0;
            }
            i1 += 3;
            j1 += 4;
        } while(true);
    }

    private static byte[] encode3to4(byte abyte0[], int i, int j, byte abyte1[], int k, byte abyte2[])
    {
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        if(j > 0)
        {
            l = (abyte0[i] << 24) >>> 8;
        } else
        {
            l = 0;
        }
        if(j > 1)
        {
            i1 = (abyte0[i + 1] << 24) >>> 16;
        } else
        {
            i1 = 0;
        }
        j1 = i1 | l;
        k1 = 0;
        if(j > 2)
        {
            k1 = (abyte0[i + 2] << 24) >>> 24;
        }
        l1 = j1 | k1;
        switch(j)
        {
        default:
            return abyte1;

        case 3: // '\003'
            abyte1[k] = abyte2[l1 >>> 18];
            abyte1[k + 1] = abyte2[0x3f & l1 >>> 12];
            abyte1[k + 2] = abyte2[0x3f & l1 >>> 6];
            abyte1[k + 3] = abyte2[l1 & 0x3f];
            return abyte1;

        case 2: // '\002'
            abyte1[k] = abyte2[l1 >>> 18];
            abyte1[k + 1] = abyte2[0x3f & l1 >>> 12];
            abyte1[k + 2] = abyte2[0x3f & l1 >>> 6];
            abyte1[k + 3] = 61;
            return abyte1;

        case 1: // '\001'
            abyte1[k] = abyte2[l1 >>> 18];
            abyte1[k + 1] = abyte2[0x3f & l1 >>> 12];
            abyte1[k + 2] = 61;
            abyte1[k + 3] = 61;
            return abyte1;
        }
    }

    public static String encodeWebSafe(byte abyte0[], boolean flag)
    {
        return encode(abyte0, 0, abyte0.length, WEBSAFE_ALPHABET, flag);
    }

    static 
    {
        boolean flag;
        byte abyte0[];
        byte abyte1[];
        if(!com.ypyproductions.utils.Base64.class.desiredAssertionStatus())
        {
            flag = true;
        } else
        {
            flag = false;
        }
        $assertionsDisabled = flag;
        abyte0 = new byte[128];
        abyte0[0] = -9;
        abyte0[1] = -9;
        abyte0[2] = -9;
        abyte0[3] = -9;
        abyte0[4] = -9;
        abyte0[5] = -9;
        abyte0[6] = -9;
        abyte0[7] = -9;
        abyte0[8] = -9;
        abyte0[9] = -5;
        abyte0[10] = -5;
        abyte0[11] = -9;
        abyte0[12] = -9;
        abyte0[13] = -5;
        abyte0[14] = -9;
        abyte0[15] = -9;
        abyte0[16] = -9;
        abyte0[17] = -9;
        abyte0[18] = -9;
        abyte0[19] = -9;
        abyte0[20] = -9;
        abyte0[21] = -9;
        abyte0[22] = -9;
        abyte0[23] = -9;
        abyte0[24] = -9;
        abyte0[25] = -9;
        abyte0[26] = -9;
        abyte0[27] = -9;
        abyte0[28] = -9;
        abyte0[29] = -9;
        abyte0[30] = -9;
        abyte0[31] = -9;
        abyte0[32] = -5;
        abyte0[33] = -9;
        abyte0[34] = -9;
        abyte0[35] = -9;
        abyte0[36] = -9;
        abyte0[37] = -9;
        abyte0[38] = -9;
        abyte0[39] = -9;
        abyte0[40] = -9;
        abyte0[41] = -9;
        abyte0[42] = -9;
        abyte0[43] = 62;
        abyte0[44] = -9;
        abyte0[45] = -9;
        abyte0[46] = -9;
        abyte0[47] = 63;
        abyte0[48] = 52;
        abyte0[49] = 53;
        abyte0[50] = 54;
        abyte0[51] = 55;
        abyte0[52] = 56;
        abyte0[53] = 57;
        abyte0[54] = 58;
        abyte0[55] = 59;
        abyte0[56] = 60;
        abyte0[57] = 61;
        abyte0[58] = -9;
        abyte0[59] = -9;
        abyte0[60] = -9;
        abyte0[61] = -1;
        abyte0[62] = -9;
        abyte0[63] = -9;
        abyte0[64] = -9;
        abyte0[66] = 1;
        abyte0[67] = 2;
        abyte0[68] = 3;
        abyte0[69] = 4;
        abyte0[70] = 5;
        abyte0[71] = 6;
        abyte0[72] = 7;
        abyte0[73] = 8;
        abyte0[74] = 9;
        abyte0[75] = 10;
        abyte0[76] = 11;
        abyte0[77] = 12;
        abyte0[78] = 13;
        abyte0[79] = 14;
        abyte0[80] = 15;
        abyte0[81] = 16;
        abyte0[82] = 17;
        abyte0[83] = 18;
        abyte0[84] = 19;
        abyte0[85] = 20;
        abyte0[86] = 21;
        abyte0[87] = 22;
        abyte0[88] = 23;
        abyte0[89] = 24;
        abyte0[90] = 25;
        abyte0[91] = -9;
        abyte0[92] = -9;
        abyte0[93] = -9;
        abyte0[94] = -9;
        abyte0[95] = -9;
        abyte0[96] = -9;
        abyte0[97] = 26;
        abyte0[98] = 27;
        abyte0[99] = 28;
        abyte0[100] = 29;
        abyte0[101] = 30;
        abyte0[102] = 31;
        abyte0[103] = 32;
        abyte0[104] = 33;
        abyte0[105] = 34;
        abyte0[106] = 35;
        abyte0[107] = 36;
        abyte0[108] = 37;
        abyte0[109] = 38;
        abyte0[110] = 39;
        abyte0[111] = 40;
        abyte0[112] = 41;
        abyte0[113] = 42;
        abyte0[114] = 43;
        abyte0[115] = 44;
        abyte0[116] = 45;
        abyte0[117] = 46;
        abyte0[118] = 47;
        abyte0[119] = 48;
        abyte0[120] = 49;
        abyte0[121] = 50;
        abyte0[122] = 51;
        abyte0[123] = -9;
        abyte0[124] = -9;
        abyte0[125] = -9;
        abyte0[126] = -9;
        abyte0[127] = -9;
        DECODABET = abyte0;
        abyte1 = new byte[128];
        abyte1[0] = -9;
        abyte1[1] = -9;
        abyte1[2] = -9;
        abyte1[3] = -9;
        abyte1[4] = -9;
        abyte1[5] = -9;
        abyte1[6] = -9;
        abyte1[7] = -9;
        abyte1[8] = -9;
        abyte1[9] = -5;
        abyte1[10] = -5;
        abyte1[11] = -9;
        abyte1[12] = -9;
        abyte1[13] = -5;
        abyte1[14] = -9;
        abyte1[15] = -9;
        abyte1[16] = -9;
        abyte1[17] = -9;
        abyte1[18] = -9;
        abyte1[19] = -9;
        abyte1[20] = -9;
        abyte1[21] = -9;
        abyte1[22] = -9;
        abyte1[23] = -9;
        abyte1[24] = -9;
        abyte1[25] = -9;
        abyte1[26] = -9;
        abyte1[27] = -9;
        abyte1[28] = -9;
        abyte1[29] = -9;
        abyte1[30] = -9;
        abyte1[31] = -9;
        abyte1[32] = -5;
        abyte1[33] = -9;
        abyte1[34] = -9;
        abyte1[35] = -9;
        abyte1[36] = -9;
        abyte1[37] = -9;
        abyte1[38] = -9;
        abyte1[39] = -9;
        abyte1[40] = -9;
        abyte1[41] = -9;
        abyte1[42] = -9;
        abyte1[43] = -9;
        abyte1[44] = -9;
        abyte1[45] = 62;
        abyte1[46] = -9;
        abyte1[47] = -9;
        abyte1[48] = 52;
        abyte1[49] = 53;
        abyte1[50] = 54;
        abyte1[51] = 55;
        abyte1[52] = 56;
        abyte1[53] = 57;
        abyte1[54] = 58;
        abyte1[55] = 59;
        abyte1[56] = 60;
        abyte1[57] = 61;
        abyte1[58] = -9;
        abyte1[59] = -9;
        abyte1[60] = -9;
        abyte1[61] = -1;
        abyte1[62] = -9;
        abyte1[63] = -9;
        abyte1[64] = -9;
        abyte1[66] = 1;
        abyte1[67] = 2;
        abyte1[68] = 3;
        abyte1[69] = 4;
        abyte1[70] = 5;
        abyte1[71] = 6;
        abyte1[72] = 7;
        abyte1[73] = 8;
        abyte1[74] = 9;
        abyte1[75] = 10;
        abyte1[76] = 11;
        abyte1[77] = 12;
        abyte1[78] = 13;
        abyte1[79] = 14;
        abyte1[80] = 15;
        abyte1[81] = 16;
        abyte1[82] = 17;
        abyte1[83] = 18;
        abyte1[84] = 19;
        abyte1[85] = 20;
        abyte1[86] = 21;
        abyte1[87] = 22;
        abyte1[88] = 23;
        abyte1[89] = 24;
        abyte1[90] = 25;
        abyte1[91] = -9;
        abyte1[92] = -9;
        abyte1[93] = -9;
        abyte1[94] = -9;
        abyte1[95] = 63;
        abyte1[96] = -9;
        abyte1[97] = 26;
        abyte1[98] = 27;
        abyte1[99] = 28;
        abyte1[100] = 29;
        abyte1[101] = 30;
        abyte1[102] = 31;
        abyte1[103] = 32;
        abyte1[104] = 33;
        abyte1[105] = 34;
        abyte1[106] = 35;
        abyte1[107] = 36;
        abyte1[108] = 37;
        abyte1[109] = 38;
        abyte1[110] = 39;
        abyte1[111] = 40;
        abyte1[112] = 41;
        abyte1[113] = 42;
        abyte1[114] = 43;
        abyte1[115] = 44;
        abyte1[116] = 45;
        abyte1[117] = 46;
        abyte1[118] = 47;
        abyte1[119] = 48;
        abyte1[120] = 49;
        abyte1[121] = 50;
        abyte1[122] = 51;
        abyte1[123] = -9;
        abyte1[124] = -9;
        abyte1[125] = -9;
        abyte1[126] = -9;
        abyte1[127] = -9;
        WEBSAFE_DECODABET = abyte1;
    }
}
