package me.replydev.qubo;

import me.replydev.utils.FileUtils;
import me.replydev.utils.KeyboardThread;
import me.replydev.utils.Log;
import me.replydev.versionChecker.VersionChecker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CLI {

	private static QuboInstance quboInstance;

	public static QuboInstance getQuboInstance() 
	{
		return quboInstance;
	}

	static void init(String[] a) 
	{
		printLogo();
		if(!isUTF8Mode()){
			System.out.println("The scanner isn't running in UTF-8 mode!");
			System.out.println("Put \"-Dfile.encoding=UTF-8\" in JVM args in order to run the program correctly!");
			System.exit(-1);
		}
		VersionChecker.checkNewVersion();
		FileUtils.createFolder("outputs");
		ExecutorService inputService = Executors.newSingleThreadExecutor();
		inputService.execute(new KeyboardThread());
		if (Arrays.equals(new String[] { "-txt" }, a))
			txtRun();
		else
			standardRun(a);
		Log.logln("Scan terminated - " + Info.serverFound + " (" + Info.serverNotFilteredFound + " in total)");
		System.exit(0);
	}

	private static void printLogo()
	{
		System.out.println("   ____        _           _____                                 \n"
				+ "  / __ \\      | |         / ____|                                \n"
				+ " | |  | |_   _| |__   ___| (___   ___ __ _ _ __  _ __   ___ _ __ \n"
				+ " | |  | | | | | '_ \\ / _ \\\\___ \\ / __/ _` | '_ \\| '_ \\ / _ \\ '__|\n"
				+ " | |__| | |_| | |_) | (_) |___) | (_| (_| | | | | | | |  __/ |   \n"
				+ "  \\___\\_\\\\__,_|_.__/ \\___/_____/ \\___\\__,_|_| |_|_| |_|\\___|_|   \n"
				+ "                                                                ");
		System.out.println(
				"By @replydev on Telegram\nVersion " + Info.version + " " + Info.otherVersionInfo);
	}

	private static void standardRun(String[] a)
	{
		InputData i;
		try 
		{
			i = new InputData(a);
		} 
		catch (Exception e) 
		{
			System.err.println(e.getMessage());
			return;
		}
		Info.debugMode = i.isDebugMode();
		quboInstance = new QuboInstance(i);
		try{
			quboInstance.run();
		}catch (NumberFormatException e){
			quboInstance.inputData.help();
		}
	}

	private static void txtRun() 
	{
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader("ranges.txt"));
			String s;
			while ((s = reader.readLine()) != null) 
			{
				if (s.isEmpty())
				{					
					continue;
				}
				
				InputData i;
				try 
				{
					i = new InputData(s.split(" "));
				}
				catch (Exception e) 
				{
					System.err.println(e.getCause().getMessage());
					reader.close();
					return;
				}
				
				quboInstance = new QuboInstance(i);
				Log.logln("Now running: " + quboInstance.getFilename());
				quboInstance.run();
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			System.err.println("File \"ranges.txt\" not found, create a new one and restart the scanner");
			System.exit(-1);
		}
	}

	private static boolean isUTF8Mode()
	{
		List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
		return arguments.contains("-Dfile.encoding=UTF-8");
	}

}