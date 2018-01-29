package org.think2framework.core.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShellUtils {

	public static void main(String[] args) {
		// List<String> list = new ArrayList<>();
		// list.add(execCmd("svn up"));
		// list.add(execCmd("date"));
		// list.add(execCmd("ls"));
		Process process = null;
		// List<String> processList = new ArrayList<String>();
		// String cmd = "dirname $0||cd /Users/zhoubin/Desktop";
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec("cd /Users;ls -a");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((null != input.readLine())) {
				System.out.println(input.readLine());
			}
			// process = runtime.exec("cd /Users/zhoubin/Desktop");
			// input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// while ((null != input.readLine())) {
			// System.out.println(input.readLine());
			// }

			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// try {
		// Process process = Runtime.getRuntime().exec("cd
		// /Users/zhoubin/Desktop/wiseks/svn/bin;ls");
		// BufferedReader input = new BufferedReader(new
		// InputStreamReader(process.getInputStream()));
		// String line;
		// while (null != (line = input.readLine())) {
		// System.out.println(line);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		execCmd("cd /Users/zhoubin/Desktop/wiseks/svn/bin", "ls");
	}

	public static List<String> execCmd(String... cmds) {
		List<String> list = new ArrayList<>();
		try {
			Process process = Runtime.getRuntime().exec("sh");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			for (String cmd : cmds) {
				list.add("exec " + cmd);
				os.writeBytes(cmd + "\n");
			}
			os.flush();
			String line;
			while (null != (line = input.readLine())) {
				list.add(line);
			}
			input.close();
			// Runtime runtime = Runtime.getRuntime();
			//
			// for (String cmd : cmds) {
			// list.add("exec " + cmd);
			// Process process = runtime.exec(cmd);
			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(process.getInputStream()));
			// String line;
			// while (null != (line = input.readLine())) {
			// list.add(line);
			// }
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
