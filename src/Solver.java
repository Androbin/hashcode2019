import java.util.*;
import java.util.stream.*;

public final class Solver {
	private Solver() {
	}

	private static <T> int intersect(final Collection<T> l0, final Collection<T> l1) {
		return (int) l0.stream().filter(l1::contains).count();
	}

	private static int slideInterest(final Slide slide0, final Slide slide1) {
		final int intersection = intersect(slide0.getTags(), slide1.getTagSet());
		return Math.min(slide0.getTags().size() - intersection,
				Math.min(intersection, slide1.getTags().size() - intersection));
	}

	private static List<Slide> getHorizontalSlides(final List<Photo> photos) {
		return photos.stream().filter(Photo::isHorizontal).map(photo -> new SlideHorizontal(photo))
				.collect(Collectors.toList());
	}

	private static List<Slide> getVerticalSlidesGreedy(final List<Photo> photos) {
		final List<Photo> verticalPhotos = photos.stream().filter(Photo::isVertical).collect(Collectors.toList());
		final List<Slide> slides = new ArrayList<>(verticalPhotos.size() / 2);

		while (!verticalPhotos.isEmpty()) {
			final Photo photo1 = verticalPhotos.get(0);
			Photo photo2 = null;
			int score2 = Integer.MAX_VALUE;

			for (final Photo photo : verticalPhotos) {
				if (photo == photo1) {
					continue;
				}

				final int score = intersect(photo1.getTags(), photo.getTags());

				if (score <= score2) {
					photo2 = photo;
					score2 = score;

					if (score == 0) {
						break;
					}
				}
			}

			if (photo2 != null) {
				slides.add(new SlideVertical(photo1, photo2));
			}

			verticalPhotos.remove(photo1);
			verticalPhotos.remove(photo2);
		}

		return slides;
	}

	public static List<Slide> solve(final List<Photo> photos) {
		final List<Slide> slides = new LinkedList<>();
		slides.addAll(getHorizontalSlides(photos));
		slides.addAll(getVerticalSlidesGreedy(photos));

		final List<Slide> presentation = new ArrayList<>();
		final List<List<Slide>> order = splitByOrder(slides);

		for (final List<Slide> orderPhotos : order) {
			if (orderPhotos.isEmpty()) {
				continue;
			}

			presentation.addAll(solveGreedy(orderPhotos));
		}

		return presentation;
	}

	private static List<Slide> solveGreedy(final List<Slide> slides) {
		final Map<String, List<Slide>> tags1 = new HashMap<>();

		for (final Slide slide : slides) {
			for (final String tag : slide.getTags()) {
				tags1.computeIfAbsent(tag, foo -> new ArrayList<>()).add(slide);
			}
		}

		final List<Slide> presentation = new ArrayList<>(slides.size());
		Slide last = null;

		while (!slides.isEmpty()) {
			final Slide next;

			if (last == null) {
				next = slides.get(0);
			} else {
				final List<Slide> reduced = last.getTags().stream().flatMap(tag -> tags1.get(tag).stream()).distinct()
						.collect(Collectors.toList());

				final Map<Slide, Integer> scores = new HashMap<>();

				for (final Slide slide : reduced) {
					scores.put(slide, slideInterest(last, slide));
				}

				next = reduced.stream().max((a, b) -> Integer.compare(scores.get(a), scores.get(b)))
						.orElseGet(() -> slides.get(0));
			}

			slides.remove(next);
			presentation.add(next);
			last = next;

			for (final String tag : last.getTags()) {
				tags1.get(tag).remove(last);
			}
		}

		System.out.println(score(presentation));
		return presentation;
	}

	// split into buckets ordered by size
	private static List<List<Slide>> splitByOrder(final List<Slide> slides) {
		final List<List<Slide>> output = new ArrayList<>();
		final int spread = 1;

		for (int i = 0; i < 100 * spread; i++) {
			output.add(new ArrayList<>());
		}

		for (final Slide slide : slides) {
			final int mod = slide.getPhotos()[0].getIdentifier() % spread;
			output.get((slide.getTags().size() - 1) * spread + mod).add(slide);
			// output.get(0).add(slide);
		}

		return output;
	}

	public static int score(final List<Slide> slides) {
		int result = 0;
		Slide lastSlide = null;

		for (final Slide slide : slides) {
			if (lastSlide == null) {
				lastSlide = slide;
				continue;
			}

			// Compute interest rate
			result += slideInterest(lastSlide, slide);
			lastSlide = slide;
		}

		return result;
	}
}