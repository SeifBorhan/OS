import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Process {

	private ArrayList<String[]> instructions=new ArrayList<String[]>();
	private String processID;
	private ArrayList<String[]> variables=new ArrayList<String[]>();

	public ArrayList<String[]> getInstructions() {
		return instructions;
	}

	public String getProcessID() {
		return processID;
	}

	public ArrayList<String[]> getVariables() {
		return variables;
	}

	public Process(String ID) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(ID));
		String currentLine = br.readLine();
		while (currentLine != null) {

			String[] content = currentLine.split(" ");
			instructions.add(content);

			currentLine = br.readLine();

		}
		br.close();
		this.processID = ID;

	}
	
	public static String toString(String[] a) {
		String results = "";
		for (int i = 0; i < a.length; i++) {
			results += " " + a[i];
		}
		return results;
	}
	
	public static String toString(LinkedList<Process> a) {
		String results = "[";
		for (int i = 0; i < a.size(); i++) {
			results += " " + "Program"+ " "+a.get(i).getProcessID().charAt(8);
		}
		return results+="]";
	}


	public  String toString(ArrayList<String[]> a) {
		String results = "";
		for (int i = 0; i < a.size(); i++) {
			results += " " + toString(a.get(i));
		}
		return results;
	}

}
