package editor;
import java.io.*;

public class Main
{ 
	static OptionFile of = new OptionFile();
	
	public static void main(String [] args)
	{
		of.readXPS(new File("KONAMI-WIN32PES6OPT"));
		//dumpDecoded("dump.bin");
		//Clubs.debugNames(of);
		//printPlayerNames();
		printLeagueNames();
	}

	

	public static void printLeagueNames()
	{
		String []arr = Leagues.get(of);
		for (int i=0; i<arr.length; i++)
			System.out.println(arr[i]);
	}
	public static void printClubNames()
	{
		Clubs.debugNames(of);
	}
	public static void printPlayerNames()
	{
		for (int i=0; i<Player.total; i++)
			System.out.println(new Player(of,i,i));
	}
	public static void dumpDecodedТeamNames(String fName)
	{
		try (FileOutputStream fos = new FileOutputStream(fName)) 
		{
   			fos.write(of.data);
   			fos.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}
