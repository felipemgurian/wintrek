import br.fsa.wintrek.cli.CLI;

public class Wintrek {
	
	public static CLI cli;
	
	public static void main(String args[]) {
		cli = new CLI();
		cli.init();
	}
}
