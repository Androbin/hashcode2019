import java.util.*;
import java.io.*;

public final class Analysis {
	private Analysis() {
	}

	@SuppressWarnings("resource")
	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("One argument expected (only).");
			System.exit(1);
		}

		final BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		final List<Photo> input = Parser.parse(reader.lines());
		assert input.size() != 0;

		System.out.println("Photos: " + input.size());

		double averageTagLength = 0.0;

		for (final Photo photo : input) {
			averageTagLength += photo.getTags().size();
		}

		averageTagLength /= input.size();
		System.out.println("Average tag length: " + averageTagLength);
	}
}