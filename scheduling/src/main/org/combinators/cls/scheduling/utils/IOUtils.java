package org.combinators.cls.scheduling.utils;

import org.combinators.cls.scheduling.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class IOUtils {

	private static final String HTML_LOCATION = "C:\\";

	public static Task loadTask(String file) throws IOException {
		return loadTask(new File(file));
	}

	public static Task loadTask(File file) throws IOException {
		Task task = new Task();
		Files.lines(file.toPath()).forEach(s -> task.add(ApplicationUtils.parse(s)));

		return task;
	}

	public static void saveTask(File file, Task currentTask) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(currentTask.getString());
		writer.close();
	}

	public static void saveHTML(String html) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(HTML_LOCATION.concat("result.html"))));
		writer.write(html);
		writer.close();
	}
	/*
	public static void writeRunner(String runnerCode) throws IOException{
		File file = getRunnerFile();
		System.out.println(file.getPath());
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
		writer.write(runnerCode);
		writer.close();
	}
	
	public static int  compileRunner() throws IOException, InterruptedException{
		Process process = Runtime.getRuntime().exec("javac -proc:none -classpath " + getClasspath() + " " + getRunnerFile());
		int e = process.waitFor();

		
		
		return e;
	}
	
	public static int invokeRunner(Task task) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException, ClassNotFoundException {
		
		return (int)Runner.class.getDeclaredMethod("run", Task.class).invoke(null, task);
		return (int) Class.forName("org.combinators.cls.scheduling.out.Runner", false, URLClassLoader.newInstance(new URL[] { getRunnerFile().toURI().toURL()}))
				             .getDeclaredMethod("run", Task.class)
				             .invoke(null, task);
	}
	
	private static String getClasspath() {
		return Runner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
	private static File getRunnerFile() {
		return new File(getClasspath().concat("org/combinators/cls/scheduling/out/Runner.java"));
	}*/
}
