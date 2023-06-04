package nd.darboKalendorius.InputData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class InputService {
    public Input getInputData() {
        Input input = null;
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Input> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/Input.json");
        try {
            input = mapper.readValue(inputStream, typeReference);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return input;
    }
}
