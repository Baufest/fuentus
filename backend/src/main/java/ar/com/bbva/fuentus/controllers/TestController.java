package ar.com.bbva.fuentus.controllers;

import javax.servlet.http.HttpServletResponse;

import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 * @author seba
 */
@Controller
public class TestController {

    @Autowired
    private AppsRepository appsRepository;

    @ResponseBody
    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    public ResponseEntity<?> getTest(HttpServletResponse response) {
        List<App> appsPorUUAA =  appsRepository.findByUuaa("ASTA");
       return ResponseEntity.ok(appsPorUUAA);
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String getIndex(HttpServletResponse response) {
        return "redirect:/index.html";
    }

}
