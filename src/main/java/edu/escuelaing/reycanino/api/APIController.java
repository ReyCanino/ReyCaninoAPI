package edu.escuelaing.reycanino.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.services.ReyCaninoService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/reyCanino")
public class APIController {

    @Autowired
    ReyCaninoService services;

    @GetMapping(value = "/")
    public ResponseEntity<List<Horario>> consultsAvailableDates() {
        try {
            return new ResponseEntity<>(services.consultsAvailableDates(), HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}