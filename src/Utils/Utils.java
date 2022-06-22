package Utils;

import com.mysql.cj.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Date;

public class Utils {
    public static final String KEY_SHA = "SHA";
    public static Timestamp getNowTimestamp(){
        return new Timestamp(new Date().getTime());
    }


    public static String sha(String s)
    {
        BigInteger sha =null;
        byte[] bys = s.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(bys);
            sha = new BigInteger(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha.toString(32);
    }

    public static String getShaPassword(String s, String userid){
        return sha(s+StaticConfig.salt + userid);
    }
}
