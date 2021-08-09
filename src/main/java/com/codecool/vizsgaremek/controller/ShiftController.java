package com.codecool.vizsgaremek.controller;

import com.codecool.vizsgaremek.dto.*;
import com.codecool.vizsgaremek.exception.EmployeeNotFoundException;
import com.codecool.vizsgaremek.model.Employee;
import com.codecool.vizsgaremek.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shift")
public class ShiftController {

   private ShiftService shiftService;

   public ShiftController(ShiftService ss) {
      this.shiftService = ss;
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public ShiftDTO createShift(@RequestBody CreateShiftCommand command) {
      return shiftService.createShift(command);
   }

   @GetMapping("/{id}")
   public ShiftDTO findShiftById(@PathVariable("id") long id) {
      return shiftService.findShiftById(id);
   }

   @PutMapping("/{id}")
   public ShiftDTO updateShift(
         @PathVariable("id") long id,
         @RequestBody UpdateShiftCommand command) {
      return shiftService.updateShift(id, command);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteShift(@PathVariable("id") long id) {
      shiftService.deleteShiftById(id);
   }

   @GetMapping
   public List<ShiftDTO> shiftList(@RequestParam Optional<String> prefix) {
      return shiftService.shiftList(prefix);
   }


}
