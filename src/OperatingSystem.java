import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class OperatingSystem {
	public Scheduler scheduler = new Scheduler();
	public static Mutex file = new Mutex();
	public static Mutex userInput = new Mutex();
	public static Mutex userOutput = new Mutex();
	public LinkedList<Process> globalBlockedQueue = new LinkedList<Process>();
	public LinkedList<Process> globalReadyQueue = new LinkedList<Process>();
	public LinkedList<Process> Processes = new LinkedList<Process>();
	public int t = 0;

	public OperatingSystem() throws IOException {
		Process p1 = new Process("Program_1.txt");
		Process p2 = new Process("Program_2.txt");
		Process p3 = new Process("Program_3.txt");
		Processes.add(p1);
		Processes.add(p2);
		Processes.add(p3);
	}

	public void parser(Process p) throws IOException {

		ArrayList<String[]> instructions = p.getInstructions();
		if (instructions.isEmpty()) {
			Processes.remove(p);
			scheduler.i = 0;
		} else {

			String[] firstInstruction = instructions.get(0);
			String x = firstInstruction[0];
			switch (x) {
			case "semWait":
				if (firstInstruction[1].equals("userInput"))
					userInput.semWait(p, this);
				else if (firstInstruction[1].equals("file"))
					file.semWait(p, this);
				else
					userOutput.semWait(p, this);
				break;
			case "semSignal":
				if (firstInstruction[1].equals("userInput"))
					userInput.semSignal(p, this);
				else if (firstInstruction[1].equals("file"))
					file.semSignal(p, this);
				else
					userOutput.semSignal(p, this);
				break;
			case "assign":
				if (firstInstruction[2].equals("readFile"))
					assign(p, firstInstruction[1], readFile(p, firstInstruction[3]));
				else if (firstInstruction[2].equals("input"))
					assign(p, firstInstruction[1], TakeInput(p));
				else
					assign(p, firstInstruction[1], firstInstruction[2]);
				break;
			case "writeFile":

				writeFile(p, firstInstruction[1], firstInstruction[2]);
				break;
			case "readFile":
				readFile(p, firstInstruction[1]);
				break;
			case "print":
				print(p, firstInstruction[1]);
				break;
			case "printFromTo":
				printFromTo(p, firstInstruction[1], firstInstruction[2]);
				break;
			default:
				System.out.println(p.getProcessID());

			}
		}
	}

	public void assign(Process p, String name, String value) {

		if (this.scheduler.i == this.scheduler.iClone) {
			System.out.println("Assigning input");
			write(p, name, value);
			this.scheduler.i--;
			p.getInstructions().remove(0);

		} else if (this.scheduler.i == 0) {

			p.getInstructions().get(0)[2] = value;

		}
		else {
			write(p, name, value);
			this.scheduler.i--;
			this.t += 1;
			System.out.println("------Time------" + " " + this.t + "s");
			System.out.println("Current Program: " + p.getProcessID().charAt(8) + "\n");
			System.out.print("Current Instruction: " + "Assigning input" + "\n");
			p.getInstructions().remove(0);
		}
	}

	public void writeFile(Process p, String path, String value) throws IOException {
		System.out.println("Writing on a file");
		if (scheduler.i != 0) {
			File file = new File(read(p, path));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(read(p, value));
			bw.close();
			scheduler.i--;
			p.getInstructions().remove(0);

		}

	}

	public String readFile(Process p, String path) throws IOException {
		System.out.println("Reading File");

		if (scheduler.i != 0) {
			BufferedReader br = new BufferedReader(new FileReader(read(p, path)));
			String currentLine = br.readLine();
			br.close();
			scheduler.i--;

			return currentLine;
		} else
			return null;

	}

	public void print(Process p, String x) {
		System.out.println("Printing");
		System.out.println("Printed value:" + " " + read(p, x));
		scheduler.i--;
		p.getInstructions().remove(0);

	}

	public void printFromTo(Process p, String a, String b) {
		String x1 = read(p, a);
		String y1 = read(p, b);
		System.out.println("Printing values from: " + x1 + " to " + y1);

		if (scheduler.i != 0) {

			int x = Integer.parseInt(x1);
			int y = Integer.parseInt(y1);

			while (x != y + 1) {
				System.out.print(x + " ");
				x++;
			}
			System.out.print("\n");
			scheduler.i--;
			p.getInstructions().remove(0);
		}
	}

	public String read(Process p, String var) {
		ArrayList<String[]> arr = p.getVariables();
		for (int j = 0; j < arr.size(); j++) {
			if (arr.get(j)[0].equals(var)) {
				return arr.get(j)[1];
			}
		}
		return "";

	}

	public void write(Process p, String var, String val) {
		String[] x = new String[2];
		x[0] = var;
		x[1] = val;
		p.getVariables().add(x);
	}

	public String TakeInput(Process p) {
		System.out.println("Taking input from the user");

		if (scheduler.i != scheduler.iClone && scheduler.i !=0) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Please enter a value: ");
			String line = sc.nextLine();
			scheduler.i--;
			return line;
		} else if (scheduler.i == this.scheduler.iClone) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Please enter a value: ");
			String line = sc.nextLine();
			scheduler.i--;
			System.out.println("\n" + "Queues:");
			System.out.println("Blocked Queue" + "" + Process.toString(this.globalBlockedQueue));
			System.out.println("Ready Queue" + "" + Process.toString(this.globalReadyQueue) + "\n");
			return line;
		}
		return null;
	}

	private void Execute() throws IOException {
		this.scheduler.run(this);

	}

	public static void main(String[] args) throws IOException {

		OperatingSystem os = new OperatingSystem();
		os.Execute();

	}
}
