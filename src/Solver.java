import java.util.*;

public final class Solver {
	private Solver() {
	}

	public static List<Slide> solve(final List<Photo> photos) {
		final Map<String, List<Photo>> tags = new HashMap<>();

		for (final Photo photo : photos) {
			for (final String tag : photo.getTags()) {
				tags.computeIfAbsent(tag, foo -> new ArrayList<>()).add(photo);
			}
		}

		return Collections.emptyList();
	}
}