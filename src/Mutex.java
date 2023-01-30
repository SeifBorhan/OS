import java.util.LinkedList;

public class Mutex {
	value val;
	String ownerID;
	public LinkedList<Process> queue = new LinkedList<Process>();

	public Mutex() {
		this.val = value.One;
	}

	public void semWait(Process p, OperatingSystem os) {
		System.out.println("SemWait");
		if (this.val == value.One) {
			this.val = value.Zero;
			this.ownerID = p.getProcessID();
			os.scheduler.i--;

		} else {
			this.queue.add(p);
			os.globalBlockedQueue.add(p);
			if (os.scheduler.i == os.scheduler.iClone)
				os.scheduler.i = 0;
			else
				os.scheduler.i--;
		}
		p.getInstructions().remove(0);

	}

	public void semSignal(Process p, OperatingSystem os) {
		System.out.println("SemSignal");
		if (this.ownerID.equals(p.getProcessID())) {
			if (queue.isEmpty()) {
				this.val = value.One;

			} else {
				this.ownerID = this.queue.getFirst().getProcessID();
				Process p1 = this.queue.removeFirst();
				os.globalReadyQueue.add(p1);
				os.globalBlockedQueue.remove(p1);

			}
			p.getInstructions().remove(0);
		}
		os.scheduler.i--;

	}
}
