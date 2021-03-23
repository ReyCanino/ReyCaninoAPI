package edu.escuelaing.reycanino.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/reyCanino")
public class APIController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> consultsAvailableDates() {
        try {
            return new ResponseEntity<>("Rey canino", HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}