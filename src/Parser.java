import java.util.*;

public class Parser {
	private Parser() {}

	public static List<Photo> parse(Stream<String> linesStream) {
		String[] lines = lineStream.toArray();
		int photoLines = lines[0];
		assert photoLines == lines.length - 1;

		List<Photo> result = new LinkedList<>();
		for (int i = 1; i < lines.length; ++i) {
			List photoTags = Arrays.asList(lines[i].split(' '));
			assert photoTags.length() > 0;

			// Get vertical property
			boolean vertical = photoTags.get(0).equals("V");
			// Get tags (remove alignment property)
			photoTags.removeAt(0);
			result.add(new Photo(vertical, photoTags));
		}
	}
}
