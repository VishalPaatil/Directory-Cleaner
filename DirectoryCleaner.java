import java.lang.*;
// For collection framework
import java.util.*;
// Fore directory traversal
import java.io.*;
// For file reading
import java.io.FileInputStream;
// For checksum
import java.security.MessageDigest;

class Main
{
	public static void main(String arg[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Please enter directory name");
		String dir = br.readLine();
		
		Cleaner cobj = new Cleaner(dir);											//Object creation of Cleaner class with reference cobj.
		
		// To remove empty files
		cobj.CleanDirectoryEmptyFile();
		
		// To remove duplicate files
		cobj.CleanDirectoryDuplicateFile();
	}
}

class Cleaner
{
	public File fdir = null;														//characteristics
	
	public Cleaner(String name)														//constructor
	{
		// Check the existance of directory
		fdir = new File(name);
		if(!fdir.exists())
		{
			System.out.println("Invalid Directory name");
			System.exit(0);
		}
	}
	
	public void CleanDirectoryEmptyFile()											//method
	{
		File filelist[] = fdir.listFiles();											//gets list of all files from given directory
		int EmptyFile = 0;
		for(File file : filelist)													//For each Loop
		{
			if(file.length() == 0)													//file.length() gives size of file.
			{		
				System.out.println("Empty file name : "+file.getName());			//getName() gives name of file
				if(!file.delete())													//if file does not get deleted then if block will execute.
				{
					System.out.println("Unable to delete");							
				}
				else																//when file will get delete else block will get execute.
				{
					EmptyFile++;													//Counter used for counting deleted file.
				}
			}
		}
		System.out.println("Total empty files deleted : "+ EmptyFile);
	}

	public void CleanDirectoryDuplicateFile() //throws Exception
	{
		// List all files from directory
		File filelist[] = fdir.listFiles();
		
		// Counter to count number of duplicate files
		int DupFile = 0;
		
		// Bucket to read the data
		byte bytearr[] = new byte[1024];
		
		// Create linkedlist of strings to store the checksum
		LinkedList<String> lobj = new LinkedList<String>();
		
		// Counter to read the data from file
		int Rcount = 0;

		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			if(digest == null)
			{
				System.out.println("Unable to get the MD5");
				System.exit(0);
			}
			for(File file : filelist)
			{
				// Object to read the data from file
				FileInputStream fis = new FileInputStream(file);
			
				if(file.length() != 0)
				{
					while((Rcount = fis.read(bytearr)) != -1)
					{
						digest.update(bytearr,0,Rcount);
					}
				}
				
				// to get the hash bytes of cheksum
				byte bytes[] = digest.digest();															//digest() gives hashbytes
				
				// Stringbuffer to create editable string
				StringBuilder sb = new StringBuilder();
				
				for(int i = 0; i < bytes.length; i++)
				{
					// Add each byte from decimal to hexadecimal in the stringbuffer
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}

				System.out.println("File name : " + file.getName()+" Checksum : "+sb);
				if(lobj.contains(sb.toString()))   
				{
					if(!file.delete())
					{
						System.out.println("Unable to delete file : "+file.getName());
					}
					else
					{
						System.out.println("File gets deleted : "+file.getName());
						DupFile++;
					}
				}
				else
				{
					lobj.add(sb.toString());
				}
				
				fis.close();
			}
		}
		catch(Exception obj)
		{
			System.out.println("Exception occured : "+obj);
		}
		finally
		{
		
		}
		
		System.out.println("Total duplicate files deleted : "+ DupFile);
	}
}