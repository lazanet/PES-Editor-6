package editor;

import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PESUtils
{
  public static final String xps = "xps";
  public static final String psu = "psu";
  public static final String max = "max";
  public static final String png = "png";
  public static final String gif = "gif";
  public static final String csv = "csv";
  public static final String[] extraSquad = { "Classic England", "Classic France", "Classic Germany", "Classic Italy", "Classic Netherlands", "Classic Argentina", "Classic Brazil", "<Japan 1>", "<Edited> National 1", "<Edited> National 2", "<Edited> National 3", "<Edited> National 4", "<Edited> National 5", "<Edited> National 6", "<Edited> National 7", "<Edited>", "<ML Default>", "<Shop 1>", "<Shop 2>", "<Shop 3>", "<Shop 4>", "<Shop 5>" };
  
  public static String getExtension(File paramFile)
  {
    String str1 = null;
    String str2 = paramFile.getName();
    int i = str2.lastIndexOf('.');
    if ((i > 0) && (i < str2.length() - 1)) {
      str1 = str2.substring(i + 1).toLowerCase();
    }
    return str1;
  }
  
  public static int swabInt(int paramInt)
  {
    return paramInt >>> 24 | paramInt << 24 | paramInt << 8 & 0xFF0000 | paramInt >> 8 & 0xFF00;
  }
  
  public static void javaUI()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
    catch (UnsupportedLookAndFeelException localUnsupportedLookAndFeelException)
    {
      localUnsupportedLookAndFeelException.printStackTrace();
    }
  }
  
  public static void systemUI()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
    catch (InstantiationException localInstantiationException)
    {
      localInstantiationException.printStackTrace();
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
    catch (UnsupportedLookAndFeelException localUnsupportedLookAndFeelException)
    {
      localUnsupportedLookAndFeelException.printStackTrace();
    }
  }
}


/* Location:              /home/lazanet/Desktop/pedit/PES Editor 6.0.6.jar!/editor/PESUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */