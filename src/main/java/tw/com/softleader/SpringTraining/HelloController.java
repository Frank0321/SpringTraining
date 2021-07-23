package tw.com.softleader.SpringTraining;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @RequestMapping("/{name}")
    public String sayHello(@PathVariable("name") String theName){
        return "Hello "+theName;
    }
}
