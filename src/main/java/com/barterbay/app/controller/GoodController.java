package com.barterbay.app.controller;

import com.barterbay.app.domain.dto.GoodDTO;
import com.barterbay.app.servcie.GoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/good")
public class GoodController {

  private final GoodService goodService;

  @GetMapping
  public ResponseEntity<GoodDTO> getGoodById(@RequestBody long goodId) {
    return ResponseEntity.ok(goodService.findGoodById(goodId));
  }

  @PutMapping("/{goodId}")
  @ResponseStatus(HttpStatus.OK)
  public void updateGoodById(@PathVariable long goodId,
                             @RequestBody GoodDTO goodDTO) {
    goodService.updateGoodById(goodId, goodDTO);
  }

  @DeleteMapping("/{goodId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable long goodId) {
    goodService.deleteGoodById(goodId);
  }

  @PostMapping
  public ResponseEntity<GoodDTO> createGood(@RequestBody GoodDTO goodDTO) {
    this.goodService.saveGood(goodDTO);
    return new ResponseEntity<>(goodDTO, HttpStatus.CREATED);
  }
}

