import java.io.IOException;
import java.util.LinkedList;

public class Scheduler {
	public int i;
	public int iClone;

	public Scheduler() {
		this.i = 2;
		this.iClone=i;
	}

	public void admit(Process p, LinkedList<Process> readyQueue) {
		readyQueue.add(p);
	}

	
	public Process dispatch(LinkedList<Process> readyQueue) {
		return readyQueue.removeFirst();
	}

	public void run(OperatingSystem os) throws IOException {
		Process p = new Process("Program_1.txt");

		while (!os.Processes.isEmpty() && !p.equals((Process)null)) {
			if (p.getInstructions().isEmpty()) {
				os.Processes.remove(p);
				System.out.println("("+p.getProcessID().substring(0, 7)+" "+p.getProcessID().charAt(8) +")"+" Finished Executing"+"\n");
			}
			if(!os.Processes.isEmpty()) {
			System.out.println("------Time------" + " " + os.t + "s");
			}

			if (os.t == 0) {
				admit(os.Processes.get(0), os.globalReadyQueue);
				System.out.println("Program 1 admitted" + "\n");
				p = dispatch(os.globalReadyQueue);

			} else if (os.t == 1) {
				System.out.println("Program 2 admitted"+ "\n");
				admit(os.Processes.get(1), os.globalReadyQueue);

			} else if (os.t == 4) {
				System.out.println("Program 3 admitted"+ "\n");
				admit(os.Processes.get(2), os.globalReadyQueue);

			}

			if (os.Processes.isEmpty()) {
				break;
			}
			if (i == 0) {
				if (!os.globalBlockedQueue.contains(p) && os.Processes.contains(p)) {
					admit(p, os.globalReadyQueue);
				}
				p = dispatch(os.globalReadyQueue);
				i = iClone;
			} else {
				if (p.getInstructions().isEmpty() && !os.Processes.isEmpty()) {
					os.Processes.remove(p);
					p = dispatch(os.globalReadyQueue);
					i = iClone;
				}
			}
			System.out.println("Current Program: " + p.getProcessID().charAt(8) + "\n");
			System.out.print("Current Instruction:" + " ");
			os.parser(p);
			System.out.println("\n" + "Queues:");
			System.out.println("Blocked Queue" + "" + Process.toString(os.globalBlockedQueue));
			System.out.println("Ready Queue" + "" + Process.toString(os.globalReadyQueue) + "\n");

			os.t++;

		}
	}
}
