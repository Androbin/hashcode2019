import java.util.*;
import java.util.stream.*;
import java.io.*;

public final class SolverSimple {
	@SuppressWarnings("resource")
	public static void main(final String[] args) throws IOException {
		assert args.length == 2;

		final BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));
		final List<Photo> input = Parser.parse(reader.lines());
		final List<Slide> output = solve(input);
		writer.write(Parser.toOutput(output));
		writer.close();
	}

	private static List<String> mergeTagLists(List<String> l0, List<String> l1) {
		return l0.stream().filter(tag -> l1.contains(tag)).collect(Collectors.toList());
	}

	public static int slideInterest(Slide slide0, Slide slide1) {
		List<String> mergedTags = mergeTagLists(slide0.getTags(), slide1.getTags());
		return Math.min(slide0.getTags().size() - mergedTags.size(),
				Math.min(mergedTags.size(), slide1.getTags().size() - mergedTags.size()));
	}

	private static Slide getSlideWithMaxInterest(List<Slide> slides, Slide slide) {
		int maxInterest = -1;
		Slide result = null;
		for (Slide tmpSlide : slides) {
			if (tmpSlide == slide)
				continue;

			// condition ?
			int interest = slideInterest(slide, tmpSlide);
			if (interest >= maxInterest && interest > 0) {
				result = tmpSlide;
				maxInterest = interest;
			}
		}

		return result;
	}

	private static List<Slide> sortByMaxInterest(List<Slide> slides) {
		if (slides.size() <= 1) {
			List<Slide> result = new LinkedList<>();
			result.addAll(slides);
			return result;
		}

		// Get first slide
		Slide first = slides.get(0);
		slides.remove(0);
		// Maximum compatible slide
		Slide maxSlide = getSlideWithMaxInterest(slides, first);
		if (maxSlide == null) {
			List<Slide> result = sortByMaxInterest(slides);
			result.add(0, first); // add first again
			return result;
		}

		// Switch position of maxslide to first
		slides.remove(maxSlide);
		slides.add(0, maxSlide);

		// Recursion
		List<Slide> result = sortByMaxInterest(slides);
		result.add(0, first); // add first again

		return result;
	}

	public static List<Slide> solve(List<Photo> input) {
		List<Slide> slides = new LinkedList<>();
		slides.addAll(Solver.getHorizontalSlides(input));
		slides.addAll(Solver.getVerticalSlides(input));

		// Order by tag length
		slides = slides.stream()
				.sorted((photo0, photo1) -> Integer.compare(photo0.getTags().size(), photo1.getTags().size()))
				.collect(Collectors.toList());

		// Make slides to linkedlist
		List<Slide> tmpSlides = new LinkedList<>();
		tmpSlides.addAll(slides);
		slides = tmpSlides;

		slides = sortByMaxInterest(slides);
		return slides;
	}
}
