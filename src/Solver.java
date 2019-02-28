import java.util.*;
import java.util.stream.*;

public final class Solver {
	private Solver() {
	}

	private static List<Slide> getHorizontalSlides(final List<Photo> photos) {
		return photos.stream().filter(Photo::isHorizontal).map(photo -> new SlideHorizontal(photo))
				.collect(Collectors.toList());
	}

	private static List<Slide> getVerticalSlides(final List<Photo> photos) {
		List<Photo> verticalPhotos = photos.stream().filter(Photo::isVertical)
				.sorted((photo0, photo1) -> Integer.compare(photo0.getTags().size(), photo1.getTags().size()))
				.collect(Collectors.toList());
		// Last with first ...
		List<Slide> slides = new LinkedList<>();
		int firstIndex = 0, lastIndex = verticalPhotos.size() - 1;
		while (firstIndex < lastIndex) {
			Photo first = verticalPhotos.get(firstIndex);
			Photo last = verticalPhotos.get(lastIndex);
			slides.add(new SlideVertical(first, last));
			firstIndex++;
			lastIndex--;
		}

		return slides;
	}

	public static List<Slide> solve(final List<Photo> photos) {
		List<Slide> slides = new LinkedList<>();
		slides.addAll(getHorizontalSlides(photos));
		slides.addAll(getVerticalSlides(photos));
		return solveSlides(slides);
	}

	public static List<Slide> solveSlides(final List<Slide> slides) {
		final List<Slide> presentation = new ArrayList<Slide>();
		final List<List<Slide>> order = splitByOrder(slides);

		for (final List<Slide> orderPhotos : order) {
			presentation.addAll(solveSameOrder(orderPhotos));
		}

		return presentation;
	}

	public static List<Slide> solveSameOrder(final List<Slide> slides) {
		final Map<String, List<Slide>> tags1 = new HashMap<>();
		final Map<String, List<Slide>> tags2 = new HashMap<>();

		for (final Slide slide : slides) {
			for (final String tag1 : slide.getTags()) {
				tags1.computeIfAbsent(tag1, foo -> new ArrayList<>()).add(slide);

				for (final String tag2 : slide.getTags()) {
					if (tag2 == tag1) {
						continue;
					}

					final String tag12 = tag1.compareTo(tag2) > 0 ? tag1 : tag2;
					tags2.computeIfAbsent(tag12, foo -> new ArrayList<>()).add(slide);
				}
			}
		}

		tags2.values().forEach(list -> System.out.println(list.size()));

		final List<List<Slide>> transitions = new ArrayList<>();

		tags2.forEach((tag, slides2) -> {
			slides2.forEach(slide1 -> {
				slides2.forEach(slide2 -> {
					if (slide1 == slide2) {
						return;
					}

					transitions.add(Arrays.asList(slide1, slide2));
				});
			});
		});

		Collections.sort(transitions, (t1, t2) -> {
			return 0;
		});

		return Collections.emptyList();
	}

	public static List<List<Slide>> splitByOrder(final List<Slide> slides) {
		List<List<Slide>> output = new ArrayList<>(100);

		for (int i = 1; i <= 100; i++) {
			output.add(new ArrayList<>());
		}

		for (final Slide slide : slides) {
			output.get(slide.getTags().size()).add(slide);
		}

		return output;
	}
}
