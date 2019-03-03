import java.util.*;
import java.util.stream.*;

public final class Parser {
	private Parser() {
	}

	public static List<Photo> parse(final Stream<String> lineStream) {
		int identifierCount = 0;

		final String[] lines = lineStream.toArray(n -> new String[n]);
		int photoLines = Integer.parseInt(lines[0]);
		assert photoLines == lines.length - 1;

		final List<Photo> result = new LinkedList<>();

		for (int i = 1; i < lines.length; ++i) {
			String[] photoTags = lines[i].split(" ");
			assert photoTags.length > 0;

			// Get vertical property
			boolean vertical = photoTags[0].equals("V");
			// Get tags (remove alignment property)
			final List<String> photoTagList = new ArrayList<>(photoTags.length - 2);

			for (int j = 2; j < photoTags.length; ++j) {
				photoTagList.add(photoTags[j]);
			}

			result.add(new Photo(vertical, photoTagList, identifierCount++));
		}

		return result;
	}

	public static String toOutput(final List<Slide> slides) {
		final StringBuilder result = new StringBuilder();
		result.append(slides.size()).append('\n');

		for (final Slide slide : slides) {
			result.append(slide.toString()).append('\n');
		}

		return result.toString();
	}

	public static String toDebugOutput(final List<Slide> slides) {
		final StringBuilder result = new StringBuilder();
		result.append(slides.size()).append('\n');

		for (final Slide slide : slides) {
			result.append(slide.toString()).append('[');

			for (final String tag : slide.getTags()) {
				result.append(' ').append(tag);
			}

			result.append(" ]\n");
		}

		return result.toString();
	}
}
