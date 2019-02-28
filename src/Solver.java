import java.util.*;
import java.util.stream.*;

public final class Solver {
	private Solver() {
	}

	public static List<Slide> getHorizontalSlides(final List<Photo> photos) {
		return photos.stream().filter(Photo::isHorizontal).map(photo -> new SlideHorizontal(photo))
				.collect(Collectors.toList());
	}

	public static List<Slide> getVerticalSlides(final List<Photo> photos) {
		List<Photo> verticalPhotos = photos.stream().filter(Photo::isVertical)
				.sorted((photo0, photo1) -> Integer.compare(photo0.getTags().size(), photo1.getTags().size()))
				.collect(Collectors.toList());
		// Last with first ...
		final List<Slide> slides = new LinkedList<>();
		int firstIndex = 0, lastIndex = verticalPhotos.size() - 1;
		while (firstIndex < lastIndex) {
			final Photo first = verticalPhotos.get(firstIndex);
			final Photo last = verticalPhotos.get(lastIndex);
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
		final List<Slide> presentation = new ArrayList<>();
		final List<List<Slide>> order = splitByOrder(slides);

		for (final List<Slide> orderPhotos : order) {
			presentation.addAll(solveSameOrderQuick(orderPhotos));
		}

		return presentation;
	}

	public static List<Slide> solveSameOrderQuick(final List<Slide> slides) {
		final List<UniSlide> uniSlides = new ArrayList<>();
		final Map<Slide, List<UniSlide>> map = new HashMap<>();

		for (final Slide slide : slides) {
			for (final String tag : slide.getTags()) {
				final UniSlide us = new UniSlide(slide, tag);
				uniSlides.add(us);
				map.computeIfAbsent(slide, foo -> new ArrayList<>()).add(us);
			}
		}

		uniSlides.sort((s1, s2) -> s1.tag.compareTo(s2.tag));

		final List<Slide> presentation = new ArrayList<>();

		while (uniSlides.size() > slides.size()) {
			System.out.println(uniSlides.size() + "\t" + slides.size());
			for (int i = 0; i < uniSlides.size(); i++) {
				uniSlides.get(i).score = 0;
			}

			for (int i = 0; i < uniSlides.size() - 1; i++) {
				final UniSlide us1 = uniSlides.get(i);
				final UniSlide us2 = uniSlides.get(i + 1);
				final int score = SolverSimple.slideInterest(us1.slide, us2.slide);
				us1.score += score;
				us2.score += score;
			}

			map.forEach((slide, uniSlides2) -> {
				uniSlides2.sort((us1, us2) -> Integer.compare(us1.score, us2.score));

				for (int i = 0; i < uniSlides2.size() - 1; i++) {
					if (uniSlides2.size() > 1) {
						uniSlides.remove(uniSlides2.remove(uniSlides2.size() - 1));
					}
				}
			});
		}

		for (final UniSlide uniSlide : uniSlides) {
			presentation.add(uniSlide.slide);
		}

		return presentation;
	}

	public static List<Slide> solveSameOrder(final List<Slide> slides) {
		System.out.print("X");
		final Map<String, List<Slide>> tags1 = new HashMap<>();
		final Map<String, List<Slide>> tags2 = new HashMap<>();

		for (final Slide slide : slides) {
			for (final String tag1 : slide.getTags()) {
				tags1.computeIfAbsent(tag1, foo -> new ArrayList<>()).add(slide);

				for (final String tag2 : slide.getTags()) {
					if (tag2 == tag1) {
						continue;
					}

					final String tag12 = tag1.compareTo(tag2) > 0 ? tag1 + tag2 : tag2 + tag1;
					tags2.computeIfAbsent(tag12, foo -> new ArrayList<>()).add(slide);
				}
			}
		}

		// tags2.values().forEach(list -> System.out.println(list.size()));

		final List<List<Slide>> transitions = new ArrayList<>();

		int[] x = new int[1];

		tags2.forEach((tag, slides2) -> {
			slides2.forEach(slide1 -> {
				slides2.forEach(slide2 -> {
					if (slide1 == slide2) {
						return;
					}

					transitions.add(Arrays.asList(slide1, slide2));
				});
			});
			x[0] += slides.size() * slides.size();
		});

		System.out.println(x[0]);

		transitions.sort((t1, t2) -> {
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

	private static class UniSlide {
		public final Slide slide;
		public final String tag;
		public int score;

		public UniSlide(final Slide slide, final String tag) {
			this.slide = slide;
			this.tag = tag;
		}
	}
}
