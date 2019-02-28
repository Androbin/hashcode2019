import java.io.*;
import java.util.*;
import java.util.stream.*;

public final class Main {
	private Main() {
	}

	public static void main(final String[] args) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));
		final List<Photo> input = Parser.parse(reader.lines());
		final List<List<Integer>> output = Solver.solve(input);
		writer.write(Serializer.serialize(output));
	}
}