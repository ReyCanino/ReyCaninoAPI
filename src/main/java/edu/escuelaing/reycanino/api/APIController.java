package edu.escuelaing.reycanino.api;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import edu.escuelaing.reycanino.model.Cliente;
import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.services.ReyCaninoService;

@CrossOrigin(origins = { "http://localhost:3000", "https://reycanino.vercel.app" })
@RestController
@RequestMapping(value = "/reyCanino")
public class APIController {

    @Autowired
    ReyCaninoService services;

    @PostMapping(value = "/consultar")
    public ResponseEntity<List<Horario>> consultsAvailableDates(@RequestBody Horario horario) {
        try {
            return new ResponseEntity<>(services.consultsAvailableDates(horario), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/reservar")
    public ResponseEntity<String> reservar(@RequestBody Horario horario) {
        try {
            services.reservar(horario);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/confirmar/{reserva}")
    public RedirectView confirmacion(@PathVariable() String reserva) {
        try {
            services.confirmar(reserva);
            return new RedirectView("https://reycanino.vercel.app/exito");
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new RedirectView("https://reycanino.vercel.app/fallo");
        }
    }

    @GetMapping(value = "/cliente/{cliente}")
    public ResponseEntity<Cliente> consultarCliente(@PathVariable() String cliente) {
        try {
            return new ResponseEntity<>(services.consultarCliente(cliente), HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Cliente> login(@RequestBody Cliente cliente) {
        try {
            return new ResponseEntity<>(services.login(cliente), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/consultar/{id}")
    public ResponseEntity<Horario> consultarReserva(@PathVariable() String id) {
        Horario horario = services.consultarReserva(id);
        return new ResponseEntity<>(horario, HttpStatus.OK);
    }

    @GetMapping(value = "/horario/{id}")
    public ResponseEntity<List<Horario>> consultarHorario(@PathVariable() String id) {
        try {
            return new ResponseEntity<>(services.consultarHorario(id), HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/tiendas")
    public ResponseEntity<List<Cliente>> consultarTiendas() {
        return new ResponseEntity<>(services.consultarTiendas(), HttpStatus.OK);
    }

    @GetMapping(value = "/horarioAdmin/{id}")
    public ResponseEntity<List<Horario>> consultarHorarioAdmin(@PathVariable() String id) {
        return new ResponseEntity<>(services.consultarHorarioAdmin(id), HttpStatus.OK);
    }

    @GetMapping(value = "/cancelar/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable() String id) {
        try {
            String reserva = services.cancelarReserva(id);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            Logger.getLogger(APIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/horario/agregar")
    public ResponseEntity<Horario> agregarHorario(@RequestBody Horario horario){
        services.agregarHorario(horario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/agregarCliente")
    public ResponseEntity<Horario> agregarCliente(@RequestBody Cliente cliente) {
        services.agregarCliente(cliente);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}