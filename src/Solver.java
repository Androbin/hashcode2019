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

	public static List<List<Photo>> splitByOrder(List<Photo> photos) {

		List<List<Photo>> output = new ArrayList<>(100);
		for (int i = 1; i <= 100; i++) {
			output.add(new ArrayList<>());
		}
		for(Photo photo : photos){
			output.get(photo.getTags().size()).add(photo);
		}
		return output;
		
	}
}