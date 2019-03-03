import java.io.*;
import java.util.*;

public final class Main {
	private Main() {
	}

	@SuppressWarnings("resource")
	public static void main(final String[] args) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));
		final List<Photo> input = Parser.parse(reader.lines());
		final List<Slide> output = Solver.solve(input);
		System.out.println("score: " + Solver.score(output));
		System.out.println("sane: " + SolverSanity.sanity(output));
		writer.write(Parser.toOutput(output));
		writer.flush();
		writer.close();
	}
}