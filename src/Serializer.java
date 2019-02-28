import java.util.List;
import java.util.StringJoiner;

public class Serializer {

    public static String serialize(List<Slide> slideshow) {
        StringJoiner output = new StringJoiner("/n");
        for(Slide slide : slideshow){
            output.add(slide.toString());
         }
        return output.toString();
    }
}
